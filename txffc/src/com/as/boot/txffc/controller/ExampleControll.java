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
			LoginFrame.accountField.setText(ini_config.getItem("account").getValue());
			LoginFrame.passField.setText(ini_config.getItem("password").getValue());
		}
		LoginFrame.loginFrame.setVisible(true);
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
