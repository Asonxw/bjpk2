package com.as.boot.frame;

import java.awt.AWTException;
import java.awt.BorderLayout;
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

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class MiniTray {
	private static final long serialVersionUID = 1L;
	private static int i = 0;
	static int runTime = 0;
	static TextArea ta = new TextArea();
	static boolean regStatus = false;
	private static TrayIcon trayIcon = null;
	static JFrame mf = new JFrame();
	static SystemTray tray = SystemTray.getSystemTray();

	public static void myFrame() { // 窗体

	mf.setLocation(300, 100);
	mf.setSize(500, 300);
	mf.setTitle("XXXX系统");
	mf.setLayout(new BorderLayout());

	mf.setVisible(true);//使窗口可见

	mf.addWindowListener(new WindowAdapter() { // 窗口关闭事件
	public void windowClosing(WindowEvent e) {
	System.exit(0);
	};

	public void windowIconified(WindowEvent e) { // 窗口最小化事件

		mf.setVisible(false);
		MiniTray.miniTray();

		}

	});

	}

	private static void miniTray() { // 窗口最小化到任务栏托盘

		ImageIcon trayImg = new ImageIcon("c://tyjkdb//leida.gif");// 托盘图标

		PopupMenu pop = new PopupMenu();// 增加托盘右击菜单
		MenuItem show = new MenuItem("show");
		MenuItem exit = new MenuItem("exit");
		show.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) { // 按下还原键

				tray.remove(trayIcon);
				mf.setVisible(true);
				mf.setExtendedState(JFrame.NORMAL);
				mf.toFront();
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

		trayIcon = new TrayIcon(trayImg.getImage(), "xxxx系统", pop);
		trayIcon.setImageAutoSize(true);

		trayIcon.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent e) { // 鼠标器双击事件

				if (e.getClickCount() == 2) {

					tray.remove(trayIcon); // 移去托盘图标
					mf.setVisible(true);
					mf.setExtendedState(JFrame.NORMAL); // 还原窗口
					mf.toFront();
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

	public static void main(String[] args) {
		MiniTray MiniTray = new MiniTray();
		MiniTray.myFrame();
	}
}