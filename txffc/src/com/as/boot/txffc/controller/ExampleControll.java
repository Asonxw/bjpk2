package com.as.boot.txffc.controller;

import java.io.File;

import org.dtools.ini.BasicIniFile;
import org.dtools.ini.BasicIniSection;
import org.dtools.ini.IniFile;
import org.dtools.ini.IniFileReader;
import org.dtools.ini.IniFileWriter;
import org.dtools.ini.IniItem;
import org.dtools.ini.IniSection;

import com.as.boot.txffc.frame.LoginFrame;
import com.as.boot.utils.ZLinkStringUtils;

public class ExampleControll{
	
	public static String FFCRound = null;
	public static String FFCResult = null;
	public static String nextFFCRound = null;
	public static IniFile iniFile = new BasicIniFile();
	public static IniFileReader ini_read = null;
	public static IniFileWriter ini_write = null;
	public static IniSection ini_config = null;
	public static void main(String[] args) {
		
		initParams();
		if(ini_config.getItem("account")!=null){
			LoginFrame.accountField.setText(ini_config.getItem("account")==null?"":ini_config.getItem("account").getValue());
			LoginFrame.passField.setText(ini_config.getItem("password")==null?"":ini_config.getItem("password").getValue());
			LoginFrame.urlMField.setText(ini_config.getItem("urlM")==null?"":ini_config.getItem("urlM").getValue());
			LoginFrame.openFile.setText(ini_config.getItem("modOpenFile")==null?"":ini_config.getItem("modOpenFile").getValue());
		}
		LoginFrame.loginFrame.setVisible(true);
		//HotClFrame.hotClFrame.setVisible(true);
		/*List<HashMap<String, String>> clList = new ArrayList<>();
		HashMap<String, String> map = new HashMap<>();
		map.put("position", "123");
		map.put("cl", "[123]");
		List<Integer> btNumList = new ArrayList<>();
		btNumList.add(0);
		btNumList.add(0);
		Integer[] btArr = {1,2,4};
		HashMap<String, String> map2 = new HashMap<>();
		map2.put("position", "024");
		map2.put("cl", "[234]");
		clList.add(map);
		clList.add(map2);
		ModHttpUtil.addTXFFCOrder_RX3("20181202-0019", clList, btNumList, btArr, 0.002);*/
		//AnyThreeFrame.anythreeFrame.setVisible(true);
		//LoginFrame.loginFrame.setVisible(true);
		
		
		//AnyThreeFrame5.anythreeFrame5.setVisible(true);
		
		//开启开奖结果获取进程
		/*KjThread kjThread = new KjThread();
		Thread threadKJ = new Thread(kjThread);
		threadKJ.start();*/
		
		//开启 出啥投啥的方案跑数
		/*PreResultClThread preResultThread = new PreResultClThread();
		Thread threadPreResult = new Thread(preResultThread);
		threadPreResult.start();*/
		
		//三星任选策略,赚N块钱换号
		/*AnyThreeThread anythreeThread = new AnyThreeThread();
		Thread anythreeResult = new Thread(anythreeThread);
		anythreeResult.start();*/
		
		//三星任选策略,中n期换号
		/*AnyThreeThread5 anythreeThread5 = new AnyThreeThread5();
		Thread anythreeResult5 = new Thread(anythreeThread5);
		anythreeResult5.start();*/
	}
	
	//初始化配置文件
	public static void initParams() {
		//获取当前路径
		File file = new File(System.getProperty("user.dir")+"\\"+"config.ini");
		try {
			if(!file.exists()){
				//创建文件
				file.createNewFile();
				ini_read = new IniFileReader(iniFile, file);
				ini_write = new IniFileWriter(iniFile, file);
				//创建一个数据Section，在本例中Section名为 config
				ini_config = new BasicIniSection( "CONFIG" );
		        iniFile.addSection(ini_config);
		        ini_write.write();
			}else{
				//初始化配置文件
				ini_read = new IniFileReader(iniFile, file);
				ini_write = new IniFileWriter(iniFile, file);
				ini_read.read();
				ini_config = iniFile.getSection("CONFIG");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void addIniItem(String name, String value){
		IniItem item = ini_config.getItem(name);
		try {
			if(item != null){
				item.setValue(value);
			}else{
				item = new IniItem(name);
				item.setValue(value);
			}
			ini_config.addItem(item);
			iniFile.addSection(ini_config);
			ini_write.write();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static IniItem getIniItem(String name){
		return ini_config.getItem(name);
	}
	
	/**
	 * @Title: initDownTime  
	 * @Description: 初始化开启真实投注时间
	 * 算法为生成24内的随机小时，生成6内的随机分钟十位，比如02:2、03:1等
	 * @author: Ason
	 * @return      
	 * @return: String      
	 * @throws
	 */
	public static String initDownTime(){
		Integer t = ZLinkStringUtils.getRandomInt(100, 0, 23);
		String t_str = t<10?("0"+t):t.toString();
		return t_str+":"+ZLinkStringUtils.getRandomInt(100, 0, 5);
	}
	
}
