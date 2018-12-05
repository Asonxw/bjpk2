package com.as.boot.thread;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.as.boot.controller.ExampleControll;
import com.as.boot.frame.HotClFrame;
import com.as.boot.utils.ModHttpUtil;
import com.as.boot.utils.ZLinkStringUtils;

public class HotClThread implements Runnable{
	
	private Double zslr = 0d;
	//真实盈利回头值
	private Double zslr_return = null;
	
	private Double mnlr = 0d;
	//模拟盈利回头
	private Double mnlr_return = null;
	//盈利转换
	public static Double zslr_swhich = 0d;
	
	//private HashMap<String,String> clMap = null;
	public static List<HashMap<String, String>> clList = null;
	public static Double baseMoney = 0.002;
	//连挂数
	public static List<Integer> failCountList = Arrays.asList(0,0,0,0,0,0,0,0,0,0);
	//连中数
	public static List<Integer> sulCountList = Arrays.asList(0,0,0,0,0,0,0,0,0,0);
	//最大连挂
	private List<Integer> maxFailCountList = Arrays.asList(0,0,0,0,0,0,0,0,0,0);
	//倍投情况
	public static List<Integer> btNumList = Arrays.asList(0,0,0,0,0,0,0,0,0,0);
	//中奖情况
	private List<Boolean> zjFlagList = Arrays.asList(false,false,false,false,false,false,false,false,false,false);
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
		if(ZLinkStringUtils.isNotEmpty(HotClFrame.returnField.getText())){
			zslr_return = zslr + Double.parseDouble(HotClFrame.returnField.getText());
			mnlr_return = mnlr + Double.parseDouble(HotClFrame.returnField.getText());
		}
		//初始化倍投阶梯
		String[] btStrArr = HotClFrame.btArrayField.getText().split(",");
		btArr = new Integer[btStrArr.length];
		for (int i = 0; i < btStrArr.length; i++)
			btArr[i] = Integer.parseInt(btStrArr[i]);
		while (true) {
			try {
				//初始连挂数
				Integer initFailCount = Integer.parseInt(HotClFrame.initFailCountField.getText());
				//获取模拟连挂转换数
				Integer mnFailSwhich = Integer.parseInt(HotClFrame.mnFailSwhichField.getText());
				
				
				String resultRound = ExampleControll.FFCRound;
				String resultKj = ExampleControll.FFCResult;
				if(ZLinkStringUtils.isNotEmpty(resultKj)){
					//更新倍投阶梯
					btStrArr = HotClFrame.btArrayField.getText().split(",");
					btArr = new Integer[btStrArr.length];
					for (int i = 0; i < btStrArr.length; i++)
						btArr[i] = Integer.parseInt(btStrArr[i]);
					
					//将开奖结果取出为数组
					String[] kjArray = resultKj.split(",");
					Double resultRound_i = Double.parseDouble(resultRound);
					if(HotClFrame.FFCRound == null || !HotClFrame.FFCRound.equals(resultRound)){
						HotClFrame.kjTableDefaultmodel.insertRow(0, new String[]{resultRound,resultKj});
						//判断是否有投注
						if(HotClFrame.FFCRound !=null && (Double.parseDouble(HotClFrame.FFCRound) == (resultRound_i-1) || resultRound.endsWith("0001"))){
							//获取表格第一行（判断是否有未开奖投注）
							Object down_first = HotClFrame.tableDefaultmodel.getRowCount()>0?HotClFrame.tableDefaultmodel.getValueAt(0, 6):null;
							if(clList!=null&&clList.size()>0&&down_first!=null&&down_first.equals("待开奖")){
								Double tempLr = 0d;
								Integer tableIndex = 0;
								//判断已投注方案类型
								String mnOrSzStr = HotClFrame.tableDefaultmodel.getValueAt(0, 8).toString();
								//利润
								for (int i = 0, il = clList.size(); i<il;i++) {
									HashMap<String, String> clItem = clList.get(i);
									//key为0则代表无策略
									if(!clItem.get("position").equals("0")){
										//获取策略注数
										String[] clArr = clItem.get("cl").split(",");
										String key = clItem.get("position");
										//累计投入值
										tempLr += -clArr.length * baseMoney *  btArr[btNumList.get(i)];
										String result = "";
										//获取下注位置并截取开奖结果
										for (int j = 0; j < key.length(); j++) {
											result += kjArray[Integer.parseInt(key.charAt(j)+"")];
										}
										//判断是否中奖
										if(clItem.get("cl").contains(result)){
											zjFlagList.set(i, true);
											failCountList.set(i, 0);
											HotClFrame.tableDefaultmodel.setValueAt("中", tableIndex, 7);
											//计算盈利值
											Double itemLr = btArr[btNumList.get(i)] * baseMoney * pl;
											tempLr += itemLr;
											HotClFrame.tableDefaultmodel.setValueAt("0/"+maxFailCountList.get(i), tableIndex, 4);
											HotClFrame.tableDefaultmodel.setValueAt(df.format(itemLr), tableIndex, 3);
											sulCountList.set(i, sulCountList.get(i) + 1);
											btNumList.set(i, 0);
										}else{
											zjFlagList.set(i, false);
											failCountList.set(i, failCountList.get(i) + 1);
											sulCountList.set(i, 0);
											if(failCountList.get(i)>maxFailCountList.get(i))
												maxFailCountList.set(i, failCountList.get(i));
											//记录连挂数
											HotClFrame.tableDefaultmodel.setValueAt(failCountList.get(i)+"/"+maxFailCountList.get(i), tableIndex, 4);
											HotClFrame.tableDefaultmodel.setValueAt("挂", tableIndex, 7);
											Double tempFailP = -clArr.length * baseMoney *  btArr[btNumList.get(i)];
											HotClFrame.tableDefaultmodel.setValueAt(df.format(tempFailP), tableIndex, 3);
										}
										HotClFrame.tableDefaultmodel.setValueAt(resultKj, tableIndex, 6);
										tableIndex++;
									}
								}
								HotClFrame.logTableDefaultmodel.insertRow(0, new String[]{"("+(new Date())+")"+resultRound+"期结算完成，单期盈亏："+df.format(tempLr)});
								if(mnOrSzStr.equals("模拟-投注")){
									mnlr += tempLr;
									HotClFrame.mnYkValueLabel.setText(df.format(mnlr));
								}else{
									zslr += tempLr;
									HotClFrame.szYkValueLabel.setText(df.format(zslr));
								}
								//判断盈利转换
								if(ZLinkStringUtils.isNotEmpty(HotClFrame.ylSwhichField.getText())&&mnOrSzStr.equals("真 实-投注")){
									Integer ylSwhich = Integer.parseInt(HotClFrame.ylSwhichField.getText());
									zslr_swhich += tempLr;
									HotClFrame.logTableDefaultmodel.insertRow(0, new String[]{"("+(new Date())+")"+"真实投注盈利转换积累值："+df.format(zslr_swhich)});
									//真实盈利达到盈利转换值
									if(zslr_swhich >= ylSwhich){
										HotClFrame.logTableDefaultmodel.insertRow(0, new String[]{"("+(new Date())+")"+"真实投注已达目标，切换为模拟投注，真实投注盈亏："+df.format(zslr_swhich)});
										//切换投注类型
										changeDownType();
									}
								}
								Boolean boomFlag = false;
								//如果设置了盈利转换需要判断是否爆仓
								if(ZLinkStringUtils.isNotEmpty(HotClFrame.ylSwhichField.getText())){
									for (int i = 0, il = clList.size(); i<il;i++) {
										//判断是否已经爆仓（达到倍投阶梯顶端）
										if(btNumList.get(i) >= (btArr.length-1)&&mnOrSzStr.equals("真 实-投注")){
											boomFlag = true;
											//如果当前为实战爆掉则需要清空盈利及回头及倍投转模拟
											if(mnOrSzStr.equals("真 实-投注")){
												HotClFrame.logTableDefaultmodel.insertRow(0, new String[]{"("+(new Date())+")"+"真实投注爆仓，转为模拟投注，损失金额："+df.format(zslr_swhich)});
												//重置回头
												zslr_return = zslr + Double.parseDouble(HotClFrame.returnField.getText());
											}
											initBtNumList();
											changeDownType();
										
										}else if(failCountList.get(i) >= mnFailSwhich&&mnOrSzStr.equals("模拟-投注")){
											boomFlag = true;
											//模拟投注，只要连挂超过倍投则判定为爆仓
											HotClFrame.logTableDefaultmodel.insertRow(0, new String[]{"("+(new Date())+")"+"模拟投注连挂爆仓，转为真实投注"});
											//重置回头
											mnlr_return = mnlr + Double.parseDouble(HotClFrame.returnField.getText());
											initBtNumList();
											changeDownType();
										}
									}
								}
								
								//盈利回头判断（爆仓不需要执行）
								if(!boomFlag&&ZLinkStringUtils.isNotEmpty(HotClFrame.returnField.getText())){
									if(mnOrSzStr.equals("模拟-投注")&&mnlr >= mnlr_return){
										//模拟盈利回头重置
										mnlr_return = mnlr + Double.parseDouble(HotClFrame.returnField.getText());
										HotClFrame.logTableDefaultmodel.insertRow(0, new String[]{"("+(new Date())+")"+"盈利回头成功，模拟投注-下期盈利回头为："+df.format(mnlr_return)});
										initBtNumList();
									}else if(mnOrSzStr.equals("真 实-投注")&&zslr >= zslr_return){
										zslr_return = zslr + Double.parseDouble(HotClFrame.returnField.getText());
										HotClFrame.logTableDefaultmodel.insertRow(0, new String[]{"("+(new Date())+")"+"盈利回头成功，真实投注-下期盈利回头为："+df.format(zslr_return)});
										initBtNumList();
									}else
										addBtnNumList();
								}else if(!boomFlag)
									addBtnNumList();
								//如果是刚从模拟转到实战，则初始化倍投
								if(ZLinkStringUtils.isNotEmpty(HotClFrame.returnField.getText())){
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
						if(HotClFrame.button.getText().equals("停止执行")){
							//判断是否需要更改策略
							/*if(changeClYl!=null&&everyClYl>=changeClYl){
								initTXFFCL();
								everyClYl = 0d;
							}else if(changeClYl!=null&&everyClYl < -4000){
								//爆仓换策略
								initTXFFCL();
								everyClYl = 0d;
							}*/
							startDownFFC(initFailCount);
							
						}
						//判断是否已经跨天
						/*if(ExampleControll.nextFFCRound.endsWith("0001"))
							HotClFrame.FFCRound = ExampleControll.nextFFCRound.substring(0, 8) + "0000";*/
						HotClFrame.FFCRound = resultRound;
						HotClFrame.FFCResult = resultKj;
						Thread.sleep(30000);//更新到数据后睡眠30s
						
					}else if(HotClFrame.FFCRound.equals(resultRound)){
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
		Integer historyNum = Integer.parseInt(HotClFrame.historyNumField.getText());
		historyRound = historyRound.substring(0, historyNum*18-1);
		//是否系统初始化策略，只能是
		Integer initClFlag = 1;
		//初始化策略组数
		Integer initClNum = Integer.parseInt(HotClFrame.initClNumField.getText());
		//策略数
		Integer clNum = Integer.parseInt(HotClFrame.clNumField.getText());
		//期望最大连挂数
		Integer aimMaxFail = Integer.parseInt(HotClFrame.aimMaxFailField.getText());
		//重置次数
		Integer maxRestN = Integer.parseInt(HotClFrame.maxRestNField.getText());
		List<HashMap<String, String>> temClList = new ArrayList<HashMap<String,String>>();
		temClList.add(null);temClList.add(null);temClList.add(null);temClList.add(null);temClList.add(null);temClList.add(null);temClList.add(null);temClList.add(null);temClList.add(null);temClList.add(null);
		HashMap<String, String> tempClMap = null;
		String clname = "";
		String clPosition = "";
		//初始连挂数
		Integer initFailCount = Integer.parseInt(HotClFrame.initFailCountField.getText());
		try {
			HashMap<String, String> clParams = null;
			for (int i = 0, il = HotClFrame.clBoxList.size(); i < il; i++) {
				tempClMap = new HashMap<String, String>();
				if(HotClFrame.clBoxList.get(i).isSelected()){
					/*clname += _index-1;
					clPosition += (_index-1)+",";*/
					//获取投注位置
					clname = HotClFrame.clBoxList.get(i).getText();
					//逗号分隔
					clPosition = clname.charAt(0)+ "," + clname.charAt(1)+ "," + clname.charAt(2);
					tempClMap.put("position", clname);
					clParams = getTXFFCL(historyRound, historyNum, clPosition, null, initClFlag, initClNum, clNum, aimMaxFail, maxRestN, initFailCount);
					tempClMap.put("cl", clParams.get("cl"));
					HotClFrame.logTableDefaultmodel.insertRow(0, new String[]{"("+(new Date())+")"+clname+"策略初始化成功，注数："+clParams.get("count")});
				}else
					tempClMap.put("position", "0");//未选中则标记为空策略
				temClList.set(i, tempClMap);
			}
			clList = temClList;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void rfreshTXFFCL(Integer clIndex, Integer initFailCount){
		//获取历史开奖情况
		String historyRound = historyResult();
		//统计历史期数
		Integer historyNum = Integer.parseInt(HotClFrame.historyNumField.getText());
		Integer l = historyNum*18-1;
		if(historyRound.length() > l)
			historyRound = historyRound.substring(0, l);
		else System.out.println(historyRound.length());
		//策略数
		Integer clNum = Integer.parseInt(HotClFrame.clNumField.getText());
		
		HashMap<String, String> tempClMap = null;
		String clPosition = "";
		try {
			HashMap<String, String> clParams = null;
			for (int i = 0, il = HotClFrame.clBoxList.size(); i < il; i++) {
				if(HotClFrame.clBoxList.get(i).isSelected()){
					/*clname += _index-1;
					clPosition += (_index-1)+",";*/
					if(clIndex.equals(i)){
						//获取投注位置
						String clname = HotClFrame.clBoxList.get(i).getText();
						tempClMap = new HashMap<String, String>();
						tempClMap.put("position", clname);
						clParams = getTXFFCL(historyRound, clname, clNum);
						tempClMap.put("cl", clParams.get("cl"));
						clList.set(clIndex, tempClMap);
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static HashMap<String, String> getTXFFCL(String historyRound, String putPosition, Integer clNum){
		//解析需要投注的位置
		Integer putPosition_i = Integer.parseInt(putPosition);
		HashMap<String, String> params = new HashMap<>();
		//取出开奖位置上的开奖号
		//初始化历史开奖
		String[] historyArr = historyRound.trim().split(";");
		List<Integer> listResult = new ArrayList<>();
		//循环
		for (int i = 0; i < historyArr.length; i++) {
			//获取开奖
			String item_result = historyArr[i].trim().split(",")[1].trim();
			listResult.add(Integer.parseInt(item_result.charAt(putPosition_i)+""));
		}
		System.out.println(listResult);
		Integer[] itemcount = new Integer[]{0,0,0,0,0,0,0,0,0,0};
		//统计出现次数最多的
		for (Integer i : listResult) {
			itemcount[i] = itemcount[i]+1; 
		}
		List<HashMap<String, Integer>> tempClList = new ArrayList<>();
		//用于防止重复
		List<Integer> temClList_i = new ArrayList<>();
		HashMap<String, Integer> tempItem  = null;
		for (int i = 10; i >= 0; i--) {
			for (int j = 0; j < itemcount.length; j++) {
				if(itemcount[j] == i)
					if(!temClList_i.contains(j)){
						tempItem = new HashMap<>();
						tempItem.put("count", itemcount[j]);
						tempItem.put("position", j);
						tempClList.add(tempItem);
						//添加进list防止重复
						temClList_i.add(j);
					}
			}
			if(temClList_i.size()>=clNum)break;//已达到标准
		}
		System.out.println(ZLinkStringUtils.parseJsonToString(tempClList));
		//初步结果获取的策略大于目标策略数，则需要进入筛选
		if(tempClList.size()>clNum){
			List<Integer> needSx = new ArrayList<>();
			Integer sxCount = tempClList.get(tempClList.size()-1).get("count");
			//将最后几个相同出现次数的取出并判断谁先出现
			for (int i = tempClList.size()-1; i >= 0; i--) {
				tempItem = tempClList.get(i);
				if(tempItem.get("count").equals(sxCount))
					needSx.add(tempItem.get("position"));
				else break;
			}
			Integer needSxCount = clNum - (tempClList.size() - needSx.size());
			List<Integer> sxSulList = new ArrayList<>();
			for (int i = 0; i < listResult.size(); i++) {
				for (int j = 0; j < needSx.size(); j++) {
					if(listResult.get(i).equals(needSx.get(j))&&!sxSulList.contains(needSx.get(j)))
						sxSulList.add(needSx.get(j));
					if(sxSulList.size() == needSxCount)break;
				}
			}
			
			//重新组装数据
			temClList_i = new ArrayList<>();
			for (int i = 0; i < tempClList.size() - needSx.size(); i++) 
				temClList_i.add(tempClList.get(i).get("position"));
			
			temClList_i.addAll(sxSulList);
		}
	
		params.put(putPosition, temClList_i.toString());
		System.out.println(temClList_i.toString());
		return params;
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
	
	public static List<String> createInitFFCCL_3(Integer initClNum, List<String> last_result){
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < initClNum; i++) {
			String item = null;
			item = createRandom()+""+createRandom()+""+createRandom();
			while (true) {
				if(list.contains(item) || last_result.contains(item))
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
		btNumList = Arrays.asList(0,0,0,0,0,0,0,0,0,0);
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
				if(btNumList.get(i)>=btArr.length-1){
					//超出倍投则回归初始
					btNumList.set(i, 0);
					//HotClFrame.logTableDefaultmodel.insertRow(0, new String[]{"("+(new Date())+")"+"策略"+i+"爆仓！！"});
				}else
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
	public static void startDownFFC(Integer initFailCount){
		
		Boolean downSulFlag = false;
		Integer changeNum = Integer.parseInt(HotClFrame.changeYlField.getText());
		for (int i = clList.size()-1; i>=0; i--) {
			//中N期更换
			if(sulCountList.get(i).equals(changeNum)){
				rfreshTXFFCL(i, initFailCount);
				sulCountList.set(i,0);
			}
		}
		//判断是否需要投注，实战且开启真实投注
		if(HotClFrame.downTypeSz.isSelected()&&HotClFrame.trueDownFlagField.isSelected()){
			//格式化奖期
			String issue = ExampleControll.nextFFCRound;
			issue = issue.substring(0,8)+"-"+issue.substring(8,12);
			downSulFlag = ModHttpUtil.addTXFFCOrder_RX3(issue, clList, btNumList, btArr, baseMoney);
		}else
			downSulFlag = true;
		//下注成功后再表格追加
		if(downSulFlag)
			//投注成功或者模拟模式时添加表格投注记录
			insertDownToTable();
		
	}
	
	/**
	 * 添加表格下注
	 * @author Ason
	 * @Title: insertDownToTable 
	 * @param changeNum
	 * @param initFailCount void
	 * @throws
	 */
	public static void insertDownToTable(){
		Integer _index = 0;
		for (int i = clList.size()-1; i>=0; i--) {
			HashMap<String, String> clItem = clList.get(i);
			if(_index == 0)
				HotClFrame.tableDefaultmodel.insertRow(0, new String[]{"--","--","--","--","--","--","--","--","--"});
			if(!clItem.get("position").equals("0")){
				HotClFrame.tableDefaultmodel.insertRow(0, new String[]{ExampleControll.nextFFCRound,clItem.get("position")+clItem.get("cl"),btArr[btNumList.get(i)].toString(),"--","--","--","待开奖","待开奖",moOrSzArr[mnOrSzFlag]});
			}
			_index++;
		}
	}
	
	//切换盈利
	public static void changeDownType() {
		if(HotClFrame.downTypeMn.isSelected()){
			//当前为模拟则改为实战
			HotClThread.mnOrSzFlag = 1;
			HotClFrame.downTypeSz.setSelected(true);
		}else if(HotClFrame.downTypeSz.isSelected()){
			//当前为实战则改为模拟
			HotClThread.mnOrSzFlag = 0;
			HotClFrame.downTypeMn.setSelected(true);
		}
		zslr_swhich = 0d;
	}
	
	public static void main(String[] args) {
		String round = "181205-1165,74817;181205-1164,47360;181205-1163,32505;181205-1162,58027;181205-1161,68425;181205-1160,67273;181205-1159,63439;181205-1158,73070;181205-1157,13344;181205-1156,59144;181205-1155,46073;181205-1154,15926;181205-1153,89153;181205-1152,80962;181205-1151,62175;181205-1150,60898;181205-1149,50248;181205-1148,99407;181205-1147,79902;181205-1146,92505;181205-1145,69532;181205-1144,57109;181205-1143,49539;181205-1142,43373;181205-1141,97744;181205-1140,01038;181205-1139,91595;181205-1138,97515;181205-1137,93670;181205-1136,02408";
		getTXFFCL(round, "0", 7);
	}
}
