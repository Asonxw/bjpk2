package com.as.boot.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.UUID;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class CommonDao {

	private final Integer type_1 = 1;//类型1为连续算法
	private final Integer type_2 = 2;//类型2为叠加算法
	
	DbUtil dbUtil = new DbUtil();
	public boolean saveResult(String result){
		//解析数据
		String[] array = result.split("@");
		JSONArray jArray = new JSONArray();
		for (int i = 0, il = array.length; i < il; i++) {
			jArray.addAll(JSONObject.parseObject(array[i]).getJSONArray("list"));
		}
		//保存用户数据
		addUser(jArray);
		return false;
	}
	
	/**
	 * 添加现状
	 * @param username
	 * @param password
	 * @param nickname
	 * @return
	 */
	public boolean addUser(JSONArray jArray){
		//生成20期的唯一标识
		String mk = UUID.randomUUID().toString();
		//创建连接对象
		Connection conn = null;
		//创建处理对象
		PreparedStatement pstat = null;
		//创建结果集
		ResultSet rs = null;
		//需要执行的sql语句
		String sql = " INSERT into msg(userid,username,round,ball,type,number,numbernum,time,hid,win,n1,n2,n3,n4,n5,n6,n7,n8,n9,n10,result,k,mk) value (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
		try {
			//初始化连接对象
			conn = dbUtil.getConnection();
			//初始化处理对象
			pstat = conn.prepareStatement(sql);
			JSONObject item = null;
			for (int i = 0, il = jArray.size(); i < il; i++) {
				item = jArray.getJSONObject(i);
				pstat.setObject(1, item.getString("userid"));
				pstat.setObject(2, item.getString("username"));
				pstat.setObject(3, item.getString("round"));
				pstat.setObject(4, item.getString("ball"));
				pstat.setObject(5, item.getString("type"));
				pstat.setObject(6, item.getString("number"));
				pstat.setObject(7, item.getString("numbernum"));
				pstat.setObject(8, new Date(Long.valueOf(item.getString("time"))));
				pstat.setObject(9, item.getString("hid"));
				pstat.setObject(10, item.getString("win"));
				pstat.setObject(11, item.getString("n1"));
				pstat.setObject(12, item.getString("n2"));
				pstat.setObject(13, item.getString("n3"));
				pstat.setObject(14, item.getString("n4"));
				pstat.setObject(15, item.getString("n5"));
				pstat.setObject(16, item.getString("n6"));
				pstat.setObject(17, item.getString("n7"));
				pstat.setObject(18, item.getString("n8"));
				pstat.setObject(19, item.getString("n9"));
				pstat.setObject(20, item.getString("n10"));
				pstat.setObject(21, item.getString("result"));
				pstat.setObject(22, item.getString("k"));
				pstat.setObject(23, mk);
				pstat.addBatch();
				
			}
			
			//获取结果集
			int[] count = pstat.executeBatch();
			System.out.println("sql语句【"+sql+"】执行成功！！");
			return true;
		} catch (Exception e) {
			System.out.println("Xsql语句执行失败X");
			e.printStackTrace();
			return false;
		}finally{
			DbUtil.close(conn, pstat, rs);
		}
	}
	
	
	/**
	 * 添加每日连挂数统合
	 * @param username
	 * @param password
	 * @param nickname
	 * @return
	 */
	public boolean addHistory(Integer g4,Integer g5,Integer g6,Integer g7,Integer g8){
		//创建连接对象
		Connection conn = null;
		//创建处理对象
		PreparedStatement pstat = null;
		//创建结果集
		ResultSet rs = null;
		//需要执行的sql语句
		String sql = " INSERT into history_g(g4,g5,g6,g7,g8,date,type) value (?,?,?,?,?,?,?)  on duplicate key update g4 = values(g4),g5 = values(g5),g6 = values(g6),g7 = values(g7),g8 = values(g8) ";
		try {
			//初始化连接对象
			conn = dbUtil.getConnection();
			//初始化处理对象
			pstat = conn.prepareStatement(sql);
			pstat.setObject(1, g4);
			pstat.setObject(2, g5);
			pstat.setObject(3, g6);
			pstat.setObject(4, g7);
			pstat.setObject(5, g8);
			pstat.setObject(6, new Date());
			pstat.setObject(7, type_1);
			//获取结果集
			pstat.executeUpdate();
			System.out.println("sql语句【"+sql+"】执行成功！！");
			return true;
		} catch (Exception e) {
			System.out.println("Xsql语句执行失败X");
			e.printStackTrace();
			return false;
		}finally{
			DbUtil.close(conn, pstat, rs);
		}
	}
	
	public boolean addHistory_dj(Integer g1,Integer g2,Integer g3,Integer g4,Integer g5){
		//创建连接对象
		Connection conn = null;
		//创建处理对象
		PreparedStatement pstat = null;
		//创建结果集
		ResultSet rs = null;
		//需要执行的sql语句
		String sql = " INSERT into history_g(g1,g2,g3,g4,g5,date,type) value (?,?,?,?,?,?,?)  on duplicate key update g1 = values(g1),g2 = values(g2),g3 = values(g3),g4 = values(g4),g5 = values(g5),g6 = values(g6),g7 = values(g7),g8 = values(g8) ";
		try {
			//初始化连接对象
			conn = dbUtil.getConnection();
			//初始化处理对象
			pstat = conn.prepareStatement(sql);
			pstat.setObject(1, g1);
			pstat.setObject(2, g2);
			pstat.setObject(3, g3);
			pstat.setObject(4, g4);
			pstat.setObject(5, g5);
			pstat.setObject(6, new Date());
			pstat.setObject(7, type_2);
			//获取结果集
			pstat.executeUpdate();
			System.out.println("sql语句【"+sql+"】执行成功！！");
			return true;
		} catch (Exception e) {
			System.out.println("Xsql语句执行失败X");
			e.printStackTrace();
			return false;
		}finally{
			DbUtil.close(conn, pstat, rs);
		}
	}
}
