package com.lo.shop.entity;



import java.util.ArrayList;

import android.content.res.Resources;

import com.lo.shop.entity.BusinessResponse;
import com.lo.shop.fragment.ProfileFragment;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.widget.Toast;

import com.external.androidquery.callback.AjaxStatus;
import com.lo.shop.view.ToastView;
import com.lo.shop.R;
import com.lo.shop.activity.SigninActivity;
import com.lo.shop.protocol.SESSION;
//import com.lo.shop.protocol.STATUS;


public class BaseModel implements BusinessResponse{

    protected com.lo.shop.entity.BaseQuery aq ;
    protected ArrayList<BusinessResponse > businessResponseArrayList = new ArrayList<BusinessResponse>();
    protected Context mContext;
    
    private SharedPreferences shared;
	private SharedPreferences.Editor editor;

    public BaseModel()
    {

    }

    public BaseModel(Context context)
    {
        aq = new BaseQuery(context);
        mContext = context;
    }
    public void addResponseListener(BusinessResponse listener)
    {
        if (!businessResponseArrayList.contains(listener))
        {
            businessResponseArrayList.add(listener);
        }
    }

    //公共的错误处理
    public void callback(String url, JSONObject jo, AjaxStatus status)
    {
        if (null == jo)
        {
            ToastView toast = new ToastView(mContext,"获取数据错误,请检查");
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;
        }
        

    }

    protected void cleanCache()
    {
        return ;
    }

    public  void OnMessageResponse(String url, JSONObject jo, AjaxStatus status) throws JSONException
    {
        for (BusinessResponse iterable_element : businessResponseArrayList)
        {
            iterable_element.OnMessageResponse(url,jo,status);
        }
    }

    public void removeResponseListener(BusinessResponse listener)
    {
        businessResponseArrayList.remove(listener);
    }

    protected void saveCache()
    {
        return ;
    }
}
