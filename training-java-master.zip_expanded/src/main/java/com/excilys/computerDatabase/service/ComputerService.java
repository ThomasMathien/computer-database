package main.java.com.excilys.computerDatabase.service;

import java.util.List;
import java.util.Optional;

import main.java.com.excilys.computerDatabase.dao.ComputerDatabaseDAO;
import main.java.com.excilys.computerDatabase.exception.FailedSQLRequestException;
import main.java.com.excilys.computerDatabase.model.Computer;

public class ComputerService {

	private static ComputerService instance;
	
	public static ComputerService getInstance() {
		if (instance == null) {
			instance = new ComputerService();
		}
		return instance;
	}
	
	private ComputerService() {}
	
	
	public List<Computer> getComputers(){
		return ComputerDatabaseDAO.getInstance().getComputers();
	}
	
	public List<Computer> getComputers(long from, long amount){
		return ComputerDatabaseDAO.getInstance().getComputers(from, amount);
	}

	public Optional<Computer> findComputer(long id){
		return ComputerDatabaseDAO.getInstance().findComputer(id);
	}
	
	public void addComputer(Computer computer) throws FailedSQLRequestException {
		ComputerDatabaseDAO.getInstance().addComputer(computer);
	}
	
	public void deleteComputer(long id) throws FailedSQLRequestException {
		ComputerDatabaseDAO.getInstance().deleteComputer(id);
	}
	
	public void updateComputer(long id, Computer computer) throws FailedSQLRequestException {
		ComputerDatabaseDAO.getInstance().updateComputer(id, computer);
	}

	public int getComputerCount() {
		return ComputerDatabaseDAO.getInstance().getComputerCount();
	}
}
