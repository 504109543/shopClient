package com.lo.shop.activity;



import java.util.ArrayList;
import java.util.Map;
import android.content.res.Resources;
import com.lo.shop.activity.BaseActivity;
import com.lo.shop.entity.BusinessResponse;
import com.lo.shop.entity.RegisterModel;
import com.lo.shop.protocol.ApiInterface;
import com.lo.shop.protocol.FIELD;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.external.activeandroid.util.ReflectionUtils;
import com.external.androidquery.callback.AjaxStatus;
import com.lo.shop.R;
import com.lo.shop.view.ToastView;

public class SignupActivity extends BaseActivity implements OnClickListener, BusinessResponse {

    private ImageView back;
    private Button register;

    private EditText userName;
    private EditText trueName;
    private EditText email;
    private EditText password;
    private EditText passwordRepeat;
    private EditText address;
    private EditText mobile;
    private String name;
    private String tname;
    private String mail;
    private String passwordStr;
    private String passwordRepeatStr;
    private String addressStr;
    private String mobileStr;

    private RegisterModel registerModel;

    private ArrayList<String> items = new ArrayList<String>();

    public static Map<Integer, EditText> edit;
    private ArrayList<FIELD> fields = new ArrayList<FIELD>();

    private boolean flag = true;


    Resources resource;

    // 关闭键盘
    public void CloseKeyBoard() {
        userName.clearFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(userName.getWindowToken(), 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_back:
                finish();
                break;
            case R.id.register_register:
                name = userName.getText().toString();
                tname= trueName.getText().toString();
                mail = email.getText().toString();
                passwordStr = password.getText().toString();
                passwordRepeatStr = passwordRepeat.getText().toString();
                addressStr = address.getText().toString();
                mobileStr = mobile.getText().toString();

                String user = resource.getString(R.string.user_name_cannot_be_empty);
                String tuser = resource.getString(R.string.tuser_name_cannot_be_empty);
                String email = resource.getString(R.string.email_cannot_be_empty);
                String pass = resource.getString(R.string.password_cannot_be_empty);
                String add = resource.getString(R.string.password_cannot_be_empty);
                String mob = resource.getString(R.string.password_cannot_be_empty);
                String fault = resource.getString(R.string.fault);
                String passw = resource.getString(R.string.password_not_match);
                String req = resource.getString(R.string.required_cannot_be_empty);

                if ("".equals(name)) {
                    ToastView toast = new ToastView(this, user);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (name.length() < 2) {
                    ToastView toast = new ToastView(this, resource.getString(R.string.username_too_short));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (name.length() > 20) {
                    ToastView toast = new ToastView(this, resource.getString(R.string.username_too_long));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if ("".equals(tname)) {
                    ToastView toast = new ToastView(this, tuser);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }  
                else if ("".equals(addressStr)) {
                    ToastView toast = new ToastView(this, add);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if ("".equals(mobileStr)) {
                    ToastView toast = new ToastView(this, mob);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                else if ("".equals(mail)) {
                    ToastView toast = new ToastView(this, email);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }else if ("".equals(passwordStr)) {
                    ToastView toast = new ToastView(this, pass);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }  else if (passwordStr.length() < 6) {
                    ToastView toast = new ToastView(this, resource.getString(R.string.password_too_short));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (passwordStr.length() > 20) {
                    ToastView toast = new ToastView(this, resource.getString(R.string.password_too_long));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (!ReflectionUtils.isEmail(mail)) {
                    ToastView toast = new ToastView(this, fault);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (!passwordStr.equals(passwordRepeatStr)) {
                    ToastView toast = new ToastView(this, passw);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } 
                else {
                    StringBuffer sbf = new StringBuffer();
                    for (int i = 0; i < registerModel.signupfiledslist.size(); ++i) {
                        if (registerModel.signupfiledslist.get(i).need.equals("1") && edit.get(i).getText().toString().equals("")) {
                            ToastView toast = new ToastView(this, req);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            flag = false;
                            break;
                        }else{
                            flag = true;
                        }
                        items.add(edit.get(i).getText().toString());
                        sbf.append(edit.get(i).getText().toString() + "/");

                        FIELD field = new FIELD();
                        field.id = Integer.parseInt(registerModel.signupfiledslist.get(i).id);
                        field.value = edit.get(i).getText().toString();
                        fields.add(field);
                    }

                    signup();

                }
                break;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        resource = (Resources) getBaseContext().getResources();

        back = (ImageView) findViewById(R.id.register_back);
        register = (Button) findViewById(R.id.register_register);
        userName = (EditText) findViewById(R.id.register_name);
        trueName = (EditText) findViewById(R.id.register_trueName);
        email = (EditText) findViewById(R.id.register_email);
        password = (EditText) findViewById(R.id.register_password1);
        passwordRepeat = (EditText) findViewById(R.id.register_password2);
        address = (EditText) findViewById(R.id.register_address);
        mobile = (EditText) findViewById(R.id.register_mobile);
        back.setOnClickListener(this);
        register.setOnClickListener(this);

        registerModel = new RegisterModel(this);
        registerModel.addResponseListener(this);
    }

    @Override
    public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status)
            throws JSONException {
        if (registerModel.responseStatus.succeed == 1) {
           if (url.endsWith(ApiInterface.USER_SIGNUP)) {
                Intent intent = new Intent();
                intent.putExtra("login", true);
                setResult(Activity.RESULT_OK, intent);
                finish();
                String wel = resource.getString(R.string.welcome);
                ToastView toast = new ToastView(this, wel);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }

    }

    public void signup() {

        if (flag) {
            CloseKeyBoard(); //关闭键盘
            registerModel.signup(name,tname, passwordStr, mail,addressStr,mobileStr, fields);

        }

    }

   
}
