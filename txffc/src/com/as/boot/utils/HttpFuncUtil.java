package com.as.boot.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class HttpFuncUtil {

	public static String getString(String request) {
		String respond = "";
		try {
			// String uniID = CommonUtils.getUniqString();
			RestTemplate restTemplate = null;
			restTemplate = new RestTemplate();
			// httpClient连接配置，底层是配置RequestConfig
			// HttpComponentsClientHttpRequestFactory clientHttpRequestFactory =
			// new HttpComponentsClientHttpRequestFactory();
			SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
			requestFactory.setReadTimeout(3000);
			requestFactory.setConnectTimeout(3000);// 3秒钟访问限制，否则定为超时
			
			restTemplate.setRequestFactory(requestFactory);
			respond = restTemplate.getForObject(request, String.class);
		} catch (Exception e) {
			ZLinkStringUtils.addLog(HttpFuncUtil.class, "getString", "["+request+"]"+e.getMessage()+" 【异常】");
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
		InputStreamReader reader = null;
		try {
			URL url = new URL(urlStr);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");

			String cookieValue = con.getHeaderField("Set-Cookie");
			String sessionId = ZLinkStringUtils.isNotEmpty(cookieValue)?cookieValue.substring(0, cookieValue.indexOf(";")):null;
			params = new HashMap<>();
			params.put("sessionId", sessionId);
			//请求结果
			if(con.getResponseCode() == 200){
	            // 读取响应内容
            	reader = new InputStreamReader(con.getInputStream(),"utf-8"); 
            	BufferedReader reader2 = new BufferedReader(reader);
            	
                params.put("result", reader2.readLine());
	        }else
            	ZLinkStringUtils.addLog(HttpFuncUtil.class, "getUrlConnection", "["+urlStr+"]get结果码"+con.getResponseCode());
		} catch (Exception e) {
			ZLinkStringUtils.addLog(HttpFuncUtil.class, "getUrlConnection", "["+urlStr+"]"+e.getMessage()+" 【异常】");
			e.printStackTrace();
			return null;
		} finally {
			try {
				if(reader!=null)
					reader.close();
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
	public static String postBySession(String sessionId, String url, String params){
        InputStreamReader reader = null;
        BufferedReader breader = null;
        DataOutputStream out = null;
        String result = null;
        try{
            URL u=new URL(url);
            HttpURLConnection con=(HttpURLConnection)u.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Cookie", sessionId);
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
	        }else
            	ZLinkStringUtils.addLog(HttpFuncUtil.class, "postBySession", "["+url+"]post结果码"+con.getResponseCode());
        }catch(Exception e){
        	ZLinkStringUtils.addLog(HttpFuncUtil.class, "postBySession", "["+url+"]"+e.getMessage()+" 【异常】");
            e.printStackTrace();
        }finally{
            try {
            	if(reader!=null)
					reader.close();
            	if(breader!=null)
            		breader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
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
        BufferedReader breader = null;
        try{
            URL u=new URL(url);
            HttpURLConnection con=(HttpURLConnection)u.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Cookie", sessionId);
            con.connect();
            if(con.getResponseCode() == 200){
                // 读取响应内容
            	reader = new InputStreamReader(con.getInputStream(),"utf-8"); 
            	breader= new BufferedReader(reader);
            	
                return breader.readLine();
            }else
            	ZLinkStringUtils.addLog(HttpFuncUtil.class, "getBySession", "["+url+"]get结果码"+con.getResponseCode());
        }catch(Exception e){
        	ZLinkStringUtils.addLog(HttpFuncUtil.class, "getBySession", "["+url+"]"+e.getMessage()+" 【异常】 ");
            e.printStackTrace();
        }finally {
        	 try {
             	if(reader!=null)
 					reader.close();
             	if(breader!=null)
             		breader.close();
             } catch (IOException e) {
                 e.printStackTrace();
             }
		}
        return null;
    }
}
