package com.excilys.computerDatabase.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.excilys.computerDatabase.ComputerDatabase;

class DbConnect {
	
	private final String CONFIG_PATH = "config/dbConnection.properties";
	
	String url;
	String user;
	String password;
	String driver;
	Connection conn = null;

	public DbConnect() {
		try {
			InputStream confFile = ComputerDatabase.class.getClassLoader().getResourceAsStream(CONFIG_PATH);
			if (confFile != null) {
				Properties p = new Properties();
				p.load(confFile);
				url = p.getProperty("url");
				user = p.getProperty("user");
				password = p.getProperty("password");
				driver = p.getProperty("driver");
				Class.forName(driver);
			}
			else {
				throw new FileNotFoundException();
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	public Connection getConnection() {
		if (conn == null) {
			try {
				conn = DriverManager.getConnection(url,user,password);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return conn;
	}
	
	protected void finalize(){
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	
}
