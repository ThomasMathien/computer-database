package com.excilys.computerDatabase.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.computerDatabase.ComputerDatabase;

class DbConnect {

	private Logger logger = LoggerFactory.getLogger(DbConnect.class);
	
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
		} catch (FileNotFoundException e) {
			logger.error("DB Connection Properties File Not Found",e);
		} catch (IOException e) {
			logger.error("DB Connection Properties Loading Failed",e);
		} catch (ClassNotFoundException e) {
			logger.error("DB Connection Properties Driver Name "+driver+" not found",e);
		}
		
	}
	
	public Connection getConnection() {
		if (conn == null) {
			try {
				conn = DriverManager.getConnection(url,user,password);
			} catch (SQLException e) {
				logger.error("Connection instanciation failed: for URL "+url);
			}
		}
		return conn;
	}

}
