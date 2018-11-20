package com.as.boot.thread;

import com.alibaba.fastjson.JSONObject;
import com.as.boot.controller.ExampleControll;
import com.as.boot.frame.AnyThreeFrame5;
import com.as.boot.utils.HttpFuncUtil;
import com.as.boot.utils.ZLinkStringUtils;
/**
 * 用于更新开奖的线程
 * <p>Title:KjThread</p>
 * @author Ason
 * @version 1.0
 */
public class AccountThread implements Runnable{
	
	private Integer urlIndex = 0; 
	public static String urlSessionId = null;
	public static String accountAmount = null;
	public static String accountName = null;
	@Override
	public void run() {
		while (true) {
			try {
				if(ZLinkStringUtils.isNotEmpty(urlSessionId)){
					//获取账户余额
					String result = HttpFuncUtil.getBySession(urlSessionId, "https://www.modgame.vip/sso/u/getUserBalance?appId=5");
					if(ZLinkStringUtils.isNotEmpty(result)){
						JSONObject resultObj = JSONObject.parseObject(result);
						if(resultObj.getInteger("code").equals(0)){
							JSONObject resultJson = resultObj.getJSONObject("result");
							accountName = resultJson.getString("name");
							accountAmount = resultJson.getJSONObject("userMoney").getString("avail");
							AnyThreeFrame5.accountAmountLabel.setText(accountAmount);
							AnyThreeFrame5.accountNameLabel.setText(accountName);
						}
						
					}
				}
				Thread.sleep(4000);
			} catch (Exception e) {
				e.printStackTrace();
				urlIndex++;
				if(urlIndex>2)urlIndex=0;
			}
		}
	}

}
