package com.lo.shop.entity;


import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.content.SharedPreferences;

import com.lo.shop.view.MyProgressDialog;
import com.lo.shop.view.ToastView;
import com.lo.shop.R;
import com.lo.shop.activity.SigninActivity;
import com.lo.shop.entity.BaseModel;
import com.lo.shop.entity.BaseCallback;
import com.lo.shop.fragment.ProfileFragment;
import com.lo.shop.protocol.*;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.Gravity;

import com.external.androidquery.callback.AjaxStatus;

public class UserInfoModel extends BaseModel {
    public USER user;
    private SharedPreferences shared;
    private SharedPreferences.Editor editor;

    public static final int RANK_LEVEL_NORMAL = 0;
    public static final int RANK_LEVEL_VIP = 1;


    public UserInfoModel(Context context) {
        super(context);
        shared = context.getSharedPreferences("userInfo", 0);
        editor = shared.edit();
    }

    public void getUserInfo() {

        userinfoRequest request = new userinfoRequest();

        BaseCallback<JSONObject> cb = new BaseCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {

                UserInfoModel.this.callback(url, jo, status);
               // System.out.println("地址:"+ url+"  json值:     "+jo);
                try {

                    userinfoResponse response = new userinfoResponse();
                    response.fromJson(jo);
                   // System.out.println("用户信息数据"+jo);
                    if (jo != null) {
                       
                            user = response.data;
                            user.save();
                            editor.putString("email",user.email);
                            editor.putString("address",user.address);
                            editor.putString("trueName",user.name);
                            editor.putString("mobile",user.mobile);
                            editor.commit();
                            UserInfoModel.this.OnMessageResponse(url, jo, status);
                    }else
                    {
                    	System.out.println("没有数据！！！");
                            ToastView toast = new ToastView(mContext, mContext.getString(R.string.session_expires_tips));
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            ProfileFragment.isRefresh=true;
                            Intent intent = new Intent(mContext, SigninActivity.class);
                            mContext.startActivity(intent);
                            
                            shared = mContext.getSharedPreferences("userInfo", 0); 
                    		editor = shared.edit();
                            
                            editor.putString("uid", "");
        		            editor.putString("sid", "");
        		            editor.commit();
        		            SESSION.getInstance().uid = shared.getString("uid", "");
        		            SESSION.getInstance().sid = shared.getString("sid", "");
                            
                        
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }

            }

        };

       
        cb.url(":8080/shop/user_getUserInfo.action").type(JSONObject.class).cookie("JSESSIONID", shared.getString("uid", null));
        MyProgressDialog pd = new MyProgressDialog(mContext,mContext.getResources().getString(R.string.hold_on));
        aq.progress(pd.mDialog).ajax(cb);

    }

}
