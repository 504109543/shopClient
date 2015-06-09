package com.lo.shop.fragment;

import java.util.ArrayList;
import java.util.Map;

import com.lo.shop.R;
import com.lo.shop.adapter.CateListAdapter;
import com.lo.shop.fragment.AnimFragment.OnFragmentDismissListener;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CategoryFragment extends BaseFragment implements
		OnItemClickListener, OnClickListener, OnFragmentDismissListener {

	private class TabPagerAdapter extends FragmentStatePagerAdapter implements
	ViewPager.OnPageChangeListener {



public TabPagerAdapter(FragmentManager fm) {
	super(fm);
	mViewPager.setOnPageChangeListener(this);
	
}

@SuppressWarnings("deprecation")
@Override
public void destroyItem(View container, int position, Object object) {
	// TODO Auto-generated method stub
	Log.e(TAG, "销毁一个fragment");
	super.destroyItem(container, position, object);
}

@Override
public int getCount() {
	return 12;
}

@Override
public Fragment getItem(int position) {
	s=1+position+"";
	HomeTabFragment fragment = HomeTabFragment.newInstance(s);
	return fragment;
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
	
}


}
	public static CategoryFragment newInstance() {
		CategoryFragment categoryFragment = new CategoryFragment();

		return categoryFragment;
	}
	private static final String TAG = "CategoryFragment";
	private static final int LENGTH_SHORT = 0;
	private static final int UPDATE = 0;
	private String s = null;
	private Activity mActivity;
	private View mview;
	private TextView mTitleTv;
	private ViewPager mViewPager;
	private TabPagerAdapter mPagerAdapter = null;
	// 左侧分类ListView
	private ListView mCateListView;
	private CateListAdapter mCateListAdapter;
	private String[] mCategories;
	private ImageView mCateIndicatorImg;
	private ImageView back;
	// 用于记录动画开始的位置
	private int mFromY = 0;

	private ImageButton mImageBtn;

	private int calculateListViewItemHeight() {
		ListAdapter listAdapter = mCateListView.getAdapter();
		if (listAdapter == null) {
			return 0;
		}

		int totalHeight = 0;
		int count = listAdapter.getCount();
		for (int i = 0; i < count; i++) {
			View listItem = listAdapter.getView(i, null, mCateListView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		return totalHeight / count;
	}

	/**
	 * 隐藏侧边动画fragment
	 */
	private void dismissAnimFragment() {
		getFragmentManager().popBackStack();
	}

	private void doAnimation(int toY) {
		int cateIndicatorY = mCateIndicatorImg.getTop()
				+ mCateIndicatorImg.getMeasuredHeight() / 2;
		TranslateAnimation animation = new TranslateAnimation(0, 0, mFromY
				- cateIndicatorY, toY - cateIndicatorY);
		animation.setInterpolator(new AccelerateDecelerateInterpolator());
		animation.setFillAfter(true);
		animation.setDuration(400);
		mCateIndicatorImg.startAnimation(animation);
		mFromY = toY;
	}

	@Override
	public String getFragmentName() {
		return TAG;
	}

	private void initViews(View view) {

		mTitleTv = (TextView) view.findViewById(R.id.top_view_text);
		mTitleTv.setText(R.string.category);
		back = (ImageView) view.findViewById(R.id.top_view_back);
        back.setVisibility(View.GONE);
		mCateListView = (ListView) view.findViewById(R.id.cate_listview);
		mCateListView.setOnItemClickListener(this);
		mCateIndicatorImg = (ImageView) view
				.findViewById(R.id.cate_indicator_img);
		mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
		mPagerAdapter = new TabPagerAdapter(getChildFragmentManager());
		mViewPager.setAdapter(mPagerAdapter);
		
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mCategories = mActivity.getResources()
				.getStringArray(R.array.cate_list);

		mCateListAdapter = new CateListAdapter(mActivity, mCategories);
		mCateListView.setAdapter(mCateListAdapter);

		// 用于计算mCateIndicatorImg的高度
		int itemHeight = calculateListViewItemHeight();
		int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		mCateIndicatorImg.measure(w, h);

		// 第一次进来设置indicator的位置
		doAnimation(itemHeight / 2 - mCateIndicatorImg.getMeasuredHeight());
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mActivity = activity;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.image_btn:
			showAnimFragment();
			
			
			break;

		default:
			break;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_category, container,
				false);
		return view;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onFragmentDismiss() {
		dismissAnimFragment();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (null != mCateListAdapter) {
			mCateListAdapter.setSelectedPos(position);
		}
		mview=view;
		mViewPager.setCurrentItem(position);
		int toY = view.getTop() + view.getHeight() / 2;
		doAnimation(toY);
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
	
	/**
	 * 显示侧边的动画fragment
	 */
	private void showAnimFragment() {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		 ft.setCustomAnimations(R.anim.left_in, R.anim.right_out);
//		ft.setCustomAnimations(R.anim.bottom_in, R.anim.bottom_out);
		Fragment prev = getFragmentManager().findFragmentByTag("anim_fragment");
		if (prev != null) {
			ft.remove(prev);
		}
		AnimFragment animFragment = new AnimFragment(this);
		ft.addToBackStack(null);
		ft.add(R.id.anim_fragment_layout, animFragment, "anim_fragment")
				.commitAllowingStateLoss();
	}
}
