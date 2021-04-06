package com.excilys.computerDatabase.service;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.excilys.computerDatabase.dao.ComputerDAO;
import com.excilys.computerDatabase.exception.FailedSQLRequestException;
import com.excilys.computerDatabase.model.Computer;
import com.excilys.computerDatabase.search.SqlFilter;

@Service
public class ComputerService {
	
	ComputerDAO computerDAO;
	
	public ComputerService(ComputerDAO computerDAO) {
		this.computerDAO = computerDAO;
	}
	
	public List<Computer> getComputers() {
		return computerDAO.getComputers();
	}
	
	public List<Computer> getComputers(long from, long amount) {
		return computerDAO.getComputers(from, amount);
	}

	public List<Computer> getComputers(long from, long amount, SqlFilter filter) {
		return computerDAO.getComputers(from, amount, filter);
	}
	
	public long[] getComputersIdFromCompany(long companyId) {
		return computerDAO.getComputersIdFromCompany(companyId);
	}
	
	public Optional<Computer> findComputer(long id) {
		return computerDAO.findComputer(id);
	}
	
	public void addComputer(Computer computer) throws FailedSQLRequestException {
		computerDAO.addComputer(computer);
	}
	
	public void deleteComputer(long id) throws FailedSQLRequestException {
		computerDAO.deleteComputer(id);
	}
	
	public void deleteComputerByCompany(long companyId, Connection conn) throws FailedSQLRequestException {
		computerDAO.deleteComputerByCompany(companyId, conn);
	}
	
	public void updateComputer(long id, Computer computer) throws FailedSQLRequestException {
		computerDAO.updateComputer(id, computer);
	}

	public int getComputerCount() {
		return computerDAO.getComputerCount();
	}

	public int getComputerCount(String search) {
		return computerDAO.getComputerCount(search);
	}


}
