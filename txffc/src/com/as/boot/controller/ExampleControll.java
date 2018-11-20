package com.as.boot.controller;

import com.as.boot.frame.AnyThreeFrame;
import com.as.boot.frame.AnyThreeFrame5;
import com.as.boot.thread.AnyThreeThread;
import com.as.boot.thread.AnyThreeThread5;
import com.as.boot.thread.KjThread;

public class ExampleControll{
	
	public static String FFCRound = null;
	public static String FFCResult = null;
	
	public static void main(String[] args) {
		/*JFrame mainFrame = new PreRsultClFrame();
		mainFrame.setVisible(true);*/
		
		AnyThreeFrame.anythreeFrame.setVisible(true);
		
		
		AnyThreeFrame5.anythreeFrame5.setVisible(true);
		
		//开启开奖结果获取进程
		KjThread kjThread = new KjThread();
		Thread threadKJ = new Thread(kjThread);
		threadKJ.start();
		
		//开启 出啥投啥的方案跑数
		/*PreResultClThread preResultThread = new PreResultClThread();
		Thread threadPreResult = new Thread(preResultThread);
		threadPreResult.start();*/
		
		//三星任选策略,赚N块钱换号
		AnyThreeThread anythreeThread = new AnyThreeThread();
		Thread anythreeResult = new Thread(anythreeThread);
		anythreeResult.start();
		
		//三星任选策略,中n期换号
		AnyThreeThread5 anythreeThread5 = new AnyThreeThread5();
		Thread anythreeResult5 = new Thread(anythreeThread5);
		anythreeResult5.start();
	}
}
