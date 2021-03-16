package com.excilys.computerDatabase.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.computerDatabase.exception.IncompleteResultSetException;
import com.excilys.computerDatabase.model.Company;

public class CompanyMapper {
	
	private Logger logger = LoggerFactory.getLogger(CompanyMapper.class);
	
	public final static String ID_COLUMN = "company_id";
	public final static String NAME_COLUMN = "company_name";
	
	
	private CompanyMapper() {}
	private static CompanyMapper instance;
	
	public static CompanyMapper getInstance() {
		if (instance == null) {
			instance = new CompanyMapper();
		}
		return instance;
	}
	
	public Optional<Company> toCompany(ResultSet rs) throws IncompleteResultSetException{
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
			company =  Optional.ofNullable(new Company(id,name));
		}catch(SQLException e) {
			logger.error("Company Mapping Failed: for ResultSet "+rs.toString(),e);
		}
		return company;
	}
	
}