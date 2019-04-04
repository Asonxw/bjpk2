package com.as.boot.common;

import java.awt.Frame;
import java.util.Date;

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
	public static Integer failTime = 0;
	public static Integer timeReset = 5;
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
							failTime = 0;
						}else{
							failTime++;
							HotClFrame.logTableDefaultmodel.insertRow(0, new String[]{(new Date())+"获取账户信息失败，信息为："+result+"   已失败次数："+failTime});
						}
					}else{
						failTime++;
						HotClFrame.logTableDefaultmodel.insertRow(0, new String[]{(new Date())+"获取账户信息失败，已失败次数："+failTime});
					}
				}
				Thread.sleep(8000);
			} catch (Exception e) {
				failTime++;
				HotClFrame.logTableDefaultmodel.insertRow(0, new String[]{(new Date())+"获取账户信息异常："+e.getMessage()});
				e.printStackTrace();
				try {
					Thread.sleep(8000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
			//重新登录
			if(failTime!=0&&failTime%timeReset==0){
				HotClFrame.logTableDefaultmodel.insertRow(0, new String[]{(new Date())+"失败次数达限，尝试重新登录"});
				if(ModHttpUtil.logind(accountName, accountPass)){
					HotClFrame.logTableDefaultmodel.insertRow(0, new String[]{(new Date())+"重新登录成功！"});
					failTime = 0;
				}
			}
		}
	}

}
