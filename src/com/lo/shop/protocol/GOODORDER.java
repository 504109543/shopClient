
package com.lo.shop.protocol;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.external.activeandroid.Model;
import com.external.activeandroid.annotation.Column;
import com.external.activeandroid.annotation.Table;

@Table(name = "GOODORDER")
public class GOODORDER  extends Model   
{

     @Column(name = "order_time")//createTime
     public String order_time;

     @Column(name = "total_fee")//cost
     public String total_fee;
     
     @Column(name = "order_sn")//orderNo
     public String order_sn;

     @Column(name = "order_id")//id
     public String order_id;
     
     @Column(name = "status")//status
     public int status;

     public ArrayList<ORDER_GOODS_LIST>   goods_list = new ArrayList<ORDER_GOODS_LIST>();//orderProductList

     @Column(name = "formated_integral_money")
     public String formated_integral_money;

     @Column(name = "formated_bonus")
     public String formated_bonus;

     

     @Column(name = "formated_shipping_fee")
     public String formated_shipping_fee;

   

 public void fromJson(JSONObject jsonObject)  throws JSONException
 {
     if(null == jsonObject){
       return ;
      }


     JSONArray subItemArray;

     this.order_time = jsonObject.optString("createTime");

     this.total_fee = jsonObject.optString("cost");

     subItemArray = jsonObject.optJSONArray("orderProductList");
     if(null != subItemArray)
      {
         for(int i = 0;i < subItemArray.length();i++)
          {
              JSONObject subItemObject = subItemArray.getJSONObject(i);
              ORDER_GOODS_LIST subItem = new ORDER_GOODS_LIST();
              subItem.fromJson(subItemObject);
              this.goods_list.add(subItem);
         }
     }


     
     this.order_sn = jsonObject.optString("orderNo");

     this.order_id = jsonObject.optString("id");
     
     this.status = jsonObject.optInt("status");

    
     return ;
 }

 public JSONObject  toJson() throws JSONException 
 {
     JSONObject localItemObject = new JSONObject();
     JSONArray itemJSONArray = new JSONArray();
     localItemObject.put("order_time", order_time);
     localItemObject.put("total_fee", total_fee);

     for(int i =0; i< goods_list.size(); i++)
     {
         ORDER_GOODS_LIST itemData =goods_list.get(i);
         JSONObject itemJSONObject = itemData.toJson();
         itemJSONArray.put(itemJSONObject);
     }
     localItemObject.put("goods_list", itemJSONArray);
     localItemObject.put("formated_integral_money", formated_integral_money);
     localItemObject.put("formated_bonus", formated_bonus);
     localItemObject.put("order_sn", order_sn);
     localItemObject.put("order_id", order_id);
     localItemObject.put("formated_shipping_fee", formated_shipping_fee);
    
     return localItemObject;
 }

}
