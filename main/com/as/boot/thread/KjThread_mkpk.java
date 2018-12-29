package com.as.boot.thread;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

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
public class KjThread_mkpk implements Runnable{
	
	
	public static DecimalFormat ddf = new DecimalFormat("#");
	private Integer urlIndex = 0; 
	@Override
	public void run() {
		while (true) {
			String result = null;
			try {
				//获取最近几期开奖结果
				//result = HttpFuncUtil.getString(urlArr[urlIndex]);
				result = HttpFuncUtil.getBySession(ModHttpUtil.urlSessionId, ModHttpUtil.mdKjUrl_mkpk);
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
						resultKj = resultKj.replace(",0", ",");
						resultKj = resultKj.startsWith("0")?resultKj.substring(1,resultKj.length()):resultKj;
						resultKj = resultKj.replace("10", "0");
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
