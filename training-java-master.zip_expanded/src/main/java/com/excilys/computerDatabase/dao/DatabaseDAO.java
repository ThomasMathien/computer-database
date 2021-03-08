package main.java.com.excilys.computerDatabase.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
}
