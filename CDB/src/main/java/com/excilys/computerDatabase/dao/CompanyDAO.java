package com.excilys.computerDatabase.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.excilys.computerDatabase.exception.IncompleteResultSetException;
import com.excilys.computerDatabase.mapper.CompanyMapper;
import com.excilys.computerDatabase.model.Company;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompanyDAO {
	
	private Logger logger = LoggerFactory.getLogger(CompanyDAO.class);
	
	private static final String FIND_COMPANY_BY_ID_QUERY = "SELECT id AS company_id,name AS company_name FROM company WHERE id=?;";
	private static final String GET_COMPANY_COUNT_QUERY = "SELECT COUNT(*) FROM company;";
	private static final String FIND_COMPANIES_INTERVAL_QUERY = "SELECT id AS company_id,name AS company_name FROM company ORDER BY id LIMIT ? OFFSET ?;";
	
	private static CompanyDAO instance = null;
	
	private CompanyDAO() {}
	
	public static CompanyDAO getInstance() {
		if (instance == null) {
			instance = new CompanyDAO();
		}
		return instance;
	}
	
	public List<Company> getCompanies(){
		return getCompanies(0,getCompanyCount());
	}
	
	public List<Company> getCompanies(int from, int amount){
		List<Company> companies = new ArrayList<>();
		try (Connection conn = new DbConnect().getConnection();
				PreparedStatement stmt = conn.prepareStatement(FIND_COMPANIES_INTERVAL_QUERY)){
			stmt.setLong(1, amount);
			stmt.setLong(2,from);
			try (ResultSet results = stmt.executeQuery()){
				while(results.next()) {
					Optional<Company> company;
					try {
						company = CompanyMapper.getInstance().toCompany(results);
						if (company.isPresent()) {
							companies.add(company.orElseThrow());
						}
					} catch (IncompleteResultSetException e) {
						logger.error("Creating Company from ResultSet Failed: with resultSet "+results.toString(),e );
						e.printStackTrace();
					}
				}
			}
		} catch (SQLException e) {
			logger.error("Get Companies SQL Request Failed: with request "+FIND_COMPANIES_INTERVAL_QUERY+" for "+amount+" rows from "+from,e );
		}
		return companies;
	}
	
	public Optional<Company> findCompany(long id){
		Optional<Company> company = Optional.empty();
		try (Connection conn = new DbConnect().getConnection();
				PreparedStatement stmt = conn.prepareStatement(FIND_COMPANY_BY_ID_QUERY)){
			stmt.setLong(1, id);
			try (ResultSet results = stmt.executeQuery()){
				if(results.next()) {
					try {
						company =  CompanyMapper.getInstance().toCompany(results);
					} catch (IncompleteResultSetException e) {
						logger.error("Creating Company from ResultSet Failed: with resultSet "+results.toString(),e );
					}
				}	
			}
		} catch (SQLException e) {
			logger.error("Find Company SQL Request Failed: with request "+FIND_COMPANY_BY_ID_QUERY+" for Id"+id,e );
		}
		return company;
	}
	
	public int getCompanyCount() {
		try (Connection conn = new DbConnect().getConnection();
				Statement stmt = conn.createStatement();
				ResultSet results = stmt.executeQuery(GET_COMPANY_COUNT_QUERY)){
			if(results.next()) {
				return results.getInt(1);
			}
		} catch (SQLException e) {
			logger.error("Get Company Count SQL Request Failed: with request "+GET_COMPANY_COUNT_QUERY,e );
		}
		return 0;
	}

}
