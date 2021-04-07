package com.excilys.computerDatabase.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.excilys.computerDatabase.exception.FailedSQLRequestException;
import com.excilys.computerDatabase.mapper.CompanyMapper;
import com.excilys.computerDatabase.model.Company;
import com.excilys.computerDatabase.service.ComputerService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class CompanyDAO {
	
	private Logger logger = LoggerFactory.getLogger(CompanyDAO.class);
	
	private static final String FIND_COMPANY_BY_ID_QUERY = "SELECT id AS company_id,name AS company_name FROM company WHERE id=?;";
	private static final String GET_COMPANY_COUNT_QUERY = "SELECT COUNT(*) FROM company;";
	private static final String FIND_COMPANIES_INTERVAL_QUERY = "SELECT id AS company_id,name AS company_name FROM company ORDER BY id LIMIT ? OFFSET ?;";
	private static final String DELETE_COMPANY_BY_ID_QUERY = "DELETE FROM company WHERE id = ?;";
	private static final String DELETE_COMPUTER_BY_COMPANY_ID_QUERY = "DELETE FROM computer WHERE company_id=?;";
	
	ComputerService computerService;
	CompanyMapper companyMapper;
	JdbcTemplate jdbcTemplate;
	
	public CompanyDAO(ComputerService computerService, CompanyMapper companyMapper, JdbcTemplate jdbcTemplate) {
		this.computerService = computerService;
		this.companyMapper = companyMapper;
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public List<Company> getCompanies() {
		return getCompanies(0, getCompanyCount());
	}
	
	public List<Company> getCompanies(int from, int amount) {
		List<Company> companies = new ArrayList<Company>();
		try {
			companies = jdbcTemplate.query(FIND_COMPANIES_INTERVAL_QUERY, companyMapper, amount, from);
		} catch (DataAccessException e) {
			logger.error("Get Companies SQL Request Failed: with request "+FIND_COMPANIES_INTERVAL_QUERY+" for "+amount+" rows from "+from,e );
		}
		return companies;
	}
	
	public Optional<Company> findCompany(long id) {
		Optional<Company> company = Optional.empty();
		try {
			company = Optional.of(jdbcTemplate.queryForObject(FIND_COMPANY_BY_ID_QUERY, companyMapper, id));
		} catch (DataAccessException e) {
			logger.error("Find Company SQL Request Failed: with request " + FIND_COMPANY_BY_ID_QUERY + " for Id" + id, e);
		}
		return company;
	}
	
	public int getCompanyCount() {
		try {
			return jdbcTemplate.queryForObject(GET_COMPANY_COUNT_QUERY, Integer.class);
		} catch (DataAccessException e) {
			logger.error("Get Company Count SQL Request Failed: with request " + GET_COMPANY_COUNT_QUERY, e);
		}
		return 0;
	}


	@Transactional
	public void deleteCompany(long id) throws FailedSQLRequestException {
		try {
			jdbcTemplate.update(DELETE_COMPUTER_BY_COMPANY_ID_QUERY, id);
			jdbcTemplate.update(DELETE_COMPANY_BY_ID_QUERY, id);
			logger.info("Company deleted of id:" + id);
		} catch (DataAccessException e) {
			logger.error("Delete Company SQL Request Failed: with request " + DELETE_COMPANY_BY_ID_QUERY + " for Id " + id, e);
			logger.warn("Reverting deletion of companies with company Id:" + id);		
		}
	}

}
