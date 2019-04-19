package com.as.boot.txffc.frame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.as.boot.common.AccountThread;
import com.as.boot.mdpk10.frame.HotClFrame_mdpk;
import com.as.boot.txffc.controller.ExampleControll;
import com.as.boot.txffc.thread.DelPreThreeThread;
import com.as.boot.txffc.thread.KjThread;
import com.as.boot.utils.ModHttpUtil;
import com.as.boot.utils.ZLinkStringUtils;

/**
 * 登录界面
 * <p>Title:LoginFrame</p>
 * @author Ason
 * @version 1.0
 */
public class LoginFrame extends JFrame{

	public static JTextField accountField = new JTextField(20);
	
	public static JPasswordField passField = new JPasswordField(20);
	
	public static JTextField urlMField = new JTextField(20);
	
	public static JFrame loginFrame = new LoginFrame();
	public LoginFrame(){
		this.setTitle("mod_game");
		this.setBounds(200, 200, 200, 200);
		this.setSize(910, 600);
		this.setLocation(200, 50);
		this.setBackground(Color.white);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		panel.setSize(910, 600);
		this.add(panel);
		
		JPanel panel_top = new JPanel();
		panel_top.setPreferredSize(new Dimension(900,150));
		panel.add(panel_top);
		
		JPanel accountPanel = new JPanel();
		accountPanel.setPreferredSize(new Dimension(880,35));
		JLabel accountLable = new JLabel("帐号:");
		accountPanel.add(accountLable);
		accountPanel.add(accountField);
		//accountField.setText("zzn1280388052");
		panel.add(accountPanel);
		
		
		JPanel passPanel = new JPanel();
		passPanel.setPreferredSize(new Dimension(880,35));
		JLabel passLable = new JLabel("密码:");
		passPanel.add(passLable);
		//passField.setText("qi951102");
		passPanel.add(passField);
		panel.add(passPanel);
		
		JPanel urlMPanel = new JPanel();
		urlMPanel.setPreferredSize(new Dimension(880,35));
		JLabel urlMLable = new JLabel("线路:");
		urlMPanel.add(urlMLable);
		urlMPanel.add(urlMField);
		panel.add(urlMPanel);
		
		JPanel btnPanel = new JPanel();
		btnPanel.setPreferredSize(new Dimension(880,35));
		JButton buttom = new JButton("登录");
		btnPanel.add(buttom);
		JButton buttom_m = new JButton("不登陆");
		btnPanel.add(buttom_m);
		
		panel.add(btnPanel);
		buttom.addMouseListener(new MouseAdapter() {
			
  			@Override
  		    public void mouseClicked(MouseEvent arg0){
  				char[] values = passField.getPassword();
  				String password = new String(values);
  				ExampleControll.addIniItem("account", accountField.getText());
  				ExampleControll.addIniItem("password", password);
  				ExampleControll.addIniItem("urlM", urlMField.getText());
  				ModHttpUtil.urlM = urlMField.getText();
  				ModHttpUtil.initUrl();
  				//记录密码，用于登录失效后重新登录
  				AccountThread.accountPass = password;
  				if(ModHttpUtil.logind(accountField.getText(), password)){
  					//启动线程获取账户信息
  					AccountThread account = new AccountThread();
  					Thread accountThread = new Thread(account);
  					accountThread.start();
  					
  					LoginFrame.loginFrame.setVisible(false);
  					//AnyThreeFrame.anythreeFrame.setVisible(true);  //任三
  					HotClFrame.hotClFrame.setVisible(true);
  					//启动线程
  					KjThread kjThread = new KjThread();
  					Thread threadKJ = new Thread(kjThread);
  					threadKJ.start();
  					
  					/*AnyThreeThread anythreeThread = new AnyThreeThread();//任三
  					*/
  					//初始化历史20期数据
  					DelPreThreeThread.preResultList = ModHttpUtil.getHistoryIssue(20, ModHttpUtil.modHistoryUrl);
  					//DelPreThreeThread anythreeThread = new DelPreThreeThread();
  					DelPreThreeThread anythreeThread = new DelPreThreeThread();
  					
  					//HotDelPreTwo_D_ClThread anythreeThread = new HotDelPreTwo_D_ClThread();
  					//DelPreThreeThread anythreeThread = new DelPreThreeThread();
  					Thread anythreeResult = new Thread(anythreeThread);
  					anythreeResult.start();
  					HotClFrame.logTableDefaultmodel.insertRow(0, new String[]{"("+(new Date())+")"+"登录成功！"});
  				}
  		    }
		});
		
		//不登陆
		buttom_m.addMouseListener(new MouseAdapter() {
			
  			@Override
  		    public void mouseClicked(MouseEvent arg0){
  					LoginFrame.loginFrame.setVisible(false);
  					//AnyThreeFrame.anythreeFrame.setVisible(true);
  					HotClFrame.hotClFrame.setVisible(true);
  					//启动线程
  					KjThread kjThread = new KjThread();
  					Thread threadKJ = new Thread(kjThread);
  					threadKJ.start();
  					
  					DelPreThreeThread.preResultList = ModHttpUtil.getHistoryIssue(20, ModHttpUtil.modHistoryUrl);
  					/*AnyThreeThread anythreeThread = new AnyThreeThread();
  					*/
  					//HotDelPreTwo_D_ClThread anythreeThread = new HotDelPreTwo_D_ClThread();
  					//HotDelPreTwoClThread anythreeThread = new HotDelPreTwoClThread();
  					DelPreThreeThread anythreeThread = new DelPreThreeThread();
  					Thread anythreeResult = new Thread(anythreeThread);
  					anythreeResult.start();
  					HotClFrame.logTableDefaultmodel.insertRow(0, new String[]{"("+(new Date())+")"+"登录成功！"});
  		    }
		});
	}
}
