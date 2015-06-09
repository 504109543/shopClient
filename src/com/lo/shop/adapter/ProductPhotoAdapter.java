package com.lo.shop.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.lo.shop.adapter.Adapter;
import com.lo.shop.view.WebImageView;
import com.lo.shop.activity.Framework;
import com.lo.shop.R;
import com.lo.shop.protocol.PHOTO;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class ProductPhotoAdapter  extends Adapter{
	
	protected class GoodDetailPhotoHolder extends BeeCellHolder
    {
        public ImageView goodPhotoImageView;
    }
	private SharedPreferences shared;
    private SharedPreferences.Editor editor;

    protected ImageLoader imageLoader = ImageLoader.getInstance();

    public ProductPhotoAdapter(Context c, ArrayList<PHOTO> dataList) {
        super(c, dataList);
    }

    @Override
    protected View bindData(final int position, View cellView, ViewGroup parent,
                            Adapter.BeeCellHolder h) {
        PHOTO photo = (PHOTO)dataList.get(position);
        GoodDetailPhotoHolder holder = (GoodDetailPhotoHolder)h;
        
        shared = mContext.getSharedPreferences("userInfo", 0); 
		editor = shared.edit();
		String imageType = shared.getString("imageType", "mind");				
        imageLoader.displayImage(photo.pic_url,holder.goodPhotoImageView, Framework.options);		                       
        return cellView;
    }

    @Override
    protected Adapter.BeeCellHolder createCellHolder(View cellView) {
        GoodDetailPhotoHolder holder = new GoodDetailPhotoHolder();
        holder.goodPhotoImageView = (ImageView)cellView.findViewById(R.id.good_photo);
        return holder;
    }

    @Override
    public View createCellView() {        
        return mInflater.inflate(R.layout.product_photo_cell, null);
    }

    @Override
    public int getCount()
    {
        return dataList.size();
    }

    public int getItemVIewType(int position)
    {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

}
