package com.as.boot.thread;

import com.alibaba.fastjson.JSONObject;
import com.as.boot.controller.ExampleControll;
import com.as.boot.utils.HttpFuncUtil;
import com.as.boot.utils.ZLinkStringUtils;
/**
 * 用于更新开奖的线程
 * <p>Title:KjThread</p>
 * @author Ason
 * @version 1.0
 */
public class KjThread implements Runnable{
	
	private String[] urlArr = {"http://gf3.pkvip08.com:92/Shared/GetNewPeriod?gameid=81","http://gf4.pkvip08.com:92/Shared/GetNewPeriod?gameid=81","http://www.pkcp20.com/Shared/GetNewPeriod?gameid=81"};
	private Integer urlIndex = 0; 
	@Override
	public void run() {
		while (true) {
			try {
				//获取最近几期开奖结果
				String result = HttpFuncUtil.getString(urlArr[urlIndex]);
				if(ZLinkStringUtils.isNotEmpty(result)){
					JSONObject resultObj = JSONObject.parseObject(result);
					String resultRound = resultObj.getString("fpreviousperiod");
					String resultKj = resultObj.getString("fpreviousresult");
					if(ExampleControll.FFCRound == null || !ExampleControll.FFCRound.equals(resultRound)){
						ExampleControll.FFCRound = resultRound;
						ExampleControll.FFCResult = resultKj;
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
				urlIndex++;
				if(urlIndex>2)urlIndex=0;
			}
		}
	}

}
