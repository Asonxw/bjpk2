package com.as.boot.txffc.thread;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.as.boot.txffc.controller.ExampleControll;
import com.as.boot.txffc.frame.HotClFrame;
import com.as.boot.utils.ModHttpUtil;
import com.as.boot.utils.ZLinkStringUtils;
/**
* @ClassName: DelPreThreeThread
* @Description:删除最近出的三个号码作为下期投注策略 
* @author Ason
* @date 2018年12月7日 上午9:28:26
* @version v1_05 定点投注不监控版
 */
public class DelPreThreeThread implements Runnable{
	
	private Double zslr = 0d;
	//真实盈利回头值
	private Double zslr_return = null;
	
	private Double mnlr = 0d;
	//模拟盈利回头
	private Double mnlr_return = null;
	
	//实投盈利
	private Double truelr = 0d;
	//盈利转换
	public static Double zslr_swhich = 0d;
	
	//private HashMap<String,String> clMap = null;
	public static List<HashMap<String, String>> clList = null;
	public static Double baseMoney = 0.002;
	//连挂数
	public static List<Integer> failCountList = Arrays.asList(0,0,0,0,0);
	//真实投注连挂数
	public static List<Integer> failCountList_t = Arrays.asList(0,0,0,0,0);
	//记录近期开奖情况，共记录10期
	public static List<String> preResultList = new ArrayList<>();
	//连中数
	public static List<Integer> sulCountList = Arrays.asList(0,0,0,0,0);
	//最大连挂
	private List<Integer> maxFailCountList = Arrays.asList(0,0,0,0,0);
	//真实投注最大连挂
	private List<Integer> maxFailCountList_t = Arrays.asList(0,0,0,0,0);
	//倍投情况
	public static List<Integer> btNumList = Arrays.asList(0,0,0,0,0);
	
	//模拟实战情况
	public static List<Boolean> mnOrSzList = null;
	
	//单策略真实投注盈亏
	public static List<Double> zsYkList = Arrays.asList(0d,0d,0d,0d,0d);
	public static Double zsYK_temp = 0d;
	//中奖情况
	private List<Boolean> zjFlagList = Arrays.asList(false,false,false,false,false);
	//倍投阶梯
	public static Integer[] btArr = null;
	
	public static DecimalFormat df = new DecimalFormat("#.000");
	
	private Double pl = 19.5;
	//用于判断是模拟还是实战投注
	public static Integer mnOrSzFlag = 0;//默认模拟
	private static String[] moOrSzArr = {"模拟-投注","真 实-投注"};
	
	private static Integer sulAllCount = 0;
	private static Integer failAllCount = 0;
	private static Integer roundAllCount = 0;
	//开投策略
	//private static String changeStr = "000010000";//0代表未中奖1代表中奖
	private static List<String> zjFlagStrList = Arrays.asList("","","","",""); 
	//每日投注时间点
	public static List<String> evTimeList = new ArrayList<>();
	
	//当前日期，用于判断投点更新
	public static String nowDateStr = "";
	@Override
	public void run() {
		
		//初始化盈利回头
		/*if(ZLinkStringUtils.isNotEmpty(HotClFrame.returnField.getText())){
			zslr_return = zslr + Double.parseDouble(HotClFrame.returnField.getText());
			mnlr_return = mnlr + Double.parseDouble(HotClFrame.returnField.getText());
		}*/
		//初始化倍投阶梯
		String[] btStrArr = HotClFrame.btArrayField.getText().split(",");
		btArr = new Integer[btStrArr.length];
		for (int i = 0; i < btStrArr.length; i++)
			btArr[i] = Integer.parseInt(btStrArr[i]);
		//初始化模拟实战数据
		if(HotClFrame.downTypeSz.isSelected())
			mnOrSzList = Arrays.asList(true,true,true,true,true);
		else 
			mnOrSzList = Arrays.asList(false,false,false,false,false);
		Boolean downFlag = false;
		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
		while (true) {
			//判断是否需要初始化投点（每日00:00进行更新）
			if(!nowDateStr.equals(formatDate.format(new Date()))&&ZLinkStringUtils.isNotEmpty(nowDateStr))
				initEvTime();
			//判断是否到达投点
			if(evTimeList.size()>0){
				String time =  format.format(new Date()).substring(0,4);
				
				
				if(evTimeList.contains(time)){
					HotClFrame.logTableDefaultmodel.insertRow(0, new String[]{"("+(new Date())+")"+"到达投注点:"+time+"，开启真实投注。"});
					//判断是否需要将真实投注开启
					if(!HotClFrame.testFlagField.isSelected())
						HotClFrame.trueDownFlagField.setSelected(true);
					//设置成功后移除该投点避免重复执行
					for (int i = 0; i < evTimeList.size(); i++) {
						if(evTimeList.get(i).equals(time))
							evTimeList.remove(i);
					}
					downFlag = true;
					
				}
			}
			try {
				//初始化投注单位
				baseMoney = Double.parseDouble(HotClFrame.price.getSelectedItem().toString());
				
				//获取中奖监控
				String changeStr = HotClFrame.mnFailSwhichField.getText();
				Double winStop = Double.parseDouble(HotClFrame.winStopField.getText());
				Double failStop = Double.parseDouble(HotClFrame.failStopField.getText());
				
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
							if(!preResultList.get(preResultList.size()-1).equals(resultKj.replace(",", "")))
								preResultList.add(resultKj.replace(",", ""));
							if(preResultList.size()>20)
								preResultList.remove(0);
							//获取表格第一行（判断是否有未开奖投注）
							Object down_first = HotClFrame.tableDefaultmodel.getRowCount()>0?HotClFrame.tableDefaultmodel.getValueAt(0, 6):null;
							if(clList!=null&&clList.size()>0&&down_first!=null&&down_first.equals("待开奖")){
								roundAllCount++;
								//Double tempLr = 0d;
								Integer tableIndex = 0;
								//判断已投注方案类型
								//String mnOrSzStr = HotClFrame.tableDefaultmodel.getValueAt(0, 8).toString();
								//利润
								for (int i = 0, il = clList.size(); i<il;i++) {
									//用于判断是否需要初始化倍投（刚从模拟转实战需要记录）
									Boolean zsChange = false;
									
									HashMap<String, String> clItem = clList.get(i);
									//key为0则代表无策略
									if(clItem.get("cl")!=null){
										//获取策略注数
										String[] clArr = clItem.get("cl").split(",");
										String key = clItem.get("position");
										//累计投入值
										Double itemIn = -clArr.length * baseMoney *  btArr[btNumList.get(i)];
										String result = "";
										//获取下注位置并截取开奖结果
										for (int j = 0; j < key.length(); j++) {
											result += kjArray[Integer.parseInt(key.charAt(j)+"")];
										}
										String tempzjFalgStr = zjFlagStrList.get(i);
										//判断目前的中奖结果长度是否达到设定值的长度，如果达到则需要删除一个最远的开奖结果
										if(ZLinkStringUtils.isNotEmpty(changeStr)&&tempzjFalgStr.length() == changeStr.length())
											tempzjFalgStr = tempzjFalgStr.substring(1, tempzjFalgStr.length());
										else tempzjFalgStr = null;
										//判断是否中奖
										if(clItem.get("cl").contains(result)){
											zjFlagList.set(i, true);
											//赋值中奖结果
											tempzjFalgStr += "1";
											
											HotClFrame.tableDefaultmodel.setValueAt("中", tableIndex, 7);
											//计算盈利值
											Double itemLr = (btArr[btNumList.get(i)] * baseMoney * pl)/2;
											itemIn += itemLr;
											HotClFrame.tableDefaultmodel.setValueAt("0/"+maxFailCountList.get(i), tableIndex, 4);
											HotClFrame.tableDefaultmodel.setValueAt("0/"+maxFailCountList_t.get(i), tableIndex, 5);
											HotClFrame.tableDefaultmodel.setValueAt(df.format(itemLr), tableIndex, 3);
											sulCountList.set(i, sulCountList.get(i) + 1);
											btNumList.set(i, 0);
											if(mnOrSzList.get(i)){
												//真实投注
												zslr += itemIn;
												zsYkList.set(i, zsYkList.get(i)+itemIn);
												zsYK_temp += itemIn;
												//实投盈利
												if(HotClFrame.trueDownFlagField.isSelected())
													truelr += itemIn;
											}else{
												//模拟投注
												mnlr += itemIn;
											}
											//清空连挂数
											failCountList.set(i, 0);
											failCountList_t.set(i, 0);
											sulAllCount++;
										}else{
											zjFlagList.set(i, false);
											//赋值中奖结果
											tempzjFalgStr += "0";
											
											//累计连挂数
											failCountList.set(i, failCountList.get(i) + 1);
											sulCountList.set(i, 0);
											if(failCountList.get(i)>maxFailCountList.get(i))
												maxFailCountList.set(i, failCountList.get(i));
											if(mnOrSzList.get(i)){
												//累计真实连挂数
												failCountList_t.set(i, failCountList_t.get(i) + 1);
												if(failCountList_t.get(i)>maxFailCountList_t.get(i))
													maxFailCountList_t.set(i, failCountList_t.get(i));
											}
												
											//记录连挂数
											HotClFrame.tableDefaultmodel.setValueAt(failCountList.get(i)+"/"+maxFailCountList.get(i), tableIndex, 4);
											//记录真实投注连挂数
											HotClFrame.tableDefaultmodel.setValueAt(failCountList_t.get(i)+"/"+maxFailCountList_t.get(i), tableIndex, 5);
											
											HotClFrame.tableDefaultmodel.setValueAt("挂", tableIndex, 7);
											Double tempFailP = -clArr.length * baseMoney *  btArr[btNumList.get(i)];
											HotClFrame.tableDefaultmodel.setValueAt(df.format(tempFailP), tableIndex, 3);
											failAllCount++;
											if(mnOrSzList.get(i)){
												//真实投注
												zslr += itemIn;
												zsYK_temp += itemIn;
												zsYkList.set(i, zsYkList.get(i)+itemIn);
												//实投盈利
												if(HotClFrame.trueDownFlagField.isSelected())
													truelr += itemIn;
											}else{
												//模拟投注
												mnlr += itemIn;
											}
											
										}
										zjFlagStrList.set(i, tempzjFalgStr);
										HotClFrame.tableDefaultmodel.setValueAt(resultKj, tableIndex, 6);
										tableIndex++;
										
										//当前处于模拟且与设定的投注策略一致则开启实战
										/*if((!mnOrSzList.get(i) && ZLinkStringUtils.isEmpty(changeStr))||(!mnOrSzList.get(i) && tempzjFalgStr.equals(changeStr))){
											//撒旦法
											//设置为实战
											mnOrSzList.set(i,true);
											HotClFrame.logTableDefaultmodel.insertRow(0, new String[]{"("+(new Date())+")"+"策略<"+i+">转实战。"});
											zsYkList.set(i, 0d);
											//初始化倍投
											zsChange = true;
										}*/
										//真实投注爆仓判断
										if(mnOrSzList.get(i)&&btNumList.get(i) >= (btArr.length-1)){
											HotClFrame.logTableDefaultmodel.insertRow(0, new String[]{"("+(new Date())+")"+"策略<"+i+">真实投注爆仓，初始化策略盈利继续投注。"});
											zsYkList.set(i, 0d);
										}
										if(!zjFlagList.get(i)){
											//增加倍投
											if(btNumList.get(i) >= (btArr.length-1))
												btNumList.set(i, 0);
											else btNumList.set(i, btNumList.get(i)+1);
										}
										//如果是刚才从模拟转实战则需要初始化倍投
										if(zsChange)
											btNumList.set(i, 0);
									}
								}
								//判断盈利转换
								if(ZLinkStringUtils.isNotEmpty(HotClFrame.ylSwhichField.getText()) && zsYK_temp!=0){
									Double ylSwhich = Double.parseDouble(HotClFrame.ylSwhichField.getText());
									HotClFrame.logTableDefaultmodel.insertRow(0, new String[]{"("+(new Date())+")"+"真实投注盈利转换积累值："+df.format(zsYK_temp)});
									//真实盈利达到盈利转换值
									if(zsYK_temp >= ylSwhich){
										HotClFrame.logTableDefaultmodel.insertRow(0, new String[]{"("+(new Date())+")"+"策略真实投注已达目标，切换为模拟投注，真实投注盈亏："+df.format(zsYK_temp)});
										//切换投注类型
										//changeDownType();
										zsYK_temp = 0d;
										initBtNumList();
										mnOrSzList = Arrays.asList(false,false,false,false,false);
										//判断是否需要取消真实投注
										if(ZLinkStringUtils.isNotEmpty(HotClFrame.evTimeField.getText()))
											HotClFrame.trueDownFlagField.setSelected(false);
									}
								}
								HotClFrame.mnYkValueLabel.setText(df.format(mnlr));
								HotClFrame.szYkValueLabel.setText(df.format(zslr));
								HotClFrame.trueYkValueLabel.setText(df.format(truelr));
								//投注新增日志
								addDownLog();
								/*if(mnOrSzStr.equals("模拟-投注")){
									mnlr += tempLr;
								}else{
									zslr += tempLr;
									
								}*/
								//判断盈利转换
								
								/*Boolean boomFlag = false;
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
								}*/
								
								//盈利回头判断（爆仓不需要执行）
								/*if(!boomFlag&&ZLinkStringUtils.isNotEmpty(HotClFrame.returnField.getText())){
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
								}else if(!boomFlag)*/
								//addBtnNumList();
								//如果是刚从模拟转到实战，则初始化倍投
								/*if(ZLinkStringUtils.isNotEmpty(HotClFrame.returnField.getText())){
									if(mnOrSzStr.equals("模拟-投注"))
										//检查到已经切换为真实投注，初始化倍投
										if(mnOrSzFlag.equals(1))
											initBtNumList();
								}*/
							}
						}else{
							//断期
						}
						
						//判断止盈止损
						if(zslr >= winStop || (zslr < 0 && zslr*-1 >= failStop)){
							//停止投注
							HotClFrame.button.setText("开始执行");
		  					//初始化连挂及倍投
		  					//连挂数
		  					/*HotClThread.failCountList = Arrays.asList(0,0,0,0,0,0,0,0,0,0);
		  					//倍投情况
		  					HotClThread.btNumList = Arrays.asList(0,0,0,0,0,0,0,0,0,0);*/
		  					failCountList = Arrays.asList(0,0,0,0,0);
		  					//倍投情况
		  					btNumList = Arrays.asList(0,0,0,0,0);
		  					clList = null;
						}
						if(downFlag){
							//全量初始化投注倍数
							initBtNumList();
							//将所有策略修改为真实投注
							for (int i = 0; i < mnOrSzList.size(); i++) 
								mnOrSzList.set(i, true);
							downFlag = false;
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
							startDownFFC();
							
						}
						//判断是否已经跨天
						/*if(ExampleControll.nextFFCRound.endsWith("0001"))
							HotClFrame.FFCRound = ExampleControll.nextFFCRound.substring(0, 8) + "0000";*/
						HotClFrame.FFCRound = resultRound;
						HotClFrame.FFCResult = resultKj;
						
						HotClFrame.roundCount.setText(roundAllCount+"期");
						Double pecent = (sulAllCount*100d/(sulAllCount+failAllCount));
						HotClFrame.sulPercent.setText(df.format(pecent)+"%");
						Thread.sleep(30000);//更新到数据后睡眠30s
						
					}else if(HotClFrame.FFCRound.equals(resultRound)){
						Thread.sleep(1000);//未更新到数据睡眠3s
					}
				}else
					Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
				HotClFrame.logTableDefaultmodel.insertRow(0, new String[]{ZLinkStringUtils.getErrorMsg(e)});
			}
		}
	}

	/*public static void initPreResultList(){
		//获取历史10期开奖情况
		String historyRound = ;
		historyRound = historyRound.substring(0, 10*18-1);
		String[] historyArr = historyRound.trim().split(";");
		//循环
		for (int i = historyArr.length-1; i>= 0 ; i--) 
			//获取开奖
			preResultList.add(historyArr[i].trim().split(",")[1].trim());
	}*/
	
	//初始化策略
	public static void initTXFFCL(){
		//获取历史开奖情况
		//String historyRound = historyResult();
		//统计历史期数
		//Integer historyNum = Integer.parseInt(HotClFrame.historyNumField.getText());
		//historyRound = historyRound.substring(0, historyNum*18-1);
		//策略数
		//Integer clNum = Integer.parseInt(HotClFrame.clNumField.getText());
		List<HashMap<String, String>> temClList = new ArrayList<HashMap<String,String>>();
		temClList.add(null);temClList.add(null);temClList.add(null);temClList.add(null);temClList.add(null);
		HashMap<String, String> tempClMap = null;
		String clname = "";
		//是否爆仓才开启真实投注
		try {
			String cl = null;
			for (int i = 0, il = HotClFrame.clBoxList.size(); i < il; i++) {
				tempClMap = new HashMap<String, String>();
				if(HotClFrame.clBoxList.get(i).isSelected()){
					/*clname += _index-1;
					clPosition += (_index-1)+",";*/
					//获取投注位置
					clname = HotClFrame.clBoxList.get(i).getText();
					tempClMap.put("position", clname);
					cl = getTXFFCL_presev(clname);
					tempClMap.put("cl", cl);
					//if(cl!=null)HotClFrame.logTableDefaultmodel.insertRow(0, new String[]{"("+(new Date())+")"+clname+"策略初始化成功，注数：7"});
				}else
					tempClMap.put("position", "00");//未选中则标记为空策略
				temClList.set(i, tempClMap);
			}
			clList = temClList;
		} catch (Exception e) {
			e.printStackTrace();
			HotClFrame.logTableDefaultmodel.insertRow(0, new String[]{ZLinkStringUtils.getErrorMsg(e)});
		}
	}
	
	
	public static void rfreshTXFFCL(Integer clIndex){
		//获取历史开奖情况
		//String historyRound = historyResult();
		//统计历史期数
		//Integer historyNum = Integer.parseInt(HotClFrame.historyNumField.getText());
		//Integer l = historyNum*18-1;
		/*if(historyRound.length() > l)
			historyRound = historyRound.substring(0, l);
		else System.out.println(historyRound.length());*/
		/*//策略数
		Integer clNum = Integer.parseInt(HotClFrame.clNumField.getText());
		//是否去重码
		Boolean delPreRsult = HotClFrame.delPreResultCheckbox.isSelected();*/
		HashMap<String, String> tempClMap = null;
		try {
			String cl = null;
			for (int i = 0, il = HotClFrame.clBoxList.size(); i < il; i++) {
				if(HotClFrame.clBoxList.get(i).isSelected()){
					/*clname += _index-1;
					clPosition += (_index-1)+",";*/
					if(clIndex.equals(i)){
						//获取投注位置
						String clname = HotClFrame.clBoxList.get(i).getText();
						tempClMap = new HashMap<String, String>();
						tempClMap.put("position", clname);
						cl = getTXFFCL_presev(clname);
						tempClMap.put("cl", cl);
						clList.set(clIndex, tempClMap);
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			HotClFrame.logTableDefaultmodel.insertRow(0, new String[]{ZLinkStringUtils.getErrorMsg(e)});
		}
	}
	
	public static String getTXFFCL(String putPosition){
		//解析需要投注的位置
		Integer putPosition_i = Integer.parseInt(putPosition);
		List<Integer> delList = new ArrayList<>();
		Integer count = 0;
		//倒叙循环获取最近开奖的三个号码进行剔除
		for (int i = preResultList.size()-1; i >= 0; i--) {
			Integer item = Integer.parseInt(preResultList.get(i).charAt(putPosition_i)+"");
			count++;
			if(!delList.contains(item)&&delList.size()<3){
				delList.add(item);
				if(delList.size()>=3)break;
			}else if(delList.size()>=3)break;
		}
		
		List<Integer> tempClList = new ArrayList<>();
		//生成策略
		for (int i = 0; i < 10; i++) {
			if(!delList.contains(i))
				tempClList.add(i);
		}
		if(count > (Integer.parseInt(HotClFrame.historyNumField.getText())))
			return tempClList.toString();
		return null;
	}
	
	public static String getTXFFCL_presev(String putPosition){
		//解析需要投注的位置
		Integer putPosition_i = Integer.parseInt(putPosition);
		List<Integer> delList = new ArrayList<>();
		Integer count = 0;
		//倒叙循环获取最近开奖的7个号码进行剔除
		for (int i = preResultList.size()-1; i >= 0; i--) {
			Integer item = Integer.parseInt(preResultList.get(i).charAt(putPosition_i)+"");
			count++;
			if(!delList.contains(item)&&delList.size()<7){
				delList.add(item);
				if(delList.size()>=7)break;
			}else if(delList.size()>=7)break;
		}
		
		//List<Integer> tempClList = new ArrayList<>();
		//生成策略
		/*for (int i = 0; i < 10; i++) {
			if(!delList.contains(i))
				tempClList.add(i);
		}*/
		if(count > (Integer.parseInt(HotClFrame.historyNumField.getText())))
			return delList.toString();
		return null;
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
			HotClFrame.logTableDefaultmodel.insertRow(0, new String[]{ZLinkStringUtils.getErrorMsg(e)});
		}
		return fileContent.toString();
	}
	
	public static void initBtNumList(){
		//初始化所有倍投
		btNumList = Arrays.asList(0,0,0,0,0);
	}
	//叠加倍投
	public void addBtnNumList(){
		for (int i = 0; i < zjFlagList.size(); i++) {
			if(zjFlagList.get(i)){
				//中的初始化倍投
				btNumList.set(i, 0);
			}else{
				//判断是否已超出倍投
				if(btNumList.get(i)>=btArr.length-1){
					//超出倍投则回归初始
					btNumList.set(i, 0);
					HotClFrame.logTableDefaultmodel.insertRow(0, new String[]{"("+(new Date())+")"+"策略"+i+"爆仓！！"});
					//当未设置盈利转换时也需要校验爆仓及初始化盈利回头
					/*if(ZLinkStringUtils.isEmpty(HotClFrame.ylSwhichField.getText())){
						//初始化盈利回头
						mnlr_return = mnlr + Double.parseDouble(HotClFrame.returnField.getText());
						zslr_return = zslr + Double.parseDouble(HotClFrame.returnField.getText());*/
					//}
				}else
					//挂的网上倍投
					if(clList.get(i).get("cl")!=null)btNumList.set(i, btNumList.get(i)+1);
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
		
		Boolean downSulFlag = false;
		//Integer changeNum = Integer.parseInt(HotClFrame.changeYlField.getText());
		for (int i = clList.size()-1; i >= 0; i--) {
			//中N期更换
			//if(sulCountList.get(i).equals(changeNum)||clList.get(i).get("cl")==null){
				rfreshTXFFCL(i);
				sulCountList.set(i,0);
			//}
		}
		//判断是否需要投注，实战且开启真实投注
		if(HotClFrame.trueDownFlagField.isSelected()){
			List<HashMap<String, String>> clList_tru = new ArrayList<>();
			List<Integer> btNumList_tru = new ArrayList<>();
			//筛选出需要投注的策略
			for (int i = 0; i < mnOrSzList.size(); i++) {
				if(mnOrSzList.get(i)){
					clList_tru.add(clList.get(i));
					btNumList_tru.add(btNumList.get(i));
				}
			}
			if(clList_tru.size()>0){
				//格式化奖期
				String issue = ExampleControll.nextFFCRound;
				issue = issue.substring(0,8)+"-"+issue.substring(8,12);
				downSulFlag = ModHttpUtil.addTXFFCOrders_DWD1(issue, clList_tru, btNumList_tru, btArr, baseMoney);
			}else
				downSulFlag = true;
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
		Integer clNum = 0;
		for (int i = clList.size()-1; i>=0; i--) {
			HashMap<String, String> clItem = clList.get(i);
			if(clItem.get("cl")!=null&&_index == 0)
				HotClFrame.tableDefaultmodel.insertRow(0, new String[]{"--","--","--","--","--","--","--","--","--"});
				
			if(clItem.get("cl")!=null){
				clNum++;
				_index++;
				HotClFrame.tableDefaultmodel.insertRow(0, new String[]{ExampleControll.nextFFCRound,clItem.get("position")+clItem.get("cl"),btArr[btNumList.get(i)].toString(),"--","--","--","待开奖","待开奖",mnOrSzList.get(i)?"真 实-投注":"模拟-投注"});
			}
		}
		if(clNum==0){
			HotClFrame.logTableDefaultmodel.insertRow(0, new String[]{ExampleControll.nextFFCRound+"期无投注策略！"});
		}
	}
	
	//切换盈利
	public static void changeDownType() {
		if(HotClFrame.downTypeMn.isSelected()){
			//当前为模拟则改为实战
			DelPreThreeThread.mnOrSzFlag = 1;
			HotClFrame.downTypeSz.setSelected(true);
		}else if(HotClFrame.downTypeSz.isSelected()){
			//当前为实战则改为模拟
			DelPreThreeThread.mnOrSzFlag = 0;
			HotClFrame.downTypeMn.setSelected(true);
		}
		zslr_swhich = 0d;
	}
	
	public static void main(String[] args) {
		/*String round = "181205-1363,26290;181205-1362,15581;181205-1361,56074;181205-1360,25586;181205-1359,45834;181205-1358,24056;181205-1357,75038;181205-1356,97744;181205-1355,20852;181205-1354,77895;181205-1353,42406;181205-1352,98001;181205-1351,04250;181205-1350,59136;181205-1349,94690;181205-1348,40005;181205-1347,10858;181205-1346,66738;181205-1345,99593;181205-1344,68887;181205-1343,59987";
		getTXFFCL(round, "0", 7, true);*/
		System.err.println((1*100d/(1+2)));
	}
	/**
	 * @Title: initEvTime  
	 * @Description:初始化投注时间 
	 * @author: Ason      
	 * @return: void      
	 * @throws
	 */
	public static void initEvTime(){
		//判断是否需要限定投注次数
		if(ZLinkStringUtils.isNotEmpty(HotClFrame.evTimeField.getText())){
			DelPreThreeThread.evTimeList = new ArrayList<>();
			//初始化N次投注时间
			Integer evTime = Integer.parseInt(HotClFrame.evTimeField.getText());
			for (int i = 0; i < evTime; i++) 
				DelPreThreeThread.evTimeList.add(ExampleControll.initDownTime());
			HotClFrame.logTableDefaultmodel.insertRow(0, new String[]{"("+(new Date())+")"+"投注时间初始化为:"+DelPreThreeThread.evTimeList.toString()});
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			//初始化当前日期
			nowDateStr = format.format(new Date());
		}
	}
	
	/**
	 * @Title: addDownLog  
	 * @Description:添加日志 
	 * @author: Ason      
	 * @return: void      
	 * @throws
	 */
	public static void addDownLog(){
		//获取当前路径
		File file = new File(System.getProperty("user.dir")+"\\"+"downLog.txt");
		try {
			if(!file.exists())
				//创建文件
				file.createNewFile();
			StringBuffer log = new StringBuffer();
			for (int i = 0; i < 5; i++) {
				for (int j = 0; j < 9; j++) {
					if(j!=5)
						log.append(HotClFrame.tableDefaultmodel.getValueAt(i, j)+"  ");
					else log.append("_"+HotClFrame.tableDefaultmodel.getValueAt(i, j)+"  ");
				}
				log.append("\r\n");
			}
			FileWriter fw = new FileWriter(file, true);
			PrintWriter pw = new PrintWriter(fw);
			pw.println(log.toString());
			pw.flush();
			
			fw.flush();
			pw.close();
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
