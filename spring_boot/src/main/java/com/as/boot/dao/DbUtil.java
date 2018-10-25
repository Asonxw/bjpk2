package com.as.boot.dao;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * 
 * 数据库连接实用工具类
 * @author jack
 * @version 1.0
 *
 */
public class DbUtil 
{
	private String dbUrl="jdbc:mysql://localhost:3306/bjpk?characterEncoding=utf8&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
	private String dbUserName="root";
	private String dbPassword="";//"rootAsonbom";
	private String jdbcName="com.mysql.jdbc.Driver";
	
	/**
	 * 获取数据库连接
	 * @return
	 * @throws Exception
	 */
	public Connection getConnection() throws Exception
	{
		Class.forName(jdbcName);
		Connection con = DriverManager.getConnection(dbUrl,dbUserName,dbPassword);
		return con;
	}
	/**
	 * 数据库连接main方法
	 * @param args
	 */
	public static void main(String[] args) {
		DbUtil dbUtil=new DbUtil();
		try {
			Connection conn=null;
			conn = dbUtil.getConnection();
			if(conn != null)
			{
				System.out.println("数据库连接成功!");
			}
			else
			{
				System.out.println("数据库连接不成功!");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 关闭连接
	 * @param conn
	 * @param stmt
	 * @param rs
	 * @return
	 */
	public static boolean close(Connection conn,PreparedStatement pstat,ResultSet rs)
	{
		boolean isClosed = true;
		
		//关闭结果集
		if(rs != null)
		{
			try {
				rs.close();
				rs = null;
			} catch (SQLException e) {
				rs = null;
				isClosed = false;
				System.out.println(e.getMessage());
				System.out.println("关闭结果集发生错误!");
			}
		}
		
		//关闭语句对象
		if(pstat != null)
		{
			try {
				pstat.close();
				pstat = null;
			} catch (SQLException e) {
				pstat = null;
				isClosed = false;
				System.out.println(e.getMessage());
				System.out.println("关闭语句对象发生错误!");
			}
		}
		
		//关闭连接对象
		if(conn != null)
		{
			try {
				conn.close();
				conn = null;
			} catch (SQLException e) {
				conn = null;
				isClosed = false;
				System.out.println(e.getMessage());
				System.out.println("关闭连接对象发生错误!");
			}
		}
		
		return isClosed;
	}
	
	
	
}
