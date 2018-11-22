package com.as.boot.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.as.boot.frame.AnyThreeFrame;
import com.as.boot.frame.AnyThreeFrame5;
import com.as.boot.frame.LoginFrame;
<<<<<<< HEAD
import com.as.boot.thread.AnyThreeThread;
import com.as.boot.thread.KjThread;
import com.as.boot.utils.ModHttpUtil;
=======
>>>>>>> 90ade01be4fb5b89b4832ef9eca08eefbfc4a44c

public class ExampleControll{
	
	public static String FFCRound = null;
	public static String FFCResult = null;
	public static String nextFFCRound = null;
	
	public static void main(String[] args) {
<<<<<<< HEAD
		LoginFrame.loginFrame.setVisible(true);
		/*List<HashMap<String, String>> clList = new ArrayList<>();
		HashMap<String, String> map = new HashMap<>();
		map.put("position", "123");
		map.put("cl", "[123]");
		List<Integer> btNumList = new ArrayList<>();
		btNumList.add(0);
		Integer[] btArr = {1,2,4};
		clList.add(map);
		ModHttpUtil.addTXFFCOrder_RX3("20181121-1278", clList, btNumList, btArr, 0.002);*/
		//AnyThreeFrame.anythreeFrame.setVisible(true);
=======
		//LoginFrame.loginFrame.setVisible(true);
		
		AnyThreeFrame.anythreeFrame.setVisible(true);
		
>>>>>>> 90ade01be4fb5b89b4832ef9eca08eefbfc4a44c
		
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
}
