package com.lo.shop.fragment;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lo.shop.R;
import com.lo.shop.protocol.ApiInterface;
import com.lo.shop.utils.Utils;
import com.lo.shop.activity.ProductDetailActivity;
import com.lo.shop.activity.Framework;
import com.lo.shop.adapter.HomeTabAdapter;
import com.lo.shop.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class HomeTabFragment extends BaseFragment {
	
	private class gvAdapter extends HomeTabAdapter{

		public gvAdapter(Context context, List<? extends Map<String, ?>> data,
				int resource, String[] from, int[] to) {
			super(context, data, resource, from, to);
			View[] views = null;
			// TODO Auto-generated constructor stub
		}
		public View getView(int position, View convertView, ViewGroup parent) {	
			//Log.e(TAG, "<getView> position = " + position + " mCount = " + mCount);  
			if (position == 0)  
		    {  
		        mCount++;  
		    }  
		    else  
		    {  
		        mCount = 0;  
		    }  
		      
		    if (mCount > 1)  
		    {  
		       
		        return super.getView(position, convertView, parent);  
		    } 
				View view = super.getView(position, convertView, parent);//有bitmap bug
				 Map<String,Object> record = (Map<String, Object>) getItem(position);	
				 ImageView imageView = (ImageView) view.findViewById(R.id.ggv_img);			 
				 imageLoader.displayImage(record.get("img").toString(),imageView, Framework.options);	
			     LayoutParams params = imageView.getLayoutParams();     
			     params.width = imgWidth;
			     params.height=imgHeight;  		     
			     imageView.setLayoutParams(params);  
				 return view;
			}  
			
	}

	static class ViewHolder {
		TextView text;
		ImageView icon;
		}
	public static HomeTabFragment newInstance(String i) {
    	HomeTabFragment newFragment = new HomeTabFragment();
        Bundle bundle = new Bundle();
        bundle.putString("cid", i);
        newFragment.setArguments(bundle);
        return newFragment;

    }
	private static final String TAG = "HomeTabFragment";
	private static final String TEST = ""+25;
	private static final int UPDATE = 0;
	private static int imgWidth=0 ;
	private static int imgHeight = 0 ;;
	private  int mCount = 0;
	private TextView mMsgTv;
	private Context context;
	private View view;
	private LayoutInflater minflater;
	
	String[] from = { "img", "sale", "discount", "price", "title", "url" };		
	
	
	int[] to = { R.id.ggv_img, R.id.ggv_sale, R.id.ggv_discount,
			R.id.ggv_price, R.id.ggv_title, R.id.ggv_url };
	GridView ggv ;
	
	gvAdapter gvadapter;
	protected ImageLoader imageLoader = ImageLoader.getInstance();	
	 
	List<Map<String, String>> list = new ArrayList<Map<String,  String>>();
    WindowManager windowManager;
    
    int sw,sh;
    String ccid;
	
    String defaultCid="20";
    
	private Handler handler = new Handler() {
		 
	        @Override
	        public void handleMessage(Message msg) {
	            // TODO 接收消息并且去更新UI线程上的控件内容
	            if (msg.what == UPDATE) {
	            	
	                 Bundle b = msg.getData();	               
	            	
	            	ArrayList<Map<String, String>> lss = null;
	            	ArrayList list = b.getParcelableArrayList("list");
	            	lss= (ArrayList<Map<String, String>>) list.get(0);//强转成你自己定义的list，这样list2就是你传过来的那个list了。
	            	gvadapter = new gvAdapter(context,lss,R.layout.item_goods, from, to);
	            	ggv.setAdapter(gvadapter);
	            	
	            }
	            super.handleMessage(msg);
	            
	        }
	        
	    };
	
	@Override
	public String getFragmentName() {
		return TAG;
	}
	
	private ArrayList<Map<String,String>> getGoods(int cateId) throws Exception{
    	
        ArrayList<Map<String,String>> ls = new ArrayList<Map<String,String>>();
        
    	String url= ApiInterface.URL+":8080/shop/testtab.txt";
    	String url1 = ApiInterface.URL+":8080/shop/product.action?s_product.smallType.id="+cateId;
		try {
			String json = (String) (Utils.getData(url1,String.class));
			
			JSONObject result = new JSONObject(json);
		    JSONArray goodsList = result.getJSONArray("data");		    
		    int length = goodsList.length();	   		    
		    for(int i=0;i<length;i++){ //商品数量
		    	JSONObject oj = goodsList.getJSONObject(i); 
		    	String cidStr =  oj.getString("id");//取出属性 
		    				
		    			Map<String,String> map = new HashMap<String,String>();
						map.put("title", oj.getString("name").toString());
						map.put("price", "￥" + oj.getString("price").toString());						
						map.put("img",ApiInterface.URL+":8080/shop/" + oj.getString("proPic"));
						map.put("url", oj.getString("id").toString());
		    			ls.add(map);		    			
		    }
		    
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    	
		//Log.e(TAG, "获取数据成功");
		return ls;    	
    }
	
	private void ggvItemClick() {
		ggv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				GridView gv = (GridView) parent;
				HashMap<String, Object> map = (HashMap<String, Object>) gv.getItemAtPosition(position);
				 Intent it = new Intent(context, ProductDetailActivity.class);
                 it.putExtra("good_id",map.get("url").toString());
                 it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
                 Toast.makeText(context, map.get("url").toString(), 0).show();
                 context.startActivity(it);
                 
			}

		});
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
	
	 @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getActivity().getApplicationContext();
		Bundle args = getArguments();		
		ccid = args != null ? args.getString("cid") : defaultCid;	
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_fashion, container,false);
        minflater=inflater;
		windowManager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
		//屏幕宽度
		sw = windowManager.getDefaultDisplay().getWidth();
		//屏幕高度
		sh = windowManager.getDefaultDisplay().getHeight();
		
		ggv = (GridView) view.findViewById(R.id.ggv);
		
		imgWidth = (int) Math.ceil(sw*0.45);	
		
	    imgHeight =  imgWidth;
		
		
		ggvItemClick();
		return view;
	}
	
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		
		super.onDestroyView();
	}

    @Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		new Thread() { //新建线程
            @Override
            public void run() {
                // TODO 子线程中通过handler发送消息给handler接收，由handler去更新值
                try {	         
                	Thread.sleep(300);
                        Message msg = new Message();
                        Bundle b = new Bundle();
                        ArrayList list = new ArrayList(); //这个list用于在budnle中传递 需要传递的ArrayList<Object>
                        ArrayList<Map<String, String>> lss = null;
                        msg.what = UPDATE;
                        int cccid = Integer.parseInt(ccid);
                        
    					try {
    						lss = getGoods(cccid);
    					} catch (Exception e) {
    						// TODO Auto-generated catch block
    						e.printStackTrace();
    					}
    					list.add(lss);
    					b.putParcelableArrayList("list",list);
                         msg.setData(b);
                        handler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
		ggv.setAdapter(gvadapter);
	}

	
	public void setMsgName(String msgName) {
	}

}
