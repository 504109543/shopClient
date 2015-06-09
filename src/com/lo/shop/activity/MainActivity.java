package com.lo.shop.activity;

import com.lo.shop.R;
import com.lo.shop.fragment.CategoryFragment;
import com.lo.shop.fragment.HomeFragment;
import com.lo.shop.fragment.ProfileFragment;
import com.lo.shop.fragment.ShoppingCartFragment;
import com.lo.shop.utils.ConstantValues;
import com.lo.shop.view.MyTabWidget;
import com.lo.shop.view.MyTabWidget.OnTabSelectedListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class MainActivity extends FragmentActivity implements
		OnTabSelectedListener {

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		System.out.println("推出");
		shared = this.getSharedPreferences("userInfo", 0); 
		editor = shared.edit();      
        editor.putString("uid", "");
        editor.putString("sid", "");
        editor.commit();
		super.finish();
	}

	private static final String TAG = "MainActivity";
	private SharedPreferences shared;
	private SharedPreferences.Editor editor;
	private MyTabWidget mTabWidget;
	private HomeFragment mHomeFragment;
	private CategoryFragment mCategoryFragment;
	private ProfileFragment ProfileFragment;
	private ShoppingCartFragment ShoppingCartFragment;
	private int mIndex = ConstantValues.HOME_FRAGMENT_INDEX;
	private FragmentManager mFragmentManager;

	private void hideFragments(FragmentTransaction transaction) {
		if (null != mHomeFragment) {
			transaction.hide(mHomeFragment);
		}
		if (null != mCategoryFragment) {
			transaction.hide(mCategoryFragment);
		}
		if (null != ShoppingCartFragment) {
			transaction.hide(ShoppingCartFragment);
		}
		if (null != ProfileFragment) {
			transaction.hide(ProfileFragment);
		}
	}

	private void init() {
		mFragmentManager = getSupportFragmentManager();
		mTabWidget = (MyTabWidget) findViewById(R.id.tab_widget);
	}

	private void initEvents() {
		mTabWidget.setOnTabSelectedListener(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
		.detectDiskReads()
		.detectDiskWrites()
		.detectNetwork() // 这里可以替换为detectAll() 就包括了磁盘读写和网络I/O
		.penaltyLog() //打印logcat，当然也可以定位到dropbox，通过文件保存相应的log
		.build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
		.detectLeakedSqlLiteObjects() //探测SQLite数据库操作
		.penaltyLog() //打印logcat
		.penaltyDeath()
		.build()); 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (android.os.Build.VERSION.SDK_INT > 9) {
	        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	        StrictMode.setThreadPolicy(policy);
	    }
		
		init();
		initEvents();
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// super.onRestoreInstanceState(savedInstanceState);
		mIndex = savedInstanceState.getInt("index");
	}

	@Override
	protected void onResume() {
		super.onResume();
		onTabSelected(mIndex);
		mTabWidget.setTabsDisplay(this, mIndex);
		mTabWidget.setIndicateDisplay(this, 3, true);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// 自己记录fragment的位置,防止activity被系统回收时，fragment错乱的问题
		// super.onSaveInstanceState(outState);
		outState.putInt("index", mIndex);
	}

	@Override
	public void onTabSelected(int index) {
		FragmentTransaction transaction = mFragmentManager.beginTransaction();
		hideFragments(transaction);
		switch (index) {
		case ConstantValues.HOME_FRAGMENT_INDEX:
			if (null == mHomeFragment) {
				mHomeFragment = new HomeFragment();
				transaction.add(R.id.center_layout, mHomeFragment);
			} else {
				transaction.show(mHomeFragment);
			}
			break;
		case ConstantValues.CATEGORY_FRAGMENT_INDEX:
			if (null == mCategoryFragment) {
				mCategoryFragment = new CategoryFragment();
				transaction.add(R.id.center_layout, mCategoryFragment);
			} else {
				transaction.show(mCategoryFragment);
			}
			break;
		case ConstantValues.COLLECT_FRAGMENT_INDEX:
			if (null == ShoppingCartFragment) {
				ShoppingCartFragment = new ShoppingCartFragment();
				transaction.add(R.id.center_layout, ShoppingCartFragment);
			} else {
				transaction.show(ShoppingCartFragment);
			}
			break;
		case ConstantValues.SETTING_FRAGMENT_INDEX:
			if (null == ProfileFragment ) {
				
				ProfileFragment = new ProfileFragment();
				transaction.add(R.id.center_layout, ProfileFragment);
			} else {
				transaction.show(ProfileFragment);
			}
			break;

		default:
			break;
		}
		mIndex = index;
		transaction.commitAllowingStateLoss();
	}

}
