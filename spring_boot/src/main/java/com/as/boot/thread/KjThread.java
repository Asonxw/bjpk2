package com.as.boot.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.as.boot.controller.ExampleControll;
import com.as.boot.utils.HttpFuncUtil;
/**
 * 用于更新开奖的线程
 * <p>Title:KjThread</p>
 * @author Ason
 * @version 1.0
 */
public class KjThread implements Runnable{
	
	@Override
	public void run() {
		while (true) {
			try {
				//获取最近几期开奖结果
				String result = HttpFuncUtil.getString("http://www.pkcp20.com/Shared/GetNewPeriod?gameid=81");
				
				JSONObject resultObj = JSONObject.parseObject(result);
				
				if(resultObj != null){
					String resultRound = resultObj.getString("fpreviousperiod");
					String resultKj = resultObj.getString("fpreviousresult");
					if(ExampleControll.FFCRound == null || !ExampleControll.FFCRound.equals(resultRound)){
						ExampleControll.FFCRound = resultRound;
						ExampleControll.FFCResult = resultKj;
						Thread.sleep(30000);//更新到数据后睡眠30s
					}else if(ExampleControll.FFCRound.equals(resultRound)){
						Thread.sleep(3000);//未更新到数据睡眠3s
					}
				}else
					Thread.sleep(3000);//未更新到数据睡眠3s
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
