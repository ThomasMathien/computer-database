package com.excilys.computerDatabase.model.builder;


import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.excilys.computerDatabase.model.Company;
import com.excilys.computerDatabase.model.Computer;
import com.excilys.computerDatabase.service.CompanyService;

public class ComputerBuilder {

	private long id;
	private String name;
	private LocalDate introduced;
	private LocalDate discontinued;
	private Company company;

	@Autowired
	CompanyService companyService;
	
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
	
	public ComputerBuilder setCompany(Long companyId) {
		Optional<Company> company = companyService.findCompany(companyId);
		if (company.isPresent()) {
			this.company = company.orElseThrow();
		}
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
