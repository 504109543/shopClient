
package com.lo.shop.protocol;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.external.activeandroid.Model;
import com.external.activeandroid.annotation.Column;
import com.external.activeandroid.annotation.Table;

@Table(name = "usersigninRequest")
public class usersigninRequest  extends Model
{

     @Column(name = "name")
     public String   name = null;

     @Column(name = "password")
     public String   password = null;

     public void  fromJson(JSONObject jsonObject)  throws JSONException
     {
          if(null == jsonObject){
            return ;
           }

          JSONArray subItemArray;

          this.name = jsonObject.optString("name");

          this.password = jsonObject.optString("password");
          return ;
     }

     public JSONObject  toJson() throws JSONException 
     {
          JSONObject localItemObject = new JSONObject();
          
  
          localItemObject.put("user.userName", name);
          localItemObject.put("user.password", password);
         
          return localItemObject;
     }

}
