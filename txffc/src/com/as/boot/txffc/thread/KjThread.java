package com.as.boot.txffc.thread;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
	
	public static DecimalFormat ddf = new DecimalFormat("#");
	public static Integer failTime = 0;
	public static Integer timeReset = 3;
	@Override
	public void run() {
		while (true) {
			String result = null;
			try {
				//获取最近几期开奖结果
				result = HttpFuncUtil.getBySession(ModHttpUtil.urlSessionId, ModHttpUtil.mdKjUrl);
				if(ZLinkStringUtils.isNotEmpty(result)){
					JSONObject resultObj = JSONObject.parseObject(result);
					if(resultObj.getJSONObject("result")!=null){
						//modGame
						JSONObject kjJson = resultObj.getJSONObject("result").getJSONArray("issue").getJSONObject(0);
						String resultRound = kjJson.getString("issueNo").replace("-", "");
						String resultKj = kjJson.getString("code");
						
						String nextRound = null;
						if(resultRound.endsWith("1440")){
							//获取明日日期，当前时间+1小时
							Long nextDate = System.currentTimeMillis() + 60*60*1000;
							nextRound = new SimpleDateFormat("yyyyMMdd").format(nextDate) + "0001";
						}else{
							//获取下一期
							Double num = Double.parseDouble(resultRound) + 1;
							nextRound = ddf.format(num);
						}
						
						failTime = 0;
						if(ExampleControll.FFCRound == null || !ExampleControll.FFCRound.equals(resultRound)){
							ExampleControll.FFCRound = resultRound;
							ExampleControll.FFCResult = resultKj;
							ExampleControll.nextFFCRound = nextRound;
							Thread.sleep(30000);//更新到数据后睡眠30s
						}else if(ExampleControll.FFCRound.equals(resultRound))
							Thread.sleep(4000);//未更新到数据睡眠4s
					}else{
						failTime++;
						HotClFrame.logTableDefaultmodel.insertRow(0, new String[]{(new Date())+ExampleControll.nextFFCRound+"期，获取开奖失败，信息为："+result+"，已失败次数："+failTime});
						Thread.sleep(4000);//未更新到数据睡眠4s
					}
				}else{
					failTime++;
					HotClFrame.logTableDefaultmodel.insertRow(0, new String[]{(new Date())+ExampleControll.nextFFCRound+"期，获取开奖为null，已失败次数："+failTime});
					
					Thread.sleep(3000);//未更新到数据睡眠3s
				}
			} catch (Exception e) {
				HotClFrame.logTableDefaultmodel.insertRow(0, new String[]{(new Date())+ExampleControll.nextFFCRound+"期，获取开奖失败！！！"});
				HotClFrame.logTableDefaultmodel.insertRow(0, new String[]{(new Date())+"错误result:"+result});
				if(ZLinkStringUtils.isNotEmpty(result)){
					JSONObject resultObj = JSONObject.parseObject(result);
					if(resultObj.getString("time")!=null&&resultObj.getString("server").equals("maintenance")){
						String tTime = resultObj.getString("time");
						Date now = new Date();
						//计算休眠时间
						long systime = now.getTime();
						SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd");
						SimpleDateFormat f2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String dateStr = f1.format(now) +" "+tTime+":00";
						try {
							long time = f2.parse(dateStr).getTime() - systime;
							HotClFrame.logTableDefaultmodel.insertRow(0, new String[]{(new Date())+"系统维护-"+tTime+"开启，系统睡眠 "+time+"ms"});
							Thread.sleep(time);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
				e.printStackTrace();
			}
			//重新登录
			if(failTime!=0&&failTime%timeReset==0){
				HotClFrame.logTableDefaultmodel.insertRow(0, new String[]{(new Date())+"获取奖期失败次数达限，尝试重新登录"});
				if(ModHttpUtil.logind(ExampleControll.getIniItem("account").getValue(), ExampleControll.getIniItem("password").getValue()))
					HotClFrame.logTableDefaultmodel.insertRow(0, new String[]{(new Date())+"重新登录成功！"});
			}
		}
	}

}
