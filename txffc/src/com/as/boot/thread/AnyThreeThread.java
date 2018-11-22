package com.as.boot.thread;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.as.boot.controller.ExampleControll;
import com.as.boot.frame.AnyThreeFrame;
import com.as.boot.frame.AnyThreeFrame5;
import com.as.boot.utils.ModHttpUtil;
import com.as.boot.utils.ZLinkStringUtils;

public class AnyThreeThread implements Runnable{
	
	private Double zslr = 0d;
	//真实盈利回头值
	private Double zslr_return = null;
	
	private Double mnlr = 0d;
	//模拟盈利回头
	private Double mnlr_return = null;
	private String[] fa = {"个","十","百","千","万"};
	//private HashMap<String,String> clMap = null;
	public static List<HashMap<String, String>> clList = null;
	public static Double baseMoney = 0.002;
	//连挂数
	public static List<Integer> failCountList = Arrays.asList(0,0,0,0,0);
	//最大连挂
	private List<Integer> maxFailCountList = Arrays.asList(0,0,0,0,0);
	//倍投情况
	public static List<Integer> btNumList = Arrays.asList(0,0,0,0,0);
	//中奖情况
	private List<Boolean> zjFlagList = Arrays.asList(false,false,false,false,false);
	//倍投阶梯
	public static Integer[] btArr = null;
	
	public static DecimalFormat df = new DecimalFormat("#.000");
	
	private Integer pl = 950;
	//用于判断是模拟还是实战投注
	public static Integer mnOrSzFlag = 0;//默认模拟
	private static String[] moOrSzArr = {"模拟-投注","真 实-投注"};
	@Override
	public void run() {
		//初始化盈利回头
		if(ZLinkStringUtils.isNotEmpty(AnyThreeFrame.returnField.getText())){
			zslr_return = zslr + Double.parseDouble(AnyThreeFrame.returnField.getText());
			mnlr_return = mnlr + Double.parseDouble(AnyThreeFrame.returnField.getText());
		}
		//初始化倍投阶梯
		String[] btStrArr = AnyThreeFrame.btArrayField.getText().split(",");
		btArr = new Integer[btStrArr.length];
		for (int i = 0; i < btStrArr.length; i++)
			btArr[i] = Integer.parseInt(btStrArr[i]);
		while (true) {
			try {
				String resultRound = ExampleControll.FFCRound;
				String resultKj = ExampleControll.FFCResult;
				if(ZLinkStringUtils.isNotEmpty(resultKj)){
					//更新倍投阶梯
					btStrArr = AnyThreeFrame.btArrayField.getText().split(",");
					btArr = new Integer[btStrArr.length];
					for (int i = 0; i < btStrArr.length; i++)
						btArr[i] = Integer.parseInt(btStrArr[i]);
					
					//将开奖结果取出为数组
					String[] kjArray = resultKj.split(",");
					Double resultRound_i = Double.parseDouble(resultRound);
					if(AnyThreeFrame.FFCRound == null || !AnyThreeFrame.FFCRound.equals(resultRound)){
						AnyThreeFrame.kjTableDefaultmodel.insertRow(0, new String[]{resultRound,resultKj});
						//判断是否有投注
						if(AnyThreeFrame.FFCRound !=null && Double.parseDouble(AnyThreeFrame.FFCRound) == (resultRound_i-1)){
							if(clList!=null&&clList.size()>0){
								Double tempLr = 0d;
								Integer clIndex = 0;
								Integer tableIndex = 0;
								//利润
								for (int i = 0, il = clList.size(); i<il;i++) {
									HashMap<String, String> clItem = clList.get(i);
									//key为0则代表无策略
									if(!clItem.get("position").equals("0")){
										//获取策略注数
										String[] clArr = clItem.get("cl").split(",");
										String key = clItem.get("position");
										//累计投入值
										tempLr += -clArr.length * baseMoney *  btArr[btNumList.get(clIndex)];
										String result = "";
										//获取下注位置并截取开奖结果
										for (int j = 0; j < key.length(); j++) {
											result += kjArray[Integer.parseInt(key.charAt(j)+"")];
										}
										//判断是否中奖
										if(clItem.get("cl").contains(result)){
											zjFlagList.set(clIndex, true);
											failCountList.set(clIndex, 0);
											AnyThreeFrame.tableDefaultmodel.setValueAt("中", tableIndex, 7);
											//计算盈利值
											Double itemLr = btArr[btNumList.get(clIndex)] * baseMoney * pl;
											tempLr += itemLr;
											AnyThreeFrame.tableDefaultmodel.setValueAt("0/"+maxFailCountList.get(clIndex), tableIndex, 4);
											AnyThreeFrame.tableDefaultmodel.setValueAt(df.format(itemLr), tableIndex, 3);
										}else{
											zjFlagList.set(clIndex, false);
											failCountList.set(clIndex, failCountList.get(clIndex) + 1);
											if(failCountList.get(clIndex)>maxFailCountList.get(clIndex))
												maxFailCountList.set(clIndex, failCountList.get(clIndex));
											//记录连挂数
											AnyThreeFrame.tableDefaultmodel.setValueAt(failCountList.get(clIndex)+"/"+maxFailCountList.get(clIndex), tableIndex, 4);
											AnyThreeFrame.tableDefaultmodel.setValueAt("挂", tableIndex, 7);
											Double tempFailP = -clArr.length * baseMoney *  btArr[btNumList.get(clIndex)];
											AnyThreeFrame.tableDefaultmodel.setValueAt(df.format(tempFailP), tableIndex, 3);
										}
										AnyThreeFrame.tableDefaultmodel.setValueAt(resultKj, tableIndex, 6);
										tableIndex++;
									}
									clIndex++;
								}
								//判断已投注方案类型
								String mnOrSzStr = AnyThreeFrame.tableDefaultmodel.getValueAt(0, 8).toString();
								if(mnOrSzStr.equals("模拟-投注")){
									mnlr += tempLr;
									AnyThreeFrame.mnYkValueLabel.setText(df.format(mnlr));
								}else{
									zslr += tempLr;
									AnyThreeFrame.szYkValueLabel.setText(df.format(zslr));
								}
								
								//盈利回头判断
								if(ZLinkStringUtils.isNotEmpty(AnyThreeFrame.returnField.getText())){
									if(mnOrSzStr.equals("模拟-投注")&&mnlr >= mnlr_return){
										//模拟盈利回头重置
										mnlr_return = mnlr + Double.parseDouble(AnyThreeFrame.returnField.getText());
										initBtNumList();
									}else if(mnOrSzStr.equals("真 实-投注")&&zslr >= zslr_return){
										zslr_return = zslr + Double.parseDouble(AnyThreeFrame.returnField.getText());
										initBtNumList();
									}else
										addBtnNumList();
								}else
									addBtnNumList();
								//如果是刚从模拟转到实战，则初始化倍投
								if(ZLinkStringUtils.isNotEmpty(AnyThreeFrame.returnField.getText())){
									if(mnOrSzStr.equals("模拟-投注"))
										//检查到已经切换为真实投注，初始化倍投
										if(mnOrSzFlag.equals(1))
											initBtNumList();
								}
							}
						}else{
							//断期
						}
						//是否投注
						if(AnyThreeFrame.button.getText().equals("停止执行")){
							//判断是否需要更改策略
							/*if(changeClYl!=null&&everyClYl>=changeClYl){
								initTXFFCL();
								everyClYl = 0d;
							}else if(changeClYl!=null&&everyClYl < -4000){
								//爆仓换策略
								initTXFFCL();
								everyClYl = 0d;
							}*/
							startDownFFC();
							
						}
						//判断是否已经跨天
						if(ExampleControll.nextFFCRound.endsWith("0001"))
							AnyThreeFrame.FFCRound = ExampleControll.nextFFCRound.substring(0, 9) + "0000";
						AnyThreeFrame.FFCRound = resultRound;
						AnyThreeFrame.FFCResult = resultKj;
						Thread.sleep(30000);//更新到数据后睡眠30s
						
					}else if(AnyThreeFrame.FFCRound.equals(resultRound)){
						Thread.sleep(1000);//未更新到数据睡眠3s
					}
				}else
					Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	//初始化策略
	public static void initTXFFCL(){
		//获取历史开奖情况
		String historyRound = historyResult();
		//统计历史期数
		Integer historyNum = Integer.parseInt(AnyThreeFrame.historyNumField.getText());
		//是否系统初始化策略，只能是
		Integer initClFlag = 1;
		//初始化策略组数
		Integer initClNum = Integer.parseInt(AnyThreeFrame.initClNumField.getText());
		//策略数
		Integer clNum = Integer.parseInt(AnyThreeFrame.clNumField.getText());
		//期望最大连挂数
		Integer aimMaxFail = Integer.parseInt(AnyThreeFrame.aimMaxFailField.getText());
		//重置次数
		Integer maxRestN = Integer.parseInt(AnyThreeFrame.maxRestNField.getText());
		Integer _index = 1;
		List<HashMap<String, String>> temClList = new ArrayList<HashMap<String,String>>();
		
		HashMap<String, String> tempClMap = null;
		String clname = "";
		String clPosition = "";
		try {
			for (int i = 0, il = AnyThreeFrame.clBoxList.size(); i < il; i++) {
				if(AnyThreeFrame.clBoxList.get(i).isSelected()){
					clname += _index-1;
					clPosition += (_index-1)+",";
				}
				if(_index%5==0){
					tempClMap = new HashMap<String, String>();
					_index = 1;
					if(clname != ""){
						tempClMap.put("position", clname);
						tempClMap.put("cl", getTXFFCL(historyRound, historyNum, clPosition.substring(0,clPosition.length()-1), null, initClFlag, initClNum, clNum, aimMaxFail, maxRestN));
					}else 
						tempClMap.put("position", "0");//未选中则标记为空策略
					clname = "";
					clPosition = "";
					temClList.add(tempClMap);
				}else
					_index++;
			}
			clList = temClList;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void rfreshTXFFCL(Integer clIndex){
		//获取历史开奖情况
		String historyRound = historyResult();
		//统计历史期数
		Integer historyNum = Integer.parseInt(AnyThreeFrame.historyNumField.getText());
		//是否系统初始化策略，只能是
		Integer initClFlag = 1;
		//初始化策略组数
		Integer initClNum = Integer.parseInt(AnyThreeFrame.initClNumField.getText());
		//策略数
		Integer clNum = Integer.parseInt(AnyThreeFrame.clNumField.getText());
		//期望最大连挂数
		Integer aimMaxFail = Integer.parseInt(AnyThreeFrame.aimMaxFailField.getText());
		//重置次数
		Integer maxRestN = Integer.parseInt(AnyThreeFrame.maxRestNField.getText());
		Integer _index = 1;
		Integer cl_item = 0;
		
		HashMap<String, String> tempClMap = null;
		String clname = "";
		String clPosition = "";
		try {
			for (int i = 0, il = AnyThreeFrame.clBoxList.size(); i < il; i++) {
				if(AnyThreeFrame.clBoxList.get(i).isSelected()){
					clname += _index-1;
					clPosition += (_index-1)+",";
				}
				if(_index%5==0){
					if(cl_item.equals(clIndex)){
						tempClMap = new HashMap<String, String>();
						if(clname != ""){
							tempClMap.put("position", clname);
							tempClMap.put("cl", getTXFFCL(historyRound, historyNum, clPosition.substring(0,clPosition.length()-1), null, initClFlag, initClNum, clNum, aimMaxFail, maxRestN));
						}else 
							tempClMap.put("position", "0");//未选中则标记为空策略
						clname = "";
						clPosition = "";
						clList.set(clIndex, tempClMap);
						break;
					}else {
						clname = "";
						clPosition = "";
					}
					cl_item++;
					_index = 1;
				}else
					_index++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getTXFFCL(String historyRound, Integer historyNum, String putPosition, String initCl, Integer initClFlag, Integer initClNum, Integer clNum, Integer aimMaxFail, Integer maxRestN){
		//初始化历史开奖
		String[] historyArr = historyRound.trim().split(";");
		if(historyNum != null && historyNum > 0)
			historyArr = Arrays.copyOfRange(historyArr, 0, historyNum);
		List<String> clList = new ArrayList<String>();
		//策略组数如果没有设定则默认为50组
		clNum = clNum==null||clNum==0?50:clNum;
		Integer failCount = 0;
		Integer maxFailCount = 0;
		Integer historymaxFail = 0;
		String maxFailResult = null;
		//记录最佳策略
		Integer bestMaxFail = 0;
		List<String> bestClList = null;
		//解析需要投注的位置
		String[] positionArr = putPosition.split(",");
		//获取星数
		Integer positionNum = positionArr.length;
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
			if(positionNum==2)clList = createInitFFCCL_2(initClNum);
			else if(positionNum==3)clList = createInitFFCCL_3(initClNum);
			else if(positionNum==4)clList = createInitFFCCL_4(initClNum);
		}
		//遍历历史开奖
		while (maxRestN != 0) {
			//反向循环
			for (int i = historyArr.length-1; i >= 0; i--) {
				if(ZLinkStringUtils.isNotEmpty(historyArr[i])){
					if(historyArr[i].trim().split(",").length > 1){
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
								maxFailCount = failCount;
								maxFailResult = result;
								//达到历史最高则可直接停止当前循环
								if(historymaxFail == failCount)
									break;
							}
						}
					}
				}
			}
			
			//将最大连挂的开奖结果加入策略
			if(clList.size()<clNum)
				clList.add(maxFailResult);
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
						if(positionNum==2)clList = createInitFFCCL_2(initClNum);
						else if(positionNum==3)clList = createInitFFCCL_3(initClNum);
						else if(positionNum==4)clList = createInitFFCCL_4(initClNum);
						failCount = 0;
						maxFailResult = null;
						//控制循环次数为maxRestN次，防止无限循环
						maxRestN --;
					}else break;
				}
			}
			//循环完一期后将最大重新计算
			if(maxRestN!=0){
				historymaxFail = maxFailCount;
				maxFailCount = 0;
				maxFailResult = null;
			}
		}
		return bestClList.toString();
	}
	
	public static List<String> createInitFFCCL_2(Integer initClNum){
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < initClNum; i++) {
			String item = null;
			item = createRandom()+""+createRandom();
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
	
	public static List<String> createInitFFCCL_3(Integer initClNum){
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < initClNum; i++) {
			String item = null;
			item = createRandom()+""+createRandom()+""+createRandom();
			while (true) {
				if(list.contains(item))
					item = createRandom()+""+createRandom()+""+createRandom();
				else{
					list.add(item);
					break;
				}
			}
		}
		return list;
	}
	
	public static List<String> createInitFFCCL_4(Integer initClNum){
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < initClNum; i++) {
			String item = null;
			item = createRandom()+""+createRandom()+""+createRandom()+""+createRandom();
			while (true) {
				if(list.contains(item))
					item = createRandom()+""+createRandom()+""+createRandom()+""+createRandom();
				else{
					list.add(item);
					break;
				}
			}
		}
		return list;
	}
	
	public static Integer createRandom(){
		return (int)(Math.random()*10);
	}
	//根据文件获取历史开奖记录
	public static String historyResult(){
		StringBuilder fileContent = new StringBuilder();
		try {
			File file = new File("G:/modeng_gj/OpenCode/TXFFC.txt");
			if(!file.exists())
				file = new File("E:/modeng_gj/OpenCode/TXFFC.txt");
			//获取文件内容
			BufferedReader bfr = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            String lineTxt = null;
            while ((lineTxt = bfr.readLine()) != null) {
            	fileContent.append(lineTxt.trim().replace("	", ",")).append(";");
            }
            bfr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileContent.toString();
	}
	
	public static void initBtNumList(){
		//达到盈利回头目标，所有倍投全部回到起点
		btNumList.set(0, 0);
		btNumList.set(1, 0);
		btNumList.set(2, 0);
		btNumList.set(3, 0);
		btNumList.set(4, 0);
	}
	//叠加倍投
	public void addBtnNumList(){
		//未达到盈利回头目标，所有挂的增加倍数
		for (int i = 0; i < zjFlagList.size(); i++) {
			if(zjFlagList.get(i)){
				//中的初始化倍投
				btNumList.set(i, 0);
			}else{
				//判断是否已超出倍投
				if(btNumList.get(i)>=btArr.length-1)
					//超出倍投则回归初始
					btNumList.set(i, 0);
				else
					//挂的网上倍投
					btNumList.set(i, btNumList.get(i)+1);
			}
		}
	}
	
	/**
	 * @Title: startDownFFC  
	 * @Description:开始投注 
	 * @author: Ason      
	 * @return: void      
	 * @throws
	 */
	public static void startDownFFC(){
		//初始化策略
		initTXFFCL();
		Integer _index = 0;
		for (int i = clList.size()-1; i>=0; i--) {
			
			if(failCountList.get(i)==0)
				rfreshTXFFCL(i);
			HashMap<String, String> clItem = clList.get(i);
			if(_index == 0)
				AnyThreeFrame.tableDefaultmodel.insertRow(0, new String[]{"--","--","--","--","--","--","--","--","--"});
			if(!clItem.get("position").equals("0"))
				AnyThreeFrame.tableDefaultmodel.insertRow(0, new String[]{ExampleControll.nextFFCRound,clItem.get("position")+clItem.get("cl"),btArr[btNumList.get(i)].toString(),"--","--","--","待开奖","待开奖",moOrSzArr[mnOrSzFlag]});
			_index++;
		}
		//判断是否需要投注
		/*if(AnyThreeFrame.downTypeSz.isSelected()){
			//格式化奖期
			String issue = ExampleControll.nextFFCRound;
			issue = issue.substring(0,9)+"-"+issue.substring(9,12);
			ModHttpUtil.addTXFFCOrder_RX3(issue, clList, btNumList, btArr, baseMoney);
			
		}*/
	}
}
