package com.as.boot.frame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
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

public class AnyThreeFrame extends JFrame{

	public static String FFCRound = null;
	public static String FFCResult = null;
	
	//开奖情况
	public static String[][] kjTableDate = {};
	public static String[] kjTataTitle = {"期数","开奖结果"};
	public static DefaultTableModel kjTableDefaultmodel = new DefaultTableModel(kjTableDate,kjTataTitle);
	
	//投注情况
	public static String[][] tableDate = {};
	public static String[] dataTitle = {"期数","方案","倍数","方案盈亏","连挂","连中","开奖情况","中挂","投注类型"};
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
	public static JLabel maxFailValueLabel = new JLabel("0.000");
	//开始投注按钮
	public static JButton button = new JButton("停止执行");
	
	public AnyThreeFrame(){
		this.setTitle("任三策略");
		this.setBounds(200, 200, 200, 200);
		this.setSize(910, 600);
		this.setLocation(200, 50);
		this.setBackground(Color.white);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		panel.setSize(910, 600);
		this.add(panel);
		
		//历史开奖panel
		
		JTable kjTable = new JTable(kjTableDefaultmodel);
		JScrollPane historyIssuBox = new JScrollPane(kjTable);
		historyIssuBox.setPreferredSize(new Dimension(270,190));
		historyIssuBox.setBorder(BorderFactory.createTitledBorder("历史开奖"));
  		
        panel.add(historyIssuBox);
		
		//初始参数panel
		JPanel initParamsBox = new JPanel();
		initParamsBox.setPreferredSize(new Dimension(250,190));
		initParamsBox.setBorder(BorderFactory.createTitledBorder("初始参数"));
		
		//系统初始策略生成组数
		JPanel initClNumPanel = new JPanel();
		initClNumPanel.setPreferredSize(new Dimension(240,25));
        JLabel initClNumLabel = new JLabel("初始策略数:");
        initClNumPanel.add(initClNumLabel);
  		
  		initClNumField.setText("300");
  		initClNumPanel.add(initClNumField);
  		initParamsBox.add(initClNumPanel);
  		
  		//理想连挂数
  		JPanel aimMaxFailPanel = new JPanel();
  		aimMaxFailPanel.setPreferredSize(new Dimension(240,25));
        JLabel aimMaxFailLabel = new JLabel("理想连挂数:");
        aimMaxFailPanel.add(aimMaxFailLabel);
  		
  		aimMaxFailField.setText("1");
  		aimMaxFailPanel.add(aimMaxFailField);
  		initParamsBox.add(aimMaxFailPanel);
  		
  		//重置次数
  		JPanel maxRestNPanel = new JPanel();
  		maxRestNPanel.setPreferredSize(new Dimension(240,25));
        JLabel maxRestNLabel = new JLabel("重置的次数:");
        maxRestNPanel.add(maxRestNLabel);
        
  		maxRestNField.setText("200");
  		maxRestNPanel.add(maxRestNField);
  		initParamsBox.add(maxRestNPanel);
  		
  		//系统策略总组数
  		JPanel clNumPanel = new JPanel();
  		clNumPanel.setPreferredSize(new Dimension(240,25));
        JLabel clNumLabel = new JLabel("策略总组数:");
        clNumPanel.add(clNumLabel);
       
  		clNumField.setText("700");
  		clNumPanel.add(clNumField);
  		initParamsBox.add(clNumPanel);
  		
  		//统计期数
  		JPanel historyNumPanel = new JPanel();
  		historyNumPanel.setPreferredSize(new Dimension(240,25));
  		JLabel historyNumLabel = new JLabel("统计总期数:");
  		historyNumPanel.add(historyNumLabel);
  		
  		historyNumField.setText("1440");
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
  		btArrayField.setText("1,4,16,63");
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
		
		//最大亏损
   		JLabel maxFailYkLabel = new JLabel("当前策略亏损:");
		downParamsBox.add(maxFailYkLabel);
		
		downParamsBox.add(maxFailValueLabel);
		
		downParamsBox.add(button);
		
  		panel.add(downParamsBox);
  		
  		
  		JTable table = new JTable(tableDefaultmodel);
  		
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
  		
	}
	
}
