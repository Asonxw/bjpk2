package com.as.boot.thread;

import com.alibaba.fastjson.JSONObject;
import com.as.boot.controller.ExampleControll;
import com.as.boot.frame.AnyThreeFrame;
import com.as.boot.frame.AnyThreeFrame5;
import com.as.boot.utils.HttpFuncUtil;
import com.as.boot.utils.ModHttpUtil;
import com.as.boot.utils.ZLinkStringUtils;
/**
 * 用于更新账户信息的线程
 * <p>AccountThread</p>
 * @author Ason
 * @version 1.0
 */
public class AccountThread implements Runnable{
	
	private Integer urlIndex = 0; 
	public static String accountAmount = null;
	public static String accountName = null;
	public static String accountPass = null;
	@Override
	public void run() {
		while (true) {
			try {
				if(ZLinkStringUtils.isNotEmpty(ModHttpUtil.urlSessionId)){
					//获取账户余额
					String result = HttpFuncUtil.getBySession(ModHttpUtil.urlSessionId, "https://www.modgame.vip/sso/u/getUserBalance?appId=5");
					if(ZLinkStringUtils.isNotEmpty(result)){
						JSONObject resultObj = JSONObject.parseObject(result);
						if(resultObj.getInteger("code").equals(0)){
							JSONObject resultJson = resultObj.getJSONObject("result");
							accountName = resultJson.getString("name");
							accountAmount = resultJson.getJSONObject("userMoney").getString("avail");
							AnyThreeFrame.accountAmountLabel.setText(accountAmount);
							AnyThreeFrame.accountNameLabel.setText(accountName);
						}
						
					}
				}else{
					AnyThreeFrame.logTableDefaultmodel.insertRow(0, new String[]{"登录已失效，尝试重新登录..."});
					//重新登录
					ModHttpUtil.logind(accountName, accountPass);
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
