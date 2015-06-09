
package com.lo.shop.protocol;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.external.activeandroid.Model;
import com.external.activeandroid.annotation.Column;
import com.external.activeandroid.annotation.Table;

@Table(name = "cartlistResponse")
public class cartlistResponse  extends Model
{

     @Column(name = "status")
     public STATUS   status;

     @Column(name = "data")
     public CART_LIST_DATA   data;
     
     @Column(name = "shoppingCartItems")
     public CART_LIST_DATA   shoppingCartItems;
     
     @Column(name = "userId")
     public int   userId;

     public void  fromJson(JSONObject jsonObject)  throws JSONException
     {
          if(null == jsonObject){
            return ;
           }

          JSONArray subItemArray;
         
          CART_LIST_DATA  shoppingCartItems = new CART_LIST_DATA();
          shoppingCartItems.fromJson(jsonObject.optJSONArray("shoppingCartItems"));
          this.shoppingCartItems = shoppingCartItems;
          int  userId = 0;
          userId=(jsonObject.optInt("userId"));
          this.userId = userId;
          return ;
     }

     public JSONObject  toJson() throws JSONException 
     {
          JSONObject localItemObject = new JSONObject();
          JSONArray itemJSONArray = new JSONArray();
          if(null != status)
          {
            localItemObject.put("status", status.toJson());
          }
          if(null != data)
          {
            localItemObject.put("data", data.toJson());
          }
          return localItemObject;
     }

}
