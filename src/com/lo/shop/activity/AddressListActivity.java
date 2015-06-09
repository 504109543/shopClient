package com.lo.shop.activity;

import java.util.List;
import android.content.res.Resources;
import com.lo.shop.activity.BaseActivity;
import com.lo.shop.protocol.ApiInterface;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.external.activeandroid.query.Select;
import com.external.androidquery.callback.AjaxStatus;
import com.lo.shop.view.ToastView;
import com.lo.shop.R;
import com.lo.shop.adapter.AddressListAdapter;
import com.lo.shop.entity.AddressModel;
import com.lo.shop.entity.BusinessResponse;
import com.lo.shop.protocol.ADDRESS;
/**
 * 收货地址管理
 */
public class AddressListActivity extends BaseActivity implements BusinessResponse {
	
	public static List<ADDRESS> getAll() {
		return new Select().from(ADDRESS.class).execute();
	}
	private ImageView back;
	private ListView listView;
	private TextView textView;
	private TextView title;
	private ImageView bg;
	private AddressListAdapter addressManageAdapter;
	private AddressModel addressModel;
	public  Handler messageHandler;
	public int flag;
    private SharedPreferences shared;
	
	private SharedPreferences.Editor editor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.address_list);
		shared = this.getSharedPreferences("userInfo", 0);
        editor = shared.edit();
		Intent intent = getIntent();
		flag = intent.getIntExtra("flag", 0);
		
		back = (ImageView) findViewById(R.id.top_view_back);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
	            finish(); 
			}
		});
		
		title = (TextView) findViewById(R.id.top_view_text);
		//listView = (ListView) findViewById(R.id.address_manage_list);
		textView= (TextView) findViewById(R.id.address_manage_list);
		bg = (ImageView) findViewById(R.id.address_list_bg);
		textView.setText(shared.getString("address", "没有地址"));
		/*addressModel = new AddressModel(this);
		*/
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {		
		if(keyCode == KeyEvent.KEYCODE_BACK){
            finish(); 
		}
		return true;
	}

	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {		
		if(url.endsWith("address")) {
			setAddress();
		} else if(url.endsWith(ApiInterface.ADDRESS_SETDEFAULT)) {
			Intent intent = new Intent();
			intent.putExtra("address", "address");
			setResult(Activity.RESULT_OK, intent); 
			finish();
		}
	}
	
	@Override
    public void onPause() {
        super.onPause();
       
    }
	
	@Override
	protected void onResume() {		
		super.onResume();
		
	}

    public void setAddress() {
		
		if(addressModel.addressList.size() == 0) {
			listView.setVisibility(View.GONE);
            Resources resource = (Resources) getBaseContext().getResources();
            String non=resource.getString(R.string.non_address);
            ToastView toast = new ToastView(this, non);
	        toast.setGravity(Gravity.CENTER, 0, 0);
	        toast.show();
	        bg.setVisibility(View.VISIBLE);
		} else {
			bg.setVisibility(View.GONE);
			listView.setVisibility(View.VISIBLE);
			addressManageAdapter = new AddressListAdapter(this, addressModel.addressList, flag);
			listView.setAdapter(addressManageAdapter);
			
			addressManageAdapter.parentHandler = messageHandler;
		}
		
	}
}
