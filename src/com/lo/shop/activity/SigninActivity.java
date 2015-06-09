package com.lo.shop.activity;


import com.lo.shop.entity.BusinessResponse;
import com.lo.shop.entity.LoginModel;
import com.lo.shop.fragment.ProfileFragment;
import com.lo.shop.protocol.ApiInterface;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.external.androidquery.callback.AjaxStatus;
import com.lo.shop.activity.BaseActivity;
import com.lo.shop.view.ToastView;
import com.lo.shop.R;

public class SigninActivity extends BaseActivity implements OnClickListener, BusinessResponse{
	
	private ImageView back;
	private Button login;
	
	private EditText userName;
	private EditText password;
	private TextView register;
	
	private String name;
	private String psd;
	
	private LoginModel loginModel;
    private final static int REQUEST_SIGN_UP = 1;
	
	// 关闭键盘
	public void CloseKeyBoard() {
		userName.clearFocus();
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(userName.getWindowToken(), 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {		
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == REQUEST_SIGN_UP) {
			if(data!=null) {
				Intent intent = new Intent();
				intent.putExtra("login", true);
				setResult(Activity.RESULT_OK, intent);  
	            finish();
                ProfileFragment.isRefresh=true;
	            overridePendingTransition(R.anim.push_up_in,R.anim.push_up_out);
    		}
		}
		
	}

	@Override
	public void onClick(View v) {		
        Resources resource = (Resources) getBaseContext().getResources();
        String usern=resource.getString(R.string.user_name_cannot_be_empty);
        String pass=resource.getString(R.string.password_cannot_be_empty);
		Intent intent;
		switch(v.getId()) {
		case R.id.login_back:
			finish();
			CloseKeyBoard();
			overridePendingTransition(R.anim.push_up_in,R.anim.push_up_out);
			break;
		case R.id.login_login:
			name = userName.getText().toString();
			psd = password.getText().toString();
            if(name.length()<2){
                ToastView toast = new ToastView(this, resource.getString(R.string.username_too_short));
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
            if(name.length()>20){
                ToastView toast = new ToastView(this, resource.getString(R.string.username_too_long));
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
            if(psd.length()<6){
                ToastView toast = new ToastView(this, resource.getString(R.string.password_too_short));
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
            if(psd.length()>20){
                ToastView toast = new ToastView(this, resource.getString(R.string.password_too_long));
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
			if("".equals(name)) {				
				ToastView toast = new ToastView(this, usern);
		        toast.setGravity(Gravity.CENTER, 0, 0);
		        toast.show();
			} else if("".equals(psd)) {				
				ToastView toast = new ToastView(this, pass);
		        toast.setGravity(Gravity.CENTER, 0, 0);
		        toast.show();
			} else {
				
				loginModel = new LoginModel(SigninActivity.this);
				loginModel.addResponseListener(this);				
				loginModel.login(name, psd);
				CloseKeyBoard();
				
			}
			break;
		case R.id.login_register:
			intent = new Intent(this, SignupActivity.class);
			startActivityForResult(intent, REQUEST_SIGN_UP);
			break;
		}
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signin);
		
		back = (ImageView) findViewById(R.id.login_back);
		login = (Button) findViewById(R.id.login_login);
		userName = (EditText) findViewById(R.id.login_name);
		password = (EditText) findViewById(R.id.login_password);
		register = (TextView) findViewById(R.id.login_register);
        register.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		
		back.setOnClickListener(this);
		login.setOnClickListener(this);
		register.setOnClickListener(this);
		
	}
	
	/*@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {		
		if(keyCode == KeyEvent.KEYCODE_BACK){
			finish();
			overridePendingTransition(R.anim.push_up_in,R.anim.push_up_out);
		}
		return true;
	}*/
	
	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {		
			if(url.endsWith(ApiInterface.USER_SIGNIN)) {
				Intent intent = new Intent();
				intent.putExtra("login", true);
				setResult(Activity.RESULT_OK, intent);  
	            finish(); 
	            overridePendingTransition(R.anim.push_up_in,R.anim.push_up_out);
			}	
	}
}
