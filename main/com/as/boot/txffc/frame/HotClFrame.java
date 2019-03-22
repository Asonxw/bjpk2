package com.as.boot.txffc.frame;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
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
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.dtools.ini.IniItem;

import com.as.boot.txffc.controller.ExampleControll;
import com.as.boot.txffc.thread.DelPreThreeThread;

public class HotClFrame extends JFrame{

	public static String FFCRound = null;
	public static String FFCResult = null;
	
	public static JLabel accountNameLabel = new JLabel("xxxx");
	public static JLabel accountAmountLabel = new JLabel("0.0000");
	
	//开奖情况
	public static String[][] kjTableDate = {};
	public static String[] kjTataTitle = {"期数","开奖结果"};
	public static DefaultTableModel kjTableDefaultmodel = new DefaultTableModel(kjTableDate,kjTataTitle);
	
	//投注情况
	public static String[][] tableDate = {};
	public static String[] dataTitle = {"期数","方案","倍数","方案盈亏","连挂","连中","开奖情况","中挂","投注类型"};
	public static DefaultTableModel tableDefaultmodel = new DefaultTableModel(tableDate,dataTitle);
	//操作日志
	public static String[][] logTableDate = {};
	public static String[] logDataTitle = {"log信息"};
	public static DefaultTableModel logTableDefaultmodel = new DefaultTableModel(logTableDate,logDataTitle);
	
	/**目标策略注数**/
    public static JTextField clNumField = new JTextField(8);
    /**统计期数**/
	public static JTextField historyNumField = new JTextField(8);
	/**投注单位**/
	public static JComboBox<Double> price = new JComboBox<>();
	
	/**开始去重码**/
	public static JCheckBox delPreResultCheckbox = new JCheckBox("是");
	/**方案一**/
	public static JCheckBox w = new JCheckBox("0");
	public static JCheckBox q = new JCheckBox("1");
	public static JCheckBox b = new JCheckBox("2");
	public static JCheckBox g = new JCheckBox("3");
	public static JCheckBox s = new JCheckBox("4");
    
	public static JLabel roundCount = new JLabel("0期");
	public static JLabel sulPercent = new JLabel("00%");
	
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
	
	/**盈利转换**/
	public static JTextField ylSwhichField = new JTextField(5);
	
	/**模拟连挂转换**/
	public static JTextField mnFailSwhichField = new JTextField(5);
	
	/**是否开启真实投注**/
	public static JCheckBox trueDownFlagField = new JCheckBox("开");
	
	/**模拟盈亏值**/
	public static JLabel mnYkValueLabel = new JLabel("0.00");
	/**真实盈亏值**/
	public static JLabel szYkValueLabel = new JLabel("0.00");
	public static JLabel maxFailValueLabel = new JLabel("0.000");
	//开始投注按钮
	public static JButton button = new JButton("开始执行");
	
	public static HotClFrame hotClFrame = new HotClFrame();
	
	
	
	public HotClFrame(){
		this.setTitle("txffc");
		this.setBounds(200, 200, 200, 200);
		this.setSize(910, 700);
		this.setLocation(200, 50);
		this.setBackground(Color.white);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		panel.setSize(910, 600);
		this.add(panel);
		
		//账户信息
		JPanel accountBox = new JPanel();
		accountBox.setPreferredSize(new Dimension(150,190));
		accountBox.setBorder(BorderFactory.createTitledBorder("账户信息"));
		
		
		JLabel AccpuntNameTLabel = new JLabel("账户名称:");
		accountBox.add(AccpuntNameTLabel);
		accountNameLabel.setPreferredSize(new Dimension(55,30));
		accountBox.add(accountNameLabel);
  		
  		JLabel accpuntAmountTLabel = new JLabel("账户余额:");
  		accountBox.add(accpuntAmountTLabel);
  		accountAmountLabel.setPreferredSize(new Dimension(55,30));
  		accountBox.add(accountAmountLabel);
  		
   		//模拟盈亏
   		JLabel mnYkLabel = new JLabel("模拟盈亏:");
   		accountBox.add(mnYkLabel);
   		mnYkValueLabel.setPreferredSize(new Dimension(55,30));
   		accountBox.add(mnYkValueLabel);
		//真实盈亏
   		JLabel szYkLabel = new JLabel("真实盈亏:");
   		accountBox.add(szYkLabel);
		
   		szYkValueLabel.setPreferredSize(new Dimension(55,30));
   		accountBox.add(szYkValueLabel);
  		
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
  		
  		//系统策略总组数
  		JPanel clNumPanel = new JPanel();
  		clNumPanel.setPreferredSize(new Dimension(210,25));
        JLabel clNumLabel = new JLabel("策略码数:");
        clNumPanel.add(clNumLabel);
        
        IniItem clNum = ExampleControll.getIniItem("clNum");
        if(clNum != null)
        	clNumField.setText(clNum.getValue());
        
  		clNumPanel.add(clNumField);
  		initParamsBox.add(clNumPanel);
  		
  		//统计期数
  		JPanel historyNumPanel = new JPanel();
  		historyNumPanel.setPreferredSize(new Dimension(210,25));
  		JLabel historyNumLabel = new JLabel("统计期数:");
  		historyNumPanel.add(historyNumLabel);
  		
  		IniItem historyNum = ExampleControll.getIniItem("historyNum");
        if(historyNum != null)
        	 historyNumField.setText(historyNum.getValue());
  		
        historyNumPanel.add(historyNumField);
  		initParamsBox.add(historyNumPanel);
  		
  		//投注单位
  		//统计期数
  		JPanel pricePanel = new JPanel();
  		pricePanel.setPreferredSize(new Dimension(210,25));
  		JLabel priceLabel = new JLabel("投注单位:");
  		pricePanel.add(priceLabel);
  		price.addItem(0.002);//厘
  		price.addItem(0.02);//分
  		price.addItem(0.2);//角
  		price.addItem(2.0);//元
  		
  		IniItem price_item = ExampleControll.getIniItem("price");
  		if(price_item != null)
  			price.setSelectedItem(Double.parseDouble(price_item.getValue()));
  		
  		price.setVisible(true);
       
  		pricePanel.add(price);
  		initParamsBox.add(pricePanel);
  		
  		
  		JPanel delPreResultPanel = new JPanel();
  		delPreResultPanel.setPreferredSize(new Dimension(210,25));
  		JLabel delPreResultLabel = new JLabel("开启去重码:");
  		delPreResultPanel.add(delPreResultLabel);
  		delPreResultPanel.add(delPreResultCheckbox);
  		initParamsBox.add(delPreResultPanel);
  		
  		panel.add(initParamsBox);
  		
  	
        //投注策略panel
        JPanel positionBox = new JPanel();
        positionBox.setPreferredSize(new Dimension(150,190));
        positionBox.setBorder(BorderFactory.createTitledBorder("投注方案"));
        w.setSelected(true);
        q.setSelected(true);
        b.setSelected(true);
        s.setSelected(true);
        g.setSelected(true);
        //万百个
        positionBox.add(w);
        positionBox.add(q);
        positionBox.add(b);
        positionBox.add(g);
        positionBox.add(s);
        
        //已运行期数
        JPanel roundCountBox = new JPanel();
        roundCountBox.setPreferredSize(new Dimension(130,35));
        JLabel roundCountLabel = new JLabel("已运行:");
        roundCountBox.add(roundCountLabel);
        roundCountBox.add(roundCount);
        positionBox.add(roundCountBox);
        //命中率
        JPanel sulPercentBox = new JPanel();
        sulPercentBox.setPreferredSize(new Dimension(130,35));
        JLabel sulPercentLabel = new JLabel("命中率:");
        sulPercentBox.add(sulPercentLabel);
        sulPercentBox.add(sulPercent);
        positionBox.add(sulPercentBox);
        
        panel.add(positionBox);
        
        
    	//投注参数
  		JPanel downParamsBox = new JPanel();
  		downParamsBox.setPreferredSize(new Dimension(880,90));
  		downParamsBox.setBorder(BorderFactory.createTitledBorder("投注参数"));
  		
  		//盈利回头
  		JLabel returnLabel = new JLabel("盈利回头:");
  		downParamsBox.add(returnLabel);
  		
  		IniItem returnYl = ExampleControll.getIniItem("returnYl");
        if(returnYl != null)
        	returnField.setText(returnYl.getValue());
        
   		downParamsBox.add(returnField);
  		//止盈
   		JLabel winStopLabel = new JLabel("止盈:");
  		downParamsBox.add(winStopLabel);

  		IniItem winStop = ExampleControll.getIniItem("winStop");
        if(winStop != null)
        	winStopField.setText(winStop.getValue());
        else winStopField.setText("999");
   		downParamsBox.add(winStopField);
   		//止损
   		JLabel failStopLabel = new JLabel("止损:");
  		downParamsBox.add(failStopLabel);
  		
  		IniItem failStop = ExampleControll.getIniItem("failStop");
        if(failStop != null)
        	failStopField.setText(failStop.getValue());
        else failStopField.setText("999");
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
        downTypeMn.setSelected(true);
        //模拟转实战需要清空倍投
        downTypeSz.addMouseListener(new MouseAdapter() {
  			@Override
  		    public void mouseClicked(MouseEvent arg0){
  				//将文字切换为实战
  				//HotClThread.mnOrSzFlag = 1;
  				DelPreThreeThread.mnOrSzFlag = 1;
  				//HotDelPreTwoClThread.mnOrSzFlag = 1;
  				//初始化模拟实战数据
				DelPreThreeThread.mnOrSzList = Arrays.asList(true,true,true,true,true);
  		    }
		});
        downTypeMn.addMouseListener(new MouseAdapter() {
  			@Override
  		    public void mouseClicked(MouseEvent arg0){
  				//将文字切换为模拟
  				//HotClThread.mnOrSzFlag = 0;
  				DelPreThreeThread.mnOrSzFlag = 0;
  				//HotDelPreTwoClThread.mnOrSzFlag = 0;
  				DelPreThreeThread.mnOrSzList = Arrays.asList(false,false,false,false,false);
  		    }
		});
   		//倍率
        JLabel btArrayLabel = new JLabel("倍投阶梯:");
  		downParamsBox.add(btArrayLabel);

  		IniItem btArray = ExampleControll.getIniItem("btArray");
        if(btArray != null)
        	btArrayField.setText(btArray.getValue());
        else btArrayField.setText("0");
   		downParamsBox.add(btArrayField);
   		
   		//切换策略盈利值
   		JLabel changeYlLabel = new JLabel("中N期换号:");
		downParamsBox.add(changeYlLabel);

  		IniItem changeYl = ExampleControll.getIniItem("changeYl");
        if(changeYl != null)
        	changeYlField.setText(changeYl.getValue());
  		
		downParamsBox.add(changeYlField);
		
		//切换策略盈利值
   		JLabel ylSwhichLabel = new JLabel("盈利转换:");
		downParamsBox.add(ylSwhichLabel);

  		IniItem ylSwhich = ExampleControll.getIniItem("ylSwhich");
        if(ylSwhich != null)
        	ylSwhichField.setText(ylSwhich.getValue());
  		
		downParamsBox.add(ylSwhichField);
		
		//模拟连挂转换
   		JLabel mnFailSwhichLabel = new JLabel("投注监控:");
		downParamsBox.add(mnFailSwhichLabel);

  		IniItem mnFailSwhich = ExampleControll.getIniItem("mnFailSwhich");
        if(mnFailSwhich != null)
        	mnFailSwhichField.setText(mnFailSwhich.getValue());
        else mnFailSwhichField.setText("000010000");
        
		downParamsBox.add(mnFailSwhichField);
		
		JLabel trueDownFlagLabel = new JLabel("开启真实投注:");
		downParamsBox.add(trueDownFlagLabel);
		//initFailCountFsield.setText("10");
		downParamsBox.add(trueDownFlagField);
		
		downParamsBox.add(button);
		
		
  		panel.add(downParamsBox);
  		
  		//投注情况
  		JTable table = new JTable(tableDefaultmodel);
  		
  		JScrollPane dowmMsgBox = new JScrollPane(table);
  		dowmMsgBox.setPreferredSize(new Dimension(880,250));
  		dowmMsgBox.setBorder(BorderFactory.createTitledBorder("投注情况"));
  		
  		panel.add(dowmMsgBox);
  		
  		//log日志
  		JTable logTtable = new JTable(logTableDefaultmodel);
  		
  		JScrollPane logMsgBox = new JScrollPane(logTtable);
  		logMsgBox.setPreferredSize(new Dimension(880,110));
  		logMsgBox.setBorder(BorderFactory.createTitledBorder("log日志"));
  		
  		panel.add(logMsgBox);
  		
  		button.addMouseListener(new MouseAdapter() {
  			@Override
  		    public void mouseClicked(MouseEvent arg0){
  				if(button.getText().equals("开始执行")){
  					//初始化策略
  					//HotClThread.initTXFFCL();
  					DelPreThreeThread.initTXFFCL();
  					button.setText("停止执行");
  					//初始化倍投阶梯
  					String[] btStrArr = HotClFrame.btArrayField.getText().split(",");
  					//HotClThread.btArr = new Integer[btStrArr.length];
  					DelPreThreeThread.btArr = new Integer[btStrArr.length];
  					for (int i = 0; i < btStrArr.length; i++)
  						//HotClThread.btArr[i] = Integer.parseInt(btStrArr[i]);
  						DelPreThreeThread.btArr[i] = Integer.parseInt(btStrArr[i]);
  					//HoHotDelPreTwoClThreadwnFFC();
  					DelPreThreeThread.startDownFFC();
  					
  					//每次开始投注保存配置
  					saveParams();
  				}else{
  					button.setText("开始执行");
  					//初始化连挂及倍投
  					//连挂数
  					/*HotClThread.failCountList = Arrays.asList(0,0,0,0,0,0,0,0,0,0);
  					//倍投情况
  					HotClThread.btNumList = Arrays.asList(0,0,0,0,0,0,0,0,0,0);*/
  					DelPreThreeThread.failCountList = Arrays.asList(0,0,0,0,0);
  					//倍投情况
  					DelPreThreeThread.btNumList = Arrays.asList(0,0,0,0,0);
  					//HotDelPreTwo_DC_ClThread.btNum = 0;
  				}
  		    }
		});
  		
  		clBoxList = new ArrayList<JCheckBox>();
  		clBoxList.add(w);
  		clBoxList.add(q);
  		clBoxList.add(b);
  		clBoxList.add(g);
  		clBoxList.add(s);
  		
  		this.addWindowListener(new WindowAdapter() { // 窗口关闭事件

			public void windowIconified(WindowEvent e) { // 窗口最小化事件
				hotClFrame.setVisible(false);
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
				hotClFrame.setVisible(true);
				hotClFrame.setExtendedState(JFrame.NORMAL);
				hotClFrame.toFront();
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

		trayIcon = new TrayIcon(trayImg.getImage(), "any three positions - Replace after hit", pop);
		trayIcon.setImageAutoSize(true);

		trayIcon.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent e) { // 鼠标器双击事件

				if (e.getClickCount() == 2) {

					tray.remove(trayIcon); // 移去托盘图标
					hotClFrame.setVisible(true);
					hotClFrame.setExtendedState(JFrame.NORMAL); // 还原窗口
					hotClFrame.toFront();
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
	
	/**
	 * @Title: saveParams  
	 * @Description:保存参数 
	 * @author: Ason      
	 * @return: void      
	 * @throws
	 */
	public void saveParams(){
		
        ExampleControll.addIniItem("clNum", clNumField.getText());
        
        ExampleControll.addIniItem("historyNum", historyNumField.getText());

        ExampleControll.addIniItem("price", price.getSelectedItem().toString());
        
        ExampleControll.addIniItem("returnYl", returnField.getText());

        ExampleControll.addIniItem("winStop", winStopField.getText());

        ExampleControll.addIniItem("failStop", failStopField.getText());

        ExampleControll.addIniItem("btArray", btArrayField.getText());

        ExampleControll.addIniItem("changeYl", changeYlField.getText());

        ExampleControll.addIniItem("ylSwhich", ylSwhichField.getText());

        ExampleControll.addIniItem("mnFailSwhich", mnFailSwhichField.getText());
        
	}
}
