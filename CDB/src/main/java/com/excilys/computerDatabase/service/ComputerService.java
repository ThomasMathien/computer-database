package com.excilys.computerDatabase.service;

import java.util.List;
import java.util.Optional;

import com.excilys.computerDatabase.dao.ComputerDAO;
import com.excilys.computerDatabase.exception.FailedSQLRequestException;
import com.excilys.computerDatabase.model.Computer;

public class ComputerService {

	private static ComputerService instance;
	
	public static ComputerService getInstance() {
		if (instance == null) {
			instance = new ComputerService();
		}
		return instance;
	}
	
	private ComputerService() { }
	
	public List<Computer> getComputers() {
		return ComputerDAO.getInstance().getComputers();
	}
	
	public List<Computer> getComputers(long from, long amount){
		return ComputerDAO.getInstance().getComputers(from, amount);
	}

	public Optional<Computer> findComputer(long id){
		return ComputerDAO.getInstance().findComputer(id);
	}
	
	public void addComputer(Computer computer) throws FailedSQLRequestException {
		ComputerDAO.getInstance().addComputer(computer);
	}
	
	public void deleteComputer(long id) throws FailedSQLRequestException {
		ComputerDAO.getInstance().deleteComputer(id);
	}
	
	public void updateComputer(long id, Computer computer) throws FailedSQLRequestException {
		ComputerDAO.getInstance().updateComputer(id, computer);
	}

	public int getComputerCount() {
		return ComputerDAO.getInstance().getComputerCount();
	}
}
