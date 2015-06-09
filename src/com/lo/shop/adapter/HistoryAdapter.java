package com.lo.shop.adapter;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;

import com.lo.shop.activity.Framework;
import com.lo.shop.R;
import com.lo.shop.protocol.GOODORDER;
import com.lo.shop.protocol.ORDER_GOODS_LIST;
import com.nostra13.universalimageloader.core.ImageLoader;

public class HistoryAdapter extends BaseAdapter {

	class ViewHolder {
		private TextView sno;
		private TextView time;
		private LinearLayout body;
		private TextView fee;
		private TextView red_paper;
		private TextView score;
		private TextView total;		
		private Button ok;
	}
	private Context context;
	public List<GOODORDER> list;
	
	public int flag;
	
	private LayoutInflater inflater;
	
	public Handler parentHandler;
	private SharedPreferences shared;
    private SharedPreferences.Editor editor;
	
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	
	
	public HistoryAdapter(Context context, List<GOODORDER> list, int flag) {
		this.context = context;
		this.list = list;
		this.flag = flag;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		 
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		 
		return list.get(position);
	}
	

	@Override
	public long getItemId(int position) {
		 
		return position;
	}


	@Override
	public int getItemViewType(int position) {
		 
		return 1;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		 
		final ViewHolder holder;
        final Resources resource = (Resources) context.getResources();
		//if(convertView == null) {
        if(list.get(position).status==flag){
			System.out.println("订单状态"+list.get(position).status);
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.history_cell, null);
			holder.sno = (TextView) convertView.findViewById(R.id.trade_item_sno);
			holder.time = (TextView) convertView.findViewById(R.id.trade_item_time);
			holder.body = (LinearLayout) convertView.findViewById(R.id.trade_item_body);
			holder.fee = (TextView) convertView.findViewById(R.id.trade_item_fee);			
			holder.total = (TextView) convertView.findViewById(R.id.trade_item_total);		
			holder.ok = (Button) convertView.findViewById(R.id.trade_item_ok);			
			ArrayList<ORDER_GOODS_LIST> goods_list = list.get(position).goods_list;		
			for(int i=0;i<goods_list.size();i++) {

				View view = LayoutInflater.from(context).inflate(R.layout.trade_body, null);
				ImageView image = (ImageView) view.findViewById(R.id.trade_body_image);
				TextView text = (TextView) view.findViewById(R.id.trade_body_text);
				TextView total = (TextView) view.findViewById(R.id.trade_body_total);
				TextView num = (TextView) view.findViewById(R.id.trade_body_num);
				holder.body.addView(view);
				
				shared = context.getSharedPreferences("userInfo", 0); 
				editor = shared.edit();
				
               imageLoader.displayImage(goods_list.get(i).img.pic_url,image, Framework.options);
					
				
				text.setText(goods_list.get(i).name);
				total.setText(goods_list.get(i).subtotal);
				num.setText("X "+goods_list.get(i).goods_number);
				
			}

		final GOODORDER order = list.get(position);
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date currentTime = null;
		try {
			currentTime = format.parse(order.order_time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		holder.time.setText(format.format(currentTime));
		holder.sno.setText(order.order_sn);
		holder.total.setText(order.total_fee);
		
		if(flag == 1) {
			holder.ok.setVisibility(View.GONE);
			
		} else if(flag == 2) {
			holder.ok.setVisibility(View.GONE);
			
		} else if(flag == 3) {
			holder.ok.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					 
					Message msg = new Message();
	                msg.what = 3;
                    msg.obj = order;
                    parentHandler.handleMessage(msg);
				}
			});
			
			
		} else {
			//System.out.println("历史订单，就是所有订单");
			holder.ok.setVisibility(View.GONE);
		}
			}else
			{
				
				convertView = inflater.inflate(R.layout.zero, null);
			}
		return convertView;
	}
	
	@Override
	public int getViewTypeCount() {
		 
		return 1;
	}
}
