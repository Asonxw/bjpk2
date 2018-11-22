package com.as.boot.thread;

import com.alibaba.fastjson.JSONObject;
import com.as.boot.controller.ExampleControll;
<<<<<<< HEAD
import com.as.boot.frame.AnyThreeFrame;
import com.as.boot.frame.AnyThreeFrame5;
import com.as.boot.utils.HttpFuncUtil;
import com.as.boot.utils.ModHttpUtil;
import com.as.boot.utils.ZLinkStringUtils;
/**
 * 用于更新账户信息的线程
 * <p>AccountThread</p>
=======
import com.as.boot.frame.AnyThreeFrame5;
import com.as.boot.utils.HttpFuncUtil;
import com.as.boot.utils.ZLinkStringUtils;
/**
 * 用于更新开奖的线程
 * <p>Title:KjThread</p>
>>>>>>> 90ade01be4fb5b89b4832ef9eca08eefbfc4a44c
 * @author Ason
 * @version 1.0
 */
public class AccountThread implements Runnable{
	
	private Integer urlIndex = 0; 
<<<<<<< HEAD
	public static String accountAmount = null;
	public static String accountName = null;
	public static String accountPass = null;
=======
	public static String urlSessionId = null;
	public static String accountAmount = null;
	public static String accountName = null;
>>>>>>> 90ade01be4fb5b89b4832ef9eca08eefbfc4a44c
	@Override
	public void run() {
		while (true) {
			try {
<<<<<<< HEAD
				if(ZLinkStringUtils.isNotEmpty(ModHttpUtil.urlSessionId)){
					//获取账户余额
					String result = HttpFuncUtil.getBySession(ModHttpUtil.urlSessionId, "https://www.modgame.vip/sso/u/getUserBalance?appId=5");
=======
				if(ZLinkStringUtils.isNotEmpty(urlSessionId)){
					//获取账户余额
					String result = HttpFuncUtil.getBySession(urlSessionId, "https://www.modgame.vip/sso/u/getUserBalance?appId=5");
>>>>>>> 90ade01be4fb5b89b4832ef9eca08eefbfc4a44c
					if(ZLinkStringUtils.isNotEmpty(result)){
						JSONObject resultObj = JSONObject.parseObject(result);
						if(resultObj.getInteger("code").equals(0)){
							JSONObject resultJson = resultObj.getJSONObject("result");
							accountName = resultJson.getString("name");
							accountAmount = resultJson.getJSONObject("userMoney").getString("avail");
<<<<<<< HEAD
							AnyThreeFrame.accountAmountLabel.setText(accountAmount);
							AnyThreeFrame.accountNameLabel.setText(accountName);
						}
						
					}
				}else{
					AnyThreeFrame.logTableDefaultmodel.insertRow(0, new String[]{"登录已失效，尝试重新登录..."});
					//重新登录
					ModHttpUtil.logind(accountName, accountPass);
				}
				
=======
							AnyThreeFrame5.accountAmountLabel.setText(accountAmount);
							AnyThreeFrame5.accountNameLabel.setText(accountName);
						}
						
					}
				}
>>>>>>> 90ade01be4fb5b89b4832ef9eca08eefbfc4a44c
				Thread.sleep(4000);
			} catch (Exception e) {
				e.printStackTrace();
				urlIndex++;
				if(urlIndex>2)urlIndex=0;
			}
		}
	}

}
