package com.as.boot.txffc.thread;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;

import com.as.boot.txffc.controller.ExampleControll;
import com.as.boot.txffc.frame.PreRsultClFrame;
import com.as.boot.utils.ZLinkStringUtils;

public class PreResultClThread2 implements Runnable{
	
	private String clStr = null;
	private Double zslr = 0d;
	private Double mnlr = 0d;
	private Double maxFailValue = 0d;
	private Double maxLr = 0d;
	private String[] fa = {"个位","十位","百位","千位","万位"};
	private DecimalFormat df = new DecimalFormat("#.000");
	@Override
	public void run() {
		while (true) {
			try {
					String resultRound = ExampleControll.FFCRound;
					String resultKj = ExampleControll.FFCResult;
					if(ZLinkStringUtils.isNotEmpty(resultKj)){
						String[] kjArray = resultKj.split(",");
						Double resultRound_i = Double.parseDouble(resultRound);
						if(PreRsultClFrame.FFCRound == null || !PreRsultClFrame.FFCRound.equals(resultRound)){
							PreRsultClFrame.kjTableDefaultmodel.insertRow(0, new String[]{resultRound,resultKj});
							//判断是否有投注
							if(PreRsultClFrame.FFCRound !=null && Double.parseDouble(PreRsultClFrame.FFCRound) == (resultRound_i-1)){
								if(clStr!=null){
									Double tempLr = null;
									//判断中奖情况
									for (int i = 0; i < 5; i++) {
										//初始化投注金额
										if(tempLr == null)
											tempLr = clStr.length() * -5.000;
										
										if(clStr.contains(kjArray[i]+"")){
											PreRsultClFrame.tableDefaultmodel.setValueAt("中", i, 7);
											//计算利润
											tempLr += 9.95;
											PreRsultClFrame.tableDefaultmodel.setValueAt("9.95", i, 3);
										}else{
											PreRsultClFrame.tableDefaultmodel.setValueAt("挂", i, 7);
											PreRsultClFrame.tableDefaultmodel.setValueAt(-clStr.length(), i, 3);
										}
										PreRsultClFrame.tableDefaultmodel.setValueAt(resultKj, i, 6);
									}
									zslr += tempLr;
									PreRsultClFrame.szYkValueLabel.setText(df.format(zslr));
									//计算最大利润及最大盈亏
									if(zslr>maxLr)
										maxLr = zslr;
									else if(maxLr>zslr){
										if((maxLr - zslr)>maxFailValue){
											maxFailValue = maxLr - zslr;
											PreRsultClFrame.maxFailValueLabel.setText(df.format(-maxFailValue));
										}
									}
								}
							}else{
								//断期
							}
							//是否投注
							if(PreRsultClFrame.button.getText().equals("停止执行")){
								clStr = "";
								//过滤掉重复值，生成策略
								Set<String> s = new HashSet<String>();
								
								for (int i = 0; i < kjArray.length; i++) {
									s.add(kjArray[i]+"");
								}
								for (String string : s) {
									if(string!=null&&string!=""&&string!=",")
										clStr += string;
								}
								resultRound_i += 1;
								for (int i = 0; i < 5; i++) {
									PreRsultClFrame.tableDefaultmodel.insertRow(0, new String[]{resultRound_i+"",fa[i]+"["+clStr+"]","1","--","--","--","待开奖","待开奖","--"});
								}
							}
							PreRsultClFrame.FFCRound = resultRound;
							PreRsultClFrame.FFCResult = resultKj;
							Thread.sleep(30000);//更新到数据后睡眠30s
							
						}else if(PreRsultClFrame.FFCRound.equals(resultRound)){
							Thread.sleep(1000);//未更新到数据睡眠3s
						}
					}else
						Thread.sleep(1000);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
