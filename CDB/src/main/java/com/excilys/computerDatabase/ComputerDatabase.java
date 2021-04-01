package com.excilys.computerDatabase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.excilys.computerDatabase.controller.cli.CLIController;

@Component
public class ComputerDatabase {
    
	@Autowired
	CLIController cli;
	public static void main(String[] args) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext("com.excilys.computerDatabase");
        ComputerDatabase cdb = ctx.getBean(ComputerDatabase.class);
        cdb.cli.run();
	}

}
