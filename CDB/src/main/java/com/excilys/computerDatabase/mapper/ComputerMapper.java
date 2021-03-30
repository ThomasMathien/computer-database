package com.excilys.computerDatabase.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.computerDatabase.dto.ComputerFormDTO;
import com.excilys.computerDatabase.dto.ComputerToDatabaseDTO;
import com.excilys.computerDatabase.dto.builder.ComputerFormDTOBuilder;
import com.excilys.computerDatabase.dto.builder.ComputerToDatabaseDTOBuilder;
import com.excilys.computerDatabase.exception.IncompleteResultSetException;
import com.excilys.computerDatabase.exception.invalidValuesException.InvalidValuesException;
import com.excilys.computerDatabase.model.Company;
import com.excilys.computerDatabase.model.Computer;
import com.excilys.computerDatabase.model.builder.ComputerBuilder;
import com.excilys.computerDatabase.validator.ComputerValidator;

public class ComputerMapper {
	
	private Logger logger = LoggerFactory.getLogger(ComputerMapper.class);
	
	public static final String ID_COLUMN = "id";
	public static final String NAME_COLUMN = "name";
	public static final String INTRODUCED_COLUMN = "introduced";
	public static final String DISCONTINUED_COLUMN = "discontinued";
	
	
	private ComputerMapper() { }
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
		} catch (SQLException e) {
			logger.error("Computer Mapping Failed: for ResultSet " + rs.toString(), e);
		}
		return computer;
	}
	
	public ComputerFormDTO toComputerFormDTO(Computer c) {
		ComputerFormDTOBuilder builder = new ComputerFormDTOBuilder()
				.setId(String.valueOf(c.getId()))
				.setName(c.getName())
				.setIntroduced(c.getIntroduced() != null ? String.valueOf(c.getIntroduced()) : "")
				.setDiscontinued(c.getDiscontinued() != null ? String.valueOf(c.getDiscontinued()) : "");
			if (c.getCompany() != null) {
				builder.setCompanyName(c.getCompany().getName());
			}
		return builder.build();
	}

	public Optional<Computer> toComputer(ComputerToDatabaseDTO dto) {
		try {
			ComputerValidator.getInstance().validateComputerDTO(dto);
			ComputerBuilder builder = new ComputerBuilder(dto.getName())
					.setIntroduced(parseToLocalDate(dto.getIntroduced()).orElse(null))
					.setDiscontinued(parseToLocalDate(dto.getDiscontinued()).orElse(null));
					if (dto.getCompanyId() != null) {
						builder.setCompany(Long.parseLong(dto.getCompanyId()));
					}
					if (dto.getId() != null) {
						builder.setId(Long.parseLong(dto.getId()));
					}
					return Optional.of(builder.build());
		} catch (InvalidValuesException e) {
			logger.warn("Couldn't map DTO to computer as values are invalid["+e.getMessage()+"]: DTO:"+dto.toString());
		}
		return Optional.empty();
	}
	
	public Optional<LocalDate> parseToLocalDate(String date) {
		try {
			return Optional.of(LocalDate.parse(date));
		} catch (DateTimeParseException e) {
			return Optional.empty();
		}
	}
	
}
