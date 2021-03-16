package com.excilys.computerDatabase.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.excilys.computerDatabase.exception.FailedSQLRequestException;
import com.excilys.computerDatabase.exception.IncompleteResultSetException;
import com.excilys.computerDatabase.mapper.ComputerMapper;
import com.excilys.computerDatabase.model.Computer;

public class ComputerDatabaseDAO {

	private final static String ADD_COMPUTER_QUERY = """
		INSERT INTO computer (name,introduced,discontinued,company_id) VALUES (?,?,?,?);""";
	private final static String DELETE_COMPUTER_BY_ID_QUERY = "DELETE FROM computer WHERE id=?;";
	private final static String UPDATE_COMPUTER_BY_ID_QUERY = """
			UPDATE computer SET name=?,introduced=?,discontinued=?, company_id=? WHERE id=?;""";
	private static final String FIND_COMPUTER_BY_ID_QUERY = """
			SELECT computer.id AS id, computer.name AS name, introduced, discontinued, computer.company_id AS company_id,
			company.name AS company_name FROM computer LEFT JOIN company ON computer.company_id = company.id WHERE computer.id = ?;""";
	private final static String GET_COMPUTERS_COUNT_QUERY = "SELECT COUNT(*) FROM computer;";
	private static final String FIND_COMPUTERS_INTERVAL_QUERY = """
			SELECT computer.id AS id, computer.name AS name, introduced, discontinued, computer.company_id AS company_id,
			company.name AS company_name FROM computer LEFT JOIN company ON computer.company_id = company.id ORDER BY computer.id LIMIT ? OFFSET ?;""";


	private static ComputerDatabaseDAO instance = null;
	
	private ComputerDatabaseDAO() {}
	
	public static ComputerDatabaseDAO getInstance() {
		if (instance == null) {
			instance = new ComputerDatabaseDAO();
		}
		return instance;
	}
	
	public List<Computer> getComputers(){
		return getComputers(0,getComputerCount());
	}
	
	public List<Computer> getComputers(long from, long amount){
		List<Computer> computers = new ArrayList<>();
		try (Connection conn = new DbConnect().getConnection();
				PreparedStatement stmt = conn.prepareStatement(FIND_COMPUTERS_INTERVAL_QUERY)){
			stmt.setLong(1, amount);
			stmt.setLong(2,from);
			try (ResultSet results = stmt.executeQuery()){
				while(results.next()) {
					Optional<Computer> c;
					try {
						c = ComputerMapper.getInstance().toComputer(results);
						computers.add(c.orElseThrow());
					} catch (IncompleteResultSetException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return computers;
	}

	public Optional<Computer> findComputer(long id){
		Optional<Computer> computer = Optional.empty();;
		try (Connection conn = new DbConnect().getConnection();
				PreparedStatement stmt = conn.prepareStatement(FIND_COMPUTER_BY_ID_QUERY)){
			stmt.setLong(1, id);
			try (ResultSet results = stmt.executeQuery()){
				if(results.next()) {
					try {
						computer =  ComputerMapper.getInstance().toComputer(results);
					} catch (IncompleteResultSetException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return computer;
	}
	
	public void addComputer(Computer computer) throws FailedSQLRequestException {
		try(Connection conn = new DbConnect().getConnection();
				PreparedStatement stmt = conn.prepareStatement(ADD_COMPUTER_QUERY, Statement.RETURN_GENERATED_KEYS)) {
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
            try (ResultSet rs = stmt.getGeneratedKeys();){
                if(rs.next())
                {
            		if (rs.getInt(1) == 0) {
            			throw new FailedSQLRequestException("Couldn't create computer:"+computer.toString());
            		}
                }
            }
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteComputer(long id) throws FailedSQLRequestException {
		try(Connection conn = new DbConnect().getConnection();
				PreparedStatement stmt = conn.prepareStatement(DELETE_COMPUTER_BY_ID_QUERY)) {
			stmt.setLong(1,id);
			if (stmt.executeUpdate() == 0) {
    			throw new FailedSQLRequestException("Couldn't delete computer with Id:"+id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateComputer(long id, Computer computer) throws FailedSQLRequestException {
		try(Connection conn = new DbConnect().getConnection();
				PreparedStatement stmt = conn.prepareStatement(UPDATE_COMPUTER_BY_ID_QUERY)) {
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
			if (stmt.executeUpdate() == 0) {
    			throw new FailedSQLRequestException("Couldn't update computer with Id:"+id+ " with Object:"+computer.toString());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int getComputerCount() {
		try (Connection conn = new DbConnect().getConnection();
				Statement stmt = conn.createStatement();
				ResultSet results = stmt.executeQuery(GET_COMPUTERS_COUNT_QUERY)){
			if(results.next()) {
				return results.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
}
