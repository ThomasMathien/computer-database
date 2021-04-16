package com.excilys.computerDatabase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.excilys.computerDatabase.config.CLIWebAppInitializer;
import com.excilys.computerDatabase.config.SpringWebConfig;
import com.excilys.computerDatabase.config.WebInitializer;
import com.excilys.computerDatabase.controller.cli.CLIController;


public class ComputerDatabase {
    
	public static void main(String[] args) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(CLIWebAppInitializer.class);
        CLIController cli = ctx.getBean(CLIController.class);
        cli.run();
    	((ConfigurableApplicationContext) ctx).close();
	}

}
