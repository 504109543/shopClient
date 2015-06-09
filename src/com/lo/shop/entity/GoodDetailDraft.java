package com.lo.shop.entity;


import android.R;
import com.lo.shop.protocol.GOODS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class GoodDetailDraft {

    public static GoodDetailDraft getInstance()
    {
        if (instance == null) {
            instance = new GoodDetailDraft();
        }
        return instance;
    }

    public GOODS goodDetail = new GOODS();
   

    public int  goodQuantity = 1;
    private static GoodDetailDraft instance;

   

    public  void clear()
    {
        goodDetail = null;
       // selectedSpecification.clear();
        goodQuantity = 1;
    }

    public float getTotalPrice()
    {
		return goodQuantity;
    }

   
}
