package com.lo.shop.activity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.lo.shop.R;
import com.lo.shop.entity.ActivityManagerModel;
import com.lo.shop.entity.BusinessMessage;

import org.json.JSONException;

@SuppressLint("NewApi")
public class BaseActivity extends Activity implements Handler.Callback
{
    public Handler mHandler;

    public BaseActivity()
    {

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
         
        super.onCreate(savedInstanceState);
        mHandler = new Handler(this);
        ActivityManagerModel.addLiveActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManagerModel.removeLiveActivity(this);
    }

    public void OnMessageResponse(BusinessMessage response)
            throws JSONException
    {
        if (response.messageState == BusinessMessage.FAILURE_MESSAGE)
        {
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        ActivityManagerModel.removeForegroundActivity(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        ActivityManagerModel.addForegroundActivity(this);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        ActivityManagerModel.addVisibleActivity(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        ActivityManagerModel.removeVisibleActivity(this);
    }
    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.push_right_in,
                R.anim.push_right_out);
    }

    public void startActivityForResult(Intent intent, int requestCode)
    {
        super.startActivityForResult(intent,requestCode);
        overridePendingTransition(R.anim.push_right_in,
                R.anim.push_right_out);
    }
}
