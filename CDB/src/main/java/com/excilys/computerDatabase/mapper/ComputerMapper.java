package com.excilys.computerDatabase.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.excilys.computerDatabase.dto.ComputerFormDTO;
import com.excilys.computerDatabase.dto.ComputerToDatabaseDTO;
import com.excilys.computerDatabase.dto.builder.ComputerFormDTOBuilder;
import com.excilys.computerDatabase.exception.IncompleteResultSetException;
import com.excilys.computerDatabase.exception.invalidValuesException.InvalidValuesException;
import com.excilys.computerDatabase.model.Company;
import com.excilys.computerDatabase.model.Computer;
import com.excilys.computerDatabase.model.builder.ComputerBuilder;
import com.excilys.computerDatabase.validator.ComputerValidator;

@Component
public class ComputerMapper {
	
	private Logger logger = LoggerFactory.getLogger(ComputerMapper.class);
	
	public static final String ID_COLUMN = "id";
	public static final String NAME_COLUMN = "name";
	public static final String INTRODUCED_COLUMN = "introduced";
	public static final String DISCONTINUED_COLUMN = "discontinued";
	
	LocalDateMapper localDateMapper;
	CompanyMapper companyMapper;
	ComputerValidator computerValidator;
	
	public ComputerMapper(CompanyMapper companyMapper, ComputerValidator computerValidator, LocalDateMapper localDateMapper) {
		this.companyMapper = companyMapper;
		this.computerValidator = computerValidator;
		this.localDateMapper = localDateMapper;
	}
	
	public Optional<Computer> toComputer(ResultSet rs) throws IncompleteResultSetException {
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
				if (rs.getLong(companyMapper.ID_COLUMN) != 0) {
					Optional<Company> company = companyMapper.toCompany(rs);
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
			computerValidator.validateComputerDTO(dto);
			ComputerBuilder builder = new ComputerBuilder(dto.getName())
					.setIntroduced(localDateMapper.parseToLocalDate(dto.getIntroduced()).orElse(null))
					.setDiscontinued(localDateMapper.parseToLocalDate(dto.getDiscontinued()).orElse(null));
					if (dto.getCompanyId() != null) {
						Company company = new Company(Long.parseLong(dto.getCompanyId()));
						builder.setCompany(company);
					}
					if (dto.getId() != null) {
						builder.setId(Long.parseLong(dto.getId()));
					}
					return Optional.of(builder.build());
		} catch (InvalidValuesException e) {
			logger.warn("Couldn't map DTO to computer as values are invalid[" + e.getMessage() + "]: DTO:" + dto.toString());
			return Optional.empty();
		}
	}
	
}
