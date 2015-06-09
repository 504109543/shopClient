
package com.lo.shop.protocol;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.external.activeandroid.Model;
import com.external.activeandroid.annotation.Column;
import com.external.activeandroid.annotation.Table;

@Table(name = "GOODS")
public class GOODS  extends Model
{

    
     @Column(name = "goods_sn")
     public String goods_sn;

   
     @Column(name = "goods_number")
     public String goods_number;

     public ArrayList<PRICE>   rank_prices = new ArrayList<PRICE>();

     @Column(name = "img")
     public PHOTO   img;

    
     public ArrayList<PHOTO>   pictures = new ArrayList<PHOTO>();

     @Column(name = "goods_name")
     public String goods_name;
     
     
     
     
     @Column(name = "name")
     public String name;
     
     @Column(name = "price")
     public String price;
     
     @Column(name = "description")
     public String  description;
     
     @Column(name = "stock")
     public String  stock;
    
     

    

     @Column(name = "goods_weight")
     public String goods_weight;

     @Column(name = "promote_price")
     public int  promote_price;
     
     @Column(name = "formated_promote_price")
     public String formated_promote_price;

     @Column(name = "integral")
     public int integral;

     @Column(name = "GOODS_id")
     public String id;

     @Column(name = "cat_id")
     public String cat_id;

     @Column(name = "is_shipping")
     public String is_shipping;

     @Column(name = "shop_price")
     public String shop_price;

     @Column(name = "market_price")
     public String market_price;

    @Column(name="collected")
    public int collected;


 public void fromJson(JSONObject jsonObject)  throws JSONException
 {
     if(null == jsonObject){
    	 System.out.println("GOODS  jsonObject为空");
       return ;
      }
     JSONArray subItemArray;
     subItemArray = jsonObject.optJSONArray("rank_prices");
     if(null != subItemArray)
      {
         for(int i = 0;i < subItemArray.length();i++)
          {
              JSONObject subItemObject = subItemArray.getJSONObject(i);
              PRICE subItem = new PRICE();
              subItem.fromJson(subItemObject);
              this.rank_prices.add(subItem);
         }
     }
      PHOTO subItem = new PHOTO();
      subItem.fromJson(jsonObject);
      this.pictures.add(subItem);    
     this.name = jsonObject.optString("name");
     this.price = jsonObject.optString("price");
     this.description = jsonObject.optString("description");
      this.stock = jsonObject.optString("stock");
                                          									
     /*subItemArray = jsonObject.optJSONArray("properties");
     if(null != subItemArray)
      {
         for(int i = 0;i < subItemArray.length();i++)
          {
              JSONObject subItemObject = subItemArray.getJSONObject(i);
              PROPERTY subItem = new PROPERTY();
              subItem.fromJson(subItemObject);
              this.properties.add(subItem);
         }
     }*/


     /*this.goods_weight = jsonObject.optString("goods_weight");

     this.promote_price = jsonObject.optInt("promote_price");

     this.formated_promote_price = jsonObject.optString("formated_promote_price");

     this.integral = jsonObject.optInt("integral");

     this.id = jsonObject.optString("id");

     this.cat_id = jsonObject.optString("cat_id");

     this.is_shipping = jsonObject.optString("is_shipping");

     this.shop_price = jsonObject.optString("shop_price");

     this.market_price = jsonObject.optString("market_price");

     this.collected=jsonObject.optInt("collected");*/

     /*subItemArray = jsonObject.optJSONArray("specification");
     if(null != subItemArray)
      {
         for(int i = 0;i < subItemArray.length();i++)
          {
              JSONObject subItemObject = subItemArray.getJSONObject(i);
              SPECIFICATION subItem1 = new SPECIFICATION();
              subItem1.fromJson(subItemObject);
              this.specification.add(subItem1);
         }
     }*/

     return ;
 }

 public JSONObject  toJson() throws JSONException 
 {
     JSONObject localItemObject = new JSONObject();
     JSONArray itemJSONArray = new JSONArray();
   
     localItemObject.put("goods_sn", goods_sn);
     
     localItemObject.put("goods_number", goods_number);

     for(int i =0; i< rank_prices.size(); i++)
     {
         PRICE itemData =rank_prices.get(i);
         JSONObject itemJSONObject = itemData.toJson();
         itemJSONArray.put(itemJSONObject);
     }
     localItemObject.put("rank_prices", itemJSONArray);
     if(null!=img)
     {
       localItemObject.put("img", img.toJson());
     }
    

     for(int i =0; i< pictures.size(); i++)
     {
         PHOTO itemData =pictures.get(i);
         JSONObject itemJSONObject = itemData.toJson();
         itemJSONArray.put(itemJSONObject);
     }
     localItemObject.put("pictures", itemJSONArray);
     localItemObject.put("goods_name", goods_name);
     localItemObject.put("name", name);
     localItemObject.put("id", id);
     localItemObject.put("specification", itemJSONArray);
     return localItemObject;
 }


}
