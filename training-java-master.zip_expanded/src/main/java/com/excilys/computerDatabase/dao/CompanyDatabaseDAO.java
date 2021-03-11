package main.java.com.excilys.computerDatabase.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import main.java.com.excilys.computerDatabase.exception.IncompleteResultSet;
import main.java.com.excilys.computerDatabase.mapper.CompanyMapper;
import main.java.com.excilys.computerDatabase.model.Company;

public abstract class CompanyDatabaseDAO {
	
	private final static String GET_ALL_COMPANIES_QUERY = "SELECT * FROM company;";
	private static final String FIND_COMPANY_BY_ID_QUERY = "SELECT id,name FROM company WHERE id=?;";
	private static final String GET_COMPANY_COUNT_QUERY = "SELECT COUNT(*) FROM company;";;
	
	public static List<Company> getCompanies(){
		List<Company> companies = new ArrayList<>();
		try (Connection conn = new DbConnect().getConnection()){
			Statement stmt = conn.createStatement();
			ResultSet results = stmt.executeQuery(GET_ALL_COMPANIES_QUERY);
			while(results.next()) {
				Company c;
				try {
					c = CompanyMapper.toCompany(results);
					companies.add(c);
				} catch (IncompleteResultSet e) {
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return companies;
	}
	
	public static Company findCompany(long id){
		Company company = null;
		try (Connection conn = new DbConnect().getConnection()){
			PreparedStatement stmt = conn.prepareStatement(FIND_COMPANY_BY_ID_QUERY);
			stmt.setLong(1, id);
			ResultSet results = stmt.executeQuery();
			if(results.next()) {
				try {
					company =  CompanyMapper.toCompany(results);
				} catch (IncompleteResultSet e) {
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return company;
	}
	

	public static int getCompanyCount() {
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
