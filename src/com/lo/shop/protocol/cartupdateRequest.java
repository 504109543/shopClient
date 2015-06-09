
package com.lo.shop.protocol;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.external.activeandroid.Model;
import com.external.activeandroid.annotation.Column;
import com.external.activeandroid.annotation.Table;

@Table(name = "cartupdateRequest")
public class cartupdateRequest  extends Model
{

     @Column(name = "new_number")
     public int new_number;

     @Column(name = "session")
     public SESSION   session;

     @Column(name = "rec_id")
     public int rec_id;
     
     @Column(name = "productId")
     public int productId;
     
     @Column(name = "count")
     public int count;

     public void  fromJson(JSONObject jsonObject)  throws JSONException
     {
          if(null == jsonObject){
            return ;
           }

          JSONArray subItemArray;

          this.new_number = jsonObject.optInt("new_number");
          SESSION  session = new SESSION();
          session.fromJson(jsonObject.optJSONObject("session"));
          this.session = session;

          this.rec_id = jsonObject.optInt("rec_id");
          return ;
     }

     public JSONObject  toJson() throws JSONException 
     {
          JSONObject localItemObject = new JSONObject();
          JSONArray itemJSONArray = new JSONArray();
         
          localItemObject.put("productId", productId);
          localItemObject.put("count", count);
          return localItemObject;
     }

}
