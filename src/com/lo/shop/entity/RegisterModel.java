package com.lo.shop.entity;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.res.Resources;

import com.lo.shop.view.MyProgressDialog;
import com.lo.shop.view.ToastView;
import com.lo.shop.entity.BaseModel;
import com.lo.shop.entity.BaseCallback;
import com.lo.shop.R;
import com.lo.shop.protocol.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.widget.Toast;

import com.external.androidquery.callback.AjaxStatus;

public class RegisterModel extends BaseModel {

    private SharedPreferences shared;
    private SharedPreferences.Editor editor;
    public ArrayList<SIGNUPFILEDS> signupfiledslist = new ArrayList<SIGNUPFILEDS>();
    public STATUS responseStatus;
     static usersignupRequest request = new usersignupRequest();
     static USER user = new USER();
    public RegisterModel(Context context) {
        super(context);

        shared = context.getSharedPreferences("userInfo", 0);
        editor = shared.edit();
    }

    public void signup(String name,String tname, String password, String email, String address, String mobile, ArrayList<FIELD> fields) {
        

        BaseCallback<JSONObject> cb = new BaseCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {

                RegisterModel.this.callback(url, jo, status);
                try {
                    usersignupResponse response = new usersignupResponse();
                    response.fromJson(jo);
                    System.out.println(jo);
                    if (jo != null) {
                       if(jo.optString("exist").toString().equals("true"))
                        	
                    	   
                       Toast.makeText(mContext, "用户名重复", 1).show();
                       else
                    	   signupFields(user);
                    }

                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }

        };
        user.userName = name;
        user.trueName = tname;
        user.password = password;
        user.email = email;
        user.address = address;
        user.mobile = mobile;
       
        cb.url("user_existUserWithUserName.action").type(JSONObject.class).param("userName",name);
        MyProgressDialog pd = new MyProgressDialog(mContext,mContext.getResources().getString(R.string.hold_on));
        aq.progress(pd.mDialog).ajax(cb);

    }

    public void signupFields(USER user) {
        usersignupFieldsRequest request = new usersignupFieldsRequest();

        BaseCallback<JSONObject> cb = new BaseCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {

                RegisterModel.this.callback(url, jo, status);
                try {
                    usersignupFieldsResponse response = new usersignupFieldsResponse();
                    response.fromJson(jo);
                    System.out.println("注册信息返回"+jo);
                    if (jo != null) {
                    	if(jo.optString("success").toString().equals("true"))
                    		Toast.makeText(mContext, "注册成功!", 1).show();
                    	
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }

        };
        
       
        cb.url("user_register.action").type(JSONObject.class).param("user.userName", user.userName).param("user.trueName", user.trueName).param("user.password", user.password).param("user.address", user.address).param("user.mobile", user.mobile);
        MyProgressDialog pd = new MyProgressDialog(mContext,mContext.getResources().getString(R.string.hold_on));
        aq.progress(pd.mDialog).ajax(cb);

    }

}
