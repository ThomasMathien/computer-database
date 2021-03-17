package com.excilys.computerDatabase.dao;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;

import com.excilys.computerDatabase.model.Company;

public class CompanyDAOTest {

	@Test
	public void testGetCompanies() {
		List<Company> companies = new ArrayList<>();
		companies = CompanyDAO.getInstance().getCompanies();
		assertTrue(companies.size() == 5);
		for (Company c: companies) {
			assertTrue(c != null);
			assertEquals(c, CompanyDAO.getInstance().findCompany(c.getId()).orElseThrow());
		}	
	}
	
	@Test
	public void testGetCompaniesWithArguments() {
		List<Company> companies = new ArrayList<>();
		companies = CompanyDAO.getInstance().getCompanies(0, 5);
		assertEquals(CompanyDAO.getInstance().getCompanies(), companies);
		final int offset = 1;
		companies = CompanyDAO.getInstance().getCompanies(offset, 2);
		assertTrue(companies.size() == 2);
		for (int i = 0; i < companies.size(); i++) {
			assertEquals(CompanyDAO.getInstance().findCompany(i + 1 + offset).orElseThrow(), companies.get(i));
		}
	}
	
	@Test
	public void testFindCompany() {
		Optional<Company> testCompany = Optional.of(new Company(3,""));
		
		Optional<Company> foundCompany = CompanyDAO.getInstance().findCompany(3);
		assertTrue(foundCompany.isPresent());
		assertNotEquals(foundCompany, testCompany);
		
		testCompany = Optional.of(new Company(3,"RCA"));
		
		assertEquals(foundCompany, testCompany);
		assertTrue(CompanyDAO.getInstance().findCompany(145).isEmpty());
		assertTrue(CompanyDAO.getInstance().findCompany(0).isEmpty());	
		assertTrue(CompanyDAO.getInstance().findCompany(-2).isEmpty());
	}
}
