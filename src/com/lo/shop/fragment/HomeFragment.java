package com.lo.shop.fragment;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.lo.shop.R;
import com.lo.shop.adapter.TopTabAdapter;
import com.lo.shop.protocol.ApiInterface;
import com.lo.shop.utils.ConstantValues;
import com.lo.shop.utils.Utils;
import com.lo.shop.view.TopIndicator;
import com.lo.shop.view.TopIndicator.OnTopIndicatorListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

public class HomeFragment extends BaseFragment implements OnTopIndicatorListener {

	private final class ClickListener implements OnClickListener {

		private int mPosition;

		public ClickListener() {
			// TODO Auto-generated constructor stub
		}

		public ClickListener(int mPosition) {
			this.mPosition = mPosition;
		}

		public void onClick(View v) {
			if (v instanceof ImageView) {
				String url = mData.get(mPosition).getmArticleUrl();
				Toast.makeText(getActivity(), url, 1).show();
			} else {
				return;
			}

		}
	}
	public final class DownLoad extends AsyncTask<String, Void, Bitmap> {

		public ImageView mImageView;
		public DownLoad(ImageView imageView) {
			mImageView = imageView;
		}
		@Override
		protected Bitmap doInBackground(String... params) {			
			try {
				//获取地址
				if (params[0].equals(SERVER_ADDRESS)) {
					String json = (String) (Utils.getData(params[0],
							String.class));
				//获取数据
					mData = Utils.fromJson(json);
					mImageViewList.clear();
					Isdownload = false;
					//Log.e("还原", Utils.toJson(mData));
				} else {
					Bitmap bitmap = (Bitmap) (Utils.getData(params[0],
							Bitmap.class));
					Isdownload = true;
					//Log.e("还原", params[0]);
					return bitmap;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (result != null) {
				mImageView.setImageBitmap(result);
				if (!loopPlayState) {
					mArticleTitle.setText(mData.get(0).getmTitle());
					mViewPagertop.setCurrentItem(0);
					mHandler.postDelayed(loopPlay, 3000);//轮播间隔3秒
					loopPlayState = true;
					//Log.e(TAG, "循环开始");
				}
			} else if(Isdownload == false){
				for (int i = 0; i < mData.size(); i++) {
					ImageView imageView = new ImageView(getActivity());
					imageView.setLayoutParams(new LayoutParams(
							LayoutParams.MATCH_PARENT,
							LayoutParams.MATCH_PARENT));
					imageView.setOnClickListener(new ClickListener(i));
					mImageViewList.add(imageView);
					View view = new View(getActivity());
					LayoutParams layoutParams = new LayoutParams(14, 14);
					layoutParams.setMargins(3, 0, 3, 0);
					view.setLayoutParams(layoutParams);
					view.setBackgroundResource(R.drawable.dot_normal);
					mCustomSpace.addView(view);
					mViewList.add(view);
					//Log.e(TAG, "插入view"+mData.size());
				}				
				adapter.notifyDataSetChanged();
			}

		}
	}
	/**
	 * 当ViewPager中页面的状态发生改变时调用
	 * 
	 * @author Administrator
	 * 
	 */
	public class MyPageChangeListener implements OnPageChangeListener {

		public int historyPosition = 0;

		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		public void onPageScrollStateChanged(int arg0) {
		}

		/**
		 * 当ViewPagertop中页面的状态发生改变时调用
		 */
		public void onPageSelected(int position) {
			mArticleTitle.setText(mData.get(position).getmTitle());
			mViewList.get(historyPosition).setBackgroundResource(
					R.drawable.dot_normal);
			mViewList.get(position).setBackgroundResource(
					R.drawable.dot_focused);
			historyPosition = position;
		}
	}
	public final class MyPagerAdapter extends PagerAdapter {
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			((ViewPager) container).removeView(mImageViewList.get(position));
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mImageViewList.size();
		}
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			if (mImageViewList.size() < position) {  
				Log.i(TAG, "错误"+position);
                return null;  
           } 
			ImageView temp = mImageViewList.get(position);
			//Log.i(TAG, "第几页"+position);			
			new DownLoad(temp).execute(mData.get(position).getmPictureUrl());
			((ViewPager) container).addView(temp);
			return temp;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

	}
	/**
	 * 中部滚动的Adapter
	 * @author Administrator
	 *
	 */
		private class TabPagerAdapter extends FragmentStatePagerAdapter implements
				ViewPager.OnPageChangeListener {
	
			public TabPagerAdapter(FragmentManager fm) {
				super(fm);
				mViewPager.setOnPageChangeListener(this);
				
			}
	
			@Override
			public int getCount() {
				return 4;
			}
	
			@Override
			public Fragment getItem(int position) {
				/*HomeTabFragment fragment = (HomeTabFragment) Fragment
						                   .instantiate(mActivity,HomeTabFragment.class.getName()); */
				//if(nu.hasNetwork(mActivity)){
				s=1+position+"";
				HomeTabFragment fragment = HomeTabFragment.newInstance(s);
				//Log.e(TAG, "getItem:position=" + position);
				return fragment;
				//}
			}
	
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				
			}
	
			@Override
			public void onPageScrollStateChanged(int arg0) {
	
			}
	
			@Override
			public void onPageSelected(int position) {
				mTopIndicator.setTabsDisplay(mActivity, position);
			}
		}
	public static HomeFragment newInstance() {
		HomeFragment homeFragment = new HomeFragment();

		return homeFragment;
	}
	public static final String TAG = "HomeFragment";
	private final String SERVER_ADDRESS = ApiInterface.URL+":8080/shop/test.txt";
	public static boolean Isdownload = false;
	private Activity mActivity;
	private TextView mTitleTv;
	private ViewPager mViewPager,mViewPagertop;
	private TabPagerAdapter mPagerAdapter = null;
	private TopIndicator mTopIndicator;	
	private TextView mArticleTitle = null;
	private LinearLayout mCustomSpace = null;
	private boolean loopPlayState = false;
	private List<TopTabAdapter> mData = null;
	private List<ImageView> mImageViewList = null;
	private List<View> mViewList = null;
	 private ImageView back;

	private MyPagerAdapter adapter = null;

	private Handler mHandler = null;

	private String s = null;


	protected ImageLoader imageLoader = ImageLoader.getInstance();
	
	Runnable loopPlay = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			int position = mViewPagertop.getCurrentItem();
			//Log.e(TAG, "数据个数为"+mData.size());
			if (position == (mData.size() - 1)) {
				position = 0;
				mViewPagertop.setCurrentItem(position);
			} else {
				mViewPagertop.setCurrentItem(++position);
			}
			mHandler.postDelayed(loopPlay, 3000);
			
		}
	};
	
	@Override
	public String getFragmentName() {
		return TAG;
	}

	private void initDisplay() {
		mViewPagertop.setAdapter(adapter);
		//Log.e(TAG, "调用了initDisplay");
		mViewPager.setAdapter(mPagerAdapter);	
		//mPagerAdapter.notifyDataSetChanged();
	}

	private void initViews(View view) {
		imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
		mTitleTv = (TextView) view.findViewById(R.id.top_view_text);
		mTitleTv.setText(R.string.home);
		back = (ImageView) view.findViewById(R.id.top_view_back);
        back.setVisibility(View.GONE);
		mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
		mPagerAdapter = new TabPagerAdapter(getFragmentManager());
		
		mTopIndicator = (TopIndicator) view.findViewById(R.id.top_indicator);
		
		
		mViewPagertop = (ViewPager) view.findViewById(R.id.viewpagertop);
		
		mArticleTitle = (TextView) view.findViewById(R.id.article_title);
		
		mCustomSpace = (LinearLayout) view.findViewById(R.id.custom_space);
		
		mHandler = new Handler();
		mData = new ArrayList<TopTabAdapter>();
		mImageViewList = new ArrayList<ImageView>();
		mViewList = new ArrayList<View>();
		adapter = new MyPagerAdapter();
		loadData();
		

		// 设置一个监听器，当ViewPagertop中的页面改变时调用
		mViewPagertop.setOnPageChangeListener(new MyPageChangeListener());
		mTopIndicator.setOnTopIndicatorListener(this);
	}

	public void loadData() {
		// TODO Auto-generated method stub
		new DownLoad(null).execute(SERVER_ADDRESS);
	}
	
@Override
public void onActivityCreated(Bundle savedInstanceState) {
	super.onActivityCreated(savedInstanceState);
	
	initDisplay();
}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mActivity = activity;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home, container, false);
		return view;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onIndicatorSelected(int index) {
		//Toast.makeText(mActivity,"Indicator响应，当前位置是"+index,0).show();
		mViewPager.setCurrentItem(index);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initViews(view);
	}
	
	
}
