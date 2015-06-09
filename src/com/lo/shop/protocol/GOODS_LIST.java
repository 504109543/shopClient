
package com.lo.shop.protocol;
import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.external.activeandroid.Model;
import com.external.activeandroid.annotation.Column;
import com.external.activeandroid.annotation.Table;

@Table(name = "GOODS_LIST")
public class GOODS_LIST  extends Model implements Serializable
{
	@Column(name = "goods_number")
    public String goods_number;

	@Column(name = "img")
    public PHOTO   img;
	
	@Column(name = "goods_name")
    public String goods_name;
	
	@Column(name = "goods_price")
    public String goods_price;
	
	@Column(name = "goods_id")
    public String goods_id;
	
	@Column(name = "totalprice")
    public String totalprice;
	

 public void fromJson(JSONObject jsonObject)  throws JSONException
 {
     if(null == jsonObject){
       return ;
      }
     JSONArray subItemArray;

     PHOTO photo=new PHOTO();
     photo.fromJson(jsonObject);
     this.img =photo;

     this.goods_name = jsonObject.optString("name");
     this.goods_price = jsonObject.optString("price");
     this.goods_id = jsonObject.optString("id");
     
     return ;
 }

 public JSONObject  toJson() throws JSONException 
 {
     JSONObject localItemObject = new JSONObject();
     JSONArray itemJSONArray = new JSONArray();
     localItemObject.put("goods_number", goods_number);
     if(null!=img)
     {
       localItemObject.put("img", img.toJson());
     }
     localItemObject.put("goods_name", goods_name);
     localItemObject.put("goods_price", goods_price);
     localItemObject.put("goods_id", goods_id);
     
     return localItemObject;
 }

}
