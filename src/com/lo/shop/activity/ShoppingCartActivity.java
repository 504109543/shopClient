package com.lo.shop.activity;


import java.util.ArrayList;

import android.content.res.Resources;

import com.lo.shop.protocol.ApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.XListView;
import com.external.maxwin.view.XListViewCart;
import com.external.maxwin.view.XListViewCart.IXListViewListenerCart;
import com.lo.shop.activity.BaseActivity;
import com.lo.shop.view.MyDialog;
import com.lo.shop.view.ToastView;
import com.lo.shop.R;
import com.lo.shop.adapter.ShoppingCartAdapter;
import com.lo.shop.entity.AddressModel;
import com.lo.shop.entity.BusinessResponse;
import com.lo.shop.entity.OrderModel;
import com.lo.shop.entity.ShoppingCartModel;

public class ShoppingCartActivity extends BaseActivity  implements BusinessResponse, IXListViewListenerCart
{
    private View footerView;

    private TextView footer_total;
    private FrameLayout footer_balance;
    private FrameLayout shop_car_null;
    private FrameLayout shop_car_isnot;
    private ImageView cart_icon;

    private XListViewCart xlistView;
    private ShoppingCartAdapter shopCarAdapter;
    private ArrayList<String> items = new ArrayList<String>();
    private ShoppingCartModel shoppingCartModel;
    public Handler messageHandler;
    //private ImageView back;
    private MyDialog mDialog;
    private OrderModel orderModel;
    private AddressModel addressModel;
    private ProgressDialog pd = null;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {        
        Resources resource = (Resources) getBaseContext().getResources();
        String suc=resource.getString(R.string.successful_operation);
        String pay=resource.getString(R.string.pay_or_not);
        final String per=resource.getString(R.string.personal_center);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
        {
            shoppingCartModel.cartList();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_cart);
        shop_car_null = (FrameLayout) findViewById(R.id.shop_car_null);
        shop_car_isnot = (FrameLayout) findViewById(R.id.shop_car_isnot);
        final Resources resource = (Resources) getBaseContext().getResources();

        xlistView = (XListViewCart) findViewById(R.id.shop_car_list);
        xlistView.setPullLoadEnable(false);
        xlistView.setRefreshTime();
        xlistView.setXListViewListener(this,1);

        footerView = LayoutInflater.from(this).inflate(R.layout.shopping_car_footer, null);
        footer_total = (TextView) footerView.findViewById(R.id.shop_car_footer_total);
        footer_balance = (FrameLayout) footerView.findViewById(R.id.shop_car_footer_balance);
        cart_icon = (ImageView) footerView.findViewById(R.id.shop_car_footer_balance_cart_icon);

        xlistView.addFooterView(footerView);

        addressModel = new AddressModel(this);
		addressModel.addResponseListener(this);
        
        footer_balance.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            	
            	/*addressModel.getAddressList();*/
            	
            	Intent intent = new Intent(ShoppingCartActivity.this, CheckOutActivity.class);
				startActivityForResult(intent, 1);
            }
        });

        shoppingCartModel = new ShoppingCartModel(this);
        shoppingCartModel.addResponseListener(this);
        shoppingCartModel.cartList();

        messageHandler = new Handler() {

            public void handleMessage(Message msg) {

                if (msg.what == ShoppingCartAdapter.CART_CHANGE_REMOVE) {
                    Integer rec_id = (Integer) msg.arg1;
                    shoppingCartModel.deleteGoods(rec_id);
                }
                if (msg.what == ShoppingCartAdapter.CART_CHANGE_MODIFY) {
                    int rec_id =  msg.arg1;
                    int new_number = msg.arg2;
                    shoppingCartModel.updateGoods(rec_id, new_number);
                }
                if (msg.what == ShoppingCartAdapter.CART_CHANGE_CHANGE1) {
                	footer_balance.setEnabled(false);
                	footer_balance.setBackgroundResource(R.drawable.item_info_add_cart_desabled_btn_red_b);
                	cart_icon.setImageResource(R.drawable.shopping_cart_acc_cart_icon);
                }
                if (msg.what == ShoppingCartAdapter.CART_CHANGE_CHANGE2) {
                	footer_balance.setEnabled(true);
                	footer_balance.setBackgroundResource(R.drawable.button_red);
                	cart_icon.setImageResource(R.drawable.shopping_cart_acc_cart_icon);
                }
            } 
        };

        /*back = (ImageView) findViewById(R.id.top_view_back);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                finish();
                overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);

            }
        });*/
        
        orderModel = new OrderModel(this);
		orderModel.addResponseListener(this);

    }

    @Override
    public void onLoadMore(int id) {        

    }

    public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status) {
        if (url.endsWith(ApiInterface.CART_LIST)) {
            xlistView.stopRefresh();
        	xlistView.setRefreshTime();
            setShopCart();
        } else if(url.endsWith(ApiInterface.CART_DELETE)) {
            updataCar();
        } else if(url.endsWith(ApiInterface.CART_UPDATE)) {
            updataCar();
        }
    }

    @Override
    public void onPause() {        
        super.onPause();
       
    }

    @Override
    public void onRefresh(int id) {        
        shoppingCartModel.cartList();
    }

    @Override
    public void onResume() {        
        super.onResume();

       
    }
    public void setShopCart() {

        if(shoppingCartModel.goods_list.size() == 0) {
            shop_car_null.setVisibility(View.VISIBLE);
            shop_car_isnot.setVisibility(View.GONE);
        } else {
            footer_total.setText(shoppingCartModel.total.goods_price);

            shop_car_isnot.setVisibility(View.VISIBLE);
            shop_car_null.setVisibility(View.GONE);

            shopCarAdapter = new ShoppingCartAdapter(this, shoppingCartModel.goods_list);
            xlistView.setAdapter(shopCarAdapter);

            footer_balance.setEnabled(true);
        	footer_balance.setBackgroundResource(R.drawable.button_red);
        	cart_icon.setImageResource(R.drawable.shopping_cart_acc_cart_icon);
            
            shopCarAdapter.parentHandler = messageHandler;
        }

    }

    public void updataCar() {
        shoppingCartModel.goods_list.clear();
        shoppingCartModel.cartList();
    }
}
