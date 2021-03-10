package main.java.com.excilys.computerDatabase.model;
import java.sql.Timestamp;

import main.java.com.excilys.computerDatabase.dao.DatabaseDAO;

public class Computer {
	private long id;
	private String name;
	private Timestamp introduced;
	private Timestamp discontinued;
	private Company company;
	
	public Computer(String name) {
		this(name,null,null,null);
	}

	public Computer(String name, Timestamp introduced, Timestamp discontinued, Company company) {
		this(0L,name,introduced,discontinued, company);
	}
	
	public Computer(String name, Timestamp introduced, Timestamp discontinued, long companyId) {
		this(name);
		this.setIntroduced(introduced);
		this.setDiscontinued(discontinued);
		Company c = DatabaseDAO.findCompany(companyId);
		this.setCompany(c);
	}
	
	public Computer(long id, String name, Timestamp introduced, Timestamp discontinued, Company company) {
		super();
		this.setId(id);
		this.setName(name);
		this.setIntroduced(introduced);
		this.setDiscontinued(discontinued);
		this.setCompany(company);
	}

	public void setId(long id) {
		if (id < 0) {
			throw new IllegalArgumentException();
		}
		this.id = id;
	}
	
	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		if (name.length()>255) {
			throw new IllegalArgumentException();
		}
		this.name = name;
	}
	
	public Timestamp getIntroduced() {
		return introduced;
	}
	
	public void setIntroduced(Timestamp introduced) {
		if (this.introduced != null && this.discontinued != null && this.introduced.compareTo(this.discontinued) > 0) {
			throw new IllegalArgumentException();
		}
		this.introduced = introduced;
	}
	
	public Timestamp getDiscontinued() {
		return discontinued;
	}
	
	public void setDiscontinued(Timestamp discontinued) {
		if (this.introduced != null && this.discontinued != null && this.introduced.compareTo(this.discontinued) < 0) {
			throw new IllegalArgumentException();
		}
		this.discontinued = discontinued;
	}
	
	public Company getCompany() {
		return company;
	}
	
	public void setCompany(Company company) {
		this.company = company;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder()
				.append("Computer: ")
				.append(this.name)
				.append(" [")
				.append(this.introduced != null ? this.introduced : "unknown")
				.append(" to ")
				.append(this.discontinued != null ? this.discontinued : "unknown")
				.append("] Company: ")
				.append(this.company != null ? this.company : "unknown");
		return sb.toString();
	}
	
	
}
