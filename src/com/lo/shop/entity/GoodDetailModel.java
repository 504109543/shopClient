package com.lo.shop.entity;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.external.androidquery.callback.AjaxStatus;
import com.lo.shop.view.MyProgressDialog;
import com.lo.shop.view.ToastView;
import com.lo.shop.entity.BaseModel;
import com.lo.shop.entity.BaseCallback;
import com.lo.shop.R;
import com.lo.shop.protocol.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class GoodDetailModel extends BaseModel {
   // public ArrayList<PHOTO> photoList = new ArrayList<PHOTO>();
    public String goodId;
    public GOODS goodDetail = new GOODS();
    private SharedPreferences shared;
    private SharedPreferences.Editor editor;
    

    public String goodsWeb;

    public GoodDetailModel(Context context) {
        super(context);
       
            shared = context.getSharedPreferences("userInfo", 0);
            editor = shared.edit();
        
    }

    public void cartCreate(int goodId) {
        cartcreateRequest request = new cartcreateRequest();
        BaseCallback<JSONObject> cb = new BaseCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {

                GoodDetailModel.this.callback(url, jo, status);
               // System.out.println("成功"+jo);
                try {
                    categoryResponse response = new categoryResponse();
                    response.fromJson(jo);
                    if (jo != null) {
                       
                            GoodDetailModel.this.OnMessageResponse(url, jo, status);
                        
                    }
                } catch (JSONException e) {
                    // TODO: handle exception
                }

            }

        };
        cb.url("shopping_addShoppingCartItem.action").type(JSONObject.class).cookie("JSESSIONID", shared.getString("uid", null)).param("productId", goodId);
        MyProgressDialog pd = new MyProgressDialog(mContext, mContext.getResources().getString(R.string.hold_on));
        aq.progress(pd.mDialog).ajax(cb);
    }

    

    public void fetchGoodDetail(int goodId) {
        BaseCallback<JSONObject> cb = new BaseCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {

                GoodDetailModel.this.callback(url, jo, status);
                //System.out.println("地址:"+ url+"  json值:     "+jo+"状态:  "+status);
                try {
                    goodsResponse response = new goodsResponse();               
                    response.fromJson(jo);                                    
                    if (jo != null) {
                            GOODS goods = response.data;       
                            
                            if (null != goods) {
                                GoodDetailModel.this.goodDetail = goods;                                 
                                GoodDetailModel.this.OnMessageResponse(url, jo, status);                               
                            }
                    }                    
                } catch (JSONException e) {
                    // TODO: handle exception
                	System.out.println("没有数据");
                }

            }
        };
       
        cb.url("product_showProduct.action?productId="+goodId).type(JSONObject.class).cookie("JSESSIONID", shared.getString("uid", null));;
        
        MyProgressDialog pd = new MyProgressDialog(mContext, mContext.getResources().getString(R.string.hold_on));
        aq.progress(pd.mDialog).ajax(cb);
        
    }

   
}
