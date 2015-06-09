package com.lo.shop.fragment;

import com.lo.shop.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * 动画fragment

 * 
 */
public class AnimFragment extends Fragment implements OnClickListener {

	public interface OnFragmentDismissListener {
		public void onFragmentDismiss();
	}
	private Activity mActivity;
	private View mWholeView;

	private ImageView mCancelImg;

	private OnFragmentDismissListener mListener;

	public AnimFragment(Fragment listener) {
		this.mListener = (OnFragmentDismissListener) listener;
	}

	private void dismissFragment() {
		if (null != mListener) {
			mListener.onFragmentDismiss();
		}
	}

	private void initEvents() {
		mWholeView.setOnClickListener(this);
		mCancelImg.setOnClickListener(this);
	}

	private void initViews(View view) {
		mWholeView = view.findViewById(R.id.mark_layout);
		mCancelImg = (ImageView) view.findViewById(R.id.cancel_img);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mActivity = activity;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mark_layout:
		case R.id.cancel_img:
			dismissFragment();
			break;

		default:
			break;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_anim, container, false);
		return view;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initViews(view);
		initEvents();
	}

}
