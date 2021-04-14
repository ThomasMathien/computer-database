package com.excilys.computerDatabase.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.excilys.computerDatabase.dto.AddComputerFormDTO;
import com.excilys.computerDatabase.dto.ComputerFormDTO;
import com.excilys.computerDatabase.dto.EditComputerFormDTO;
import com.excilys.computerDatabase.dto.builder.ComputerFormDTOBuilder;
import com.excilys.computerDatabase.exception.IncompleteResultSetException;
import com.excilys.computerDatabase.model.Company;
import com.excilys.computerDatabase.model.Computer;
import com.excilys.computerDatabase.model.builder.ComputerBuilder;

@Component
public class ComputerMapper implements RowMapper<Computer> {
	
	private Logger logger = LoggerFactory.getLogger(ComputerMapper.class);
	
	public static final String ID_COLUMN = "id";
	public static final String NAME_COLUMN = "name";
	public static final String INTRODUCED_COLUMN = "introduced";
	public static final String DISCONTINUED_COLUMN = "discontinued";
	
	LocalDateMapper localDateMapper;
	CompanyMapper companyMapper;
	
	public ComputerMapper(CompanyMapper companyMapper, LocalDateMapper localDateMapper) {
		this.companyMapper = companyMapper;
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

	public Optional<Computer> toComputer(EditComputerFormDTO dto) {
		try {
			ComputerBuilder builder = new ComputerBuilder(dto.getName());
					if (dto.getIntroduced() != null) {
						builder.setIntroduced(localDateMapper.parseToLocalDate(dto.getIntroduced()).orElse(null));
					}
					if (dto.getDiscontinued() != null) {
						builder.setDiscontinued(localDateMapper.parseToLocalDate(dto.getDiscontinued()).orElse(null));;
					}
					if (dto.getCompanyId() != null && Long.parseLong(dto.getCompanyId()) != 0) {
						Company company = new Company(Long.parseLong(dto.getCompanyId()));
						builder.setCompany(company);
					}
					if (dto.getId() != null) {
						builder.setId(Long.parseLong(dto.getId()));
					}
					return Optional.of(builder.build());
		} catch (NumberFormatException e) {
			logger.warn("Couldn't map DTO to computer as values are invalid[" + e.getMessage() + "]: DTO:" + dto.toString());
			return Optional.empty();
		}
	}
	
	public Optional<Computer> toComputer(@Valid AddComputerFormDTO dto) {
		try {
			ComputerBuilder builder = new ComputerBuilder(dto.getName());
					if (dto.getIntroduced() != null) {
						builder.setIntroduced(localDateMapper.parseToLocalDate(dto.getIntroduced()).orElse(null));
					}
					if (dto.getDiscontinued() != null) {
						builder.setDiscontinued(localDateMapper.parseToLocalDate(dto.getDiscontinued()).orElse(null));;
					}
					if (dto.getCompanyId() != null && Long.parseLong(dto.getCompanyId()) != 0) {
						Company company = new Company(Long.parseLong(dto.getCompanyId()));
						builder.setCompany(company);
					}
					return Optional.of(builder.build());
		} catch (NumberFormatException e) {
			logger.warn("Couldn't map DTO to computer as values are invalid[" + e.getMessage() + "]: DTO:" + dto.toString());
			return Optional.empty();
		}
	}

	@Override
	public Computer mapRow(ResultSet rs, int rowNum) throws SQLException {
		Computer computer = null;
		try {
			String name = rs.getString(NAME_COLUMN);
			long id = rs.getLong(ID_COLUMN);
			if (id == 0) {
				throw new IncompleteResultSetException("ResultSet requires Id to create a Computer object");
			}
			java.sql.Date introduced = rs.getDate(INTRODUCED_COLUMN);
			java.sql.Date discontinued = rs.getDate(DISCONTINUED_COLUMN);
			computer = new ComputerBuilder(name)
					.setId(id)
					.setIntroduced(introduced != null ? introduced.toLocalDate() : null)
					.setDiscontinued(discontinued != null ? discontinued.toLocalDate() : null)
					.build();
			if (rs.getLong(companyMapper.ID_COLUMN) != 0) {
				Company company = companyMapper.mapRow(rs, rowNum);
				computer.setCompany(company);
			}
		} catch (SQLException e) {
			logger.error("Computer Mapping Failed: for ResultSet " + rs.toString(), e);
			throw e;
		} catch (IncompleteResultSetException e) {
			logger.error(e.getMessage());
		}
		return computer;
	}
	
}
