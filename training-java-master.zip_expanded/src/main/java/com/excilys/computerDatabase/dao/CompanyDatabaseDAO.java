package main.java.com.excilys.computerDatabase.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import main.java.com.excilys.computerDatabase.exception.IncompleteResultSetException;
import main.java.com.excilys.computerDatabase.mapper.CompanyMapper;
import main.java.com.excilys.computerDatabase.model.Company;

public class CompanyDatabaseDAO {
	
	private static final String FIND_COMPANY_BY_ID_QUERY = "SELECT id AS company_id,name AS company_name FROM company WHERE id=?;";
	private static final String GET_COMPANY_COUNT_QUERY = "SELECT COUNT(*) FROM company;";
	private static final String FIND_COMPANIES_INTERVAL_QUERY = "SELECT id AS company_id,name AS company_name FROM company ORDER BY id LIMIT ? OFFSET ?;";
	
	private static CompanyDatabaseDAO instance = null;
	
	public static CompanyDatabaseDAO getInstance() {
		if (instance == null) {
			instance = new CompanyDatabaseDAO();
		}
		return instance;
	}
	
	public List<Company> getCompanies(){
		return getCompanies(0,getCompanyCount());
	}
	
	public List<Company> getCompanies(int from, int amount){
		List<Company> companies = new ArrayList<>();
		try (Connection conn = new DbConnect().getConnection()){
			PreparedStatement stmt = conn.prepareStatement(FIND_COMPANIES_INTERVAL_QUERY);
			stmt.setLong(1, amount);
			stmt.setLong(2,from);
			ResultSet results = stmt.executeQuery();
			while(results.next()) {
				Optional<Company> c;
				try {
					c = CompanyMapper.toCompany(results);
					if (c.isPresent()) {
						companies.add(c.orElseThrow());
					}
				} catch (IncompleteResultSetException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return companies;
	}
	
	public Optional<Company> findCompany(long id){
		Optional<Company> company = Optional.empty();
		try (Connection conn = new DbConnect().getConnection()){
			PreparedStatement stmt = conn.prepareStatement(FIND_COMPANY_BY_ID_QUERY);
			stmt.setLong(1, id);
			ResultSet results = stmt.executeQuery();
			if(results.next()) {
				try {
					company =  CompanyMapper.toCompany(results);
				} catch (IncompleteResultSetException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return company;
	}
	
	public int getCompanyCount() {
		try (Connection conn = new DbConnect().getConnection()){
			Statement stmt = conn.createStatement();
			ResultSet results = stmt.executeQuery(GET_COMPANY_COUNT_QUERY);
			if(results.next()) {
				return results.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

}
