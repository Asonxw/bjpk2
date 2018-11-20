package com.as.boot.frame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.alibaba.fastjson.JSONObject;
import com.as.boot.thread.AccountThread;
import com.as.boot.utils.HttpFuncUtil;
import com.as.boot.utils.ZLinkStringUtils;

/**
 * 登录界面
 * <p>Title:LoginFrame</p>
 * @author Ason
 * @version 1.0
 */
public class LoginFrame extends JFrame{

	private static JTextField accountField = new JTextField(20);
	
	private static JPasswordField passField = new JPasswordField(20);
	
	public static JFrame loginFrame = new LoginFrame();
	public LoginFrame(){
		this.setTitle("任三策略-盈利N元换号");
		this.setBounds(200, 200, 200, 200);
		this.setSize(910, 600);
		this.setLocation(200, 50);
		this.setBackground(Color.white);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		panel.setSize(910, 600);
		this.add(panel);
		
		JPanel accountPanel = new JPanel();
		accountPanel.setPreferredSize(new Dimension(880,35));
		JLabel accountLable = new JLabel("帐号:");
		accountPanel.add(accountLable);
		accountPanel.add(accountField);
		accountField.setText("ason_x");
		panel.add(accountPanel);
		
		
		JPanel passPanel = new JPanel();
		passPanel.setPreferredSize(new Dimension(880,35));
		JLabel passLable = new JLabel("密码:");
		passPanel.add(passLable);
		passField.setText("9c3db9e8694b4dec98f60858ea8b8d67");
		passPanel.add(passField);
		panel.add(passPanel);
		
		JPanel btnPanel = new JPanel();
		btnPanel.setPreferredSize(new Dimension(880,35));
		JButton buttom = new JButton("登录");
		btnPanel.add(buttom);
		panel.add(btnPanel);
		buttom.addMouseListener(new MouseAdapter() {
			
  			@Override
  		    public void mouseClicked(MouseEvent arg0){
  				char[] values = passField.getPassword();
  				String password = new String(values);
  				HashMap<String, String> resultMap = HttpFuncUtil.getUrlConnection("https://www.modgame.vip/sso/login?callback=jsonp1&way=pwd&from=portal&cn="+accountField.getText()+"&appId=5&password="+password);
  				if(resultMap!=null&&ZLinkStringUtils.isNotEmpty(resultMap.get("result"))){
  					JSONObject obj = JSONObject.parseObject(resultMap.get("result").replace("jsonp1(", "").replace(")", ""));
  					if(obj.getInteger("code").equals(0)&&obj.getString("msg").equals("登录成功")){
  						AccountThread.urlSessionId = resultMap.get("sessionId");
  						//启动线程获取账户信息
  						AccountThread account = new AccountThread();
  						Thread accountThread = new Thread(account);
  						accountThread.start();
  						loginFrame.setVisible(false);
  						AnyThreeFrame5.anythreeFrame5.setVisible(true);
  					}
  					
  				}
  		    }
		});
	}
}
