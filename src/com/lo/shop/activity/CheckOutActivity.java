package com.lo.shop.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;

import com.lo.shop.activity.BaseActivity;
import com.lo.shop.entity.BusinessResponse;
import com.lo.shop.entity.OrderModel;
import com.lo.shop.entity.ShoppingCartModel;
import com.lo.shop.view.MyDialog;
import com.lo.shop.protocol.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.external.androidquery.callback.AjaxStatus;
import com.lo.shop.R;
import com.lo.shop.view.ToastView;

import java.util.ArrayList;

public class CheckOutActivity extends BaseActivity implements OnClickListener, BusinessResponse {
	
	private SharedPreferences shared;
    private SharedPreferences.Editor editor;
	private TextView title;
	private ImageView back;
	private LinearLayout user;
	private TextView name;
	private TextView phoneNum;
	private TextView address;
	private TextView invoice_message;
	private TextView score_num;
	private LinearLayout body;
	private TextView fees;
	private TextView coupon;
	private TextView totalPriceTextView;
	private FrameLayout submit;
	private  ShoppingCartModel shoppingCartModel;
	private float totalGoodsPrice;  //总价格
	private String paymentJSONString;
    private MyDialog mDialog;
    private OrderModel orderModel;    
    private final static int REQUEST_ADDRESS_LIST = 1;
    
    
	private void handleIntent(Intent intent)
    {
        String action = intent.getAction();
    }

    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {		
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == REQUEST_ADDRESS_LIST)
        {
			if (data != null)
            {
				shoppingCartModel.checkOrder();
			}
		}
        /*
         * 支付返回字符串:success、fail、cancel
         *      分别代表支付成功，支付失败，支付取消
         */
            String str = data.getExtras().getString("pay_result");
            if (str.equalsIgnoreCase("success")) {
                Resources resource = getResources();
                String exit = resource.getString(R.string.pay_success);
                String exiten = resource.getString(R.string.continue_shopping_or_not);
                final MyDialog mDialog = new MyDialog(CheckOutActivity.this, exit, exiten);
                mDialog.show();
                mDialog.positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.dismiss();
                       /* Intent it = new Intent(CheckOutActivity.this, EcmobileMainActivity.class);
                        startActivity(it);
                        finish();*/

                    }
                });
                mDialog.negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.dismiss();
                        Intent intent = new Intent(CheckOutActivity.this, HistoryActivity.class);
                        intent.putExtra("flag", "await_ship");
                        startActivity(intent);
                        finish();

                    }
                });
            } else if (str.equalsIgnoreCase("fail") || str.equals("cancel")) {
                ToastView toast = new ToastView(CheckOutActivity.this, getResources().getString(R.string.pay_failed));
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                Intent intent = new Intent(CheckOutActivity.this, HistoryActivity.class);
                intent.putExtra("flag", "await_pay");
                startActivity(intent);
                finish();
            }
        }
	
	@Override
	public void onClick(View v) {		
		Intent intent;
		switch(v.getId()) {
		case R.id.balance_user:
			intent = new Intent(this, AddressListActivity.class);
			intent.putExtra("flag", 1);
			startActivityForResult(intent, REQUEST_ADDRESS_LIST);
			break;
		
		case R.id.balance_submit:
          
                    shoppingCartModel.flowDone();
			break;
		}
		
	}
    @Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		Intent intent = this.getIntent(); 
		shoppingCartModel=(ShoppingCartModel)intent.getSerializableExtra("shoppingCartModel");	
		setContentView(R.layout.check_out);
		shared = this.getSharedPreferences("userInfo", 0);
        editor = shared.edit();
        title = (TextView) findViewById(R.id.top_view_text);
        Resources resource = (Resources) getBaseContext().getResources();
        String set=resource.getString(R.string.shopcarfooter_settleaccounts);
		title.setText(set);	
		back = (ImageView) findViewById(R.id.top_view_back);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
	            finish(); 
			}
		});
		user = (LinearLayout) findViewById(R.id.balance_user);
		name = (TextView) findViewById(R.id.balance_name);
		phoneNum = (TextView) findViewById(R.id.balance_phoneNum);
		address = (TextView) findViewById(R.id.balance_address);
		fees = (TextView) findViewById(R.id.balance_fees);
        totalPriceTextView = (TextView) findViewById(R.id.balance_total);
		submit = (FrameLayout) findViewById(R.id.balance_submit);
		body = (LinearLayout) findViewById(R.id.balance_body);
		user.setOnClickListener(this);	
		submit.setOnClickListener(this);	
		//System.out.println("检查");
        if(null == shoppingCartModel)
        {
            shoppingCartModel = new ShoppingCartModel(this);
            shoppingCartModel.addResponseListener(this);
            shoppingCartModel.checkOrder();    
        }
        else
        {      	
            setInfo();
        }		
	}
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

        @Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {		
		if(url.endsWith("shopping_list.action"))
        {         
				setInfo();
		}
	}

	@Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume(); 
    }
    void refreshTotalPrice()
    {
        float total_price_show = totalGoodsPrice;
        totalPriceTextView.setText("￥"+total_price_show);
    }
    public void setInfo() {
        totalGoodsPrice = 0;
        //System.out.println("各种数据"+shared.getString("trueName", null)+"还有"+shared.getString("mobile", null)+""+shared.getString("address", null));	
		name.setText(shared.getString("trueName", null));
		phoneNum.setText(shared.getString("mobile", null));
		address.setText(shared.getString("address", null));
		for(int i=0;i<shoppingCartModel.goods_list.size();i++)
        {
			View view = LayoutInflater.from(this).inflate(R.layout.check_out_body_item, null);
			TextView goods_name = (TextView) view.findViewById(R.id.body_goods_name);
			TextView goods_num = (TextView) view.findViewById(R.id.body_goods_num);
			TextView goods_total = (TextView) view.findViewById(R.id.body_goods_total);
			goods_name.setText(shoppingCartModel.goods_list.get(i).goods_name);
			goods_num.setText("X "+shoppingCartModel.goods_list.get(i).goods_number);
			goods_total.setText("￥"+shoppingCartModel.goods_list.get(i).totalprice);
			body.addView(view);
           // totalGoodsPrice+=Float.valueOf(shoppingCartModel.balance_goods_list.get(i).subtotal);
		}
        totalPriceTextView.setText("￥"+shoppingCartModel.totalAll);

	}
    
}
