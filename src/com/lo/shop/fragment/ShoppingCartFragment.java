package com.lo.shop.fragment;

import java.util.ArrayList;





import com.lo.shop.protocol.ApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.XListViewCart;
import com.external.maxwin.view.XListViewCart.IXListViewListenerCart;
import com.lo.shop.entity.AddressModel;
import com.lo.shop.entity.BusinessResponse;
import com.lo.shop.entity.OrderModel;
import com.lo.shop.entity.ShoppingCartModel;
import com.lo.shop.fragment.BaseFragment;
import com.lo.shop.view.MyDialog;
import com.lo.shop.view.ToastView;
import com.lo.shop.R;
import com.lo.shop.activity.CheckOutActivity;
//import com.lo.shop.activity.PayWebActivity;
import com.lo.shop.adapter.ShoppingCartAdapter;
/**
 * 购物车
 */
public class ShoppingCartFragment extends BaseFragment implements BusinessResponse, IXListViewListenerCart{
	
	private View view;
	private View footerView;
	
	private TextView footer_total;
	private TextView mTitleTv;
	private FrameLayout footer_balance;
	private FrameLayout shop_car_null;
	private FrameLayout shop_car_isnot;
	private ImageView cart_icon;
	
	private XListViewCart xlistView;
	private ShoppingCartAdapter shopCarAdapter;
	private ArrayList<String> items = new ArrayList<String>();
	
	private ShoppingCartModel shoppingCartModel;
    public  Handler messageHandler;
    private ImageView back;
    
    private AddressModel addressModel;
    private OrderModel orderModel;

    private SharedPreferences shared;
	private SharedPreferences.Editor editor;
	
	@Override
	public String getFragmentName() {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressLint("NewApi")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		 
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1)
        {
		    shoppingCartModel.cartList();
		} 
	}

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		 
        final Resources resource = this.getResources();

		view = inflater.inflate(R.layout.shopping_cart, null);
		back = (ImageView) view.findViewById(R.id.top_view_back);
        back.setVisibility(View.GONE);
		mTitleTv = (TextView) view.findViewById(R.id.top_view_text);
		mTitleTv.setText(R.string.collect);
		
		shared = getActivity().getSharedPreferences("userInfo", 0); 
		editor = shared.edit();

		shop_car_null = (FrameLayout) view.findViewById(R.id.shop_car_null);
		shop_car_isnot = (FrameLayout) view.findViewById(R.id.shop_car_isnot);
		
		xlistView = (XListViewCart) view.findViewById(R.id.shop_car_list);
		xlistView.setPullLoadEnable(false);
		xlistView.setRefreshTime();
		xlistView.setXListViewListener(this,1);
		
		footerView = LayoutInflater.from(getActivity()).inflate(R.layout.shopping_car_footer, null);
		footer_total = (TextView) footerView.findViewById(R.id.shop_car_footer_total);
		footer_balance = (FrameLayout) footerView.findViewById(R.id.shop_car_footer_balance);
		cart_icon = (ImageView) footerView.findViewById(R.id.shop_car_footer_balance_cart_icon);
		
		xlistView.addFooterView(footerView);
		
		addressModel = new AddressModel(getActivity());
		addressModel.addResponseListener(this);

		footer_balance.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(getActivity(), CheckOutActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("shoppingCartModel", shoppingCartModel);
				startActivityForResult(intent, 1);
                getActivity().overridePendingTransition(R.anim.push_right_in,
                        R.anim.push_right_out);
			}
		});

        if (null == shoppingCartModel)
        {
            shoppingCartModel = new ShoppingCartModel(getActivity());
        }
		
        String uid = shared.getString("uid", "");
       
		if (uid.equals("")) {
			shop_car_null.setVisibility(View.VISIBLE);
			shop_car_isnot.setVisibility(View.GONE);
		} else {
			shoppingCartModel.addResponseListener(this);
			shoppingCartModel.cartList();
		}
        
        messageHandler = new Handler(){

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

       
        
        
        orderModel = new OrderModel(getActivity());
		orderModel.addResponseListener(this);
		
		return view;
	}

	@Override
	public void onLoadMore(int id) {
		 
		
	}


	@SuppressLint("NewApi")
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status) {
		if (url.endsWith("shopping_list.action")) {
			xlistView.stopRefresh();
			xlistView.setRefreshTime();
			//System.out.println(jo.optJSONArray("shoppingCartItems"));
			if(jo == null){
				shop_car_null.setVisibility(View.VISIBLE);
			    shop_car_isnot.setVisibility(View.GONE);}
			else{
				System.out.println("执行");
			    setShopCart();
			    }
			
		} else if(url.endsWith("removeShoppingCartItem.action")) {
			updataCar();
		} else if(url.endsWith("updateShoppingCartItem.action")) {
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
    	shoppingCartModel.cartList();
        super.onResume();
        
    }


	public void setShopCart() {
		
		if(shoppingCartModel == null) {
			
			shop_car_null.setVisibility(View.VISIBLE);
			shop_car_isnot.setVisibility(View.GONE);
		} else {
			footer_total.setText(shoppingCartModel.totalAll+"");
			
			shop_car_isnot.setVisibility(View.VISIBLE);
			shop_car_null.setVisibility(View.GONE);
			if (null == shopCarAdapter)
            {
                shopCarAdapter = new ShoppingCartAdapter(getActivity(), shoppingCartModel.getInstance().goods_list);
            }

			xlistView.setAdapter(shopCarAdapter);
            shopCarAdapter.notifyDataSetChanged();
            
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
