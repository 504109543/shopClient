
package com.lo.shop.protocol;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.external.activeandroid.Model;
import com.external.activeandroid.annotation.Column;
import com.external.activeandroid.annotation.Table;
import com.lo.shop.entity.ORDER_NUM;

@Table(name = "USER")
public class USER  extends Model
{

    @Column(name = "USER_id",unique = true)
    public String   id;

    @Column(name = "name")
    public String   name;   

    @Column(name="email")
    public String email;
    
    @Column(name = "userName")
    public String   userName;
    
    @Column(name = "password")
    public String   password;
    
    @Column(name = "trueName")
    public String   trueName;
    
    @Column(name = "mobile")
    public String   mobile;
    
    
    @Column(name="address")
    public String address;

    public void  fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return ;
        }

        JSONArray subItemArray;

    
        this.id = jsonObject.optString("id");

        this.name = jsonObject.optString("trueName");
        this.userName = jsonObject.optString("userName");
        this.password = jsonObject.optString("password");
        this.mobile = jsonObject.optString("mobile");
        this.trueName = jsonObject.optString("trueName");
        this.email=jsonObject.optString("email");
        this.address = jsonObject.optString("address");
        return ;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();
        JSONArray itemJSONArray = new JSONArray();
        
        localItemObject.put("user.userName", userName);
        localItemObject.put("user.password", password);
     
        localItemObject.put("user.email",email);
        localItemObject.put("user.address",address);
        localItemObject.put("user.mobile",mobile);
        return localItemObject;
    }

}
