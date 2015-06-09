package com.lo.shop.entity;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

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
import com.lo.shop.view.ToastView;

public class ShoppingCartModel extends BaseModel implements Serializable{

    public static ShoppingCartModel getInstance() {
        return instance;
    }
    public ArrayList<GOODS_LIST> goods_list = new ArrayList<GOODS_LIST>();
  
    public int goods_num,totalAll=0;
    public TOTAL total;
    public int[] total1;
    // 结算（提交订单前的订单预览）
    public ADDRESS address;
    public ArrayList<GOODS_LIST> balance_goods_list = new ArrayList<GOODS_LIST>();
    


    public String orderInfoJsonString;
    private static ShoppingCartModel instance;
    private SharedPreferences shared;
    private SharedPreferences.Editor editor;

    public int order_id;

    public ShoppingCartModel() {
        super();
    }

    public ShoppingCartModel(Context context) {
        super(context);
        instance = this;
        shared = context.getSharedPreferences("userInfo", 0);
        editor = shared.edit();
    }

    

    


    // 获取购物车列表
    public void cartList() {
        cartlistRequest request = new cartlistRequest();
        BaseCallback<JSONObject> cb = new BaseCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
            	//System.out.println("SHOPIPINGCART111"+jo);
                //ShoppingCartModel.this.callback(url, jo, status);
                if (null == jo)
                {
                    
                	try {
						ShoppingCartModel.this.OnMessageResponse(url, jo, status);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }
                else{
                try {
                    cartlistResponse response = new cartlistResponse();
                    response.fromJson(jo);
                   // System.out.println("SHOPIPINGCART"+jo);
                            CART_LIST_DATA data = response.shoppingCartItems;
                            //total = data.total;
                            ArrayList<GOODS_LIST> goods_lists = data.goods_list;
                            goods_list.clear();
                            ShoppingCartModel.this.goods_num = 0;
                            ShoppingCartModel.this.totalAll = 0;
                            if (null != goods_lists && goods_lists.size() > 0) {
                                goods_list.clear();
                                for (int i = 0; i < goods_lists.size(); i++) {
                                    GOODS_LIST goods_list_Item = goods_lists.get(i);
                                    goods_list_Item.totalprice=""+Integer.parseInt(goods_list_Item.goods_number)*Integer.parseInt(goods_list_Item.goods_price);
                                    goods_list.add(goods_list_Item);
                                    ShoppingCartModel.this.goods_num += Integer.valueOf(goods_list_Item.goods_number);
                                    ShoppingCartModel.this.totalAll+=Integer.parseInt(goods_list_Item.totalprice);
                                }
                            }
                            ShoppingCartModel.this.OnMessageResponse(url, jo, status);
                    }
                   
                 catch (JSONException e) {

                    e.printStackTrace();
                }
            }

        }
        };
    
        

       
        cb.url("shopping_list.action").
        type(JSONObject.class).
        cookie("JSESSIONID", shared.getString("uid", null));;
        MyProgressDialog pd = new MyProgressDialog(mContext,mContext.getResources().getString(R.string.hold_on));
        aq.progress(pd.mDialog).ajax(cb);

    }

    public void checkOrder() {
        flowcheckOrderRequest request = new flowcheckOrderRequest();
        BaseCallback<JSONObject> cb = new BaseCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {

                ShoppingCartModel.this.callback(url, jo, status);
                try {
                    cartlistResponse response = new cartlistResponse();
                    response.fromJson(jo);
                    //System.out.println("SHOPIPINGCART"+jo);
                    if (null != jo) {
                      
                            CART_LIST_DATA data = response.shoppingCartItems;
                            ArrayList<GOODS_LIST> goods_lists = data.goods_list;
                            goods_list.clear();
                            ShoppingCartModel.this.goods_num = 0;
                            ShoppingCartModel.this.totalAll = 0;
                            if (null != goods_lists && goods_lists.size() > 0) {
                                goods_list.clear();
                                for (int i = 0; i < goods_lists.size(); i++) {
                                    GOODS_LIST goods_list_Item = goods_lists.get(i);
                                    goods_list_Item.totalprice=""+Integer.parseInt(goods_list_Item.goods_number)*Integer.parseInt(goods_list_Item.goods_price);
                                    goods_list.add(goods_list_Item);
                                    ShoppingCartModel.this.goods_num += Integer.valueOf(goods_list_Item.goods_number);
                                    ShoppingCartModel.this.totalAll+=Integer.parseInt(goods_list_Item.totalprice);
                                }
                            }
                            ShoppingCartModel.this.OnMessageResponse(url, jo, status);

                    }
                }
                 catch (JSONException e) {

                    e.printStackTrace();
                }
            }

        };

        cb.url("shopping_list.action").type(JSONObject.class).cookie("JSESSIONID", shared.getString("uid", null));;
        MyProgressDialog pd = new MyProgressDialog(mContext, mContext.getResources().getString(R.string.hold_on));
        aq.progress(pd.mDialog).ajax(cb);

    }

    public void deleteGoods(int productId) {
        cartdeleteRequest request = new cartdeleteRequest();
        BaseCallback<JSONObject> cb = new BaseCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {

                ShoppingCartModel.this.callback(url, jo, status);
                try {
                	System.out.println("删除商品"+jo);
                    cartdeleteResponse response = new cartdeleteResponse();
                    response.fromJson(jo);
                    if (jo != null) {
                        if (response.status.succeed == 1) {

                            

                        }ShoppingCartModel.this.OnMessageResponse(url, jo, status);
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }

        };
        
        cb.url("shopping_removeShoppingCartItem.action").
        type(JSONObject.class).
        param("productId", productId).       
        cookie("JSESSIONID", shared.getString("uid", null));
        MyProgressDialog pd = new MyProgressDialog(mContext, mContext.getResources().getString(R.string.hold_on));
        aq.progress(pd.mDialog).ajax(cb);

    }

    // 订单生成
    public void flowDone() {
        flowdoneRequest request = new flowdoneRequest();

        BaseCallback<JSONObject> cb = new BaseCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {

                ShoppingCartModel.this.callback(url, jo, status);
                try {
                          if (jo != null) {
                        	  System.out.println("订单提交"+jo.optString("success"));
                        	  if(jo.optString("success").equals("true"))
                              	Toast.makeText(mContext, "购买成功", 0).show();
                              	else
                              		Toast.makeText(mContext, "购买失败，请重新购买", 0).show();
                        	//System.out.println("订单提交"+jo);
                            ShoppingCartModel.this.OnMessageResponse(url, jo, status);
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }

        };

        cb.url("order_save.action").type(JSONObject.class).cookie("JSESSIONID", shared.getString("uid", null));;
        MyProgressDialog pd = new MyProgressDialog(mContext, mContext.getResources().getString(R.string.hold_on));
        aq.progress(pd.mDialog).ajax(cb);

    }

    

    public void updateGoods(int productId, int count) {
        cartupdateRequest request = new cartupdateRequest();
        BaseCallback<JSONObject> cb = new BaseCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {

                ShoppingCartModel.this.callback(url, jo, status);
                try {
                	System.out.println("购物车更新"+jo);
                    cartdeleteResponse response = new cartdeleteResponse();
                    response.fromJson(jo);
                    if (jo != null) {
                        if (response.status.succeed == 1) {

                        }
                        ShoppingCartModel.this.OnMessageResponse(url, jo, status);
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }

        };

       
        cb.url("shopping_updateShoppingCartItem.action").
        type(JSONObject.class).
        param("productId", productId).
        param("count", count).
        cookie("JSESSIONID", shared.getString("uid", null));
        MyProgressDialog pd = new MyProgressDialog(mContext, mContext.getResources().getString(R.string.hold_on));
        aq.progress(pd.mDialog).ajax(cb);

    }

}
