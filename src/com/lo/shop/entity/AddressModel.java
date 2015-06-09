package com.lo.shop.entity;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.lo.shop.view.MyProgressDialog;
import com.lo.shop.R;
import com.lo.shop.entity.BaseModel;
import com.lo.shop.entity.BaseCallback;
import com.lo.shop.protocol.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;

import com.external.androidquery.callback.AjaxStatus;

public class AddressModel extends BaseModel {

    public ArrayList<ADDRESS> addressList = new ArrayList<ADDRESS>();
    
    public ADDRESS address;
    private SharedPreferences shared;
    private SharedPreferences.Editor editor;
    public AddressModel(Context context) {
        super(context);
        shared = context.getSharedPreferences("userInfo", 0);
        editor = shared.edit();
    }
    // 获取地址列表
    public void getAddressList() {

        final addresslistRequest request = new addresslistRequest();
        BaseCallback<JSONObject> cb = new BaseCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                try {
                    AddressModel.this.callback(url, jo, status);
                    if (jo != null) {
                        addresslistResponse response = new addresslistResponse();
                        response.fromJson(jo);
                        System.out.println("直接返回了");
                        if (response.status.succeed == 1) {
                           addressList.clear();
                           ArrayList<ADDRESS> data = response.data;
                            if (null != data && data.size() > 0) {
                                addressList.addAll(data);
                            }
                        }
                       AddressModel.this.OnMessageResponse(url, jo, status);
                    }

                } catch (JSONException e) {

                    e.printStackTrace();
                }

            }

        };
        request.session=SESSION.getInstance();
        Map<String, String> params = new HashMap<String, String>();
        try {
            params.put("json", request.toJson().toString());
        } catch (JSONException e) {
            // TODO: handle exception
        }
        cb.url("address").type(JSONObject.class).params(params);
        MyProgressDialog pd = new MyProgressDialog(mContext,mContext.getResources().getString(R.string.hold_on));
        aq.progress(pd.mDialog).ajax(cb);

    }

    

}
