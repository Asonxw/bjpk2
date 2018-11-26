package com.as.boot.thread;

import com.alibaba.fastjson.JSONObject;
import com.as.boot.controller.ExampleControll;
import com.as.boot.frame.AnyThreeFrame;
import com.as.boot.frame.LoginFrame;
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
	
	private Integer urlIndex = 0; 
	@Override
	public void run() {
		while (true) {
			String result = null;
			try {
				//获取最近几期开奖结果
				result = HttpFuncUtil.getString(urlArr[urlIndex]);
				if(ZLinkStringUtils.isNotEmpty(result)){
					JSONObject resultObj = JSONObject.parseObject(result);
					//modGame
					/*JSONObject kjJson = resultObj.getJSONObject("result").getJSONArray("issue").getJSONObject(0);
					String resultRound = kjJson.getString("issueNo").replace("-", "");
					String resultKj = kjJson.getString("code");*/
					
					
					String resultRound = resultObj.getString("fpreviousperiod");
					String resultKj = resultObj.getString("fpreviousresult");
					String nextRound = resultObj.getString("fnumberofperiod");
					if(ExampleControll.FFCRound == null || !ExampleControll.FFCRound.equals(resultRound)){
						ExampleControll.FFCRound = resultRound;
						ExampleControll.FFCResult = resultKj;
						ExampleControll.nextFFCRound = nextRound;
						Thread.sleep(30000);//更新到数据后睡眠30s
					}else if(ExampleControll.FFCRound.equals(resultRound)){
						Thread.sleep(3000);//未更新到数据睡眠3s
					}
				}else{
					Thread.sleep(3000);//未更新到数据睡眠3s
					urlIndex++;
					if(urlIndex>2)urlIndex=0;
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(result);
				AnyThreeFrame.logTableDefaultmodel.insertRow(0, new String[]{"！！！！！！！！！！"+ExampleControll.nextFFCRound+"期，获取开奖失败！！！"});
				urlIndex++;
				if(urlIndex>2)urlIndex=0;
			}
		}
	}

}
