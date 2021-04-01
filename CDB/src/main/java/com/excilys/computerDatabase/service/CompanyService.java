package com.excilys.computerDatabase.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.excilys.computerDatabase.dao.CompanyDAO;
import com.excilys.computerDatabase.exception.FailedSQLRequestException;
import com.excilys.computerDatabase.model.Company;

@Service
public class CompanyService {
	
	@Autowired
	CompanyDAO companyDAO;
	
	public List<Company> getCompanies() {
		return companyDAO.getCompanies();
	}
	
	public List<Company> getCompanies(int from, int amount) {
		return companyDAO.getCompanies(from, amount);
	}
	
	public Optional<Company> findCompany(long id) {
		return companyDAO.findCompany(id);
	}
	
	public int getCompanyCount() {
		return companyDAO.getCompanyCount();
	}
	
	public void deleteCompany(long id) throws FailedSQLRequestException {
		companyDAO.deleteCompany(id);
	}
}
