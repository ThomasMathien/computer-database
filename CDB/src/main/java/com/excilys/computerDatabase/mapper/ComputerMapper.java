package com.excilys.computerDatabase.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.computerDatabase.dto.ComputerDTO;
import com.excilys.computerDatabase.exception.IncompleteResultSetException;
import com.excilys.computerDatabase.model.Company;
import com.excilys.computerDatabase.model.Computer;
import com.excilys.computerDatabase.model.builder.ComputerBuilder;
import com.excilys.computerDatabase.validator.InputValidator;

public class ComputerMapper {
	
	private Logger logger = LoggerFactory.getLogger(ComputerMapper.class);
	
	public final static String ID_COLUMN = "id";
	public final static String NAME_COLUMN = "name";
	public final static String INTRODUCED_COLUMN = "introduced";
	public final static String DISCONTINUED_COLUMN = "discontinued";
	
	
	private ComputerMapper() {}
	private static ComputerMapper instance;
	
	public static ComputerMapper getInstance() {
		if (instance == null) {
			instance = new ComputerMapper();
		}
		return instance;
	}
	
	public Optional<Computer> toComputer(ResultSet rs) throws IncompleteResultSetException{
		if (rs == null) {
			throw new IllegalArgumentException();
		}
		Optional<Computer> computer = Optional.empty();
		try {
			String name = rs.getString(NAME_COLUMN);
			long id = rs.getLong(ID_COLUMN);
			if (id == 0) {
				throw new IncompleteResultSetException("ResultSet requires Id to create a Computer object");
			}
			java.sql.Date introduced = rs.getDate(INTRODUCED_COLUMN);
			java.sql.Date discontinued = rs.getDate(DISCONTINUED_COLUMN);
			computer = Optional.of(new ComputerBuilder(name)
					.setId(id)
					.setIntroduced(introduced != null ? introduced.toLocalDate() : null)
					.setDiscontinued(discontinued != null ? discontinued.toLocalDate() : null)
					.build());
			if (computer.isPresent()) {
				if (rs.getLong(CompanyMapper.getInstance().ID_COLUMN) != 0) {
					Optional<Company> company = CompanyMapper.getInstance().toCompany(rs);
					if (company.isPresent()) {
						((Computer) computer.orElseThrow()).setCompany(company.orElseThrow());
					}
				}
			}
		}
		catch (SQLException e) {
			logger.error("Computer Mapping Failed: for ResultSet " + rs.toString(), e);
		}
		return computer;
	}
	
	public ComputerDTO toComputerDTO(Optional<Computer> computer) {
		ComputerDTO dto = new ComputerDTO();
		if (computer.isPresent()) {
			dto.setId(String.valueOf(computer.orElseThrow().getId()));
			dto.setName(computer.orElseThrow().getName());
			String introduced = String.valueOf(computer.orElseThrow().getIntroduced());
			dto.setIntroduced(introduced != "null" ? introduced : "");
			String discontinued = String.valueOf(computer.orElseThrow().getDiscontinued());
			dto.setDiscontinued(discontinued != "null" ? discontinued : "");
			if (computer.orElseThrow().getCompany() != null) {
				dto.setCompanyName(computer.orElseThrow().getCompany().getName());
			}
		}
		return dto;
	}
	
	public Computer toComputer(String name, String introduced, String discontinued, String companyId) {
		if (name == null) {
			throw new IllegalArgumentException();
		}
		ComputerBuilder builder = new ComputerBuilder(name);
		if (InputValidator.isValidDate(introduced)) {
			builder.setIntroduced(LocalDate.parse(introduced));
		}
		if (InputValidator.isValidDate(discontinued)) {
			builder.setDiscontinued(LocalDate.parse(discontinued));
		}
		if (!"0".equals(companyId)) {
			builder.setCompany(Long.parseLong(companyId));
		}
		return builder.build();
	}
	
}
