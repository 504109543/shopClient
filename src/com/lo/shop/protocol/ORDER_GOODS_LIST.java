
package com.lo.shop.protocol;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;

import com.external.activeandroid.Model;
import com.external.activeandroid.annotation.Column;
import com.external.activeandroid.annotation.Table;

@Table(name = "ORDER_GOODS_LIST")
public class ORDER_GOODS_LIST  extends Model
{

     @Column(name = "goods_number")//num
     public String goods_number;

     @Column(name = "goods_id") //id
     public String goods_id;

     @Column(name = "name")
     public String name;

     @Column(name = "img")
     public PHOTO   img;

     @Column(name = "formated_shop_price")
     public String formated_shop_price;

     @Column(name = "subtotal")
     public String subtotal;

 public void  fromJson(JSONObject jsonObject)  throws JSONException
 {
     if(null == jsonObject){
       return ;
      }



     JSONArray subItemArray;

     this.goods_number = jsonObject.optString("num");

     this.goods_id = jsonObject.optJSONObject("product").optString("id");

     this.name = jsonObject.optJSONObject("product").optString("name");
     PHOTO photo=new PHOTO();
     photo.fromJson(jsonObject.optJSONObject("product"));
     this.img = photo;

     this.formated_shop_price = jsonObject.optJSONObject("product").optString("price");

     this.subtotal = Integer.parseInt(this.formated_shop_price)*Integer.parseInt(this.goods_number)+"";
     return ;
 }

 public JSONObject  toJson() throws JSONException 
 {
     JSONObject localItemObject = new JSONObject();
     JSONArray itemJSONArray = new JSONArray();
     localItemObject.put("goods_number", goods_number);
     localItemObject.put("goods_id", goods_id);
     localItemObject.put("name", name);
     if(null!=img)
     {
       localItemObject.put("img", img.toJson());
     }
     localItemObject.put("formated_shop_price", formated_shop_price);
     localItemObject.put("subtotal", subtotal);
     return localItemObject;
 }

}
