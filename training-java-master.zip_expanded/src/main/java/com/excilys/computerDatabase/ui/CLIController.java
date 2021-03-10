package main.java.com.excilys.computerDatabase.ui;

import java.sql.Timestamp;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import main.java.com.excilys.computerDatabase.dao.CompanyDatabaseDAO;
import main.java.com.excilys.computerDatabase.dao.ComputerDatabaseDAO;
import main.java.com.excilys.computerDatabase.model.Company;
import main.java.com.excilys.computerDatabase.model.Computer;
import main.java.com.excilys.computerDatabase.validator.InputValidator;

public class CLIController {

	Scanner sc = new Scanner(System.in);
	
	public void run(){

		while (true) {
			displayMainMenu();
			System.out.print(">>");	
			int command;
			try {
				command = Integer.parseInt(sc.nextLine());
			}
			catch(NumberFormatException e) {
				command = 0;
			}
			System.out.println("Please wait...");
			switch(command) {
			case 1:
				displayComputers();
				break;
			case 2:
				displayCompanies();
				break;
			case 3: 
				displayDetails();
				break;
			case 4:
				addComputer();
				break;
			case 5:
				updateComputer();
				break;
			case 6:
				break;
			case 7:
				sc.close();
				return;
			default: 
				System.out.println("Invalid command, please try again");
			}
		}
	}

	public void displayMainMenu() {
		System.out.println("\n*** Please enter the desired command number ***");
		System.out.println("   1-List computers");
		System.out.println("   2-List companies");
		System.out.println("   3-Show computer details");
		System.out.println("   4-Add new computer");
		System.out.println("   5-Update existing computer");
		System.out.println("   6-Delete existing computer");
		System.out.println("   7-Exit");
	}
	
	
	private void displayDetails() {
		System.out.print("Enter computer ID:\n>>");
		long id = sc.nextLong();
		Computer c = ComputerDatabaseDAO.findComputer(id);
		if (c != null) {
			System.out.println("Computer Details:");
			String displayedName = c.getName() != null ? c.getName() : "unknown";
			System.out.printf("--- %s (%d) ---\n",displayedName,c.getId());
			String displayedIntro = c.getIntroduced() != null ? c.getIntroduced().toString() : "unknown";
			String displayedDisc = c.getDiscontinued() != null ? c.getDiscontinued().toString() : "unknown";
			System.out.printf("** Produced from %s to %s \n",displayedIntro,displayedDisc);
			Company company = c.getCompany();
			String displayedCompanyId = "unknown";
			String displayedCompanyName = "unknown";
			if(company != null) {
				displayedCompanyId = String.valueOf(company.getId());
				displayedCompanyName = company.getName();
			}
			System.out.printf("** Company: %s (%s)\n",displayedCompanyName,displayedCompanyId);
		}
		else {
			System.out.println("No such computer found, please kindly enter a proper computer's ID");
		}

	}
	
	private void updateComputer() {
		Computer c = createComputerForm();
		System.out.print("+++Enter replaced computer id:\n>>");
		long computerId = takeIdInput();
		System.out.println("+++Update computer "+computerId+" with:"+c.toString());
		long result = ComputerDatabaseDAO.updateComputer(computerId,c);
		if (result != 0) {
			System.out.println("Computer successfully updated!");
		}
		else {
			System.out.println("Computer not updated");
		}
	}
	
	private void addComputer() {
		Computer c = createComputerForm();
		System.out.println("+++Create:"+c.toString());
		long result = ComputerDatabaseDAO.addComputer(c);
		if (result != 0) {
			System.out.println("Computer successfully created! (ID="+result+")");
		}
		else {
			System.out.println("Computer not added");
		}
	}
	
	private Computer createComputerForm() {
		System.out.println("Please enter computer informations, ignore if not applicable:");
		System.out.println("+++Enter computer name (<=255 characters):");
		String name = takeNameInput();
		System.out.println("+++Enter date of introduction (Format: yyyy-[m]m-[d]d [hh:mm:ss[.f...]]:");
		Timestamp introducted = takeTimestampInput();
		System.out.println("+++Enter date of end (Format: yyyy-[m]m-[d]d [hh:mm:ss[.f...]]:");
		Timestamp discontinued = takeTimestampInput();
		System.out.print("+++Enter company id:\n>>");
		long companyId = takeIdInput();
		return new Computer(name,introducted,discontinued,companyId);
	}

	private void displayCompanies() {
		List<Company> companies = CompanyDatabaseDAO.getCompanies();
		System.out.printf("| %-3s   %-50s |\n","ID","NAME");
		for (int i =0; i <10; i++) {
			System.out.printf("| %-3d   %-50s |\n",companies.get(i).getId(),companies.get(i).getName());
		}
	}

	private void displayComputers() {
		List<Computer> computers = ComputerDatabaseDAO.getComputers();
		System.out.printf("| %-3s   %-50s |\n","ID","NAME");
		for (int i =0; i <10; i++) {
			System.out.printf("| %-3d   %-50s |\n",computers.get(i).getId(),computers.get(i).getName());
		}
	}
	
	private String takeNameInput() {
		System.out.print(">>");
		String input = sc.nextLine();
		if (input.trim().length()>255) {
			input = input.trim().substring(0,255);
		}
		return input;
	}
	
	private long takeIdInput() {
		long input;
		do { 
			System.out.print(">>");
			input =  sc.nextLong();
		} while (!InputValidator.isValidId(input));
		return input;
	}
	
	private Timestamp takeTimestampInput() {
		String input;
		do { 
			System.out.print(">>");
			
			input = sc.nextLine();
			if(Pattern.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$", input)) {
				input += " 00:00:00";
			}
		} while (!InputValidator.isValidTimestamp(input));
		return Timestamp.valueOf(input);
	}
	
	protected void finalize() {
		if (sc != null){
			sc.close();
		}
	}
	
}
