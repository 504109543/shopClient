
package com.lo.shop.protocol;
import java.util.ArrayList;

import com.lo.shop.protocol.GOODS_LIST;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.external.activeandroid.Model;
import com.external.activeandroid.annotation.Column;
import com.external.activeandroid.annotation.Table;

@Table(name = "CART_LIST_DATA")
public class CART_LIST_DATA  extends Model
{

    
     public ArrayList<GOODS_LIST>   goods_list = new ArrayList<GOODS_LIST>();
     
     @Column(name = "count")
     public int count;

     public void  fromJson(JSONArray jsonArray)  throws JSONException
     {
          if(null == jsonArray){
            return ;
           }

          JSONArray subItemArray;
         
          int  count = 0;
          

          subItemArray =  jsonArray;
          if(null != subItemArray)
           {
        	  //System.out.println("CART_LIST数据有"+subItemArray.length()+"个");
              for(int i = 0;i < subItemArray.length();i++)
               {
                  JSONObject subItemObject = subItemArray.getJSONObject(i);
                 
                  count=subItemObject.optInt("count");
     
                  GOODS_LIST subItem = new GOODS_LIST();
                  subItem.fromJson(subItemObject.optJSONObject("product"));
                  subItem.goods_number=count+"";
                  this.goods_list.add(subItem);
                  
               }
           }

          return ;
     }

     public JSONObject  toJson() throws JSONException 
     {
          JSONObject localItemObject = new JSONObject();
          JSONArray itemJSONArray = new JSONArray();
       
          for(int i =0; i< goods_list.size(); i++)
          {
              GOODS_LIST itemData =goods_list.get(i);
              JSONObject itemJSONObject = itemData.toJson();
              itemJSONArray.put(itemJSONObject);
          }
          localItemObject.put("goods_list", itemJSONArray);
          return localItemObject;
     }

}
