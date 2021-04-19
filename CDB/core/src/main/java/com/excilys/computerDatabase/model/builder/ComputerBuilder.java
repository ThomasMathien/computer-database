package com.excilys.computerDatabase.model.builder;


import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.excilys.computerDatabase.model.Company;
import com.excilys.computerDatabase.model.Computer;

public class ComputerBuilder {

	private long id;
	private String name;
	private LocalDate introduced;
	private LocalDate discontinued;
	private Company company;
	
	public ComputerBuilder(String name) {
		this.name = name;
	}
	
	public ComputerBuilder setId(long id) {
		this.id = id;
		return this;
	}
	
	public ComputerBuilder setName(String name) {
		this.name = name;
		return this;
	}
	
	public ComputerBuilder setIntroduced(LocalDate introduced) {
		this.introduced = introduced;
		return this;
	}
	
	public ComputerBuilder setDiscontinued(LocalDate discontinued) {
		this.discontinued = discontinued;
		return this;
	}
	
	public ComputerBuilder setCompany(Company company) {
		this.company = company;
		return this;
	}
	
	public Computer build() {
		Computer c = new Computer(name);
		c.setDiscontinued(discontinued);
		c.setIntroduced(introduced);
		c.setId(id);
		c.setCompany(company);
		return c;
	}
	
	
}
