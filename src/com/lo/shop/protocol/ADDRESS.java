
package com.lo.shop.protocol;
import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.external.activeandroid.Model;
import com.external.activeandroid.annotation.Column;
import com.external.activeandroid.annotation.Table;

@Table(name = "ADDRESS")
public class ADDRESS  extends Model implements Serializable
{

   

     @Column(name = "ADDRESS_id",unique = true)
     public int id;

    

     @Column(name = "address")
     public String address;

   

 public void fromJson(JSONObject jsonObject)  throws JSONException
 {
     if(null == jsonObject){
       return ;
      }


     JSONArray subItemArray;

    
     this.id = jsonObject.optInt("id");

     
     this.address = jsonObject.optString("address");

     return ;
 }

 public JSONObject  toJson() throws JSONException 
 {
     JSONObject localItemObject = new JSONObject();
     JSONArray itemJSONArray = new JSONArray();
     
     localItemObject.put("id", id);
     
     localItemObject.put("address", address);
     
     return localItemObject;
 }

}
