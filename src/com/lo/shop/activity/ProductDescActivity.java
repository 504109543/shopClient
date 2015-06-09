package com.lo.shop.activity;


import android.content.res.Resources;

import com.lo.shop.activity.BaseActivity;
import com.lo.shop.entity.BusinessResponse;
import com.lo.shop.entity.GoodDetailModel;
import com.lo.shop.protocol.ApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.external.androidquery.callback.AjaxStatus;
import com.lo.shop.R;
public class ProductDescActivity extends BaseActivity  {

	
	private TextView title;
	private ImageView back;
	private GoodDetailModel goodsModel;
	private TextView webView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_desc);
		
		Intent intent = getIntent();
		String desc = intent.getStringExtra("description");
		
		title = (TextView) findViewById(R.id.top_view_text);
        Resources resource = (Resources) getBaseContext().getResources();
        String  det=resource.getString(R.string.gooddetail_desc);
		title.setText(det);
		
		
		webView = (TextView) findViewById(R.id.description);		
		webView.setText(desc);
		
	} 

	
}
