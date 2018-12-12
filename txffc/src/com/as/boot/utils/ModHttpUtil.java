package com.as.boot.utils;

import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.as.boot.ModOrder;
import com.as.boot.frame.AnyThreeFrame;
import com.as.boot.frame.AnyThreeFrame5;
import com.as.boot.frame.LoginFrame;
import com.as.boot.thread.AccountThread;

public class ModHttpUtil {

	public static String addOrderUrl = "https://www.modgame.vip/lottery/api/u/v1/lottery/add_order";
	
	public static String urlSessionId = null;//"SIG=OoaPUBd/ll692/ZWTNx/Hurqi3BcXDpKqbFo+RhqIf9mNwtp3nmOumC7hFHwoSq9";
	
	public static String mdKjUrl = "https://www.modgame.vip/lottery/api/anon/v1/lottery/simpleLast?size=1&lottery=TXFFC&method=qsm_zx_fs&_=1542874378712";
	
	public static DecimalFormat df = new DecimalFormat("#.000");
	
	public static String mdLoginUrl = "https://www.modgame.vip/sso/login?callback=jsonp1&way=pwd&from=portal&cn=";
	
	/**
	 * @Title: addOrder  
	 * @Description:摩登下注 
	 * @author: Ason
	 * @param lottery 类型：TXFFC
	 * @param issue 投注期号
	 * @param order 订单信息
	 * [{"method":"rx3_zx_ds","code":"1,1,1","nums":"1","piece":"1","price":"0.002","odds":"1950","point":"0","amount":"0.002","position":"1,3,5"}]
	 * @param betType 1
	 * @param sourceType 0
	 * @return      
	 * @return: Boolean      
	 * @throws
	 */
	public static Boolean addTXFFCOrder_RX3(String issue, List<HashMap<String, String>> clList, List<Integer> btNumList, Integer[] btArr, Double price){
		String lottery = "TXFFC";
		
		Integer sourceType = 0;
		List<ModOrder> orderList = new ArrayList<>();
		ModOrder order = null;
		Integer clCount = 0;
		HashMap<String, String> clItem = null;
		for (int i = 0; i < clList.size(); i++) {
			//倍数不为0的进行投注
			if(!btArr[btNumList.get(i)].equals(0)){
				clItem = clList.get(i);
				if(!clItem.get("position").equals("0")){
					Integer nums = clItem.get("cl").split(",").length;
					order = new ModOrder("rx3_zx_ds", clItem.get("cl"), btArr[btNumList.get(i)].toString(), df.format(price), "1950", "0", df.format(btArr[btNumList.get(i)]*price*nums), clItem.get("position"));
					orderList.add(order);
					clCount++;
				}
			}
		}
		Integer betType = clCount>1?2:1;
		if(orderList.size()>0){
			//String params = "lottery="+lottery+"&issue="+issue+"&order="+URLEncoder.encode(ZLinkStringUtils.parseJsonToString(orderList))+"&betType="+betType+"&sourceType="+sourceType;
			String params = "lottery="+lottery+"&issue="+issue+"&order="+ZLinkStringUtils.parseJsonToString(orderList)+"&betType="+betType+"&sourceType="+sourceType;
			System.out.println(params);
			//发送post请求
			String result = HttpFuncUtil.postBySession(urlSessionId, addOrderUrl, params);
			System.out.println(result);
			if(ZLinkStringUtils.isNotEmpty(result)){
				JSONObject resultJson = JSONObject.parseObject(result);
				Integer resultCode = resultJson.getInteger("code");
				String resultmsg = resultJson.getString("msg");
				if(resultCode.equals(1)||resultmsg.equals("ok")){
					AnyThreeFrame.logTableDefaultmodel.insertRow(0, new String[]{issue+"期投注成功！"});
					return true;
				}else if(resultmsg.contains("奖期错误")){
					//奖期错误，已错过投注时间
					AnyThreeFrame.logTableDefaultmodel.insertRow(0, new String[]{"！！！！！！！！！！！！！！"+issue+"期投注失败：改期投注时间已过！"});
				}else
					AnyThreeFrame.logTableDefaultmodel.insertRow(0, new String[]{"！！！！！！！！！！！！！！"+issue+"期投注失败："+resultmsg});
			}
		}else return true;
		return false;
	}
	
	public static Boolean logind(String accountName, String password){
		HashMap<String, String> resultMap = HttpFuncUtil.getUrlConnection(ModHttpUtil.mdLoginUrl+accountName+"&appId=5&password="+password);
		if(resultMap!=null&&ZLinkStringUtils.isNotEmpty(resultMap.get("result"))){
			JSONObject obj = JSONObject.parseObject(resultMap.get("result").replace("jsonp1(", "").replace(")", ""));
			if(obj.getInteger("code")!=null&&obj.getInteger("code").equals(0)&&obj.getString("msg").equals("登录成功")){
				ModHttpUtil.urlSessionId = resultMap.get("sessionId");
				AnyThreeFrame.logTableDefaultmodel.insertRow(0, new String[]{"登录成功！"});
				return true;
			}
		}
		return false;
	}
}
