
package com.lo.shop.protocol;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.external.activeandroid.Model;
import com.external.activeandroid.annotation.Column;
import com.external.activeandroid.annotation.Table;

@Table(name = "PHOTO")
public class PHOTO  extends Model
{

	
    @Column(name = "pic_url")
    public String pic_url;		//图
     
     

 public void fromJson(JSONObject jsonObject)  throws JSONException
 {
     if(null == jsonObject){
       return ;
      }


     JSONArray subItemArray;
     this.pic_url = ApiInterface.URL+":8080/shop/"+jsonObject.optString("proPic");
     //Log.e("PHOTO","PHOTO"+this.pic_url);
     
     return ;
 }

 public JSONObject  toJson() throws JSONException 
 {
     JSONObject localItemObject = new JSONObject();
     JSONArray itemJSONArray = new JSONArray();
     
     return localItemObject;
 }

}
