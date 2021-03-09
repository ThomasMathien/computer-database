package main.java.com.excilys.computerDatabase.model;
import java.sql.Timestamp;

public class Computer {
	private long id;
	private String name;
	private Timestamp introduced;
	private Timestamp discontinued;
	private Company company;
	
	public Computer(String name) {
		this.name = name;
	}
	
	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Timestamp getIntroduced() {
		return introduced;
	}
	public void setIntroduced(Timestamp introduced) {
		this.introduced = introduced;
	}
	public Timestamp getDiscontinued() {
		return discontinued;
	}
	public void setDiscontinued(Timestamp discontinued) {
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
