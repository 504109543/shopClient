package com.lo.shop.activity;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import com.external.activeandroid.app.Application;
import com.lo.shop.utils.FrameworkConst;
import com.lo.shop.utils.CustomExceptionHandler;
import com.lo.shop.R;
import com.lo.shop.protocol.SESSION;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class Framework extends Application implements OnClickListener{
    public static Framework getInstance()
    {
        if (instance == null) {
            instance = new Framework();
        }
        return instance;
    }
    private static Framework instance;
    private ImageView bugImage;
    public static DisplayImageOptions options;		// DisplayImageOptions是用于设置图片显示的类
    public static DisplayImageOptions options_head;		// DisplayImageOptions是用于设置图片显示的类
    //public static ImageLoader imageLoader = ImageLoader.getInstance();
    public Context currContext;
    private WindowManager wManager ;
    
    private boolean flag = true;

    public Handler messageHandler;

    void initConfig() {
        SharedPreferences shared;
        shared =    this.getSharedPreferences("userInfo", 0);
        SESSION.getInstance().uid = shared.getString("uid", "");
        SESSION.getInstance().sid = shared.getString("sid", "");
    }
    
   /* public void onClick(View v) {
    	if(flag != false) {
        Intent intent = new Intent(Framework.getInstance().currContext,DebugTabActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    	}
        flag = true;
    }*/
	
    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
        initConfig();
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + FrameworkConst.LOG_DIR_PATH;
        File storePath = new File(path);
        storePath.mkdirs();
        Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(
                path, null));
        options = new DisplayImageOptions.Builder()
        .showStubImage(R.drawable.default_image)			// 设置图片下载期间显示的图片
        .showImageForEmptyUri(R.drawable.default_image)	// 设置图片Uri为空或是错误的时候显示的图片
        .showImageOnFail(R.drawable.default_image)		// 设置图片加载或解码过程中发生错误显示的图片
        .cacheInMemory(true)						// 设置下载的图片是否缓存在内存中
        .cacheOnDisc(true)							// 设置下载的图片是否缓存在SD卡中
                //.displayer(new RoundedBitmapDisplayer(20))	// 设置成圆角图片
        .bitmapConfig(Bitmap.Config.RGB_565)
        .build();

        options_head = new DisplayImageOptions.Builder()
        .showStubImage(R.drawable.profile_no_avarta_icon)			// 设置图片下载期间显示的图片
        .showImageForEmptyUri(R.drawable.profile_no_avarta_icon)	// 设置图片Uri为空或是错误的时候显示的图片
        .showImageOnFail(R.drawable.profile_no_avarta_icon)		// 设置图片加载或解码过程中发生错误显示的图片
        .cacheInMemory(true)						// 设置下载的图片是否缓存在内存中
        .cacheOnDisc(true)							// 设置下载的图片是否缓存在SD卡中
                //.displayer(new RoundedBitmapDisplayer(30))	// 设置成圆角图片
        .build();
        
        
    }

    

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
