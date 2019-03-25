package com.as.boot.txffc.thread;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import com.alibaba.fastjson.JSONObject;
import com.as.boot.txffc.controller.ExampleControll;
import com.as.boot.txffc.frame.HotClFrame;
import com.as.boot.utils.HttpFuncUtil;
import com.as.boot.utils.ModHttpUtil;
import com.as.boot.utils.ZLinkStringUtils;
/**
 * 用于更新开奖的线程
 * <p>Title:KjThread</p>
 * @author Ason
 * @version 1.0
 */
public class KjThread implements Runnable{
	
	private String[] urlArr = {"http://gf1.pkvip08.com:92/Shared/GetNewPeriod?gameid=81","http://gf2.pkvip08.com:92/Shared/GetNewPeriod?gameid=81","http://gf3.pkvip08.com:92/Shared/GetNewPeriod?gameid=81","http://gf4.pkvip08.com:92/Shared/GetNewPeriod?gameid=81","http://gf5.pkvip08.com:92/Shared/GetNewPeriod?gameid=81"};
	
	public static DecimalFormat ddf = new DecimalFormat("#");
	private Integer urlIndex = 0; 
	@Override
	public void run() {
		while (true) {
			String result = null;
			try {
				//获取最近几期开奖结果
				//result = HttpFuncUtil.getString(urlArr[urlIndex]);
				result = HttpFuncUtil.getBySession(ModHttpUtil.urlSessionId, ModHttpUtil.mdKjUrl);
				if(ZLinkStringUtils.isNotEmpty(result)){
					JSONObject resultObj = JSONObject.parseObject(result);
					//modGame
					JSONObject kjJson = resultObj.getJSONObject("result").getJSONArray("issue").getJSONObject(0);
					String resultRound = kjJson.getString("issueNo").replace("-", "");
					String resultKj = kjJson.getString("code");
					
					String nextRound = null;
					if(resultRound.endsWith("1440")){
						//获取明日日期，当前时间+2小时
						Long nextDate = System.currentTimeMillis() + 60*60*1000;
						nextRound = new SimpleDateFormat("yyyyMMdd").format(nextDate) + "0001";
					}else{
						//获取下一期
						Double num = Double.parseDouble(resultRound) + 1;
						nextRound = ddf.format(num);
					}
				
					/*String resultRound = resultObj.getString("fpreviousperiod");
					String resultKj = resultObj.getString("fpreviousresult");
					String nextRound = resultObj.getString("fnumberofperiod");
					*/
					if(ExampleControll.FFCRound == null || !ExampleControll.FFCRound.equals(resultRound)){
						ExampleControll.FFCRound = resultRound;
						ExampleControll.FFCResult = resultKj;
						ExampleControll.nextFFCRound = nextRound;
						Thread.sleep(30000);//更新到数据后睡眠30s
					}else if(ExampleControll.FFCRound.equals(resultRound)){
						Thread.sleep(3000);//未更新到数据睡眠3s
					}
				}else{
					urlIndex++;
					if(urlIndex>2)urlIndex=0;
					HotClFrame.logTableDefaultmodel.insertRow(0, new String[]{"！！！！！！！！！！"+ExampleControll.nextFFCRound+"期，获取开奖为null，尝试重新登录！！！"});
					if(ModHttpUtil.logind(ExampleControll.getIniItem("account").getValue(), ExampleControll.getIniItem("password").getValue()))
						HotClFrame.logTableDefaultmodel.insertRow(0, new String[]{"重新登录成功！"});
					Thread.sleep(2000);//未更新到数据睡眠2s
				}
			} catch (Exception e) {
				e.printStackTrace();
				HotClFrame.logTableDefaultmodel.insertRow(0, new String[]{"！！！！！！！！！！"+ExampleControll.nextFFCRound+"期，获取开奖失败！！！"});
				HotClFrame.logTableDefaultmodel.insertRow(0, new String[]{"错误result:"+result});
				urlIndex++;
				if(urlIndex>2)urlIndex=0;
			}
		}
	}

}
