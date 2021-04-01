package com.excilys.computerDatabase.dao;

import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.stereotype.Component;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Component
class Datasource {
	
	private final String DATASOURCE_CONFIG_PATH = "/config/datasource.properties";
	
	private HikariConfig config = new HikariConfig(DATASOURCE_CONFIG_PATH);
	private HikariDataSource datasource = new HikariDataSource(config);
	
	public Connection getConnection() throws SQLException {
		return datasource.getConnection();
	}

}
