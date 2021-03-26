package com.excilys.computerDatabase.dto;

public class ComputerToDatabaseDTO {
	
	private String id;
	private String name;
	private String introduced;
	private String discontinued;
	private String companyId;
	
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
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
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
	public String getCompanyId() {
		return companyId;
	}
	
}
