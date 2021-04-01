package com.excilys.computerDatabase.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.excilys.computerDatabase.exception.FailedSQLRequestException;
import com.excilys.computerDatabase.exception.IncompleteResultSetException;
import com.excilys.computerDatabase.mapper.ComputerMapper;
import com.excilys.computerDatabase.model.Computer;

@Repository
public class ComputerDAO {

	private final static String ADD_COMPUTER_QUERY = """
		INSERT INTO computer (name,introduced,discontinued,company_id) VALUES (?,?,?,?);""";
	private final static String DELETE_COMPUTER_BY_ID_QUERY = "DELETE FROM computer WHERE id=?;";
	private final static String UPDATE_COMPUTER_BY_ID_QUERY = """
			UPDATE computer SET name=?,introduced=?,discontinued=?, company_id=? WHERE id=?;""";
	private static final String FIND_COMPUTER_BY_ID_QUERY = """
			SELECT computer.id AS id, computer.name AS name, introduced, discontinued, computer.company_id AS company_id,
			company.name AS company_name FROM computer LEFT JOIN company ON computer.company_id = company.id WHERE computer.id = ?;""";
	private final static String GET_COMPUTERS_COUNT_FILTER_QUERY = """
			SELECT COUNT(*) FROM computer LEFT JOIN company ON computer.company_id = company.id WHERE LOWER(computer.name)
			LIKE LOWER(?) OR LOWER(company.name) LIKE LOWER(?);""";
	private static final String FIND_COMPUTERS_INTERVAL_QUERY = """
			SELECT computer.id AS id, computer.name AS name, introduced, discontinued, computer.company_id AS company_id,
			company.name AS company_name FROM computer LEFT JOIN company ON computer.company_id = company.id WHERE LOWER(computer.name) 
			LIKE LOWER(?) OR LOWER(company.name) LIKE LOWER(?) ! LIMIT ? OFFSET ?;""";
	private static final String FIND_COMPUTERS_FROM_COMPANY = """
			SELECT computer.id FROM computer LEFT JOIN company ON computer.company_id = company.id WHERE company.id = ?;
			""";
	private static final String DELETE_COMPUTER_BY_COMPANY_ID_QUERY = """
			DELETE FROM computer WHERE company_id=?;
			""";

	private Logger logger = LoggerFactory.getLogger(ComputerDAO.class);
	
	public List<Computer> getComputers(){
		return getComputers(0,getComputerCount());
	}
	
	public List<Computer> getComputers(long from, long amount){
		return getComputers(from, amount, new SqlFilter("%","computer.id","ASC"));
	}
	
	public long [] getComputersIdFromCompany(long companyId){
		List<Long> computersId = new ArrayList<>();
		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(FIND_COMPUTERS_FROM_COMPANY)){
			stmt.setLong(1,companyId);
			ResultSet results = stmt.executeQuery();
			while(results.next()) {
				computersId.add(results.getLong(1));
			}
		} catch (SQLException e) {
			logger.error("Get Computers SQL Request Failed: with request "+FIND_COMPUTERS_FROM_COMPANY+" for compay id: "+companyId, e);
		}
		return computersId.stream().mapToLong( l -> l).toArray();
	}
	
	public List<Computer> getComputers(long from, long amount, SqlFilter filter) {
		List<Computer> computers = new ArrayList<>();
		final String orderByInjection = "ORDER BY " + filter.getSortedColumn()+ " "+ filter.getSortOrder()+" ";
		final String query = FIND_COMPUTERS_INTERVAL_QUERY.replace("!", orderByInjection);
		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(query)){
			String search = adaptToLikeQuery(filter.getSearchFilter());
			stmt.setString(1, search);
			stmt.setString(2, search);
			stmt.setLong(3, amount);
			stmt.setLong(4,from);
			try (ResultSet results = stmt.executeQuery()){
				while(results.next()) {
					Optional<Computer> c;
					try {
						c = ComputerMapper.getInstance().toComputer(results);
						if (c.isPresent()) {
							computers.add(c.orElseThrow());
						}
					} catch (IncompleteResultSetException e) {
						logger.error("Couldn't map Computer from resultSet: with resultSet "+results.toString(),e );
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
		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(FIND_COMPUTER_BY_ID_QUERY)){
			stmt.setLong(1, id);
			try (ResultSet results = stmt.executeQuery()){
				if(results.next()) {
					try {
						computer =  ComputerMapper.getInstance().toComputer(results);
					} catch (IncompleteResultSetException e) {
						logger.error("Couldn't map Computer from resultSet"+results.toString(),e );
					}
				}
			}
		} catch (SQLException e) {
			logger.error("Find Computer SQL Request Failed: with request "+FIND_COMPUTER_BY_ID_QUERY+" for Id"+id,e );
		}
		return computer;
	}
	
	public void addComputer(Computer computer) throws FailedSQLRequestException {
		try(Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(ADD_COMPUTER_QUERY, Statement.RETURN_GENERATED_KEYS)) {
			stmt.setString(1,computer.getName());
			LocalDate introduced = computer.getIntroduced();
			stmt.setDate(2,introduced != null ? Date.valueOf(computer.getIntroduced()) : null);
			LocalDate discontinued = computer.getDiscontinued();
			stmt.setDate(3,discontinued != null ? Date.valueOf(computer.getDiscontinued()) : null);
			if (computer.getCompany() != null) {
				stmt.setLong(4,computer.getCompany().getId());
			}
			else {
				stmt.setNull(4,Types.BIGINT);
			}
			stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys();){
                if(rs.next() && (computer.getId() != 0 && computer.getId() != rs.getLong(1)) ) {
            		logger.warn("Ignored entered Id of "+computer.getId()+" as it is auto generated");
                }
            }
            logger.info("Computer added:"+computer.toString());
		} catch (SQLException e) {
			logger.error("Add Computer SQL Request Failed: with request "+ADD_COMPUTER_QUERY+" for Computer "+computer.toString(),e);
		}
	}
	
	public void deleteComputerByCompany(long companyId, Connection conn) throws FailedSQLRequestException {
		try(PreparedStatement stmt = conn.prepareStatement(DELETE_COMPUTER_BY_COMPANY_ID_QUERY)) {
			stmt.setLong(1,companyId);
			int amount = stmt.executeUpdate();
			if (amount == 0) {
    			throw new FailedSQLRequestException("Couldn't delete computer with Id:"+companyId);
			}
			logger.info(amount + " Computers deleted for companyId:"+companyId);
		} catch (SQLException e) {
			logger.error("Delete Computers SQL Request Failed: with request "+DELETE_COMPUTER_BY_COMPANY_ID_QUERY+" for Id "+companyId,e);
		}
	}
	
	public void deleteComputer(long id) throws FailedSQLRequestException {
		try(Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(DELETE_COMPUTER_BY_ID_QUERY)) {
			stmt.setLong(1,id);
			if (stmt.executeUpdate() == 0) {
    			throw new FailedSQLRequestException("Couldn't delete computer with Id:"+id);
			}
			logger.info("Computer deleted of id:"+id);
		} catch (SQLException e) {
			logger.error("Delete Computer SQL Request Failed: with request "+DELETE_COMPUTER_BY_ID_QUERY+" for Id "+id,e);
		}
	}
	
	public void updateComputer(long id, Computer computer) throws FailedSQLRequestException {
		try(Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(UPDATE_COMPUTER_BY_ID_QUERY)) {
			stmt.setString(1, computer.getName());
			stmt.setDate(2,computer.getIntroduced() != null ? Date.valueOf(computer.getIntroduced()) : null);
			stmt.setDate(3,computer.getDiscontinued() != null ? Date.valueOf(computer.getDiscontinued()) : null);
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
			logger.info("Computer updated for id:"+id+" with:"+computer.toString());
		} catch (SQLException e) {
			logger.error("Update Computer SQL Request Failed: with request " + UPDATE_COMPUTER_BY_ID_QUERY +
					" for Id:"+id+ " with Object:"+computer.toString(),e);
		}
	}

	public int getComputerCount() {
		return getComputerCount("%");
	}
	
	public int getComputerCount(String searchFilter) {
		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(GET_COMPUTERS_COUNT_FILTER_QUERY)){
				searchFilter = adaptToLikeQuery(searchFilter);
				stmt.setString(1, searchFilter);
				stmt.setString(2, searchFilter);
				ResultSet results = stmt.executeQuery();
			if(results.next()) {
				return results.getInt(1);
			}
		} catch (SQLException e) {
			logger.error("Get Computer Count SQL Request Failed: with request "+GET_COMPUTERS_COUNT_FILTER_QUERY,e );
		}
		return 0;
	}
	
	private Connection getConnection() throws SQLException {
		return Datasource.getInstance().getConnection();
	}

	private String adaptToLikeQuery(String search){
		if (search != null) {
			search = "%" + search.trim().toLowerCase() +"%";
		} else {
			search = "%";
		}
		return search;
	}
	
}
