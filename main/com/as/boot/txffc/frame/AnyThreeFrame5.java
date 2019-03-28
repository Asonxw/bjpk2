package com.as.boot.txffc.frame;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TextArea;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class AnyThreeFrame5 extends JFrame{

	public static String FFCRound = null;
	public static String FFCResult = null;
	
	public static JLabel accountNameLabel = new JLabel("");
	public static JLabel accountAmountLabel = new JLabel("0.00");
	
	//开奖情况
	public static String[][] kjTableDate = {};
	public static String[] kjTataTitle = {"期数","开奖结果"};
	public static DefaultTableModel kjTableDefaultmodel = new DefaultTableModel(kjTableDate,kjTataTitle);
	
	//投注情况
	public static String[][] tableDate = {};
	public static String[] dataTitle = {"期数","方案","倍数","方案盈亏","连挂","已命中期数","开奖情况","中挂","投注类型"};
	public static DefaultTableModel tableDefaultmodel = new DefaultTableModel(tableDate,dataTitle);
	public static JTextField initClNumField = new JTextField(8);
	
	/**目标最大连错**/
	public static JTextField aimMaxFailField = new JTextField(8);
	/**策略获取重置次数**/
	public static JTextField maxRestNField = new JTextField(8);
	/**目标策略注数**/
    public static JTextField clNumField = new JTextField(8);
    /**统计期数**/
	public static JTextField historyNumField = new JTextField(8);
	/**方案一**/
	public static JCheckBox w1 = new JCheckBox("万");
	public static JCheckBox q1 = new JCheckBox("千");
	public static JCheckBox b1 = new JCheckBox("百");
	public static JCheckBox s1 = new JCheckBox("十");
	public static JCheckBox g1 = new JCheckBox("个");
	/**方案二**/
    public static JCheckBox w2 = new JCheckBox("万");
    public static JCheckBox q2 = new JCheckBox("千");
    public static JCheckBox b2 = new JCheckBox("百");
    public static JCheckBox s2 = new JCheckBox("十");
    public static JCheckBox g2 = new JCheckBox("个");
    /**方案三**/
    public static JCheckBox w3 = new JCheckBox("万");
    public static JCheckBox q3 = new JCheckBox("千");
    public static JCheckBox b3 = new JCheckBox("百");
    public static JCheckBox s3 = new JCheckBox("十");
    public static JCheckBox g3 = new JCheckBox("个");
    /**方案四**/
    public static JCheckBox w4 = new JCheckBox("万");
    public static JCheckBox q4 = new JCheckBox("千");
    public static JCheckBox b4 = new JCheckBox("百");
    public static JCheckBox s4 = new JCheckBox("十");
    public static JCheckBox g4 = new JCheckBox("个");
    /**方案五**/
    public static JCheckBox w5 = new JCheckBox("万");
    public static JCheckBox q5 = new JCheckBox("千");
    public static JCheckBox b5 = new JCheckBox("百");
    public static JCheckBox s5 = new JCheckBox("十");
    public static JCheckBox g5 = new JCheckBox("个");
    
    public static List<JCheckBox> clBoxList = null; 
    /**盈利回头值**/
	public static JTextField returnField = new JTextField(8);
	/**止盈值**/
	public static JTextField winStopField = new JTextField(8);
	/**止损值**/
	public static JTextField failStopField = new JTextField(8);
	/**下注模式-模拟下注**/
    public static JRadioButton downTypeMn = new JRadioButton("模拟");
    /**下注模式-真实下注**/
    public static JRadioButton downTypeSz = new JRadioButton("实战");
    /**倍投阶梯**/
	public static JTextField btArrayField = new JTextField(15);
	
	/**达值切换策略**/
	public static JTextField changeYlField = new JTextField(5);
	
	/**模拟盈亏值**/
	public static JLabel mnYkValueLabel = new JLabel("0.00");
	/**真实盈亏值**/
	public static JLabel szYkValueLabel = new JLabel("0.00");
	//开始投注按钮
	public static JButton button = new JButton("停止执行");
	
	public static JTable table = null;
	
	public static JFrame anythreeFrame5 = new AnyThreeFrame5();
	
	public AnyThreeFrame5(){
		this.setTitle("任三策略-连中N期换号");
		this.setBounds(200, 200, 200, 200);
		this.setSize(910, 600);
		this.setLocation(200, 50);
		this.setBackground(Color.white);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		panel.setSize(910, 600);
		this.add(panel);
		
		//账户信息
		JPanel accountBox = new JPanel();
		accountBox.setPreferredSize(new Dimension(140,190));
		accountBox.setBorder(BorderFactory.createTitledBorder("账户信息"));
		
		JPanel accountNameBox = new JPanel();
		accountNameBox.setPreferredSize(new Dimension(130,35));
		
		JLabel AccpuntNameTLabel = new JLabel("账户:");
		accountNameBox.add(AccpuntNameTLabel);
		accountNameBox.add(accountNameLabel);
		accountBox.add(accountNameBox);
  		
		JPanel accountAmountBox = new JPanel();
		accountAmountBox.setPreferredSize(new Dimension(130,35));
  		JLabel accpuntAmountTLabel = new JLabel("余额:");
  		accountAmountBox.add(accpuntAmountTLabel);
  		accountAmountBox.add(accountAmountLabel);
		accountBox.add(accountAmountBox);
  		
  		
  		panel.add(accountBox);
		
		
		//历史开奖panel
		
		JTable kjTable = new JTable(kjTableDefaultmodel);
		JScrollPane historyIssuBox = new JScrollPane(kjTable);
		historyIssuBox.setPreferredSize(new Dimension(220,190));
		historyIssuBox.setBorder(BorderFactory.createTitledBorder("历史开奖"));
  		
        panel.add(historyIssuBox);
		
		//初始参数panel
		JPanel initParamsBox = new JPanel();
		initParamsBox.setPreferredSize(new Dimension(220,190));
		initParamsBox.setBorder(BorderFactory.createTitledBorder("初始参数"));
		
		//系统初始策略生成组数
		JPanel initClNumPanel = new JPanel();
		initClNumPanel.setPreferredSize(new Dimension(210,25));
        JLabel initClNumLabel = new JLabel("初始策略数:");
        initClNumPanel.add(initClNumLabel);
  		
  		initClNumField.setText("300");
  		initClNumPanel.add(initClNumField);
  		initParamsBox.add(initClNumPanel);
  		
  		//理想连挂数
  		JPanel aimMaxFailPanel = new JPanel();
  		aimMaxFailPanel.setPreferredSize(new Dimension(210,25));
        JLabel aimMaxFailLabel = new JLabel("理想连挂数:");
        aimMaxFailPanel.add(aimMaxFailLabel);
  		
  		aimMaxFailField.setText("1");
  		aimMaxFailPanel.add(aimMaxFailField);
  		initParamsBox.add(aimMaxFailPanel);
  		
  		//重置次数
  		JPanel maxRestNPanel = new JPanel();
  		maxRestNPanel.setPreferredSize(new Dimension(210,25));
        JLabel maxRestNLabel = new JLabel("重置的次数:");
        maxRestNPanel.add(maxRestNLabel);
        
  		maxRestNField.setText("200");
  		maxRestNPanel.add(maxRestNField);
  		initParamsBox.add(maxRestNPanel);
  		
  		//系统策略总组数
  		JPanel clNumPanel = new JPanel();
  		clNumPanel.setPreferredSize(new Dimension(210,25));
        JLabel clNumLabel = new JLabel("策略总组数:");
        clNumPanel.add(clNumLabel);
       
  		clNumField.setText("700");
  		clNumPanel.add(clNumField);
  		initParamsBox.add(clNumPanel);
  		
  		//统计期数
  		JPanel historyNumPanel = new JPanel();
  		historyNumPanel.setPreferredSize(new Dimension(210,25));
  		JLabel historyNumLabel = new JLabel("统计总期数:");
  		historyNumPanel.add(historyNumLabel);
  		
  		historyNumField.setText("2400");
  		historyNumPanel.add(historyNumField);
  		initParamsBox.add(historyNumPanel);
  		
  		panel.add(initParamsBox);
  		
  	
  		//投注位置  多个
		
		JLabel fun1Label = new JLabel("方案1:");
		
		
		
        
        JLabel fun2Label = new JLabel("方案2:");
        
        
        JLabel fun3Label = new JLabel("方案3:");
        
        
        JLabel fun4Label = new JLabel("方案4:");
        
        
        JLabel fun5Label = new JLabel("方案5:");
        
        
        //投注策略panel
        JPanel positionBox = new JPanel();
        positionBox.setPreferredSize(new Dimension(270,190));
        positionBox.setBorder(BorderFactory.createTitledBorder("投注方案"));
        positionBox.add(fun1Label);
        //万百个
        positionBox.add(w1);
        w1.setSelected(true);
        positionBox.add(q1);
        positionBox.add(b1);
        b1.setSelected(true);
        positionBox.add(s1);
        positionBox.add(g1);
        g1.setSelected(true);
        
        positionBox.add(fun2Label);
        positionBox.add(w2);
        positionBox.add(q2);
        q2.setSelected(true);
        positionBox.add(b2);
        b2.setSelected(true);
        positionBox.add(s2);
        s2.setSelected(true);
        positionBox.add(g2);
        
        positionBox.add(fun3Label);
        positionBox.add(w3);
        positionBox.add(q3);
        positionBox.add(b3);
        b3.setSelected(true);
        positionBox.add(s3);
        s3.setSelected(true);
        positionBox.add(g3);
        g3.setSelected(true);
        
        positionBox.add(fun4Label);
        positionBox.add(w4);
        w4.setSelected(true);
        positionBox.add(q4);
        q4.setSelected(true);
        positionBox.add(b4);
        b4.setSelected(true);
        positionBox.add(s4);
        positionBox.add(g4);

        positionBox.add(fun5Label);
        positionBox.add(w5);
        positionBox.add(q5);
        positionBox.add(b5);
        positionBox.add(s5);
        positionBox.add(g5);
        
        panel.add(positionBox);
        
        
    	//投注参数
  		JPanel downParamsBox = new JPanel();
  		downParamsBox.setPreferredSize(new Dimension(880,90));
  		downParamsBox.setBorder(BorderFactory.createTitledBorder("投注参数"));
  		
  		//盈利回头
  		JLabel returnLabel = new JLabel("盈利回头:");
  		downParamsBox.add(returnLabel);
  		
   		returnField.setText("0.01");
   		downParamsBox.add(returnField);
  		//止盈
   		JLabel winStopLabel = new JLabel("止盈:");
  		downParamsBox.add(winStopLabel);
  		
   		winStopField.setText("40");
   		downParamsBox.add(winStopField);
   		//止损
   		JLabel failStopLabel = new JLabel("止损:");
  		downParamsBox.add(failStopLabel);
  		
   		failStopField.setText("100");
   		downParamsBox.add(failStopField);
   		//投注模式
        JLabel downTypeLabel = new JLabel("投注模式:");
        downParamsBox.add(downTypeLabel);
        
        // 单选按钮组,同一个单选按钮组的互斥.
        ButtonGroup downTypeGroup = new ButtonGroup();
        downTypeGroup.add(downTypeMn);
        downTypeGroup.add(downTypeSz);
        downParamsBox.add(downTypeMn);
        downParamsBox.add(downTypeSz);
   		//倍率
        JLabel btArrayLabel = new JLabel("倍投阶梯:");
  		downParamsBox.add(btArrayLabel);
  		btArrayField.setText("1,4,16,65,270");
   		downParamsBox.add(btArrayField);
   		
   		//切换策略盈利值
   		JLabel changeYlLabel = new JLabel("达值更换策略:");
		downParamsBox.add(changeYlLabel);
		changeYlField.setText("5");
		downParamsBox.add(changeYlField);
		
   		//模拟盈亏
   		JLabel mnYkLabel = new JLabel("模拟盈亏:");
		downParamsBox.add(mnYkLabel);
		
		downParamsBox.add(mnYkValueLabel);
		//真实盈亏
   		JLabel szYkLabel = new JLabel("真实盈亏:");
		downParamsBox.add(szYkLabel);
		
		downParamsBox.add(szYkValueLabel);
		
		downParamsBox.add(button);
		
  		panel.add(downParamsBox);
  		
  		
  		table = new JTable(tableDefaultmodel);
  		
  		JScrollPane dowmMsgBox = new JScrollPane(table);
  		dowmMsgBox.setPreferredSize(new Dimension(880,250));
  		dowmMsgBox.setBorder(BorderFactory.createTitledBorder("投注情况"));
  		
  		panel.add(dowmMsgBox);
  		button.addMouseListener(new MouseAdapter() {
  			@Override
  		    public void mouseClicked(MouseEvent arg0){
  				if(button.getText().equals("开始执行"))
  					button.setText("停止执行");
  				else button.setText("开始执行");
  		    }
		});
  		
  		clBoxList = new ArrayList<JCheckBox>();
  		clBoxList.add(w1);
  		clBoxList.add(q1);
  		clBoxList.add(b1);
  		clBoxList.add(s1);
  		clBoxList.add(g1);
  		clBoxList.add(w2);
  		clBoxList.add(q2);
  		clBoxList.add(b2);
  		clBoxList.add(s2);
  		clBoxList.add(g2);
  		clBoxList.add(w3);
  		clBoxList.add(q3);
  		clBoxList.add(b3);
  		clBoxList.add(s3);
  		clBoxList.add(g3);
  		clBoxList.add(w4);
  		clBoxList.add(q4);
  		clBoxList.add(b4);
  		clBoxList.add(s4);
  		clBoxList.add(g4);
  		clBoxList.add(w5);
  		clBoxList.add(q5);
  		clBoxList.add(b5);
  		clBoxList.add(s5);
  		clBoxList.add(g5);
  		this.addWindowListener(new WindowAdapter() { // 窗口关闭事件

  			public void windowIconified(WindowEvent e) { // 窗口最小化事件
  				anythreeFrame5.setVisible(false);
  				miniTray();

			}

		});
	}
	
	static int runTime = 0;
	static TextArea ta = new TextArea();
	static boolean regStatus = false;
	private static TrayIcon trayIcon = null;
	static SystemTray tray = SystemTray.getSystemTray();
	
	private static void miniTray() { // 窗口最小化到任务栏托盘

		ImageIcon trayImg = new ImageIcon("c://tyjkdb//leida.gif");// 托盘图标

		PopupMenu pop = new PopupMenu();// 增加托盘右击菜单
		MenuItem show = new MenuItem("show");
		MenuItem exit = new MenuItem("exit");
		show.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { // 按下还原键

				tray.remove(trayIcon);
				anythreeFrame5.setVisible(true);
				anythreeFrame5.setExtendedState(JFrame.NORMAL);
				anythreeFrame5.toFront();
			}

		});

		exit.addActionListener(new ActionListener() { // 按下退出键

			public void actionPerformed(ActionEvent e) {

				tray.remove(trayIcon);
				System.exit(0);

			}

		});

		pop.add(show);
		pop.add(exit);

		trayIcon = new TrayIcon(trayImg.getImage(), "任三-ylN期更换", pop);
		trayIcon.setImageAutoSize(true);

		trayIcon.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent e) { // 鼠标器双击事件

				if (e.getClickCount() == 2) {

					tray.remove(trayIcon); // 移去托盘图标
					anythreeFrame5.setVisible(true);
					anythreeFrame5.setExtendedState(JFrame.NORMAL); // 还原窗口
					anythreeFrame5.toFront();
				}

			}

		});

		try {
			tray.add(trayIcon);

		} catch (AWTException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}
}
