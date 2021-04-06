package com.excilys.computerDatabase.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.AbstractContextLoaderInitializer;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@ComponentScan({"com.excilys.computerDatabase.controller.servlet",
		"com.excilys.computerDatabase.controller.cli",
		"com.excilys.computerDatabase.controller.page",
		"com.excilys.computerDatabase.dao",
		"com.excilys.computerDatabase.mapper",
		"com.excilys.computerDatabase.service",
		"com.excilys.computerDatabase.validator"})
public class SpringWebConfig extends AbstractContextLoaderInitializer {
	
 	private final String DATASOURCE_CONFIG_PATH = "/config/datasource.properties";
 	
    @Override
    protected WebApplicationContext createRootApplicationContext() {
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(SpringWebConfig.class);
        return rootContext;
    }
	
	@Bean
	public DataSource getDataSource() {
		return new HikariDataSource(new HikariConfig(DATASOURCE_CONFIG_PATH));
	}

}
