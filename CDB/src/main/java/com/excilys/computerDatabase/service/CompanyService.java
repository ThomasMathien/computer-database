package com.excilys.computerDatabase.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.excilys.computerDatabase.dao.CompanyDAO;
import com.excilys.computerDatabase.dto.CompanyDTO;
import com.excilys.computerDatabase.mapper.CompanyMapper;
import com.excilys.computerDatabase.model.Company;

public class CompanyService {

	private static CompanyService instance;
	
	public static CompanyService getInstance() {
		if (instance == null) {
			instance = new CompanyService();
		}
		return instance;
	}
	
	private CompanyService() { }
	
	public List<Company> getCompanies() {
		return CompanyDAO.getInstance().getCompanies();
	}
	
	public List<Company> getCompanies(int from, int amount) {
		return CompanyDAO.getInstance().getCompanies(from, amount);
	}
	
	public Optional<Company> findCompany(long id) {
		return CompanyDAO.getInstance().findCompany(id);
	}
	
	public int getCompanyCount() {
		return CompanyDAO.getInstance().getCompanyCount();
	}
	
}
