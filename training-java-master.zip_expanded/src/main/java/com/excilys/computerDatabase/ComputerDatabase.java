package main.java.com.excilys.computerDatabase;

import java.sql.SQLException;
import java.util.List;

import main.java.com.excilys.computerDatabase.dao.DatabaseDAO;
import main.java.com.excilys.computerDatabase.model.Company;
import main.java.com.excilys.computerDatabase.model.Computer;

public class ComputerDatabase {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		List<Computer> computers = DatabaseDAO.getComputers();
		System.out.println("Result: "+computers.size()+" computer(s) found");
		List<Company> companies = DatabaseDAO.getCompanies();
		System.out.println("Result: "+companies.size()+" companies found");
		try {
			DatabaseDAO.addComputer(computers.get(0));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Result: "+computers.get(0));
	}

}
