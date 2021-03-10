package main.java.com.excilys.computerDatabase.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import main.java.com.excilys.computerDatabase.exception.IncompleteResultSet;
import main.java.com.excilys.computerDatabase.mapper.CompanyMapper;
import main.java.com.excilys.computerDatabase.mapper.ComputerMapper;
import main.java.com.excilys.computerDatabase.model.Company;
import main.java.com.excilys.computerDatabase.model.Computer;

public abstract class DatabaseDAO {
	
	private final static String GET_ALL_COMPUTERS_QUERY = "SELECT computer.id AS id, computer.name AS name, "
			+ "introduced, discontinued, computer.company_id, company.name AS company_name "
			+ "FROM computer LEFT JOIN company ON computer.company_id = company.id;";
	private final static String GET_ALL_COMPANIES_QUERY = "SELECT * FROM company;";
	private final static String ADD_COMPUTER_QUERY = "INSERT INTO computer (name,introduced,discontinued,company_id) "
			+ "VALUES (?,?,?,?);";
	private final static String DELETE_COMPUTER_BY_ID_QUERY = "DELETE FROM computer WHERE id=?;";
	private final static String UPDATE_COMPUTER_BY_ID_QUERY = "UPDATE computer SET name=?,introduced=?,discontinued=?,"
			+ "company_id=? WHERE id=?;";
	private static final String FIND_COMPUTER_BY_ID_QUERY = "SELECT computer.id AS id, computer.name AS name, "
			+ "introduced, discontinued, computer.company_id, company.name AS company_name "
			+ "FROM computer LEFT JOIN company ON computer.company_id = company.id WHERE computer.id = ?;";
	private static final String FIND_COMPANY_BY_ID_QUERY = "SELECT id,name FROM company WHERE id=?;";
	
	public static List<Computer> getComputers(){
		List<Computer> computers = new ArrayList<>();
		try (Connection conn = new DbConnect().getConnection()){
			Statement stmt = conn.createStatement();
			ResultSet results = stmt.executeQuery(GET_ALL_COMPUTERS_QUERY);
			while(results.next()) {
				Computer c;
				try {
					c = ComputerMapper.toComputer(results,"company_id","company_name");
					computers.add(c);
				} catch (IncompleteResultSet e) {
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return computers;
	}
	
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
	
	public static Computer findComputer(long id){
		Computer computer = null;
		try (Connection conn = new DbConnect().getConnection()){
			PreparedStatement stmt = conn.prepareStatement(FIND_COMPUTER_BY_ID_QUERY);
			stmt.setLong(1, id);
			ResultSet results = stmt.executeQuery();
			if(results.next()) {
				try {
					computer =  ComputerMapper.toComputer(results,"company_id","company_name");
				} catch (IncompleteResultSet e) {
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return computer;
	}
	
	public static long addComputer(Computer computer) {
		long id = 0;
		try(Connection conn = new DbConnect().getConnection()) {
			PreparedStatement stmt = conn.prepareStatement(ADD_COMPUTER_QUERY, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1,computer.getName());
			stmt.setTimestamp(2,computer.getIntroduced());
			stmt.setTimestamp(3,computer.getDiscontinued());
			if (computer.getCompany() != null) {
				stmt.setLong(4,computer.getCompany().getId());
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
			e.printStackTrace();
		}
		return id;
	}
	
	public static int deleteComputer(long id) {
		int deletedRecords = 0;
		try(Connection conn = new DbConnect().getConnection()) {
			PreparedStatement stmt = conn.prepareStatement(DELETE_COMPUTER_BY_ID_QUERY);
			stmt.setLong(1,id);
			deletedRecords = stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return deletedRecords;
	}
	
	public static int updateComputer(long id, Computer computer) {
		int updatedRecords = 0;
		try(Connection conn = new DbConnect().getConnection()) {
			PreparedStatement stmt = conn.prepareStatement(UPDATE_COMPUTER_BY_ID_QUERY);
			stmt.setString(1, computer.getName());
			stmt.setTimestamp(2,computer.getIntroduced());
			stmt.setTimestamp(3,computer.getDiscontinued());
			System.out.print("HERE:"+computer.getCompany().toString());
			if (computer.getCompany() != null) {
				stmt.setLong(4,computer.getCompany().getId());
			}
			else {
				stmt.setNull(4,Types.BIGINT);
			}
			stmt.setLong(5, id);
			updatedRecords = stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return updatedRecords;
	}
}
