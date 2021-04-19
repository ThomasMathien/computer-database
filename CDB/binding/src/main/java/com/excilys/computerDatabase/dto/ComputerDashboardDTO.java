package com.excilys.computerDatabase.dto;

import java.time.LocalDate;

public interface ComputerDashboardDTO {

	String getId();
	String getName();
	LocalDate getIntroduced();
	LocalDate getDiscontinued();
	String getCompanyName();
	
}
