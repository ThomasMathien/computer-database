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
import main.java.com.excilys.computerDatabase.mapper.ComputerMapper;
import main.java.com.excilys.computerDatabase.model.Computer;

public abstract class ComputerDatabaseDAO {

	private final static String ADD_COMPUTER_QUERY = "INSERT INTO computer (name,introduced,discontinued,company_id) "
			+ "VALUES (?,?,?,?);";
	private final static String DELETE_COMPUTER_BY_ID_QUERY = "DELETE FROM computer WHERE id=?;";
	private final static String UPDATE_COMPUTER_BY_ID_QUERY = "UPDATE computer SET name=?,introduced=?,discontinued=?,"
			+ "company_id=? WHERE id=?;";
	private static final String FIND_COMPUTER_BY_ID_QUERY = "SELECT computer.id AS id, computer.name AS name, "
			+ "introduced, discontinued, computer.company_id, company.name AS company_name "
			+ "FROM computer LEFT JOIN company ON computer.company_id = company.id WHERE computer.id = ?;";
	private final static String GET_COMPUTERS_COUNT_QUERY = "SELECT COUNT(*) FROM computer;";
	private static final String FIND_COMPUTERS_INTERVAL_QUERY = "SELECT computer.id AS id, computer.name AS name, "
			+ "introduced, discontinued, computer.company_id, company.name AS company_name "
			+ "FROM computer LEFT JOIN company ON computer.company_id = company.id ORDER BY computer.id LIMIT ? OFFSET ?;";


	public static List<Computer> getComputers(){
		return getComputers(0,getComputerCount());
	}
	
	public static List<Computer> getComputers(long from, long amount){
		List<Computer> computers = new ArrayList<>();
		try (Connection conn = new DbConnect().getConnection()){
			PreparedStatement stmt = conn.prepareStatement(FIND_COMPUTERS_INTERVAL_QUERY);
			stmt.setLong(1, amount);
			stmt.setLong(2,from);
			ResultSet results = stmt.executeQuery();
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


	public static int getComputerCount() {
		try (Connection conn = new DbConnect().getConnection()){
			Statement stmt = conn.createStatement();
			ResultSet results = stmt.executeQuery(GET_COMPUTERS_COUNT_QUERY);
			if(results.next()) {
				return results.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
}
