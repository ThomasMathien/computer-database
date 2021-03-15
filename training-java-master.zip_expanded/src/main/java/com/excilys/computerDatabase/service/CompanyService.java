package main.java.com.excilys.computerDatabase.service;

import java.util.List;
import java.util.Optional;

import main.java.com.excilys.computerDatabase.dao.CompanyDatabaseDAO;
import main.java.com.excilys.computerDatabase.model.Company;

public class CompanyService {
	private static CompanyService instance;
	
	
	public static CompanyService getInstance() {
		if (instance == null) {
			instance = new CompanyService();
		}
		return instance;
	}
	
	private CompanyService() {}
	
	public List<Company> getCompanies(){
		return CompanyDatabaseDAO.getInstance().getCompanies();
	}
	
	public List<Company> getCompanies(int from, int amount){
		return CompanyDatabaseDAO.getInstance().getCompanies(from, amount);
	}
	
	public Optional<Company> findCompany(long id){
		return CompanyDatabaseDAO.getInstance().findCompany(id);
	}
	
	public int getCompanyCount() {
		return CompanyDatabaseDAO.getInstance().getCompanyCount();
	}
	
}
