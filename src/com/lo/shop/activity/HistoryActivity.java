package com.lo.shop.activity;



import java.util.ArrayList;

import android.app.Dialog;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.*;

import com.lo.shop.activity.BaseActivity;
import com.lo.shop.protocol.ApiInterface;
import com.lo.shop.protocol.GOODORDER;
import com.lo.shop.protocol.SESSION;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;

import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.XListView;
import com.external.maxwin.view.XListView.IXListViewListener;
import com.lo.shop.R;
import com.lo.shop.view.MyDialog;
import com.lo.shop.view.ToastView;
import com.lo.shop.adapter.ShoppingCartAdapter;
import com.lo.shop.adapter.HistoryAdapter;
import com.lo.shop.entity.BusinessResponse;
import com.lo.shop.entity.OrderModel;

public class HistoryActivity extends BaseActivity implements BusinessResponse, IXListViewListener {
	
	private String flag;
	private TextView title;
	private ImageView back;
	private XListView xlistView;
	private HistoryAdapter tradeAdapter;
	private View null_paView;
	private OrderModel orderModel;
	public Handler messageHandler;
    private MyDialog mDialog;

	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
    }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
        Resources resource = (Resources) getBaseContext().getResources();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history);
		
		
		Intent intent = getIntent();
		flag = intent.getStringExtra("flag");

		title = (TextView) findViewById(R.id.top_view_text);

		back = (ImageView) findViewById(R.id.top_view_back);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
	            finish(); 
			}
		});
				
		null_paView = findViewById(R.id.null_pager);
		xlistView = (XListView) findViewById(R.id.trade_list);
		xlistView.setPullLoadEnable(true);
		xlistView.setRefreshTime();
		xlistView.setXListViewListener(this,1);
		
		orderModel = new OrderModel(this);
		orderModel.addResponseListener(this);

        String awa=resource.getString(R.string.await_audit);
        String ship=resource.getString(R.string.await_ship);
        String shipped=resource.getString(R.string.shipped);
        String fin=resource.getString(R.string.profile_history);

        if(flag.equals("await_audit")) {
            title.setText(awa);
            /**
             * 在这里请求数据
             */
            orderModel.getOrder("await_audit");
        } else if(flag.equals("await_ship")) {
            title.setText(ship);
            /**
             * 在这里请求数据
             */
            orderModel.getOrder("await_ship");

        } else if(flag.equals("shipped")) {
            title.setText(shipped);
            /**
             * 在这里请求数据
             */
            orderModel.getOrder("shipped");

        } else if(flag.equals("finished")) {
            title.setText(fin);
            /**
             * 在这里请求数据
             */
            orderModel.getOrder("finished");
        }
        messageHandler = new Handler(){

            public void handleMessage(final Message msg) {

                if (msg.what == 1)//付款
                {
                    
                }
                else if (msg.what == 2)//取消订单
                {
                    Resources resource = (Resources) getBaseContext().getResources();
                    String exit=resource.getString(R.string.balance_cancel_or_not);
                    String exiten=resource.getString(R.string.prompt);
                    final MyDialog cancelOrders = new MyDialog(HistoryActivity.this, exiten, exit);
                    cancelOrders.show();
                    cancelOrders.positive.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cancelOrders.dismiss();
                            GOODORDER order = (GOODORDER)msg.obj;
                           

                        }
                    });
                    cancelOrders.negative.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cancelOrders.dismiss();
                        }
                    });

                }

                else if(msg.what == 3)
                {
                    GOODORDER order = (GOODORDER)msg.obj;
                    orderModel.affirmReceived(order.order_sn);
                }

            }
        };
	}

	@Override
	public void onLoadMore(int id) {		
		orderModel.getOrderMore(flag);
	}

	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {		
        Resources resource = (Resources) getBaseContext().getResources();
		xlistView.stopRefresh();
		xlistView.stopLoadMore();
		if(url.endsWith("order_findOrder.action")) {
			xlistView.setRefreshTime();
			setOrder();
		} else if(url.endsWith("order_confirmReceive.action")) {

			String suc=resource.getString(R.string.successful_operation);
            String check=resource.getString(R.string.check_or_not);
			mDialog = new MyDialog(this, suc, check);
            mDialog.show();
            mDialog.positive.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {					
					mDialog.dismiss();
					Intent intent = new Intent(HistoryActivity.this, HistoryActivity.class);
					intent.putExtra("flag", "finished");
					startActivity(intent);
					finish();
				}
			});
            mDialog.negative.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {					
					mDialog.dismiss();
				}
			});
            
            orderModel.getOrder(flag);
            
		}
		
	}

	@Override
    public void onPause() {
        super.onPause();
       
    }
    @Override
	public void onRefresh(int id) {			
		orderModel.getOrder(flag);
	}
    @Override
    public void onResume() {
        super.onResume();
        
}

    public void setOrder() {

        Resources resource = (Resources) getBaseContext().getResources();
        String nodata=resource.getString(R.string.no_data);
		if(orderModel.order_list.size() == 0) {
			null_paView.setVisibility(View.VISIBLE);
	        xlistView.setVisibility(View.GONE);
		} else {
			null_paView.setVisibility(View.GONE);
			xlistView.setVisibility(View.VISIBLE);
		}
		
	
		
		if(flag.equals("await_audit")) {
			
			if(tradeAdapter == null) {
				tradeAdapter = new HistoryAdapter(this, orderModel.order_list, 1);
				xlistView.setAdapter(tradeAdapter);
			} else {
				tradeAdapter.list = orderModel.order_list;
				tradeAdapter.notifyDataSetChanged();
			}
			tradeAdapter.parentHandler = messageHandler;
			
		} else if(flag.equals("await_ship")) {
			if(tradeAdapter == null) {
				tradeAdapter = new HistoryAdapter(this, orderModel.order_list, 2);
				xlistView.setAdapter(tradeAdapter);
			} else {
				tradeAdapter.list = orderModel.order_list;
				tradeAdapter.notifyDataSetChanged();
			}	
			tradeAdapter.parentHandler = messageHandler;
			
			
		} else if(flag.equals("shipped")) {
			if(tradeAdapter == null) {
				tradeAdapter = new HistoryAdapter(this, orderModel.order_list, 3);
				xlistView.setAdapter(tradeAdapter);
			} else {
				tradeAdapter.list = orderModel.order_list;
				tradeAdapter.notifyDataSetChanged();
			}	
				
			tradeAdapter.parentHandler = messageHandler;
			
		} else if(flag.equals("finished")) {
			
			if(tradeAdapter == null) {
				tradeAdapter = new HistoryAdapter(this, orderModel.order_list, 4);
				
				xlistView.setAdapter(tradeAdapter);
			} else {
				tradeAdapter.list = orderModel.order_list;
				tradeAdapter.notifyDataSetChanged();
				
			}	
				
			tradeAdapter.parentHandler = messageHandler;
			
		}
		
	}
   
}
