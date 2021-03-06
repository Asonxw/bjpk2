package com.as.boot.txffc.thread;

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

import com.as.boot.txffc.controller.ExampleControll;
import com.as.boot.txffc.frame.AnyThreeFrame;
import com.as.boot.utils.ModHttpUtil;
import com.as.boot.utils.ZLinkStringUtils;

public class AnyThreeThread implements Runnable{
	
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
				//初始连挂数
				Integer initFailCount = Integer.parseInt(AnyThreeFrame.initFailCountField.getText());
				//获取模拟连挂转换数
				Integer mnFailSwhich = Integer.parseInt(AnyThreeFrame.mnFailSwhichField.getText());
				
				
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
						if(AnyThreeFrame.FFCRound !=null && (Double.parseDouble(AnyThreeFrame.FFCRound) == (resultRound_i-1) || resultRound.endsWith("0001"))){
							//获取表格第一行（判断是否有未开奖投注）
							Object down_first = AnyThreeFrame.tableDefaultmodel.getRowCount()>0?AnyThreeFrame.tableDefaultmodel.getValueAt(0, 6):null;
							if(clList!=null&&clList.size()>0&&down_first!=null&&down_first.equals("待开奖")){
								Double tempLr = 0d;
								Integer tableIndex = 0;
								//判断已投注方案类型
								String mnOrSzStr = AnyThreeFrame.tableDefaultmodel.getValueAt(0, 8).toString();
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
											AnyThreeFrame.tableDefaultmodel.setValueAt("中", tableIndex, 7);
											//计算盈利值
											Double itemLr = btArr[btNumList.get(i)] * baseMoney * pl;
											tempLr += itemLr;
											AnyThreeFrame.tableDefaultmodel.setValueAt("0/"+maxFailCountList.get(i), tableIndex, 4);
											AnyThreeFrame.tableDefaultmodel.setValueAt(df.format(itemLr), tableIndex, 3);
											sulCountList.set(i, sulCountList.get(i) + 1);
											btNumList.set(i, 0);
										}else{
											zjFlagList.set(i, false);
											failCountList.set(i, failCountList.get(i) + 1);
											sulCountList.set(i, 0);
											if(failCountList.get(i)>maxFailCountList.get(i))
												maxFailCountList.set(i, failCountList.get(i));
											//记录连挂数
											AnyThreeFrame.tableDefaultmodel.setValueAt(failCountList.get(i)+"/"+maxFailCountList.get(i), tableIndex, 4);
											AnyThreeFrame.tableDefaultmodel.setValueAt("挂", tableIndex, 7);
											Double tempFailP = -clArr.length * baseMoney *  btArr[btNumList.get(i)];
											AnyThreeFrame.tableDefaultmodel.setValueAt(df.format(tempFailP), tableIndex, 3);
										}
										AnyThreeFrame.tableDefaultmodel.setValueAt(resultKj, tableIndex, 6);
										tableIndex++;
									}
								}
								AnyThreeFrame.logTableDefaultmodel.insertRow(0, new String[]{"("+(new Date())+")"+resultRound+"期结算完成，单期盈亏："+df.format(tempLr)});
								if(mnOrSzStr.equals("模拟-投注")){
									mnlr += tempLr;
									AnyThreeFrame.mnYkValueLabel.setText(df.format(mnlr));
								}else{
									zslr += tempLr;
									AnyThreeFrame.szYkValueLabel.setText(df.format(zslr));
								}
								//判断盈利转换
								if(ZLinkStringUtils.isNotEmpty(AnyThreeFrame.ylSwhichField.getText())&&mnOrSzStr.equals("真 实-投注")){
									Integer ylSwhich = Integer.parseInt(AnyThreeFrame.ylSwhichField.getText());
									zslr_swhich += tempLr;
									AnyThreeFrame.logTableDefaultmodel.insertRow(0, new String[]{"("+(new Date())+")"+"真实投注盈利转换积累值："+df.format(zslr_swhich)});
									//真实盈利达到盈利转换值
									if(zslr_swhich >= ylSwhich){
										AnyThreeFrame.logTableDefaultmodel.insertRow(0, new String[]{"("+(new Date())+")"+"真实投注已达目标，切换为模拟投注，真实投注盈亏："+df.format(zslr_swhich)});
										//切换投注类型
										changeDownType();
									}
								}
								Boolean boomFlag = false;
								//如果设置了盈利转换需要判断是否爆仓
								if(ZLinkStringUtils.isNotEmpty(AnyThreeFrame.ylSwhichField.getText())){
									for (int i = 0, il = clList.size(); i<il;i++) {
										//判断是否已经爆仓（达到倍投阶梯顶端）
										if(btNumList.get(i) >= (btArr.length-1)&&mnOrSzStr.equals("真 实-投注")){
											boomFlag = true;
											//如果当前为实战爆掉则需要清空盈利及回头及倍投转模拟
											if(mnOrSzStr.equals("真 实-投注")){
												AnyThreeFrame.logTableDefaultmodel.insertRow(0, new String[]{"("+(new Date())+")"+"真实投注爆仓，转为模拟投注，损失金额："+df.format(zslr_swhich)});
												//重置回头
												zslr_return = zslr + Double.parseDouble(AnyThreeFrame.returnField.getText());
											}
											initBtNumList();
											changeDownType();
										
										}else if(failCountList.get(i) >= mnFailSwhich&&mnOrSzStr.equals("模拟-投注")){
											boomFlag = true;
											//模拟投注，只要连挂超过倍投则判定为爆仓
											AnyThreeFrame.logTableDefaultmodel.insertRow(0, new String[]{"("+(new Date())+")"+"模拟投注连挂爆仓，转为真实投注"});
											//重置回头
											mnlr_return = mnlr + Double.parseDouble(AnyThreeFrame.returnField.getText());
											initBtNumList();
											changeDownType();
										}
									}
								}
								
								//盈利回头判断（爆仓不需要执行）
								if(!boomFlag&&ZLinkStringUtils.isNotEmpty(AnyThreeFrame.returnField.getText())){
									if(mnOrSzStr.equals("模拟-投注")&&mnlr >= mnlr_return){
										//模拟盈利回头重置
										mnlr_return = mnlr + Double.parseDouble(AnyThreeFrame.returnField.getText());
										AnyThreeFrame.logTableDefaultmodel.insertRow(0, new String[]{"("+(new Date())+")"+"盈利回头成功，模拟投注-下期盈利回头为："+df.format(mnlr_return)});
										initBtNumList();
									}else if(mnOrSzStr.equals("真 实-投注")&&zslr >= zslr_return){
										zslr_return = zslr + Double.parseDouble(AnyThreeFrame.returnField.getText());
										AnyThreeFrame.logTableDefaultmodel.insertRow(0, new String[]{"("+(new Date())+")"+"盈利回头成功，真实投注-下期盈利回头为："+df.format(zslr_return)});
										initBtNumList();
									}else
										addBtnNumList();
								}else if(!boomFlag)
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
							startDownFFC(initFailCount);
							
						}
						//判断是否已经跨天
						/*if(ExampleControll.nextFFCRound.endsWith("0001"))
							AnyThreeFrame.FFCRound = ExampleControll.nextFFCRound.substring(0, 8) + "0000";*/
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
		historyRound = historyRound.substring(0, historyNum*18-1);
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
		List<HashMap<String, String>> temClList = new ArrayList<HashMap<String,String>>();
		temClList.add(null);temClList.add(null);temClList.add(null);temClList.add(null);temClList.add(null);temClList.add(null);temClList.add(null);temClList.add(null);temClList.add(null);temClList.add(null);
		HashMap<String, String> tempClMap = null;
		String clname = "";
		String clPosition = "";
		//初始连挂数
		Integer initFailCount = Integer.parseInt(AnyThreeFrame.initFailCountField.getText());
		try {
			HashMap<String, String> clParams = null;
			for (int i = 0, il = AnyThreeFrame.clBoxList.size(); i < il; i++) {
				tempClMap = new HashMap<String, String>();
				if(AnyThreeFrame.clBoxList.get(i).isSelected()){
					/*clname += _index-1;
					clPosition += (_index-1)+",";*/
					//获取投注位置
					clname = AnyThreeFrame.clBoxList.get(i).getText();
					//逗号分隔
					clPosition = clname.charAt(0)+ "," + clname.charAt(1)+ "," + clname.charAt(2);
					tempClMap.put("position", clname);
					clParams = getTXFFCL(historyRound, historyNum, clPosition, null, initClFlag, initClNum, clNum, aimMaxFail, maxRestN, initFailCount);
					tempClMap.put("cl", clParams.get("cl"));
					AnyThreeFrame.logTableDefaultmodel.insertRow(0, new String[]{"("+(new Date())+")"+clname+"策略初始化成功，注数："+clParams.get("count")});
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
		Integer historyNum = Integer.parseInt(AnyThreeFrame.historyNumField.getText());
		Integer l = historyNum*18-1;
		if(historyRound.length() > l)
			historyRound = historyRound.substring(0, l);
		else System.out.println(historyRound.length());
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
		
		HashMap<String, String> tempClMap = null;
		String clname = "";
		String clPosition = "";
		try {
			HashMap<String, String> clParams = null;
			for (int i = 0, il = AnyThreeFrame.clBoxList.size(); i < il; i++) {
				if(AnyThreeFrame.clBoxList.get(i).isSelected()){
					/*clname += _index-1;
					clPosition += (_index-1)+",";*/
					if(clIndex.equals(i)){
						//获取投注位置
						clname = AnyThreeFrame.clBoxList.get(i).getText();
						//逗号分隔
						clPosition = clname.charAt(0)+ "," + clname.charAt(1)+ "," + clname.charAt(2);
						tempClMap = new HashMap<String, String>();
						tempClMap.put("position", clname);
						clParams = getTXFFCL(historyRound, historyNum, clPosition, null, initClFlag, initClNum, clNum, aimMaxFail, maxRestN, initFailCount);
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
	
	public static HashMap<String, String> getTXFFCL(String historyRound, Integer historyNum, String putPosition, String initCl, Integer initClFlag, Integer initClNum, Integer clNum, Integer aimMaxFail, Integer maxRestN, Integer initFailCount){
		//解析需要投注的位置
		String[] positionArr = putPosition.split(",");
		//获取星数
		Integer positionNum = positionArr.length;
		Integer[] positionArr_i = new Integer[positionArr.length];
		for (int i = 0; i < positionArr.length; i++)
			positionArr_i[i] = Integer.parseInt(positionArr[i]);
		//截取后N 期作为连挂
		List<String> last_Result = new ArrayList<String>();
		if(!initFailCount.equals(0)){
			String last_ResultStr = historyRound.substring(0,18*initFailCount);
			last_ResultStr = last_ResultStr.substring(0, last_ResultStr.length()-1);
			String[] last_ResultArr =  last_ResultStr.split(";");
			for (String roundR : last_ResultArr){
				String temp_result = roundR.split(",")[1];
				String temp_positionR = "";
				for (int j = 0, jl = positionArr_i.length; j < jl; j++) 
					temp_positionR += temp_result.charAt(positionArr_i[j]);
				last_Result.add(temp_positionR);
			}
			historyRound = historyRound.substring(18*initFailCount,historyRound.length());
		}
		
		//初始化历史开奖
		String[] historyArr = historyRound.trim().split(";");
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
			else if(positionNum==3)clList = createInitFFCCL_3(initClNum, last_Result);
			else if(positionNum==4)clList = createInitFFCCL_4(initClNum);
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
					if(failCount > maxFailCount && !last_Result.contains(result)){
						//更新最大连挂及重置连挂出奖记录
						maxFailCount = failCount;
						maxFailResult = result;
						//达到历史最高则可直接停止当前循环
						if(historymaxFail == failCount)
							break;
					}
				}
			}
			
			//将最大连挂的开奖结果加入策略
			if(clList.size()<clNum&&maxFailResult!=null&&!clList.contains(maxFailResult))
				clList.add(maxFailResult);
			
			if(clList.size()==clNum||maxFailCount == 0){
				
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
						else if(positionNum==3)clList = createInitFFCCL_3(initClNum, last_Result);
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
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("cl", bestClList.toString());
		params.put("count", bestClList.size()+"");
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
					//AnyThreeFrame.logTableDefaultmodel.insertRow(0, new String[]{"("+(new Date())+")"+"策略"+i+"爆仓！！"});
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
		Integer changeNum = Integer.parseInt(AnyThreeFrame.changeYlField.getText());
		for (int i = clList.size()-1; i>=0; i--) {
			//中N期更换
			if(sulCountList.get(i).equals(changeNum)){
				rfreshTXFFCL(i, initFailCount);
				sulCountList.set(i,0);
			}
		}
		//判断是否需要投注，实战且开启真实投注
		if(AnyThreeFrame.downTypeSz.isSelected()&&AnyThreeFrame.trueDownFlagField.isSelected()){
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
				AnyThreeFrame.tableDefaultmodel.insertRow(0, new String[]{"--","--","--","--","--","--","--","--","--"});
			if(!clItem.get("position").equals("0")){
				AnyThreeFrame.tableDefaultmodel.insertRow(0, new String[]{ExampleControll.nextFFCRound,clItem.get("position")+clItem.get("cl"),btArr[btNumList.get(i)].toString(),"--","--","--","待开奖","待开奖",moOrSzArr[mnOrSzFlag]});
			}
			_index++;
		}
	}
	
	//切换盈利
	public static void changeDownType() {
		if(AnyThreeFrame.downTypeMn.isSelected()){
			//当前为模拟则改为实战
			AnyThreeThread.mnOrSzFlag = 1;
			AnyThreeFrame.downTypeSz.setSelected(true);
		}else if(AnyThreeFrame.downTypeSz.isSelected()){
			//当前为实战则改为模拟
			AnyThreeThread.mnOrSzFlag = 0;
			AnyThreeFrame.downTypeMn.setSelected(true);
		}
		zslr_swhich = 0d;
	}
}
