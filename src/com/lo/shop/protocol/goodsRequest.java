
package com.lo.shop.protocol;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.external.activeandroid.Model;
import com.external.activeandroid.annotation.Column;
import com.external.activeandroid.annotation.Table;

@Table(name = "goodsRequest")
public class goodsRequest  extends Model
{

     @Column(name = "goods_id")
     public int goods_id;

    @Column(name = "session")
    public SESSION   session;
    
    @Column(name = "page")
    public int   page;
    
    @Column(name = "rows")
    public int   rows;
   		

     public void  fromJson(JSONObject jsonObject)  throws JSONException
     {
          if(null == jsonObject){
            return ;
           }

          JSONArray subItemArray;

          this.goods_id = jsonObject.optInt("goods_id");
          this.page = jsonObject.optInt("page");
          this.rows = jsonObject.optInt("rows");
         SESSION  session = new SESSION();
         session.fromJson(jsonObject.optJSONObject("session"));
         this.session = session;
          return ;
     }

     public JSONObject  toJson() throws JSONException 
     {
          JSONObject localItemObject = new JSONObject();
          JSONArray itemJSONArray = new JSONArray();
          localItemObject.put("goods_id", goods_id);
          localItemObject.put("page", page);
          localItemObject.put("rows", rows);
         if(null != session)
         {
             localItemObject.put("session", session.toJson());
         }
          return localItemObject;
     }

}
