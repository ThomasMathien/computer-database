package com.excilys.computerDatabase.dao;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import com.excilys.computerDatabase.model.Company;

public class CompanyDAOTest extends DataSourceDBUnitTest{

	static CompanyDAO dao = CompanyDAO.getInstance();

	private final int COMPANIES_AMOUNT = 5;
	
	@Test
	public void testGetCompanies() {
		List<Company> companies = dao.getCompanies();
		assertEquals(COMPANIES_AMOUNT,companies.size());
		for (Company c: companies) {
			assertTrue(c != null);
			assertEquals(c, dao.findCompany(c.getId()).orElseThrow());
		}	
	}
	
	@Test
	public void testGetCompaniesWithArguments() {
		assertEquals(dao.getCompanies(), dao.getCompanies(0, COMPANIES_AMOUNT));
		final int offset = 1;
		final int amount = 4;
		List<Company>  companies = dao.getCompanies(offset, amount);
		assertEquals(amount,companies.size());
		for (int i = 0; i < companies.size(); i++) {
			assertEquals(dao.findCompany(i + 1 + offset).orElseThrow(), companies.get(i));
		}
	}
	
	@Test
	public void testFindCompany() {
		Optional<Company> testCompany = Optional.of(new Company(3,""));
		
		Optional<Company> foundCompany = dao.findCompany(3);
		assertTrue(foundCompany.isPresent());
		assertNotEquals(foundCompany, testCompany);
		
		testCompany = Optional.of(new Company(3,"RCA"));
		
		assertEquals(foundCompany, testCompany);
		assertTrue(dao.findCompany(145).isEmpty());
		assertTrue(dao.findCompany(0).isEmpty());	
		assertTrue(dao.findCompany(-2).isEmpty());
	}
}
