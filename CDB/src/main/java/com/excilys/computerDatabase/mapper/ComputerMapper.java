package com.excilys.computerDatabase.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.computerDatabase.dto.ComputerDTO;
import com.excilys.computerDatabase.exception.IncompleteResultSetException;
import com.excilys.computerDatabase.model.Company;
import com.excilys.computerDatabase.model.Computer;
import com.excilys.computerDatabase.model.builder.ComputerBuilder;

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
			computer = Optional.of(new ComputerBuilder(name)
					.setId(id)
					.setIntroduced(rs.getTimestamp(INTRODUCED_COLUMN))
					.setDiscontinued(rs.getTimestamp(DISCONTINUED_COLUMN))
					.build());
			if (computer.isPresent()) {
				if (rs.getLong(CompanyMapper.ID_COLUMN) != 0) {
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
	
}
