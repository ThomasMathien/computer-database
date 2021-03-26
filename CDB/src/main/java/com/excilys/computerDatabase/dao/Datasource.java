package com.excilys.computerDatabase.dao;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

class Datasource {
	
	private final String DATASOURCE_CONFIG_PATH = "/config/datasource.properties";
	
	private HikariConfig config = new HikariConfig(DATASOURCE_CONFIG_PATH);
	private HikariDataSource ds = new HikariDataSource(config);
	
	private static Datasource instance = null;
	
	private Datasource() { }
	
	public static Datasource getInstance() {
		if (instance == null) {
			instance = new Datasource();
		}
		return instance;
	}
	
	public Connection getConnection() throws SQLException {
		return ds.getConnection();
	}

}
