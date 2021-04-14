package com.excilys.computerDatabase.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.computerDatabase.dao.CompanyDAO;
import com.excilys.computerDatabase.dao.CompanyRepository;
import com.excilys.computerDatabase.exception.FailedSQLRequestException;
import com.excilys.computerDatabase.model.Company;

@Service
public class CompanyService {

	CompanyDAO companyDAO;
	CompanyRepository repository;
	
	public CompanyService(CompanyDAO companyDAO, CompanyRepository repository) {
		this.companyDAO = companyDAO;
		this.repository = repository;
	}
	
	public List<Company> getCompanies() {
		return repository.getCompanies();
	}
	
	public List<Company> getCompanies(int page, int amount) {
		return repository.getCompanies(PageRequest.of(page, amount));
	}
	
	public Optional<Company> findCompany(long id) {
		return repository.findCompany(id);
	}
	
	public int getCompanyCount() {
		return repository.getCompanyCount();
	}
	
	@Transactional
	public void deleteCompany(long id) throws FailedSQLRequestException {
		companyDAO.deleteCompany(id);
	}
}
