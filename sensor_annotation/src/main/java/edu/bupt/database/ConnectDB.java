package edu.bupt.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {
	
	@SuppressWarnings("finally")
	public static Connection getConnection() {
		String user = "root";
		String password = "930208";
		//本地测试
		String url = "jdbc:mysql://localhost:3306/sensor_annotation?serverTimezone=PRC&useUnicode=true&characterEncoding=UTF-8&useSSL=false"; 
		Connection con = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(url, user, password);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			return con;
		}
	}
}
