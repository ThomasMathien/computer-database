package com.excilys.computerDatabase.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.excilys.computerDatabase.model.Company;
import com.excilys.computerDatabase.repository.company.CompanyRepository;

@Service
public class CompanyService {

	CompanyRepository repository;
	
	public CompanyService(CompanyRepository repository) {
		this.repository = repository;
	}
	
	public List<Company> getCompanies() {
		return repository.findAll();
	}
	
	public Page<Company> getCompanies(int page, int amount) {
		return repository.findAll(PageRequest.of(page, amount));
	}
	
	public Optional<Company> findCompany(long id) {
		return repository.findById(id);
	}
	
	public long getCompanyCount() {
		return repository.count();
	}
	
	public void deleteCompany(long id) {
		repository.deleteById(id);
	}
}
