package main.java.com.excilys.computerDatabase.model.builder;

import java.sql.Timestamp;

import main.java.com.excilys.computerDatabase.dao.CompanyDatabaseDAO;
import main.java.com.excilys.computerDatabase.model.Company;
import main.java.com.excilys.computerDatabase.model.Computer;

public class ComputerBuilder {

	private long id;
	private String name;
	private Timestamp introduced;
	private Timestamp discontinued;
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
	
	public ComputerBuilder setIntroduced(Timestamp introduced) {
		this.introduced = introduced;
		return this;
	}
	
	public ComputerBuilder setDiscontinued(Timestamp discontinued) {
		this.discontinued = discontinued;
		return this;
	}
	
	public ComputerBuilder setCompany(Company company) {
		this.company = company;
		return this;
	}
	
	public ComputerBuilder setCompany(Long companyId) {
		this.company = CompanyDatabaseDAO.findCompany(companyId);
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
