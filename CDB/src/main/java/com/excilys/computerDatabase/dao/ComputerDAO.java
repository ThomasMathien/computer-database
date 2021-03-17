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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.computerDatabase.exception.FailedSQLRequestException;
import com.excilys.computerDatabase.exception.IncompleteResultSetException;
import com.excilys.computerDatabase.mapper.ComputerMapper;
import com.excilys.computerDatabase.model.Computer;

public class ComputerDAO {

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

	private Logger logger = LoggerFactory.getLogger(ComputerDAO.class);
	
	private static ComputerDAO instance = null;
	
	private ComputerDAO() {}
	
	public static ComputerDAO getInstance() {
		if (instance == null) {
			instance = new ComputerDAO();
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
						if (c.isPresent()) {
							computers.add(c.orElseThrow());
						}
					} catch (IncompleteResultSetException e) {
						logger.error("Creating Computer from ResultSet Failed: with resultSet "+results.toString(),e );
					}
				}
			}
		} catch (SQLException e) {
			logger.error("Get Computers SQL Request Failed: with request "+FIND_COMPUTERS_INTERVAL_QUERY+" for "+amount+" rows from "+from,e );
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
						logger.error("Creating Computer from ResultSet Failed: with resultSet "+results.toString(),e );
					}
				}
			}
		} catch (SQLException e) {
			logger.error("Find Computer SQL Request Failed: with request "+FIND_COMPUTER_BY_ID_QUERY+" for Id"+id,e );
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
            		System.out.print(rs.getInt(1));
                }
            }
		} catch (SQLException e) {
			logger.error("Add Computer SQL Request Failed: with request "+ADD_COMPUTER_QUERY+" for Computer "+computer.toString(),e);
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
			logger.error("Delete Computer SQL Request Failed: with request "+DELETE_COMPUTER_BY_ID_QUERY+" for Id "+id,e);
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
			logger.error("Update Computer SQL Request Failed: with request " + UPDATE_COMPUTER_BY_ID_QUERY +
					" for Id:"+id+ " with Object:"+computer.toString(),e);
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
			logger.error("Get Computer Count SQL Request Failed: with request "+GET_COMPUTERS_COUNT_QUERY,e );
		}
		return 0;
	}
}
