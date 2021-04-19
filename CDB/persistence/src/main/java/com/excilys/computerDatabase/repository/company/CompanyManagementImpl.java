package com.excilys.computerDatabase.repository.company;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CompanyManagementImpl implements CompanyManagement {

	private Logger logger = LoggerFactory.getLogger(CompanyManagementImpl.class);
	
	private static final String DELETE_COMPANY_BY_ID_QUERY = "DELETE FROM company WHERE id = ?;";
	private static final String DELETE_COMPUTER_BY_COMPANY_ID_QUERY = "DELETE FROM computer WHERE company_id=?;";
	
	JdbcTemplate jdbcTemplate;
	
	public CompanyManagementImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	@Modifying
	@Transactional(rollbackFor = DataAccessException.class)
	public void deleteById(long id) {
		try {
			System.out.println("IN");
			jdbcTemplate.update(DELETE_COMPUTER_BY_COMPANY_ID_QUERY, id);
			System.out.println("MID");
			jdbcTemplate.update(DELETE_COMPANY_BY_ID_QUERY, id);
			System.out.println("OUT");
		} catch (DataAccessException e) {
			logger.error("Delete Company SQL Request Failed [Revert back]: with request " + DELETE_COMPANY_BY_ID_QUERY + " for Id " + id, e);
			throw e;
		}

	}

}
