package com.as.boot.utils;

import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class HttpFuncUtil {

	public static String getString(String request){
		String respond="";
		//String uniID = CommonUtils.getUniqString();
		RestTemplate restTemplate = null;
		try{
			restTemplate = new RestTemplate();
			// httpClient连接配置，底层是配置RequestConfig
	        //HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
	        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
	        requestFactory.setReadTimeout(30000);
	        requestFactory.setConnectTimeout(30000);//30秒钟访问限制，否则定为超时
	        
	        restTemplate.setRequestFactory(requestFactory);
			respond = restTemplate.getForObject(request, String.class);
		}catch(Exception e){
			e.printStackTrace();
        }
		return respond;
	}
}
