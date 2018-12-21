package com.as.boot.thread;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.as.boot.controller.ExampleControll;
import com.as.boot.frame.HotClFrame;
import com.as.boot.utils.ZLinkStringUtils;

public class HotDelPreTwo_l_ClThread implements Runnable{
	
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
	public static List<Integer> failCountList = Arrays.asList(0,0,0,0,0);
	//连中数
	public static List<Integer> sulCountList = Arrays.asList(0,0,0,0,0);
	//最大连挂
	private List<Integer> maxFailCountList = Arrays.asList(0,0,0,0,0);
	//倍投情况
	public static List<Integer> btNumList = Arrays.asList(0,0,0,0,0);
	//中奖情况
	private List<Boolean> zjFlagList = Arrays.asList(false,false,false,false,false);
	//倍投阶梯
	public static Integer[] btArr = null;
	
	public static DecimalFormat df = new DecimalFormat("#.000");
	public static DecimalFormat ddf = new DecimalFormat("#");
	//历史开奖
	public static List<String> preResultList = new ArrayList<String>();
	
	private Double pl = 19.5;
	//用于判断是模拟还是实战投注
	public static Integer mnOrSzFlag = 0;//默认模拟
	private static String[] moOrSzArr = {"模拟-投注","真 实-投注"};
	
	private static Integer sulAllCount = 0;
	private static Integer failAllCount = 0;
	private static Integer roundAllCount = 0;
	private static Double ylht = 0.01;
	//获取模拟连挂转换数
	private static Integer mnFailSwhich = 4;
	private static String FFCRound = null;
	private static String nextFFCRound = null;
	private static boolean sFlag = false;
	@Override
	public void run() {
		//初始化盈利回头
		zslr_return = zslr + ylht;
		mnlr_return = mnlr + ylht;
		//初始化倍投阶梯
		String[] btStrArr = "2,7,25,89,315,1117,3960".split(",");
		btArr = new Integer[btStrArr.length];
		for (int i = 0; i < btStrArr.length; i++)
			btArr[i] = Integer.parseInt(btStrArr[i]);
		//根据文件获取历史开奖记录
		String[] historyReuslt = historyResult().split(";");
		//初始化历史30期数据
		initPreResultList(historyReuslt);
		Integer his_index = historyReuslt.length-21;
		HotDelPreTwo_l_ClThread.initTXFFCL();
		
		while (true) {
			try {
				String[] tempKj = historyReuslt[his_index].split(",");
				String tempResult = "";
				for (int i = 0; i < tempKj[1].length(); i++) {
					tempResult += tempKj[1].charAt(i)+",";
				}
				if(roundAllCount>=43210)break;
				String resultRound = tempKj[0].replace("-", "");
				String resultKj = ZLinkStringUtils.removeLastStr(tempResult);
				if(resultRound.endsWith("1440")){
					//获取明日日期，当前时间+2小时
					Long nextDate = System.currentTimeMillis() + 60*60*1000;
					nextFFCRound = new SimpleDateFormat("yyyyMMdd").format(nextDate) + "0001";
				}else{
					//获取下一期
					Double num = Double.parseDouble(resultRound) + 1;
					nextFFCRound = ddf.format(num);
				}
				
				if(ZLinkStringUtils.isNotEmpty(resultKj)){
					btArr = new Integer[btStrArr.length];
					for (int i = 0; i < btStrArr.length; i++)
						btArr[i] = Integer.parseInt(btStrArr[i]);
					
					//将开奖结果取出为数组
					String[] kjArray = resultKj.split(",");
					Double resultRound_i = Double.parseDouble(resultRound);
					if(FFCRound == null || !FFCRound.equals(resultRound)){
						Integer failC = 0;
						//判断是否有投注
						if(FFCRound !=null && (Double.parseDouble(FFCRound) == (resultRound_i-1) || resultRound.endsWith("0001"))){
							if(!preResultList.get(preResultList.size()-1).equals(resultKj.replace(",", "")))
								preResultList.add(resultKj.replace(",", ""));
							if(preResultList.size()>30)
								preResultList.remove(0);
							
							//获取表格第一行（判断是否有未开奖投注）
							Object down_first = HotClFrame.tableDefaultmodel.getRowCount()>0?HotClFrame.tableDefaultmodel.getValueAt(0, 6):null;
							if(clList!=null&&clList.size()>0&&down_first!=null&&down_first.equals("待开奖")){
								roundAllCount++;
								Double tempLr = 0d;
								Integer tableIndex = 0;
								//判断已投注方案类型
								String mnOrSzStr = HotClFrame.tableDefaultmodel.getValueAt(0, 8).toString();
								Boolean boomFlag = false;
								//利润
								for (int i = 0, il = clList.size(); i<il;i++) {
									HashMap<String, String> clItem = clList.get(i);
									//key为0则代表无策略
									if(!clItem.get("position").equals("00")){
										//获取策略注数
										String[] clArr = clItem.get("cl").split(",");
										String key = clItem.get("position");
										Double downNum = -clArr.length * baseMoney *  btArr[btNumList.get(i)];
										//累计投入值
										tempLr += downNum;
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
											Double itemLr = (btArr[btNumList.get(i)] * baseMoney * pl)/2;
											tempLr += itemLr;
											HotClFrame.tableDefaultmodel.setValueAt("0/"+maxFailCountList.get(i), tableIndex, 4);
											HotClFrame.tableDefaultmodel.setValueAt(df.format(itemLr), tableIndex, 3);
											sulCountList.set(i, sulCountList.get(i) + 1);
											
											sulAllCount++;
											//中了都转为真实投注
											if(mnOrSzStr.equals("模拟-投注")){
												//模拟投注，只要连挂超过倍投则判定为爆仓
												HotClFrame.logTableDefaultmodel.insertRow(0, new String[]{"("+(new Date())+")"+"模拟投注中，转为真实投注"});
												//重置回头
												//mnlr_return = mnlr + Double.parseDouble(HotClFrame.returnField.getText());
												mnlr_return = mnlr;
												
												changeDownType();
											}else{
												//如果真实投注中了则初始化倍投阶梯
												initBtNumList();
												btNumList.set(i, 0);
											}
										}else{
											failC++;
											//真实投注挂了则切换为模拟投注，保留真实投注倍数上升
											if(mnOrSzStr.equals("真 实-投注")){
												btNumList.set(i, btNumList.get(i)+1);
												changeDownType();
											}
											zjFlagList.set(i, false);
											failCountList.set(i, failCountList.get(i) + 1);
											sulCountList.set(i, 0);
											if(failCountList.get(i)>maxFailCountList.get(i))
												maxFailCountList.set(i, failCountList.get(i));
											//记录连挂数
											HotClFrame.tableDefaultmodel.setValueAt(failCountList.get(i)+"/"+maxFailCountList.get(i), tableIndex, 4);
											HotClFrame.tableDefaultmodel.setValueAt("挂", tableIndex, 7);
											HotClFrame.tableDefaultmodel.setValueAt(df.format(downNum), tableIndex, 3);
											failAllCount++;
											
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
								/*if(ZLinkStringUtils.isNotEmpty(HotClFrame.ylSwhichField.getText())&&mnOrSzStr.equals("真 实-投注")){
									Double ylSwhich = Double.parseDouble(HotClFrame.ylSwhichField.getText());
									zslr_swhich += tempLr;
									HotClFrame.logTableDefaultmodel.insertRow(0, new String[]{"("+(new Date())+")"+"真实投注盈利转换积累值："+df.format(zslr_swhich)});
									//真实盈利达到盈利转换值
									if(zslr_swhich >= ylSwhich){
										HotClFrame.logTableDefaultmodel.insertRow(0, new String[]{"("+(new Date())+")"+"真实投注已达目标，切换为模拟投注，真实投注盈亏："+df.format(zslr_swhich)});
										//切换投注类型
										changeDownType();
									}
								}*/
								
								//如果设置了盈利转换需要判断是否爆仓
								/*if(ZLinkStringUtils.isNotEmpty(HotClFrame.ylSwhichField.getText())){
									for (int i = 0, il = clList.size(); i<il;i++) {
										if(clList.get(i).get("position").equals("00"))continue;
										//判断是否已经爆仓（达到倍投阶梯顶端）
										if(btNumList.get(i) >= (btArr.length-1)&&mnOrSzStr.equals("真 实-投注")){
											boomFlag = true;
											//如果当前为实战爆掉则需要清空盈利及回头及倍投转模拟
											if(mnOrSzStr.equals("真 实-投注")){
												HotClFrame.logTableDefaultmodel.insertRow(0, new String[]{"("+(new Date())+")"+"真实投注爆仓，转为模拟投注，损失金额："+df.format(zslr_swhich)});
												//重置回头
												//zslr_return = zslr + Double.parseDouble(HotClFrame.returnField.getText());
												zslr_return = zslr;
											}
											initBtNumList();
											changeDownType();
										
										}else if(failCountList.get(i) >= mnFailSwhich&&mnOrSzStr.equals("模拟-投注")){
											boomFlag = true;
											//模拟投注，只要连挂超过倍投则判定为爆仓
											HotClFrame.logTableDefaultmodel.insertRow(0, new String[]{"("+(new Date())+")"+"模拟投注连挂爆仓，转为真实投注"});
											//重置回头
											//mnlr_return = mnlr + Double.parseDouble(HotClFrame.returnField.getText());
											mnlr_return = mnlr;
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
								}else if(!boomFlag)
									addBtnNumList();*/
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
						//是否投注
						//if(HotClFrame.button.getText().equals("停止执行")){
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
							
						//}
						//判断是否已经跨天
						/*if(ExampleControll.nextFFCRound.endsWith("0001"))
							HotClFrame.FFCRound = ExampleControll.nextFFCRound.substring(0, 8) + "0000";*/
						FFCRound = resultRound;
						
						HotClFrame.roundCount.setText(roundAllCount+"期");
						Double pecent = (sulAllCount*100d/(sulAllCount+failAllCount));
						HotClFrame.sulPercent.setText(df.format(pecent)+"%");
						//Thread.sleep(30000);//更新到数据后睡眠30s
						
					}else if(FFCRound.equals(resultRound)){
						//Thread.sleep(1000);//未更新到数据睡眠3s
					}
				}/*else
					Thread.sleep(1000);*/
				his_index--;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	//初始化策略
	public static void initTXFFCL(){
		List<HashMap<String, String>> temClList = new ArrayList<HashMap<String,String>>();
		temClList.add(null);temClList.add(null);temClList.add(null);temClList.add(null);temClList.add(null);
		HashMap<String, String> tempClMap = null;
		String clname = "";
		try {
			HashMap<String, String> clParams = null;
			for (int i = 0, il = HotClFrame.clBoxList.size(); i < il; i++) {
				tempClMap = new HashMap<String, String>();
				if(HotClFrame.clBoxList.get(i).isSelected()){
					/*clname += _index-1;
					clPosition += (_index-1)+",";*/
					//获取投注位置
					clname = HotClFrame.clBoxList.get(i).getText();
					tempClMap.put("position", clname);
					clParams = getTXFFCL(clname, 7);
					tempClMap.put("cl", clParams.get("cl"));
					HotClFrame.logTableDefaultmodel.insertRow(0, new String[]{"("+(new Date())+")"+clname+"策略初始化成功，注数："+clParams.get("count")});
				}else
					tempClMap.put("position", "00");//未选中则标记为空策略
				temClList.set(i, tempClMap);
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
		Integer historyNum = Integer.parseInt(HotClFrame.historyNumField.getText());
		Integer l = historyNum*18-1;
		if(historyRound.length() > l)
			historyRound = historyRound.substring(0, l);
		//策略数
		Integer clNum = Integer.parseInt(HotClFrame.clNumField.getText());
		//是否去重码
		Boolean delPreRsult = HotClFrame.delPreResultCheckbox.isSelected();
		HashMap<String, String> tempClMap = null;
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
						clParams = getTXFFCL(clname, clNum);
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
	
	public static HashMap<String, String> getTXFFCL(String putPosition, Integer clNum){
		HashMap<String, String> params = new HashMap<String, String>();
		//解析需要投注的位置
		Integer putPosition_i = Integer.parseInt(putPosition);
		//取出开奖位置上的开奖号
		List<Integer> listResult = new ArrayList<Integer>();
		//循环
		for (int i = preResultList.size()-1; i >= 0 ; i--) {
			//获取开奖
			listResult.add(Integer.parseInt(preResultList.get(i).charAt(putPosition_i)+""));
		}
		//获取最新两个开奖的开奖结果
		List<Integer> newReuslt = new ArrayList<Integer>();
		for (int i = preResultList.size()-1; i >= 0 ; i--) {
			Integer itemResult = Integer.parseInt(preResultList.get(i).charAt(putPosition_i)+"");
			if(newReuslt.size()<2&&!newReuslt.contains(itemResult))
				newReuslt.add(itemResult);
			else if(newReuslt.size()>=2)break;
		}
		/*if(ZLinkStringUtils.isNotEmpty(ExampleControll.FFCResult))
			newReuslt = Integer.parseInt(ExampleControll.FFCResult.replace(",", "").charAt(putPosition_i)+"");//listResult.get(0);
		else newReuslt = listResult.get(0);*/
		Integer[] itemcount = new Integer[]{0,0,0,0,0,0,0,0,0,0};
		//统计出现次数最多的
		for (Integer i : listResult)
			itemcount[i] = itemcount[i]+1; 
		
		List<HashMap<String, Integer>> tempClList = new ArrayList<HashMap<String, Integer>>();
		//用于防止重复
		List<Integer> temClList_i = new ArrayList<Integer>();
		HashMap<String, Integer> tempItem  = null;
		for (int i = 10; i >= 0; i--) {
			for (int j = 0; j < itemcount.length; j++) {
				if(itemcount[j] == i)
					if(!temClList_i.contains(j)){
						if(!newReuslt.contains(j)){
							tempItem = new HashMap<String, Integer>();
							tempItem.put("count", itemcount[j]);
							tempItem.put("position", j);
							tempClList.add(tempItem);
							//添加进list防止重复
							temClList_i.add(j);
						}
						
					}
			}
			if(temClList_i.size()>=clNum)break;//已达到标准
		}
		//初步结果获取的策略大于目标策略数，则需要进入筛选
		if(tempClList.size()>clNum){
			List<Integer> needSx = new ArrayList<Integer>();
			Integer sxCount = tempClList.get(tempClList.size()-1).get("count");
			//将最后几个相同出现次数的取出并判断谁先出现
			for (int i = tempClList.size()-1; i >= 0; i--) {
				tempItem = tempClList.get(i);
				if(tempItem.get("count").equals(sxCount))
					needSx.add(tempItem.get("position"));
				else break;
			}
			Integer needSxCount = clNum - (tempClList.size() - needSx.size());
			List<Integer> sxSulList = new ArrayList<Integer>();
			for (int i = 0; i < listResult.size(); i++) {
				for (int j = 0; j < needSx.size(); j++) {
					if(listResult.get(i).equals(needSx.get(j))&&!sxSulList.contains(needSx.get(j)))
						sxSulList.add(needSx.get(j));
					if(needSxCount.equals(sxSulList.size()))break;
				}
				if(needSxCount.equals(sxSulList.size()))break;
			}
			
			//重新组装数据
			temClList_i = new ArrayList<Integer>();
			for (int i = 0; i < tempClList.size() - needSx.size(); i++) 
				temClList_i.add(tempClList.get(i).get("position"));
			
			temClList_i.addAll(sxSulList);
			//当有多个0次出现的号码时可能会导致策略出号不足，默认添加基哥出现次数为0的
			if(temClList_i.size()<clNum){
				for (int i = 0; i < needSx.size(); i++) {
					if(temClList_i.size()<clNum){
						temClList_i.add(needSx.get(i));
					}else break;
				}
			}
		}
		
		params.put("cl", temClList_i.toString());
		params.put("count", clNum.toString());
		return params;
	}
	
	public static List<String> createInitFFCCL_1(Integer initClNum){
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < initClNum; i++) {
			String item = null;
			item = createRandom()+"";
			while (true) {
				if(list.contains(item))
					item = createRandom()+"";
				else{
					list.add(item);
					break;
				}
			}
		}
		return list;
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
	
	
	public static void initPreResultList(String[] historyReuslt){
		//循环
		for (int i = historyReuslt.length-1; i>= 0 ; i--) {
			//获取开奖
			preResultList.add(historyReuslt[i].trim().split(",")[1].trim());
			if(preResultList.size()==20)break;
		}
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
					HotClFrame.logTableDefaultmodel.insertRow(0, new String[]{"("+(new Date())+")"+"策略"+i+"爆仓！！"});
					//当未设置盈利转换时也需要校验爆仓及初始化盈利回头
					if(ZLinkStringUtils.isEmpty(HotClFrame.ylSwhichField.getText())){
						//初始化盈利回头
						mnlr_return = mnlr + Double.parseDouble(HotClFrame.returnField.getText());
						zslr_return = zslr + Double.parseDouble(HotClFrame.returnField.getText());
					}
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
	public static void startDownFFC(){
		
		Boolean downSulFlag = false;
		Integer changeNum = Integer.parseInt(HotClFrame.changeYlField.getText());
		for (int i = clList.size()-1; i>=0; i--) {
			//中N期更换
			if(sulCountList.get(i).equals(changeNum)){
				rfreshTXFFCL(i);
				sulCountList.set(i,0);
			}
		}
		//判断是否需要投注，实战且开启真实投注
		if(HotClFrame.downTypeSz.isSelected()&&HotClFrame.trueDownFlagField.isSelected()){
			//格式化奖期
			//String issue = ExampleControll.nextFFCRound;
			//issue = issue.substring(0,8)+"-"+issue.substring(8,12);
			//downSulFlag = ModHttpUtil.addTXFFCOrder_RX3(issue, clList, btNumList, btArr, baseMoney);
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
			if(!clItem.get("position").equals("00"))
				HotClFrame.tableDefaultmodel.insertRow(0, new String[]{nextFFCRound,clItem.get("position")+clItem.get("cl"),btArr[btNumList.get(i)].toString(),"--","--","--","待开奖","待开奖",moOrSzArr[mnOrSzFlag]});
			_index++;
		}
	}
	
	//切换盈利
	public static void changeDownType() {
		if(HotClFrame.downTypeMn.isSelected()){
			//当前为模拟则改为实战
			HotDelPreTwo_l_ClThread.mnOrSzFlag = 1;
			HotClFrame.downTypeSz.setSelected(true);
		}else if(HotClFrame.downTypeSz.isSelected()){
			//当前为实战则改为模拟
			HotDelPreTwo_l_ClThread.mnOrSzFlag = 0;
			HotClFrame.downTypeMn.setSelected(true);
		}
		zslr_swhich = 0d;
	}
	
	public static void main(String[] args) {
		/*String round = "181205-1363,26290;181205-1362,15581;181205-1361,56074;181205-1360,25586;181205-1359,45834;181205-1358,24056;181205-1357,75038;181205-1356,97744;181205-1355,20852;181205-1354,77895;181205-1353,42406;181205-1352,98001;181205-1351,04250;181205-1350,59136;181205-1349,94690;181205-1348,40005;181205-1347,10858;181205-1346,66738;181205-1345,99593;181205-1344,68887;181205-1343,59987";
		getTXFFCL(round, "0", 7, true);*/
		System.err.println((1*100d/(1+2)));
	}
}
