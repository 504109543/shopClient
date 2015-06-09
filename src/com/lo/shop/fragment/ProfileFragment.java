package com.lo.shop.fragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.content.res.Resources;

import com.lo.shop.activity.*;
import com.lo.shop.entity.BusinessResponse;
import com.lo.shop.entity.LoginModel;
import com.lo.shop.entity.UserInfoModel;
import com.lo.shop.protocol.ApiInterface;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.external.activeandroid.query.Select;
import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.XListView;
import com.external.maxwin.view.XListView.IXListViewListener;
import com.external.maxwin.view.XListView;
import com.external.maxwin.view.XListView.IXListViewListener;
import com.lo.shop.view.WebImageView;
import com.lo.shop.R;
import com.lo.shop.protocol.USER;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


public class ProfileFragment extends BaseFragment implements IXListViewListener, OnClickListener, BusinessResponse 
{

	public static ProfileFragment newInstance() {
    	ProfileFragment ProfileFragment = new ProfileFragment();

		return ProfileFragment;
	}
	
	public static final String TAG = "ProfileFragment";
	private File file;
	private View view;
	
	private View headView;
	private XListView xlistView;
	private ImageView back;
	private WebImageView photo;
	private ImageView camera;
	private TextView name;
	private FrameLayout payment;
	
	private FrameLayout ship;

	private FrameLayout receipt;
	private FrameLayout history;
	private LinearLayout collect;

    private LinearLayout notify;
    private LinearLayout address_manage;
    private LinearLayout memberLevelLayout;

    private TextView     memberLevelName;
	
	private ImageView    memberLevelIcon;
	private LinearLayout help;
	private USER user;
	
	private UserInfoModel userInfoModel;
	private LoginModel LoginModel;
	
	private SharedPreferences shared;
	private SharedPreferences.Editor editor;
	private ImageView image;

	private String uid;
	
    public static boolean isRefresh = false;
    
    private TextView mTitleTv;
    
    protected Context mContext;
    
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    
	@Override
	public String getFragmentName() {
		// TODO Auto-generated method stub
		return TAG;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {  
           
        super.onActivityResult(requestCode, resultCode, data);  
        
        if (resultCode == Activity.RESULT_OK) {

        	if(requestCode == 1) {
        		String sdStatus = Environment.getExternalStorageState();
                Bundle bundle = data.getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式

                if (file == null) {
					file = new File(Environment.getExternalStorageDirectory()+ "/shop/cache");
					if (!file.exists()) {
						file.mkdirs();
					}
				}
                FileOutputStream b = null;
                String fileName = Environment.getExternalStorageDirectory() + "/download_test11"+"/"+uid+"-temp.jpg";
                try {
                    b = new FileOutputStream(fileName);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        b.flush();
                        b.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                ((ImageView) view.findViewById(R.id.profile_head_photo)).setImageBitmap(bitmap);// 将图片显示在ImageView里
        	}

        }
        
        if(requestCode == 2) {
        	userInfoModel.getUserInfo();
    	}
    	
    	
    }
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
	
	@Override
	public void onClick(View v) {
		 
		Intent intent;
		switch(v.getId()) {
		case R.id.profile_head_camera:
			if (uid.equals("")) {
				isRefresh = true;
				intent = new Intent(getActivity(), SigninActivity.class);
				startActivity(intent);
            	getActivity().overridePendingTransition(R.anim.push_buttom_in,R.anim.push_buttom_out);
			} else {
				intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  
	            startActivityForResult(intent, 1);
                getActivity().overridePendingTransition(R.anim.push_right_in,
                        R.anim.push_right_out);
			}
			break;
		case R.id.profile_head_payment://代付款
			if (uid.equals("")) {
				isRefresh = true;
				intent = new Intent(getActivity(), SigninActivity.class);
				startActivity(intent);
            	getActivity().overridePendingTransition(R.anim.push_buttom_in,R.anim.push_buttom_out);
			} else {
				intent = new Intent(getActivity(), HistoryActivity.class);
				intent.putExtra("flag", "await_audit");
				startActivityForResult(intent, 2);
                getActivity().overridePendingTransition(R.anim.push_right_in,
                        R.anim.push_right_out);
			}
			break;
		case R.id.profile_head_ship://待发货
			if (uid.equals("")) {
				isRefresh = true;
				intent = new Intent(getActivity(), SigninActivity.class);
				startActivity(intent);
            	getActivity().overridePendingTransition(R.anim.push_buttom_in,R.anim.push_buttom_out);
			} else {
				intent = new Intent(getActivity(), HistoryActivity.class);
				intent.putExtra("flag", "await_ship");
				startActivityForResult(intent, 2);
                getActivity().overridePendingTransition(R.anim.push_right_in,
                        R.anim.push_right_out);
			}
			break;
		case R.id.profile_head_receipt://待收货
			if (uid.equals("")) {
				isRefresh = true;
				intent = new Intent(getActivity(), SigninActivity.class);
				startActivity(intent);
            	getActivity().overridePendingTransition(R.anim.push_buttom_in,R.anim.push_buttom_out);
			} else {
				intent = new Intent(getActivity(), HistoryActivity.class);
				intent.putExtra("flag", "shipped");
				startActivityForResult(intent, 2);
                getActivity().overridePendingTransition(R.anim.push_right_in,
                        R.anim.push_right_out);
			}
			break;
		case R.id.profile_head_history://历史订单
			if (uid.equals("")) {
				isRefresh = true;
				intent = new Intent(getActivity(), SigninActivity.class);
				startActivity(intent);
            	getActivity().overridePendingTransition(R.anim.push_buttom_in,R.anim.push_buttom_out);
			} else {
				intent = new Intent(getActivity(), HistoryActivity.class);
				intent.putExtra("flag", "finished");
				startActivityForResult(intent, 2);
                getActivity().overridePendingTransition(R.anim.push_right_in,
                        R.anim.push_right_out);
			}
			break;
		
		case R.id.profile_head_notify:
			if (uid.equals("")) {
				isRefresh = true;
				intent = new Intent(getActivity(), SigninActivity.class);
				startActivity(intent);
            	getActivity().overridePendingTransition(R.anim.push_buttom_in,R.anim.push_buttom_out);
			} else {
				Toast.makeText(mContext, "反馈             2015-4-1 18:25:22", 0).show();;
			}
			break;
		case R.id.profile_head_address_manage:
			if (uid.equals("")) {
				isRefresh = true;
				intent = new Intent(getActivity(), SigninActivity.class);
            	startActivity(intent);
            	getActivity().overridePendingTransition(R.anim.push_buttom_in,R.anim.push_buttom_out);
			} else {
				intent = new Intent(getActivity(), AddressListActivity.class);
				startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_right_in,
                        R.anim.push_right_out);
			}
			break;
		case R.id.profile_head_name:
			if (uid.equals("")) {
				isRefresh = true;
				intent = new Intent(getActivity(), SigninActivity.class);
            	startActivity(intent);
            	getActivity().overridePendingTransition(R.anim.push_buttom_in,R.anim.push_buttom_out);
			}
			break;
         case R.id.profile_help:
            
			Toast.makeText(mContext, "帮助。     on 2015-4-1 18:19:57", 0).show();
			break;
         case R.id.profile_head_photo:
        	 if (uid.equals("")) {
 				isRefresh = true;
 				intent = new Intent(getActivity(), SigninActivity.class);
             	startActivity(intent);
             	getActivity().overridePendingTransition(R.anim.push_buttom_in,R.anim.push_buttom_out);
 			 }else{
                 intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                 startActivityForResult(intent, 1);
                 getActivity().overridePendingTransition(R.anim.push_right_in,
                         R.anim.push_right_out);
             }
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
		 
		imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
		view = inflater.inflate(R.layout.profile, container,
				false);
		
		mContext = getActivity();
		back = (ImageView) view.findViewById(R.id.top_view_back);
        back.setVisibility(View.GONE);
		shared = getActivity().getSharedPreferences("userInfo", 0); 
		editor = shared.edit();

        headView = LayoutInflater.from(getActivity()).inflate(R.layout.profile_head, null);


		xlistView = (XListView) view.findViewById(R.id.profile_list);
        xlistView.addHeaderView(headView);

		xlistView.setPullLoadEnable(false);
		xlistView.setRefreshTime();
		xlistView.setXListViewListener(this,1);
		xlistView.setAdapter(null);

        memberLevelLayout = (LinearLayout)headView.findViewById(R.id.member_level_layout);
        memberLevelName   = (TextView)headView.findViewById(R.id.member_level);
        memberLevelIcon   = (ImageView)headView.findViewById(R.id.member_level_icon);
		photo = (WebImageView) headView.findViewById(R.id.profile_head_photo);
		camera = (ImageView) headView.findViewById(R.id.profile_head_camera);
		name = (TextView) headView.findViewById(R.id.profile_head_name);
		
		payment = (FrameLayout) headView.findViewById(R.id.profile_head_payment);
		
		
		ship = (FrameLayout) headView.findViewById(R.id.profile_head_ship);
		
		
		receipt = (FrameLayout) headView.findViewById(R.id.profile_head_receipt);		
		history = (FrameLayout) headView.findViewById(R.id.profile_head_history);
		notify = (LinearLayout) headView.findViewById(R.id.profile_head_notify);
		address_manage = (LinearLayout) headView.findViewById(R.id.profile_head_address_manage);
        help = (LinearLayout)headView.findViewById(R.id.profile_help);
        
        mTitleTv = (TextView) view.findViewById(R.id.top_view_text);
		mTitleTv.setText(R.string.setting);
		camera.setOnClickListener(this);
		payment.setOnClickListener(this);
		ship.setOnClickListener(this);
		receipt.setOnClickListener(this);
		history.setOnClickListener(this);
		notify.setOnClickListener(this);
		address_manage.setOnClickListener(this);
		name.setOnClickListener(this);
        help.setOnClickListener(this);

        uid = shared.getString("uid", "");
		File files = new File(Environment.getExternalStorageDirectory() + "/download_test11"+"/"+uid+"-temp.jpg");
		if(files.exists()&&!uid.equals("")) {
			photo.setImageBitmap(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/download_test11"+"/"+uid+"-temp.jpg"));
		} else {
			photo.setImageResource(R.drawable.profile_no_avarta_icon);
		}
		
		photo.setOnClickListener(this);

        if (null == userInfoModel)
        {
            userInfoModel = new UserInfoModel(getActivity());
        }
        userInfoModel.addResponseListener(this);
		if (uid.equals("")) {

			Resources resource = mContext.getResources();
           
            String click=resource.getString(R.string.click_to_login);
			name.setText(click);
			camera.setVisibility(View.GONE);
            memberLevelLayout.setVisibility(View.GONE);

		} else {
			userInfoModel.getUserInfo();
			camera.setVisibility(View.VISIBLE);
            memberLevelLayout.setVisibility(View.VISIBLE);
		}
		return view;
	}

	@Override
    public void onDestroy() {
        userInfoModel.removeResponseListener(this);
        super.onDestroy();
    }
	

	@Override
	public void onLoadMore(int id) {
		 
		
	}  

	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status) {
		if (url.endsWith(ApiInterface.USER_INFO)) {			
			xlistView.stopRefresh();
			xlistView.setRefreshTime();
			user = userInfoModel.user; // 从网络获取			
			setUserInfo();
		} 
	}


	@Override
    public void onPause() {
         
        super.onPause();
        
    }

    @Override
	public void onRefresh(int id) {
		 

		if (!uid.equals("")) {
			userInfoModel.getUserInfo();
		}
		
	}
    @Override
	public void onResume() {
		 
		super.onResume();
		uid = shared.getString("uid", "");
		if (!uid.equals("")) {
			if(isRefresh == true) {
				userInfoModel.getUserInfo();
			}
			camera.setVisibility(View.VISIBLE);
		} else {
			camera.setVisibility(View.GONE);
		}
		isRefresh = false;
        
	}

	// set User 信息
	public void setUserInfo() {
		name.setText(user.name);
        File files = new File(Environment.getExternalStorageDirectory() + "/download_test11"+"/"+uid+"-temp.jpg");
        if(files.exists()&&!uid.equals("")) {
            photo.setImageBitmap(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/download_test11"+"/"+uid+"-temp.jpg"));
        } else {
            photo.setImageResource(R.drawable.profile_no_avarta_icon);
        }
        

		
	}
}
