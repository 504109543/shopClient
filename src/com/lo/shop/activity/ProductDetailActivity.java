package com.lo.shop.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;

import com.lo.shop.protocol.ApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.external.HorizontalVariableListView.widget.HorizontalVariableListView;
import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.XListView;
import com.external.maxwin.view.XListView.IXListViewListener;
import com.lo.shop.activity.BaseActivity;
import com.lo.shop.view.ToastView;
import com.lo.shop.R;
import com.lo.shop.adapter.ProductPhotoAdapter;
//import com.lo.shop.entity.GoodDetailDraft;

import com.lo.shop.entity.BusinessResponse;
import com.lo.shop.entity.GoodDetailDraft;
import com.lo.shop.entity.GoodDetailModel;
import com.lo.shop.entity.ShoppingCartModel;

public class ProductDetailActivity extends BaseActivity implements BusinessResponse, IXListViewListener
{
    private ProductPhotoAdapter photoListAdapter;
    private GoodDetailModel dataModel;
    HorizontalVariableListView goodDetailPhotoList;

    private TextView goodBriefTextView;
    private TextView goodPromotePriceTextView;
    private TextView goodMarketPriceTextView;
    private TextView goodPropertyTextView;
    private TextView goodCategoryTextView;
    private TextView countDownTextView;

    private LinearLayout goodBasicParameterLayout;
    private LinearLayout goodsDesc;
    private LinearLayout goodsComment;

    private TextView title;
    private ImageView back;
    private ImageView share;

    private TextView addToCartTextView;
    private TextView buyNowTextView;
    private Boolean isBuyNow = false;
    private ImageView  collectionButton;
    private ImageView pager;

    private ImageView goodDetailShoppingCart;
    private TextView good_detail_shopping_cart_num;
    private LinearLayout good_detail_shopping_cart_num_bg;
    
    private View headView;
    private XListView xlistView;

    private SharedPreferences shared;
	private SharedPreferences.Editor editor;
    private Timer timer;
    private  Boolean isFresh=true;//是否选择的属性
    private final static int REQUEST_SHOPPINGCAR = 1;
    private final static int REQUEST_SPECIFICATION= 2;
    

    void cartCreate()
    {   	
    	dataModel.cartCreate(Integer.parseInt(dataModel.goodId));
    }


   

    

    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {		
		
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.product_detail);
        
        shared = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
		editor = shared.edit();
        
        xlistView = (XListView) findViewById(R.id.good_detail_list);

        headView = LayoutInflater.from(this).inflate(R.layout.product_detail_head, null);

        xlistView.addHeaderView(headView);
        xlistView.setPullLoadEnable(false);
		xlistView.setRefreshTime();
		xlistView.setXListViewListener(this,1);
		xlistView.setAdapter(null);

        dataModel = new GoodDetailModel(this);
        dataModel.addResponseListener(this);
        dataModel.goodId =  getIntent().getStringExtra("good_id");
        dataModel.fetchGoodDetail(Integer.parseInt(dataModel.goodId));

        goodDetailPhotoList = (HorizontalVariableListView)headView.findViewById(R.id.good_detail_photo_list);
        photoListAdapter = new ProductPhotoAdapter(this,dataModel.goodDetail.pictures);
        goodDetailPhotoList.setAdapter(photoListAdapter);
        goodDetailPhotoList.setSelectionMode( HorizontalVariableListView.SelectionMode.Single );

        goodDetailPhotoList.setOverScrollMode( HorizontalVariableListView.OVER_SCROLL_NEVER );
        goodDetailPhotoList.setEdgeGravityY( Gravity.CENTER );

        final Resources resource = (Resources) getBaseContext().getResources();

        title = (TextView) findViewById(R.id.top_view_text);
        String det=resource.getString(R.string.gooddetail_product);
        title.setText(det);
        back = (ImageView) findViewById(R.id.top_view_back);
        back.setVisibility(1);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {                
                finish();
            }
        });

        goodBriefTextView = (TextView)headView.findViewById(R.id.good_brief);       
        goodPromotePriceTextView = (TextView)headView.findViewById(R.id.promote_price);
        goodMarketPriceTextView = (TextView)headView.findViewById(R.id.market_price);
        goodMarketPriceTextView.getPaint().setAntiAlias(true);
        goodMarketPriceTextView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        countDownTextView = (TextView)headView.findViewById(R.id.count_down);

        goodPropertyTextView = (TextView)headView.findViewById(R.id.good_property);//价格
        goodCategoryTextView = (TextView)headView.findViewById(R.id.good_category);
        goodCategoryTextView.setSingleLine(false);
        goodCategoryTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {            	            	
            	if(dataModel.goodDetail.stock != null) {
            		System.out.println("库存"+dataModel.goodDetail.stock);
            		GoodDetailDraft.getInstance().goodDetail = dataModel.goodDetail;
                    Intent it = new Intent(ProductDetailActivity.this,SpecificationActivity.class);
                    it.putExtra("num", Integer.valueOf(dataModel.goodDetail.stock));
                    startActivity(it);
                    isFresh=false;
                    overridePendingTransition(R.anim.my_scale_action,R.anim.my_alpha_action);
                    
            	} else {
                    String che=resource.getString(R.string.check_the_network);            		
            		ToastView toast = new ToastView(ProductDetailActivity.this, che);
            		toast.setGravity(Gravity.CENTER, 0, 0);
            		toast.show();
            	}
                
            }
        });

  
        goodsDesc = (LinearLayout) headView.findViewById(R.id.goods_desc);
        goodsDesc.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {				
				Intent intent = new Intent(ProductDetailActivity.this, ProductDescActivity.class);
				intent.putExtra("description",dataModel.goodDetail.description);
				startActivity(intent);
			}
		});
        
       

        addToCartTextView = (TextView)findViewById(R.id.add_to_cart);
        addToCartTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid = shared.getString("uid", "");
                if(uid.equals(""))
                {
                    Intent intent = new Intent(ProductDetailActivity.this, SigninActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_buttom_in,R.anim.push_buttom_out);
                    String nol=resource.getString(R.string.no_login);                    
                    ToastView toast = new ToastView(ProductDetailActivity.this, nol);
        	        toast.setGravity(Gravity.CENTER, 0, 0);
        	        toast.show();
                } else {
                	isBuyNow = false;
                	cartCreate();
                }
            }
        });

   

       
        pager = (ImageView) findViewById(R.id.good_detail_pager);
        pager.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {				
			}
		});
        
        
        
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GoodDetailDraft.getInstance().clear();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
                finish();
                overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
                return false;
        }
        return true;
    }

    @Override
	public void onLoadMore(int id) {		
		
	}


    public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status) throws JSONException
    {   	
    	xlistView.stopRefresh();
        Resources resource = (Resources) getBaseContext().getResources();
        if (url.contains("showProduct"))
        {
        	pager.setVisibility(View.GONE);
        	xlistView.setRefreshTime();
            //GoodDetailDraft.getInstance().goodDetail = dataModel.goodDetail;            
            goodBriefTextView.setText(dataModel.goodDetail.name);
            photoListAdapter.dataList = dataModel.goodDetail.pictures;
            goodDetailPhotoList.setAdapter(photoListAdapter);
            goodPropertyTextView.setText("商品价格: "+dataModel.goodDetail.price);
           goodCategoryTextView.setText(getSpecificationDesc());
        }
        
        else if (url.endsWith("addShoppingCartItem.action")){
        	ToastView toast = new ToastView(this, R.string.add_to_cart_success);
	        toast.setGravity(Gravity.CENTER, 0, 0);
	        toast.show();
        	
        }
       


    }
    public String getSpecificationDesc()
    {
		
        Resources resource = (Resources) getBaseContext().getResources();
        String none=resource.getString(R.string.none);
        String speciationDesc = ""; 
        String num=resource.getString(R.string.amount);
        speciationDesc += num + GoodDetailDraft.getInstance().goodQuantity;
        return speciationDesc;
    }

	@Override
    protected void onPause() {
        super.onPause();
        
    }

	@Override
	public void onRefresh(int id) {			
		dataModel.fetchGoodDetail(Integer.parseInt(dataModel.goodId));
	}
	
	@Override
    protected void onResume() {
        super.onResume();
        goodCategoryTextView.setText(getSpecificationDesc());
        if(isFresh){
            dataModel.fetchGoodDetail(Integer.parseInt(dataModel.goodId));
        }
        

    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
