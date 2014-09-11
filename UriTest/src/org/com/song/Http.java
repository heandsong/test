package org.com.song;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

public class Http {
	private static int i=0;
	public static String sendGet(String param){
		i++;
		String result="";
		BufferedReader in=null;
		String realUrlName=param;
		
		
		try {
			URL realUri=new URL(realUrlName);
			URLConnection connection=realUri.openConnection();
			connection.setRequestProperty("accept", "*/*");
	        connection.setRequestProperty("connection", "Keep-Alive");
	        connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
	        connection.connect();
           // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//请求未成功再次请求
		if(result.equals("")&&i<3){sendGet(realUrlName);}
		return result;
		
	}

}
