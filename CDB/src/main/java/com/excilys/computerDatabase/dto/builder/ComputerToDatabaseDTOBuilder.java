package com.excilys.computerDatabase.dto.builder;

import com.excilys.computerDatabase.dto.ComputerToDatabaseDTO;

public class ComputerToDatabaseDTOBuilder {
	
	private String id;
	private String name;
	private String introduced;
	private String discontinued;
	private String companyId;
	
	public ComputerToDatabaseDTOBuilder() { }

	public ComputerToDatabaseDTOBuilder setId(String id) {
		this.id = id;
		return this;
	}

	public ComputerToDatabaseDTOBuilder setName(String name) {
		this.name = name;
		return this;
	}

	public ComputerToDatabaseDTOBuilder setIntroduced(String introduced) {
		this.introduced = introduced;
		return this;
	}

	public ComputerToDatabaseDTOBuilder setDiscontinued(String discontinued) {
		this.discontinued = discontinued;
		return this;
	}

	public ComputerToDatabaseDTOBuilder setCompanyId(String companyId) {
		this.companyId = companyId;
		return this;
	}
	
	public ComputerToDatabaseDTO build() {
		ComputerToDatabaseDTO dto = new ComputerToDatabaseDTO();
		dto.setId(id);
		dto.setName(name);
		dto.setIntroduced(introduced);
		dto.setDiscontinued(discontinued);
		dto.setCompanyId(companyId);
		return dto;
	}
}
