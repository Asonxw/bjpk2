package com.as.boot.utils;

import java.io.BufferedReader;
<<<<<<< HEAD
import java.io.DataOutputStream;
=======
>>>>>>> 90ade01be4fb5b89b4832ef9eca08eefbfc4a44c
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
<<<<<<< HEAD
import java.io.PrintWriter;
=======
>>>>>>> 90ade01be4fb5b89b4832ef9eca08eefbfc4a44c
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.as.boot.controller.ExampleControll;
import com.as.boot.frame.AnyThreeFrame;

public class HttpFuncUtil {

	public static String getString(String request) {
		String respond = "";
		// String uniID = CommonUtils.getUniqString();
		RestTemplate restTemplate = null;
		try {
			restTemplate = new RestTemplate();
			// httpClient连接配置，底层是配置RequestConfig
			// HttpComponentsClientHttpRequestFactory clientHttpRequestFactory =
			// new HttpComponentsClientHttpRequestFactory();
			SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
<<<<<<< HEAD
			requestFactory.setReadTimeout(3000);
			requestFactory.setConnectTimeout(3000);// 3秒钟访问限制，否则定为超时
=======
			requestFactory.setReadTimeout(30000);
			requestFactory.setConnectTimeout(30000);// 30秒钟访问限制，否则定为超时
>>>>>>> 90ade01be4fb5b89b4832ef9eca08eefbfc4a44c
			
			restTemplate.setRequestFactory(requestFactory);
			respond = restTemplate.getForObject(request, String.class);
		} catch (Exception e) {
<<<<<<< HEAD
			AnyThreeFrame.logTableDefaultmodel.insertRow(0, new String[]{"！！！！！！！！！！"+ExampleControll.nextFFCRound+"期，获取开奖失败！！！"});
=======
>>>>>>> 90ade01be4fb5b89b4832ef9eca08eefbfc4a44c
			e.printStackTrace();
		}
		return respond;
	}

	/**
	 * 访问链接并返回sessionid
	 * @author Ason
	 * @Title: getUrlConnection 
	 * @param urlStr
	 * @return String
	 * @throws
	 */
	public static HashMap<String, String> getUrlConnection(String urlStr) {
		HashMap<String, String> params = null;
<<<<<<< HEAD
=======
		OutputStream out = null;
		InputStream in = null;
>>>>>>> 90ade01be4fb5b89b4832ef9eca08eefbfc4a44c
		InputStreamReader reader = null;
		try {
			URL url = new URL(urlStr);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");

			String cookieValue = con.getHeaderField("Set-Cookie");
			String sessionId = cookieValue.substring(0, cookieValue.indexOf(";"));
			params = new HashMap<>();
			params.put("sessionId", sessionId);
			//请求结果
			if(con.getResponseCode() == 200){
	            // 读取响应内容
            	reader = new InputStreamReader(con.getInputStream(),"utf-8"); 
            	BufferedReader reader2 = new BufferedReader(reader);
            	
                params.put("result", reader2.readLine());
	        }
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
<<<<<<< HEAD
				if(reader!=null)
					reader.close();
=======
				if (in != null)
					in.close();
				if (out != null)
					out.close();
>>>>>>> 90ade01be4fb5b89b4832ef9eca08eefbfc4a44c
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return params;
	}
	
	/**
	 * 根据sessionId访问链接
	 * @author Ason
	 * @Title: connectionBySession 
	 * @param sessionId
	 * @param url void
	 * @throws
	 */
<<<<<<< HEAD
	public static String postBySession(String sessionId, String url, String params){
        InputStreamReader reader = null;
        BufferedReader breader = null;
        DataOutputStream out = null;
        String result = null;
=======
	public static void postBySession(String sessionId, String url){
        InputStream in=null;
>>>>>>> 90ade01be4fb5b89b4832ef9eca08eefbfc4a44c
        try{
            URL u=new URL(url);
            HttpURLConnection con=(HttpURLConnection)u.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Cookie", sessionId);
<<<<<<< HEAD
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(false);
            con.setInstanceFollowRedirects(true);
            con.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
            con.connect();
            out = new DataOutputStream(con.getOutputStream());
            // 发送请求参数
            out.writeBytes(params);
            out.flush();
            out.close();
            //请求结果
			if(con.getResponseCode() == 200){
	            // 读取响应内容
            	reader = new InputStreamReader(con.getInputStream(),"utf-8"); 
            	breader = new BufferedReader(reader);
            	result = breader.readLine();
	        }
=======
            in=con.getInputStream();
>>>>>>> 90ade01be4fb5b89b4832ef9eca08eefbfc4a44c
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try {
<<<<<<< HEAD
            	if(reader!=null)
					reader.close();
            	if(breader!=null)
            		breader.close();
=======
                if(in!=null)
                    in.close();
>>>>>>> 90ade01be4fb5b89b4832ef9eca08eefbfc4a44c
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
<<<<<<< HEAD
        return result;
=======
>>>>>>> 90ade01be4fb5b89b4832ef9eca08eefbfc4a44c
    }
	
	/**
	 * 根据sessionId访问链接
	 * @author Ason
	 * @Title: connectionBySession 
	 * @param sessionId
	 * @param url void
	 * @throws
	 */
	public static String getBySession(String sessionId, String url){
		InputStreamReader reader = null;
<<<<<<< HEAD
        BufferedReader breader = null;
=======
>>>>>>> 90ade01be4fb5b89b4832ef9eca08eefbfc4a44c
        try{
            URL u=new URL(url);
            HttpURLConnection con=(HttpURLConnection)u.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Cookie", sessionId);
            con.connect();
            if(con.getResponseCode() == 200){
                // 读取响应内容
            	reader = new InputStreamReader(con.getInputStream(),"utf-8"); 
<<<<<<< HEAD
            	breader= new BufferedReader(reader);
            	
                return breader.readLine();
=======
            	BufferedReader reader2 = new BufferedReader(reader);
            	
                return reader2.readLine();
>>>>>>> 90ade01be4fb5b89b4832ef9eca08eefbfc4a44c
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
<<<<<<< HEAD
        	 try {
             	if(reader!=null)
 					reader.close();
             	if(breader!=null)
             		breader.close();
             } catch (IOException e) {
                 e.printStackTrace();
             }
=======
			if(reader !=null){
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
>>>>>>> 90ade01be4fb5b89b4832ef9eca08eefbfc4a44c
		}
        return null;
    }
}
