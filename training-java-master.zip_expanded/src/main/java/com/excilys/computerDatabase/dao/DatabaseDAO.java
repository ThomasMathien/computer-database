package main.java.com.excilys.computerDatabase.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import main.java.com.excilys.computerDatabase.mapper.CompanyMapper;
import main.java.com.excilys.computerDatabase.mapper.ComputerMapper;
import main.java.com.excilys.computerDatabase.model.Company;
import main.java.com.excilys.computerDatabase.model.Computer;

public abstract class DatabaseDAO {
	
	public static List<Computer> getComputers(){
		List<Computer> computers = new ArrayList<>();
		final String QUERY = "SELECT * FROM computer;";
		try (Connection conn = new DbConnect().getConnection()){
			Statement stmt = conn.createStatement();
			ResultSet results = stmt.executeQuery(QUERY);
			while(results.next()) {
				Computer c = ComputerMapper.toComputer(results);
				computers.add(c);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return computers;
	}
	
	public static List<Company> getCompanies(){
		List<Company> companies = new ArrayList<>();
		final String QUERY = "SELECT * FROM company;";
		try (Connection conn = new DbConnect().getConnection()){
			Statement stmt = conn.createStatement();
			ResultSet results = stmt.executeQuery(QUERY);
			while(results.next()) {
				Company c = CompanyMapper.toCompany(results);
				companies.add(c);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return companies;
	}
	
	public static long addComputer(Computer c) throws SQLException {
		long id = 0;
		final String QUERY = "INSERT INTO computer (name,introduced,discontinued,company_id) VALUES (?,?,?,?);";
		try(Connection conn = new DbConnect().getConnection()) {
			PreparedStatement stmt = conn.prepareStatement(QUERY, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1,c.getName());
			stmt.setTimestamp(2,c.getIntroduced());
			stmt.setTimestamp(3,c.getDiscontinued());
			if (c.getCompany() != null) {
				stmt.setLong(4,c.getCompany().getId());
			}
			else {
				stmt.setNull(4,Types.BIGINT);
			}
			stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if(rs.next())
            {
                id = rs.getInt(1);
            }
		} catch (SQLException e) {
			throw e; 
		}
		System.out.println("Added id: "+id);
		return id;
	}
}
