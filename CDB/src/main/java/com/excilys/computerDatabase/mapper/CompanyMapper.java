package com.excilys.computerDatabase.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.excilys.computerDatabase.dto.CompanyDTO;
import com.excilys.computerDatabase.exception.IncompleteResultSetException;
import com.excilys.computerDatabase.model.Company;

@Component
public class CompanyMapper implements RowMapper<Company> {
	
	private Logger logger = LoggerFactory.getLogger(CompanyMapper.class);
	
	public final String ID_COLUMN = "company_id";
	public final String NAME_COLUMN = "company_name";
	
	public Optional<Company> toCompany(ResultSet rs) throws IncompleteResultSetException {
		if (rs == null) {
			throw new IllegalArgumentException();
		}
		Optional<Company> company = Optional.empty();
		try {
			long id = rs.getLong(ID_COLUMN);
			String name =  rs.getString(NAME_COLUMN);
			if (id == 0) {
				throw new IncompleteResultSetException("ResultSet requires Id to create a Company object");
			}
			company =  Optional.ofNullable(new Company(id, name));
		} catch (SQLException e) {
			logger.error("Company Mapping Failed: for ResultSet " + rs.toString(), e);
		}
		return company;
	}
	

	public CompanyDTO toCompanyDTO(Optional<Company> company) {
		CompanyDTO dto = new CompanyDTO();
		if (company.isPresent()) {
			dto.setId(String.valueOf(company.orElseThrow().getId()));
			dto.setName(company.orElseThrow().getName());
		}
		return dto;
	}


	@Override
	public Company mapRow(ResultSet rs, int rowNum) throws SQLException {
		Company company = null;
		try {
			long id = rs.getLong(ID_COLUMN);
			String name =  rs.getString(NAME_COLUMN);
			if (id == 0) {
				throw new IncompleteResultSetException("ResultSet requires Id to create a Company object");
			}
			company =  new Company(id, name);
		} catch (SQLException e) {
			logger.error("Company Mapping Failed: for ResultSet " + rs.toString(), e);
			throw e;
		} catch (IncompleteResultSetException e) {
			logger.error(e.getMessage());
		}
		return company;
	}
	
}