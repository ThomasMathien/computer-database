package com.excilys.computerDatabase.config;


import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@ComponentScan({"com.excilys.computerDatabase.controller.servlet",
	"com.excilys.computerDatabase.controller.cli",
	"com.excilys.computerDatabase.controller.page",
	"com.excilys.computerDatabase.repository",
	"com.excilys.computerDatabase.mapper",
	"com.excilys.computerDatabase.service",
	"com.excilys.computerDatabase.validator",
	"com.excilys.computerDatabase.session"})
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.excilys.computerDatabase.repository")
public class CLIWebAppInitializer implements WebApplicationInitializer {

 	private final String DATASOURCE_CONFIG_PATH = "/config/datasource.properties";
 	
	public void onStartup(ServletContext container) throws ServletException {
		AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
		context.register(SpringWebConfig.class);
		context.setServletContext(container);
		context.close();
	}

	@Bean
	public DataSource getDataSource() {
		return new HikariDataSource(new HikariConfig(DATASOURCE_CONFIG_PATH));
	}
	
	@Bean
	public JdbcTemplate getJdbcTemplate(DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}
	
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setDatabase(Database.MYSQL);

		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(getDataSource());
		em.setPackagesToScan("com.excilys.computerDatabase.model");
		em.setJpaVendorAdapter(vendorAdapter);

		return em;
	}
	
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }

	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}
}