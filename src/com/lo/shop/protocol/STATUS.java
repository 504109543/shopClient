
package com.lo.shop.protocol;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.external.activeandroid.Model;
import com.external.activeandroid.annotation.Column;
import com.external.activeandroid.annotation.Table;

@Table(name = "STATUS")
public class STATUS  extends Model
{

     @Column(name = "succeed")
     public int succeed;

 public  void fromJson(JSONObject jsonObject)  throws JSONException
 {
     if(null == jsonObject){
       return ;
      }

     JSONArray subItemArray;

     this.succeed = jsonObject.optInt("succeed");
     return ;
 }

 public JSONObject  toJson() throws JSONException 
 {
     JSONObject localItemObject = new JSONObject();
     JSONArray itemJSONArray = new JSONArray();
     localItemObject.put("succeed", succeed);
     return localItemObject;
 }

}
