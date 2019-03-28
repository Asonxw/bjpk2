package com.as.boot.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.as.boot.ModOrder;
import com.as.boot.ModOrder_DWD;
import com.as.boot.txffc.frame.AnyThreeFrame;
import com.as.boot.txffc.frame.HotClFrame;

public class ModHttpUtil {

	public static String addOrderUrl = "https://www.modgame.vip/lottery/api/u/v1/lottery/add_order";
	
	public static String urlSessionId = null;//"SIG=OoaPUBd/ll692/ZWTNx/Hurqi3BcXDpKqbFo+RhqIf9mNwtp3nmOumC7hFHwoSq9";
	
	public static String mdKjUrl = "https://www.modgame.vip/lottery/api/anon/v1/lottery/simpleLast?size=1&lottery=TXFFC&method=qsm_zx_fs&_=1542874378712";
	
	public static String mdKjUrl_mkpk = "https://www.modgame.vip/lottery/api/anon/v1/lottery/simpleLast?size=1&lottery=MDPK10&method=qsm_zx_fs";
	
	public static DecimalFormat df = new DecimalFormat("#.000");
	
	public static String mdLoginUrl = "https://www.modgame.vip/sso/login?callback=jsonp1&way=pwd&from=portal&cn=";
	
	public static String modHistoryUrl = "https://www.modgame.vip/lottery/api/anon/v1/lottery/simpleLast?lottery=TXFFC&method=dwd_dwd_dwd"; 
	
	public static String modHistoryUrl_mdpk = "https://www.modgame.vip/lottery/api/anon/v1/lottery/simpleLast?lottery=MDPK10&method=dwd_dwd_dwd";
	
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
		password = ZLinkStringUtils.MD5_32(password, null);
		HashMap<String, String> resultMap = HttpFuncUtil.getUrlConnection(ModHttpUtil.mdLoginUrl+accountName+"&appId=5&password="+password);
		if(resultMap!=null&&ZLinkStringUtils.isNotEmpty(resultMap.get("result"))){
			JSONObject obj = JSONObject.parseObject(resultMap.get("result").replace("jsonp1(", "").replace(")", ""));
			if(obj.getInteger("code")!=null&&obj.getInteger("code").equals(0)&&obj.getString("msg").equals("登录成功")){
				ModHttpUtil.urlSessionId = resultMap.get("sessionId");
				return true;
			}
		}
		return false;
	}

	/**
	 * @Title: addTXFFCOrder_DWD1  
	 * @Description:1码定位胆7注下注 
	 * @author: Ason
	 * @param issue 奖期
	 * @param clMap 策略（包含position和cl）
	 * @param btNum 倍投索引
	 * @param btArr 倍投阶梯
	 * @param baseMoney 基本金额
	 * @return      
	 * @return: Boolean
	 * @throws
	 */
	public static Boolean addTXFFCOrder_DWD1(String issue, HashMap<String, String> clMap, Integer btNum,
			Integer[] btArr, Double price) {
		String lottery = "TXFFC";
		
		Integer sourceType = 0;
		List<ModOrder_DWD> orderList = new ArrayList<>();
		ModOrder_DWD order = null;
		Integer clCount = 1;
		if(clMap!=null){
			Integer nums = clMap.get("cl").split(",").length;
			order = new ModOrder_DWD("dwd_dwd_dwd", serializeCode(clMap), btArr[btNum].toString(), df.format(price), "19.5", "0", df.format(btArr[btNum]*price*nums), "7");
			orderList.add(order);
		}
		Integer betType = clCount>1?2:1;
		if(orderList.size()>0){
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
					HotClFrame.logTableDefaultmodel.insertRow(0, new String[]{issue+"期投注成功！"});
					return true;
				}else if(resultmsg.contains("奖期错误")){
					//奖期错误，已错过投注时间
					HotClFrame.logTableDefaultmodel.insertRow(0, new String[]{"！！！！！！！！！！！！！！"+issue+"期投注失败：改期投注时间已过！"});
				}else
					HotClFrame.logTableDefaultmodel.insertRow(0, new String[]{"！！！！！！！！！！！！！！"+issue+"期投注失败："+resultmsg});
			}
		}else return true;
		return false;
	}
	
	/**
	 * @Title: addTXFFCOrders_DWD1  
	 * @Description:1码定位胆7注下注 批量订单
	 * @author: Ason
	 * @param issue 奖期
	 * @param clMap 策略（包含position和cl）
	 * @param btNum 倍投索引
	 * @param btArr 倍投阶梯
	 * @param baseMoney 基本金额
	 * @return      
	 * @return: Boolean
	 * @throws
	 */
	public static Boolean addTXFFCOrders_DWD1(String issue, List<HashMap<String, String>> clList, List<Integer> btNumList,
			Integer[] btArr, Double price) {
		String lottery = "TXFFC";
		
		Integer sourceType = 0;
		List<ModOrder_DWD> orderList = new ArrayList<>();
		ModOrder_DWD order = null;
		Integer clCount = 0;
		for (int i = 0; i < clList.size(); i++) {
			HashMap<String, String> clMap = clList.get(i);
			if(clMap.get("cl")!=null&&!btArr[btNumList.get(i)].equals(0)){
				Integer nums = clMap.get("cl").split(",").length;
				order = new ModOrder_DWD("dwd_dwd_dwd", serializeCode(clMap), btArr[btNumList.get(i)].toString(), df.format(price), "19.5", "0", df.format(btArr[btNumList.get(i)]*price*nums), "7");
				orderList.add(order);
				clCount++;
			}
		}
		Integer betType = clCount>1?2:1;
		if(orderList.size()>0){
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
					HotClFrame.logTableDefaultmodel.insertRow(0, new String[]{"("+(new Date())+")"+issue+"期投注成功！"});
					return true;
				}else if(resultmsg.contains("奖期错误")){
					//奖期错误，已错过投注时间
					HotClFrame.logTableDefaultmodel.insertRow(0, new String[]{"("+(new Date())+")"+"！！！！！！！！！！！！！！"+issue+"期投注失败：改期投注时间已过！"});
				}else
					HotClFrame.logTableDefaultmodel.insertRow(0, new String[]{"("+(new Date())+")"+"！！！！！！！！！！！！！！"+issue+"期投注失败："+resultmsg});
			}
		}else return true;
		return false;
	}
	
	public static String serializeCode(HashMap<String, String> clMap){
		Integer position_i = Integer.parseInt(clMap.get("position"));
		String code = "";
		for (int i = 0; i < 5; i++) {
			if(position_i.equals(i)){
				code += clMap.get("cl").replace("[", "").replace("]", "").replace(" ", "")+"|";
			}else
				code += "|";
		}
		return ZLinkStringUtils.removeLastStr(code);
	}
	
	public static List<String> getHistoryIssue(Integer num, String url){
		String purl = url + "&size="+num;
		String resultStr = HttpFuncUtil.getBySession(urlSessionId, purl);
		JSONObject resultO = JSONObject.parseObject(resultStr);
		if(resultO.getString("code").equals("1")){
			List<String> preHistory = new ArrayList<>();
			JSONArray array = resultO.getJSONObject("result").getJSONArray("issue");
			for (int i = array.size()-1; i >= 0 ; i--) {
				String resultKj = array.getJSONObject(i).getString("code");
				if(url.contains("PK10")){
					resultKj = resultKj.replace(",0", ",");
					resultKj = resultKj.startsWith("0")?resultKj.substring(1,resultKj.length()):resultKj;
					resultKj = resultKj.replace("10", "0");
				}
				preHistory.add(resultKj.replace(",", ""));
			}
			return preHistory;
		}else
			return getHistoryIssue(num, url);
	}
}
