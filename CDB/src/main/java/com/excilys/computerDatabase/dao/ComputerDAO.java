package com.excilys.computerDatabase.dao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.excilys.computerDatabase.exception.FailedSQLRequestException;
import com.excilys.computerDatabase.mapper.ComputerMapper;
import com.excilys.computerDatabase.model.Computer;
import com.excilys.computerDatabase.search.SqlFilter;

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


	private Logger logger = LoggerFactory.getLogger(ComputerDAO.class);
	
	ComputerMapper computerMapper;
	JdbcTemplate jdbcTemplate;
	
	public ComputerDAO(ComputerMapper computerMapper, JdbcTemplate jdbcTemplate) {
		this.computerMapper = computerMapper;
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public List<Computer> getComputers(){
		return getComputers(0,getComputerCount());
	}
	
	public List<Computer> getComputers(long from, long amount){
		return getComputers(from, amount, new SqlFilter("%","computer.id","ASC"));
	}
	
	public long [] getComputersIdFromCompany(long companyId){
		List<Long> computersId = new ArrayList<>();
		try {
			computersId = jdbcTemplate.queryForList(FIND_COMPUTERS_FROM_COMPANY, Long.class, companyId);
		} catch (DataAccessException e) {
			logger.error("Get Computers SQL Request Failed: with request "+FIND_COMPUTERS_FROM_COMPANY+" for compay id: "+companyId, e);
		}
		return computersId.stream().mapToLong( l -> l).toArray();
	}
	
	public List<Computer> getComputers(long from, long amount, SqlFilter filter) {
		List<Computer> computers = new ArrayList<>();
		final String orderByInjection = "ORDER BY " + filter.getSortedColumn()+ " "+ filter.getSortOrder()+" ";
		final String query = FIND_COMPUTERS_INTERVAL_QUERY.replace("!", orderByInjection);
		
		try {
			String search = adaptToLikeQuery(filter.getSearchFilter());
			computers = jdbcTemplate.query(query, computerMapper, search, search, amount, from);
		} catch (DataAccessException e) {
			logger.error("Get Computers SQL Request Failed: with request "+FIND_COMPUTERS_INTERVAL_QUERY+" for "+amount+" rows from "+from,e );
		}
		
		return computers;
	}

	public Optional<Computer> findComputer(long id){
		Optional<Computer> computer = Optional.empty();;
		try {
			computer = Optional.of(jdbcTemplate.queryForObject(FIND_COMPUTER_BY_ID_QUERY, computerMapper, id));
		} catch (DataAccessException e) {
			logger.error("Find Computer SQL Request Failed: with request "+FIND_COMPUTER_BY_ID_QUERY+" for Id"+id,e );
		}
		return computer;
	}
	
	public void addComputer(Computer computer) throws FailedSQLRequestException {
		try {
			LocalDate introduced = computer.getIntroduced();
			Date introducedDate = introduced != null ? Date.valueOf(computer.getIntroduced()) : null;
			LocalDate discontinued = computer.getDiscontinued();
			Date discontinuedDate = discontinued != null ? Date.valueOf(computer.getDiscontinued()) : null;
			Long companyId = computer.getCompany() != null ? computer.getCompany().getId() : null;
			Object[] params = {computer.getName(), introducedDate, discontinuedDate, companyId};
			jdbcTemplate.update(ADD_COMPUTER_QUERY, params);
            logger.info("Computer added:"+computer.toString());
		} catch (DataAccessException e) {
			logger.error("Add Computer SQL Request Failed: with request "+ADD_COMPUTER_QUERY+" for Computer "+computer.toString(),e);
			throw new FailedSQLRequestException(e);
		}
	}
	
	public void deleteComputer(long id) throws FailedSQLRequestException {
		try {
			jdbcTemplate.update(DELETE_COMPUTER_BY_ID_QUERY, id);
			logger.info("Computer deleted of id:"+id);
		} catch (DataAccessException e) {
			logger.error("Delete Computer SQL Request Failed: with request "+DELETE_COMPUTER_BY_ID_QUERY+" for Id "+id,e);
			throw new FailedSQLRequestException("Couldn't delete computer with Id:"+id);
		}
	}
	
	public void updateComputer(long id, Computer computer) throws FailedSQLRequestException {
		Date introduced = computer.getIntroduced() != null ? Date.valueOf(computer.getIntroduced()) : null;
		Date discontinued = computer.getDiscontinued() != null ? Date.valueOf(computer.getDiscontinued()) : null;
		Long companyId = computer.getCompany() != null ? computer.getCompany().getId() : null;
		Object[] params = {computer.getName(), introduced, discontinued, companyId, id};
		try {
			jdbcTemplate.update(UPDATE_COMPUTER_BY_ID_QUERY, params);
			logger.info("Computer updated for id:"+id+" with:"+Arrays.toString(params));
		} catch (DataAccessException e) {
			logger.error("Update Computer SQL Request Failed: with request " + UPDATE_COMPUTER_BY_ID_QUERY +
					" for Id:"+id+ " with params:"+Arrays.toString(params),e);
			throw new FailedSQLRequestException(e);
		}
	}

	public int getComputerCount() {
		return getComputerCount("%");
	}
	
	public int getComputerCount(String searchFilter) {
		try {
			searchFilter = adaptToLikeQuery(searchFilter);
			return jdbcTemplate.queryForObject(GET_COMPUTERS_COUNT_FILTER_QUERY, Integer.class, searchFilter, searchFilter);
		} catch (DataAccessException e) {
			logger.error("Get Computer Count SQL Request Failed: with request "+GET_COMPUTERS_COUNT_FILTER_QUERY,e );
		}
		return 0;
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
