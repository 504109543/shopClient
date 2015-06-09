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

import android.R.integer;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;

import com.external.androidquery.callback.AjaxStatus;

public class OrderModel extends BaseModel {

    private int page = 1;
    private SharedPreferences shared;
    private SharedPreferences.Editor editor;
    public ArrayList<GOODORDER> order_list = new ArrayList<GOODORDER>();
    public String pay_wap = "";
    public String pay_online = "";
    public String upop_tn = "";
    public String shipping_name;

    public OrderModel(Context context) {
        super(context);
        shared = context.getSharedPreferences("userInfo", 0);
        editor = shared.edit();
    }

    // 确认收货
    public void affirmReceived(String order_id) {
        orderaffirmReceivedRequest request = new orderaffirmReceivedRequest();
        BaseCallback<JSONObject> cb = new BaseCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {

                OrderModel.this.callback(url, jo, status);
                try {
                    orderaffirmReceivedResponse response = new orderaffirmReceivedResponse();
                    response.fromJson(jo);
                    if (jo != null) {
                        if (jo.optString("success").toString()=="true") {
                            OrderModel.this.OnMessageResponse(url, jo, status);
                        }
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        };

        
        cb.url("order_confirmReceive.action").type(JSONObject.class).param("status", 4).param("orderNo", order_id);
        aq.ajax(cb);

    }

    public void getOrder(String type) {
        page = 1;
        orderlistRequest request = new orderlistRequest();

        BaseCallback<JSONObject> cb = new BaseCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {

                OrderModel.this.callback(url, jo, status);
                //System.out.println("订单"+jo);
                try {
                    orderlistResponse response = new orderlistResponse();
                    response.fromJson(jo);
                    if (jo != null) {

                       // if (response.status.succeed == 1) {
                            order_list.clear();
                            ArrayList<GOODORDER> data = response.data;
                            if (null != data && data.size() > 0) {
                                order_list.addAll(data);
                                
                            }
                           
                        //}
                        OrderModel.this.OnMessageResponse(url, jo, status);
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }

            }

        };

       

        cb.url("order_findOrder.action").type(JSONObject.class).cookie("JSESSIONID", shared.getString("uid", null));
        MyProgressDialog pd = new MyProgressDialog(mContext, mContext.getResources().getString(R.string.hold_on));
        aq.progress(pd.mDialog).ajax(cb);

    }

    public void getOrderMore(String type) {

        final orderlistRequest request = new orderlistRequest();

        BaseCallback<JSONObject> cb = new BaseCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {

                OrderModel.this.callback(url, jo, status);

                try {
                    orderlistResponse response = new orderlistResponse();
                    response.fromJson(jo);
                    if (jo != null) {

                        if (response.status.succeed == 1) {

                            ArrayList<GOODORDER> data = response.data;

                            if (null != data && data.size() > 0) {

                                for (int i = 0; i < data.size(); i++) {
                                    order_list.addAll(data);
                                }
                            }

                          

                        }
                    }
                    OrderModel.this.OnMessageResponse(url, jo, status);

                } catch (JSONException e) {

                    e.printStackTrace();
                }

            }

        };

        SESSION session = SESSION.getInstance();
       
      
        request.session = session;
       
        request.type = type;

        Map<String, String> params = new HashMap<String, String>();
        try {
            params.put("json", request.toJson().toString());
        } catch (JSONException e) {
            // TODO: handle exception
        }

        cb.url(ApiInterface.ORDER_LIST).type(JSONObject.class).params(params);
        aq.ajax(cb);

    }

   

    
}
