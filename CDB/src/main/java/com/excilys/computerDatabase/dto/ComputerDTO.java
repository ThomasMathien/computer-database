package com.excilys.computerDatabase.dto;

public class ComputerDTO {
	
	private String id;
	private String name;
	private String introduced;
	private String discontinued;
	private String companyName;
	
	public void setId(String id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setIntroduced(String introduced) {
		this.introduced = introduced;
	}
	public void setDiscontinued(String discontinued) {
		this.discontinued = discontinued;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	public String getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getIntroduced() {
		return introduced;
	}
	public String getDiscontinued() {
		return discontinued;
	}
	public String getCompanyName() {
		return companyName;
	}
	
	
}
