package com.lo.shop.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;

import org.json.JSONException;
import org.json.JSONTokener;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lo.shop.adapter.TopTabAdapter;

public class Utils {

	public static List<TopTabAdapter> fromJson(String jsonStr)
			throws Exception {
		Gson gson = new Gson();
		Type list = new TypeToken<List<TopTabAdapter>>() {
		}.getType();
		List<TopTabAdapter> data = gson.fromJson(jsonStr, list);
		return data;
	}
	public static String getAppVersionName(Context context) {
        String versionName = "";
        try
        {
            // ---get the package info---
            PackageManager pm  = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0)
            {
                return "";
            }
        } catch (Exception e) {
        }
        return versionName;
    }
	public static Object getData(String url_address, Class<?> c)
			throws Exception {
		URL url = new URL(url_address);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("GET");
		InputStream is = conn.getInputStream();
		if (conn.getResponseCode() == 200) {
			byte[] b = readInputStream(conn.getInputStream());
			if (c == String.class) {
				String temp = new String(b);
				return temp;
			} else {
				tempbit = new String(b);
				Bitmap temp = BitmapFactory.decodeByteArray(b, 0, b.length);
				return temp;
				// Load image, decode it to Bitmap and return Bitmap synchronously				
				//Bitmap bmp = il.loadImageSync(tempbit, targetSize, null);
				//return bmp;
			}
		}
		return null;
	}
	
	public static Object parseResponse(String responseBody) throws JSONException {
        Object result = null;
        //trim the string to prevent start with blank, and test if the string is valid JSON, because the parser don't do this :(. If Json is not valid this will return null
        responseBody = responseBody.trim();
        if(responseBody.startsWith("{") || responseBody.startsWith("[")) {
            result = new JSONTokener(responseBody).nextValue();
        }
        if (result == null) {
            result = responseBody;
        }
        return result;
    }

	private static byte[] readInputStream(InputStream in) throws Exception {
		// TODO Auto-generated method stub
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] b = new byte[1024];
		int len = 0;
		while ((len = in.read(b)) != -1) {
			baos.write(b, 0, len);
		}
		in.close();
		return baos.toByteArray();
	}

	public static Bitmap scaleBitmap(Bitmap bm,int pixel){
        int srcHeight = bm.getHeight();
        int srcWidth = bm.getWidth();


        if(srcHeight>pixel || srcWidth>pixel)
        {
            float scale_y = 0;
            float scale_x = 0;
            if (srcHeight > srcWidth)
            {
                scale_y = ((float)pixel)/srcHeight;
                scale_x = scale_y;
            }
            else
            {
                scale_x = ((float)pixel)/srcWidth;
                scale_y = scale_x;
            }

            Matrix  matrix = new Matrix();
            matrix.postScale(scale_x, scale_y);
            Bitmap dstbmp = Bitmap.createBitmap(bm, 0, 0, srcWidth, srcHeight, matrix, true);
            return dstbmp;
        }
        else{
            return Bitmap.createBitmap(bm);
        }
    }

	public static Bitmap scaleBitmap(Bitmap bm,int dstHeight,int dstWidth){
        if(bm == null) return null;//java.lang.NullPointerException
        int srcHeight = bm.getHeight();
        int srcWidth = bm.getWidth();
        if(srcHeight>dstHeight || srcWidth>dstWidth){
            float scale_y = ((float)dstHeight)/srcHeight;
            float scale_x = ((float)dstWidth)/srcWidth;
            float scale = scale_y<scale_x?scale_y:scale_x;
            Matrix  matrix = new Matrix();
            matrix.postScale(scale, scale);
            Bitmap dstbmp = Bitmap.createBitmap(bm, 0, 0, srcWidth, srcHeight, matrix, true);
            return dstbmp;
        }
        else{
            return Bitmap.createBitmap(bm);
        }
    }

	public static String toJson(Object object) {
		Gson gson = new Gson();
		String temp = gson.toJson(object);
		return temp;
	}

    private static final String TAG = "Util";

    private static final int size = 100;

    static String tempbit = null;
	
}
