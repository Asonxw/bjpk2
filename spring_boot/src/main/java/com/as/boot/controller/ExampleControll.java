package com.as.boot.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.as.boot.dao.CommonDao;
import com.as.boot.utils.HttpFuncUtil;


@RestController
@RequestMapping("/test")
public class ExampleControll {
	
	private String round = ""; 
	private String msRound = "";
	private String result = "";
	private String msResult = null;
	private CommonDao cd = new CommonDao();
	private List<Integer> history = new ArrayList<Integer>();
	@RequestMapping("/t")
	public String test(Integer number){
		Integer ballCount = 10;
		Integer useIdCount = 20;
		StringBuffer sb = new StringBuffer();
		String response = "";
		String newResult = "";
		HashMap<String, Object> obj = new HashMap<String, Object>();
		boolean flag = false;
		//先判断是否需要更新
		newResult = HttpFuncUtil.getString("https://www.91jihua.com/api/newest?code=pk10");
		String newTerm = JSONObject.parseObject(newResult).getJSONObject("data").getString("current");
		if(!newTerm.equals(round.toString())){
			for (int i = 1; i <= useIdCount; i++) {
				for (int j = 1; j <= ballCount; j++) {
					response = HttpFuncUtil.getString("https://www.91jihua.com/api/number-plan?code=pk10&ball="+j+"&uid="+i+"&num="+number);
					if(i == 1 && j ==1){
						JSONObject firstListItem = JSONObject.parseObject(response).getJSONArray("list").getJSONObject(0);
						//获取数据最新期号
						String roundTemp = firstListItem.getString("round");
						//获取期数
						Integer k = firstListItem.getInteger("k");
						if(roundTemp.equals(this.round))
							return this.result;
						else{
							this.round = roundTemp;
							//获取最新pk10开奖的结果
							newResult = JSONObject.parseObject(newResult).getJSONObject("data").getJSONObject("newest").getString("code");
						}
						if(k == 20)
							//如果为20期需要将数据存储起来
							flag = true;
					}
					sb.append(response).append("@");
				}
			}
			result = sb.substring(0, sb.length()-1);
			if(flag){
/*				try {
					//存储数据
					cd.saveResult(result);
				} catch (Exception e) {
				}
*/			}
			obj.put("result", result);
			obj.put("newResult", newResult);
			result = JSONObject.toJSONString(obj);
		}
		return result;
	}
	
	@RequestMapping("/getCurrent")
	public String getCurrent(){
		Integer ballCount = 10;
		Integer useIdCount = 20;
		StringBuffer sb = new StringBuffer();
		String response = "";
		String newResult = "";
		HashMap<String, Object> obj = new HashMap<String, Object>();
		boolean flag = false;
		//先判断是否需要更新
		newResult = HttpFuncUtil.getString("https://www-ry111.com/static/data/80CurIssue.json");
		String newTerm = JSONObject.parseObject(newResult).getString("issue");
		if(!newTerm.equals(msRound.toString())){
			String resultArray = JSONObject.parseObject(newResult).getString("nums");
			this.msRound = newTerm;
			this.msResult = resultArray;
			String first = resultArray.substring(0, 2);
			if(first.equals("10"))
				first = "0";
			else first = first.replace("0", "");
			//杀重
			Integer first_n = Integer.parseInt(first);
			//添加历史开奖冠军
			history.add(first_n);
			if(history.size()>20)
				history.remove(0);
		}
		obj.put("result", this.msResult);
		obj.put("current", this.msRound);
		return JSONObject.toJSONString(obj);
	}
	
	@RequestMapping("/nowkjresult")
	public String getCurrent_(){
		Integer ballCount = 10;
		Integer useIdCount = 20;
		StringBuffer sb = new StringBuffer();
		String response = "";
		String newResult = "";
		HashMap<String, Object> obj = new HashMap<String, Object>();
		boolean flag = false;
		//先判断是否需要更新
		newResult = HttpFuncUtil.getString("https://www-ry111.com/static/data/80CurIssue.json");
		String newTerm = JSONObject.parseObject(newResult).getString("issue");
		if(!newTerm.equals(msRound.toString())){
			String resultArray = JSONObject.parseObject(newResult).getString("nums");
			this.msRound = newTerm;
			this.msResult = resultArray;
			String first = resultArray.substring(0, 2);
			if(first.equals("10"))
				first = "0";
			else first = first.replace("0", "");
			//杀重
			Integer first_n = Integer.parseInt(first);
			//添加历史开奖冠军
			history.add(first_n);
			if(history.size()>20)
				history.remove(0);
		}
		return this.msResult+"-"+this.msRound;
	}
	
	
	@RequestMapping("/history_g")
	public void saveHistory(Integer g4,Integer g5,Integer g6,Integer g7,Integer g8){
		try {
			cd.addHistory(g4,g5,g6,g7,g8);
		} catch (Exception e) {
		}
	}
	
	@RequestMapping("/history_g_dj")
	public void saveHistory_g_dj(Integer g1,Integer g2,Integer g3,Integer g4,Integer g5){
		try {
			cd.addHistory_dj(g1,g2,g3,g4,g5);
			
		} catch (Exception e) {
		}
	}
	
	@RequestMapping("/getPage")
	public String getPage(){
		return "get";
	}
	
	@RequestMapping("/random")
	public String getRandomStr(){
		List<Integer> result = new ArrayList<Integer>();
		Integer first_n = null;
		if(msResult!=null){
			//获取上把冠军
			String first = msResult.substring(0,2);
			if(first.equals("10"))
				first = "0";
			else first = first.replace("0", "");
		}
		while(true){
			if(result.size()==7)break;
			int i = createRandom();
			if(!result.contains(i)){
				result.add(i);
			}
		}
		Collections.sort(result);
		return result.toString();
	}
	
	@RequestMapping("/hotNum")
	public String getHotNumStr(Integer first_n){
		history.add(first_n);
		if(history.size()>25)
			history.remove(0);
		List<Integer> result = new ArrayList<Integer>();
		first_n = null;
		if(history.size()==25){
			int[] array = {0,0,0,0,0,0,0,0,0,0};
			int[] array1 = {0,0,0,0,0,0,0,0,0,0};
			for (Integer i : history) {
				array[i] = array[i]+1;
				array1[i] = array1[i]+1;
			}
			Arrays.sort(array);
			for (int i = 9; i >= 0; i--) {
				List<Integer> _t_array = new ArrayList<Integer>();
				//取出目标字数的号码
				for (int j = 0; j < array1.length; j++) {
					//获取次数为_array[i]个的号码
					if(array1[j] == array[i])
						_t_array.add(j);
				}
				//倒叙遍历历史开奖，得出最近出现优先的策略
				for (int j = history.size()-1; j >=0 ; j--) {
					for (int k = 0; k < _t_array.size(); k++) {
						if(history.get(j).equals(_t_array.get(k)))
							if(!result.contains(history.get(j)))result.add(history.get(j));
						if(result.size()==7)break;
					}
					if(result.size()==7)break;
				}
				if(result.size()==7)break;
			}
			if(result.size() == 6){
				//根据顺序只获取到6码，这种情况是20把中只出现了6个号，此时按顺序选择出现次数为0的号
				for (int i = 0; i < array1.length; i++) {
					if(array1[i] == 0)
						if(!result.contains(i))result.add(i);
					if(result.size()==7)break;
				}
			}
			Collections.sort(result);
			return result.toString();
		}else return null;
	}
	
	@RequestMapping("/get15")
	public String get15(){
			List<Integer> result = new ArrayList<Integer>();
			String[] rArray = msResult.split(",");
			for (int i = 0; i < 5; i++) {
				int item = Integer.parseInt(rArray[i]);
				if(item == 10)item = 0;
				result.add(item);
			}
			Collections.sort(result);
			return result.toString();
	}
	
	@RequestMapping("/get26")
	public String get26(){
			List<Integer> result = new ArrayList<Integer>();
			String[] rArray = msResult.split(",");
			for (int i = 1; i < 6; i++) {
				int item = Integer.parseInt(rArray[i]);
				if(item == 10)item = 0;
				result.add(item);
			}
			Collections.sort(result);
			return result.toString();
	}
	
	@RequestMapping("/getCut")
	public String getCut(){
			List<Integer> result = new ArrayList<Integer>();
			String[] rArray = msResult.split(",");
			//获取第一名
			Integer first = Integer.parseInt(rArray[0]);
			first = first==10?0:first;
			//获取当期尾号
			Integer w = Integer.parseInt(msRound.substring(8, msRound.length())) + 1;
			if(w==10)w=0;
			for (int i = 9; i < 10; i--) {
				int item = Integer.parseInt(rArray[i]);
				item = Math.abs(item - w);
				if(item == 10)item = 0;
				if(!result.contains(item)&&first!=item)result.add(item);
				if(result.size() == 5)break;
			}
			Collections.sort(result);
			return result.toString();
	}
	
	@RequestMapping("/getCut2")
	public String getCut2(){
			List<Integer> result = new ArrayList<Integer>();
			String[] rArray = msResult.split(",");
			//获取当期尾号
			Integer w = Integer.parseInt(msRound.substring(8, msRound.length()));
			for (int i = 9; i < 10; i--) {
				int item = Integer.parseInt(rArray[i]);
				item = Math.abs(item - w);
				if(item == 10)item = 0;
				if(!result.contains(item))result.add(item);
				if(result.size() == 5)break;
			}
			Collections.sort(result);
			return result.toString();
	}
	
	@RequestMapping("/getCut3")
	public String getCut3(){
			List<Integer> result = new ArrayList<Integer>();
			String[] rArray = msResult.split(",");
			//获取当期尾号
			Integer w = Integer.parseInt(msRound.substring(8, msRound.length())) + 1;
			for (int i = 9; i < 10; i--) {
				int item = Integer.parseInt(rArray[i]);
				if(item<w)item += 10;
				item =item - w;
				if(item == 10)item = 0;
				if(!result.contains(item))result.add(item);
				if(result.size() == 5)break;
			}
			Collections.sort(result);
			return result.toString();
	}
	
	public static Integer createRandom(){
		return (int)(Math.random()*10);
	}
	
	@RequestMapping("/getFFC")
	public String getTXFFCL(String historyRound, Integer historyNum, String putPosition, String initCl, Integer initClFlag, Integer initClNum, Integer clNum, Integer aimMaxFail, Integer maxRestN){
		//初始化历史开奖
		String[] historyArr = historyRound.trim().split(";");
		if(historyNum != null && historyNum > 0)
			historyArr = Arrays.copyOfRange(historyArr, 0, historyNum);
		List<String> clList = new ArrayList<String>();
		//策略组数如果没有设定则默认为50组
		clNum = clNum==null||clNum==0?50:clNum;
		Integer failCount = 0;
		Integer maxFailCount = 0;
		List<String> maxFailResult = null;
		//记录最佳策略
		Integer bestMaxFail = 0;
		List<String> bestClList = null;
		//解析需要投注的位置
		String[] positionArr = putPosition.split(",");
		Integer[] positionArr_i = new Integer[positionArr.length];
		for (int i = 0; i < positionArr.length; i++)
			positionArr_i[i] = Integer.parseInt(positionArr[i]);
		aimMaxFail = aimMaxFail==null?7:aimMaxFail;
		//最多重置策略次数
		maxRestN = maxRestN==null?200:maxRestN;
		//获取初始策略
		if(initClFlag == 0 && initCl!=null){
			String[] initClArr = initCl.split(",");
			for (String string : initClArr) 
				clList.add(string);
		}else{
			//若无初始策略则用随机数生成固定数量的初始策略
			initClNum = initClNum == null||initClNum == 0?40:initClNum;
			clList = createInitFFCCL(initClNum);
		}
		//遍历历史开奖
		while (maxRestN != 0) {
			//反向循环
			for (int i = historyArr.length-1; i >= 0; i--) {
				//获取开奖
				String item_result = historyArr[i].trim().split(",")[1].trim();
				String result = "";
				for (int j = 0, jl = positionArr_i.length; j < jl; j++) 
					result += item_result.charAt(positionArr_i[j]);
				if(clList.contains(result)){
					//中
					failCount = 0;
				}else{
					failCount++;
					if(failCount > maxFailCount){
						//更新最大连挂及重置连挂出奖记录
						maxFailResult = new ArrayList<String>();
						maxFailResult.add(result);
						maxFailCount = failCount;
					}else if(failCount==maxFailCount&&failCount!=0){
						//添加同级连挂的开奖结果
						maxFailResult.add(result);
					}
				}
			}
			
			//奖最大连挂的开奖结果加入策略
			if(clList.size()<clNum){
				for (int i = 0; i < maxFailResult.size(); i++) {
					if(!clList.contains(maxFailResult.get(i))){
						clList.add(maxFailResult.get(i));
						if(clList.size()==clNum)
							break;
					}
				}
			}
			//已经没有连挂则退出循环
			if(maxFailCount == 0)break;
			if(clList.size()==clNum){
				
				if(bestMaxFail==0||bestMaxFail>maxFailCount){
					bestMaxFail = maxFailCount;
					bestClList = clList;
				}
				//如果连挂小于等于预期，则退出循环返回结果
				if(maxFailCount <= aimMaxFail){
					break;
				}else{
					//如果不符合预期则重新执行(系统生成随机数的才能重新执行)
					if(initClFlag == 1){
						//重置系统随机初始策略
						clList = createInitFFCCL(initClNum);
						failCount = 0;
						maxFailResult = null;
						//控制循环次数为maxRestN次，防止无限循环
						maxRestN --;
					}else break;
				}
			}
			//循环完一期后将最大重新计算
			if(maxRestN!=0)maxFailCount = 0;
		}
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("maxFailCount", bestMaxFail);
		params.put("cl", bestClList.toString());
		return JSONObject.toJSONString(params);
	}
	
	public static List<String> createInitFFCCL(Integer initClNum){
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < initClNum; i++) {
			String item = createRandom()+""+createRandom();
			while (true) {
				if(list.contains(item))
					item = createRandom()+""+createRandom();
				else{
					list.add(item);
					break;
				}
			}
		}
		return list;
	}
	
	/*public static List<String> initCl(Integer initClFlag, Integer , String initCl){
		List<String> clList = new ArrayList<String>();
		//获取初始策略
		if(initClFlag == 0 && initCl!=null){
			String[] initClArr = initCl.split(",");
			clList = Arrays.asList(initClArr);
		}else{
			//若无初始策略则用随机数生成固定数量的初始策略
			initClNum = initClNum == null||initClNum == 0?40:initClNum;
			clList = createInitFFCCL(initClNum);
		}
	}*/
}
