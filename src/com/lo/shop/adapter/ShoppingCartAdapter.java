package com.lo.shop.adapter;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.external.activeandroid.util.AnimationUtil;
import com.lo.shop.view.MyDialog;
import com.lo.shop.view.WebImageView;
import com.lo.shop.activity.Framework;
import com.lo.shop.activity.ShoppingCartActivity;
import com.lo.shop.activity.HistoryActivity;
import com.lo.shop.fragment.ShoppingCartFragment;
import com.lo.shop.protocol.GOODS_LIST;
import com.lo.shop.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ShoppingCartAdapter extends BaseAdapter {

	class ViewHolder {
		
		private TextView totel;
		private Button change;
		private FrameLayout view;
		private LinearLayout view1;
		private FrameLayout view2;
		
		private ImageView image;
		private TextView text;
		private TextView property;
		
		private ImageView min;
		private EditText editNum;
		private ImageView sum;
		private Button remove;
		
	}
	private LayoutInflater inflater;
	private Context context;
	public List<GOODS_LIST> list;
	private int i;

	public Handler parentHandler;
	public static int CART_CHANGE_CHANGE2 = 4;
    public static int CART_CHANGE_CHANGE1 = 3;
    public static int CART_CHANGE_MODIFY = 2;
    
    public static int CART_CHANGE_REMOVE = 1;
	private SharedPreferences shared;
    
    private SharedPreferences.Editor editor;; 
    
    public static Map<Integer, Boolean> isSelected = new HashMap<Integer, Boolean>();
    private MyDialog mDialog;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	
	public ShoppingCartAdapter(Context context, List<GOODS_LIST> list) {
		this.context = context;
		this.list = list;
		//System.out.println("一共有"+this.list.size()+"商品");
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
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {		
		final ViewHolder holder;
        final Resources resource = (Resources) context.getResources();
		i = 0;
		if(convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.shopping_cart_cell, null);
			
			holder.totel = (TextView) convertView.findViewById(R.id.shop_car_item_total);
			holder.change = (Button) convertView.findViewById(R.id.shop_car_item_change);
			
			holder.view = (FrameLayout) convertView.findViewById(R.id.shop_car_item_view);
			holder.view1 = (LinearLayout) convertView.findViewById(R.id.shop_car_item_view1);
			holder.view2 = (FrameLayout) convertView.findViewById(R.id.shop_car_item_view2);
			
			holder.image = (ImageView) convertView.findViewById(R.id.shop_car_item_image);
			holder.text = (TextView) convertView.findViewById(R.id.shop_car_item_text);
			holder.property = (TextView) convertView.findViewById(R.id.shop_car_item_property);
			
			holder.min = (ImageView) convertView.findViewById(R.id.shop_car_item_min);
			holder.editNum = (EditText) convertView.findViewById(R.id.shop_car_item_editNum);
			holder.sum = (ImageView) convertView.findViewById(R.id.shop_car_item_sum);
			holder.remove = (Button) convertView.findViewById(R.id.shop_car_item_remove);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		final GOODS_LIST goods = list.get(position);
		
		isSelected.put(position, false);
		//System.out.println("商品数量"+goods.goods_number+"商品名字"+goods.goods_name);
		
		shared = context.getSharedPreferences("userInfo", 0); 
		editor = shared.edit();
		holder.text.setText(goods.goods_name);
		imageLoader.displayImage(goods.img.pic_url,holder.image, Framework.options);				
		holder.totel.setText("共"+goods.totalprice+" 元");
		holder.text.setText(goods.goods_name);
		
		StringBuffer sbf = new StringBuffer();
		sbf.append(resource.getString(R.string.amount));
		sbf.append(goods.goods_number);
		
		holder.property.setText(sbf.toString());
		
		holder.editNum.setText(goods.goods_number+"");
		
		holder.change.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {				
				if (holder.view2.getVisibility() == View.GONE) {
					AnimationUtil.showAnimation1(holder.view1, holder.view);
					AnimationUtil.showAnimation2(holder.view2, holder.view);
					
					holder.view2.setVisibility(View.VISIBLE);
					holder.change.setText(resource.getString(R.string.collect_done));
					
					Message msg = new Message();
	                msg.what = CART_CHANGE_CHANGE1;
                    parentHandler.handleMessage(msg);
                    
                    isSelected.put(position, true);
					
				} else { // 收回隐藏布局
					AnimationUtil.backAnimation1(holder.view1);
					AnimationUtil.backAnimation2(holder.view2);
					
					holder.view2.setVisibility(View.GONE);
					holder.change.setText(resource.getString(R.string.modify));
					
					if(Integer.valueOf(goods.goods_number) != Integer.valueOf(holder.editNum.getText().toString())) {
						Message msg = new Message();
		                msg.what = CART_CHANGE_MODIFY;
		                msg.arg1 = Integer.valueOf(goods.goods_id);
		                msg.arg2 = Integer.valueOf(holder.editNum.getText().toString());
                        parentHandler.handleMessage(msg);
					}					
					
					isSelected.put(position, false);
					boolean a = false;
					for(int j=0;j<list.size();j++) {
						if(init(j) == true) {
							a = true;
							break;
						} else {
							a = false;
						}
					}
					if(a == false) {
						Message msg1 = new Message();
		                msg1.what = CART_CHANGE_CHANGE2;
	                    parentHandler.handleMessage(msg1);
					}
				}
			}
		});
		
		holder.min.setOnClickListener(new OnClickListener() {
				
			@Override
			public void onClick(View v) {				
				i = Integer.valueOf(holder.editNum.getText().toString());
				if(i>1) {
					holder.editNum.setText(--i+"");
				}
			}
		});
		
		holder.sum.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {				
				i = Integer.valueOf(holder.editNum.getText().toString());
                    ++i;
                    holder.editNum.setText(i+"");

			}
		});
		
		holder.remove.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {								
				mDialog = new MyDialog(context, resource.getString(R.string.shopcaritem_remove), resource.getString(R.string.delete_confirm));
                mDialog.show();
                mDialog.positive.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {						
						mDialog.dismiss();
						Message msg = new Message();
		                msg.what = CART_CHANGE_REMOVE;
		                msg.arg1 = Integer.valueOf(goods.goods_id);

		                parentHandler.handleMessage(msg);
						
					}
				});
                mDialog.negative.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {						
						mDialog.dismiss();
						
					}
				});
			}
		});
		
		return convertView;
	}

	@Override
	public int getViewTypeCount() {		
		int count = list != null ? list.size() : 1;
		return count;
	}
	
	private boolean init(int position) {
		if(isSelected.containsKey(Integer.valueOf(position))) {
			if(isSelected.get(position) == true) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

}
