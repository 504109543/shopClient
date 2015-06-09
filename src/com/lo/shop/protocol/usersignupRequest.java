
package com.lo.shop.protocol;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.external.activeandroid.Model;
import com.external.activeandroid.annotation.Column;
import com.external.activeandroid.annotation.Table;

@Table(name = "usersignupRequest")
public class usersignupRequest  extends Model
{


     @Column(name = "email")
     public String   email;

     @Column(name = "userName")
     public String   userName;

     @Column(name = "password")
     public String   password;
     
     @Column(name = "address")
     public String   address;
     
     @Column(name = "mobile")
     public String   mobile;

     public void  fromJson(JSONObject jsonObject)  throws JSONException
     {
          if(null == jsonObject){
            return ;
           }

         
          this.email = jsonObject.optString("email");

          this.userName = jsonObject.optString("name");
          this.address = jsonObject.optString("address");
          this.mobile = jsonObject.optString("mobile");

          this.password = jsonObject.optString("password");
          return ;
     }

     public JSONObject  toJson() throws JSONException 
     {
          JSONObject localItemObject = new JSONObject();
          JSONArray itemJSONArray = new JSONArray();

          localItemObject.put("field", itemJSONArray);
          localItemObject.put("email", email);
          localItemObject.put("name", userName);
          localItemObject.put("address", address);
          localItemObject.put("mobile", mobile);
          localItemObject.put("password", password);
          return localItemObject;
     }

}
