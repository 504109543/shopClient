package com.lo.shop.entity;



import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.res.Resources;

import com.lo.shop.utils.FrameworkConst;
import com.lo.shop.view.MyProgressDialog;
import com.lo.shop.entity.BaseCallback;
import com.lo.shop.R;
import com.lo.shop.protocol.*;

import org.apache.http.cookie.Cookie;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Gravity;

import com.external.androidquery.callback.AjaxStatus;
import com.lo.shop.view.ToastView;


public class LoginModel extends BaseModel {

    private SharedPreferences shared;
    private SharedPreferences.Editor editor;
    public STATUS responseStatus;
    public AjaxStatus aj;

    public LoginModel(Context context) {
        super(context);
        shared = context.getSharedPreferences("userInfo", 0);
        editor = shared.edit();
    }

    public void login(String name, String password) {
    	
        usersigninRequest request = new usersigninRequest();       
        BaseCallback<JSONObject> cb = new BaseCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
            	//System.out.println("地址:"+ url+"  json值:     "+jo);
                LoginModel.this.callback(url, jo, status);
                try {
                    usersigninResponse response = new usersigninResponse();
                    response.fromJson(jo);                 
                    if (jo != null) {                    	                  	
                            SIGNIN_DATA data = response.data;                  
                            SESSION session = data.session;
                            SESSION.getInstance().uid=session.uid;
                            //SESSION.getInstance().sid = session.sid;
                            USER user = data.user;            
                            //System.out.println(user.name);
                            user.save();
                            editor.putString("uid", session.uid);
                           // editor.putString("sid", session.sid);
                            editor.putString("email",user.email);
                            editor.putString("address",user.address);
                            editor.putString("trueName",user.name);
                            editor.putString("mobile",user.mobile);
                            editor.commit();
                        //}
                        LoginModel.this.OnMessageResponse(url, jo, status);
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }

        };
        request.name = name;
        request.password = password;       
        Map<String, String> params = new HashMap<String, String>();
        params.put("user.userName", name);
		params.put("user.password", password);
		System.out.println(params.toString());
		
        cb.url(":8080/shop/user_login.action").type(JSONObject.class).params(params);
        
        MyProgressDialog pd = new MyProgressDialog(mContext,mContext.getResources().getString(R.string.hold_on));
        aq.progress(pd.mDialog).ajax(cb);

    }
}
