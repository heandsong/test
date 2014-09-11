package org.com.song;
import java.io.File;
import java.io.IOException;
import java.net.HttpCookie;

import javax.lang.model.element.Element;

import net.sf.json.JSON;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class  HtmlAnalysis{

	private static String cityId = "9051";
	private static String clientType = "1";
    private static Session s;
    private static Product product;
	public static void main(String[] args) throws JSONException {
		
		long startTime = System.currentTimeMillis();
		getProductInfo();
		//dataPersistence(product);
		long endTime = System.currentTimeMillis();
        
		System.out.println("程序运行时间： " + (endTime - startTime) + "ms");

	}
    public static void dataPersistence(Object o){
    	s=HibernateBase.getSession();
    	Transaction tx=s.beginTransaction();
		s.save(o);
		tx.commit();
		s.close();
    }
	private static void getProductInfo() throws JSONException {
		// TODO Auto-generated method stub
		JSONObject price,category;
		Elements elements;
		String p1;
		product=Product.getInstance();
		String param = "";
		String vendor = "0000000000";
		String storeId="";
		String catalogId="";
		String partNumber="";
		String imageLink="";
		
		try {
				
				Document doc = Jsoup.connect("http://www.suning.com/emall/cprd_10052_10051_0070071164_121204552_.html").timeout(3000).get();
				
				
				// 提取产品价格
				elements = doc.getElementsByTag("head");
				String script = elements.toString();
				int a = script.indexOf("storeId");
				int b = script.indexOf("catalogId");
				int c = script.indexOf("partNumber");
				int d = script.indexOf("vendorCode");

				storeId = script.substring(a + 10, a + 15);
				catalogId = script.substring(b + 12, b + 17);
				partNumber = script.substring(c + 13, c + 31);
				product.setStoreId(storeId);
				product.setCatalogId(catalogId);
				product.setPartNumber(partNumber);
				if (script.substring(d + 14, d + 16).equals("00")) {
					vendor=script.substring(d + 14, d + 24);
					param = "http://product.suning.com/SNProductStatusView?"
							+ "storeId=" + storeId + "&catalogId=" + catalogId
							+ "&partNumber=" + partNumber + "&vendorCode=" + vendor
							+ "&cityId=9051&clientType=1";
				} else {
					param = "http://product.suning.com/SNProductStatusView?"
							+ "storeId=" + storeId + "&catalogId=" + catalogId
							+ "&partNumber=" + partNumber + "&vendor=" + vendor
							+ "&cityId=9051&clientType=1";
				}
                product.setVendor(vendor);
                // 提取产品价格

				//Document doc1 = Jsoup.connect(param).timeout(3000).get();
				//System.out.println(doc1.text());
				if (!((p1 = Http.sendGet(param)).equals(""))) {
				    price = new JSONObject(p1);
				    product.setPromotionPrice(price.getString("promotionPrice"));
					product.setNetPrice( price.getString("netPrice"));
				}

				
				elements = doc.getElementsByClass("product-main-title");
				product.setProductName(elements.text());
				
				org.jsoup.nodes.Element productInfo= doc.getElementById("canshu_box");
				category=new JSONObject(script.substring(script.indexOf("{",script.indexOf("snqa")),script.indexOf("}",script.indexOf("snqa"))+1 ));
				
				product.setProductCatagory(category.getString("categoryName1")+"/"+category.getString("categoryName2")+"/"+category.getString("categoryName3"));
				
				//提取商家信息
				org.jsoup.nodes.Element vendorInfo=doc.getElementById("curShopName");
				if(vendorInfo!=null){
					product.setVendorName(vendorInfo.text());
				}
				else{
					product.setVendorName("苏宁自营");
				}
				// 提取产品价参数
				if (productInfo != null) {
					product.setProductInfo(productInfo.text().replaceAll("纠错", " "));
				}
				
				
				//提取产品图片链接
				org.jsoup.nodes.Element productImage= doc.getElementById("preView_box");
				for(int i=0;i<productImage.select("img").size();i++){
				imageLink+=productImage.select("img").get(i).attr("src")+"##";
				};
				product.setProductImage(imageLink);
				System.out.println("productName：" + product.getProductName());
				System.out.println("productCatagory:"+product.getProductCatagory());
				System.out.println("productLink:"+"");
				System.out.println("partNumber:"+partNumber);
				System.out.println("vender:" + vendor);
				System.out.println("promotionPrice:" + product.getPromotionPrice());
				System.out.println("netPrice:" + product.getNetPrice());
				System.out.println("vendorName:"+product.getVendorName());
				System.out.println("ProductImage:"+product.getProductImage());
				System.out.println("==================================================");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 
		
	}

}
