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
		for (int i =0; i <30; i++) {
			System.out.println(computers.get(i).toString());
		}
		try {
			System.out.println("Added id:"+DatabaseDAO.addComputer(computers.get(0)));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		System.out.println("Result: "+computers.get(0));
		DatabaseDAO.updateComputer(1, computers.get(5));
		System.out.println("Updated: "+DatabaseDAO.getComputers().get(0));
		System.out.println("Deleted "+DatabaseDAO.deleteComputer(578)+" records");
		System.out.println("Deleted "+DatabaseDAO.deleteComputer(578)+" records");
		
	}

}
