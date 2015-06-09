package com.lo.shop.entity;

import android.content.Context;
import android.util.Log;

import com.external.androidquery.AQuery;
import com.external.androidquery.callback.AjaxCallback;
import com.lo.shop.protocol.AddressServer;

import java.util.Map;


public class BaseQuery<T> extends AQuery {
	

	private static String getAbsoluteUrl(String relativeUrl) {
        return  BaseQuery.serviceUrl() + relativeUrl;
    }
	public static String serviceUrl()
	{
          return AddressServer.SERVER_PRODUCTION;

	}
	
	public BaseQuery(Context context) {
		super(context);
		 
	}

    public <K> AQuery ajax(AjaxCallback<K> callback){

		
            String url = callback.getUrl();
            String absoluteUrl = getAbsoluteUrl(url);           
            System.out.println("地址"+absoluteUrl);
            callback.url(absoluteUrl);
		return (BaseQuery)super.ajax(callback);
	}

	public <K> AQuery ajax(String url, Map<String, ?> params, Class<K> type, BaseCallback<K> callback){
						
		callback.type(type).url(url).params(params);
		
		
            String absoluteUrl = getAbsoluteUrl(url);
            
            callback.url(absoluteUrl);
        
		return ajax(callback);
	}


    public <K> AQuery ajaxAbsolute(AjaxCallback<K> callback){

        return (BaseQuery)super.ajax(callback);
    }
}