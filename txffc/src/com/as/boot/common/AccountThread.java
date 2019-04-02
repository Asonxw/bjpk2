package com.as.boot.common;

import java.awt.Frame;

import javax.swing.JFrame;

import com.alibaba.fastjson.JSONObject;
import com.as.boot.txffc.frame.HotClFrame;
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
						if(resultObj.getInteger("code")!=null&&resultObj.getInteger("code").equals(0)){
							JSONObject resultJson = resultObj.getJSONObject("result");
							accountName = resultJson.getString("name");
							accountAmount = resultJson.getJSONObject("userMoney").getString("avail");
							HotClFrame.accountAmountLabel.setText(accountAmount);
							HotClFrame.accountNameLabel.setText(accountName);
						}else
							HotClFrame.logTableDefaultmodel.insertRow(0, new String[]{"获取账户信息失败，信息为："+result});
					}else{
						HotClFrame.logTableDefaultmodel.insertRow(0, new String[]{"获取账户信息失败，登录已失效，尝试重新登录..."});
						//重新登录
						if(ModHttpUtil.logind(accountName, accountPass))
							HotClFrame.logTableDefaultmodel.insertRow(0, new String[]{"登录成功！"});
					}
				}else{
					HotClFrame.logTableDefaultmodel.insertRow(0, new String[]{"获取账户信息失败，登录已失效，尝试重新登录..."});
					//重新登录
					if(ModHttpUtil.logind(accountName, accountPass))
						HotClFrame.logTableDefaultmodel.insertRow(0, new String[]{"登录成功！"});
				}
				Thread.sleep(5000);
			} catch (Exception e) {
				HotClFrame.logTableDefaultmodel.insertRow(0, new String[]{"获取账户信息异常："+e.getMessage()});
				e.printStackTrace();
				urlIndex++;
				if(urlIndex>2)urlIndex=0;
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

}
