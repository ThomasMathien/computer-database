package com.excilys.computerDatabase.dto.builder;

import com.excilys.computerDatabase.dto.ComputerFormDTO;

public class ComputerFormDTOBuilder {

	private String id;
	private String name;
	private String introduced;
	private String discontinued;
	private String companyName;
	
	public ComputerFormDTOBuilder() { }

	public ComputerFormDTOBuilder setId(String id) {
		this.id = id;
		return this;
	}

	public ComputerFormDTOBuilder setName(String name) {
		this.name = name;
		return this;
	}

	public ComputerFormDTOBuilder setIntroduced(String introduced) {
		this.introduced = introduced;
		return this;
	}

	public ComputerFormDTOBuilder setDiscontinued(String discontinued) {
		this.discontinued = discontinued;
		return this;
	}

	public ComputerFormDTOBuilder setCompanyName(String companyName) {
		this.companyName = companyName;
		return this;
	}
	
	public ComputerFormDTO build() {
		ComputerFormDTO dto = new ComputerFormDTO();
		dto.setId(id);
		dto.setName(name);
		dto.setIntroduced(introduced);
		dto.setDiscontinued(discontinued);
		dto.setCompanyName(companyName);
		return dto;
	}
}
