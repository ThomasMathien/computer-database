package main.java.com.excilys.computerDatabase.ui;

import java.sql.Timestamp;
import java.util.List;
import java.util.Scanner;

import main.java.com.excilys.computerDatabase.dao.DatabaseDAO;
import main.java.com.excilys.computerDatabase.model.Company;
import main.java.com.excilys.computerDatabase.model.Computer;

public class CLIController {

	Scanner sc = new Scanner(System.in);
	
	public void displayMainMenu() {
		System.out.println("\n*** Please enter the desired command number ***");
		System.out.println("   1-List computers");
		System.out.println("   2-List companies");
		System.out.println("   3-Show computer details");
		System.out.println("   4-Add new computer");
		System.out.println("   5-Update existing computer");
		System.out.println("   6-Delete existing computer");
	}
	
	public void run(){

		while (true) {
			displayMainMenu();
			System.out.print(">>");	
			int command = sc.nextInt();
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
			default:
				sc.close();
				return;
			}
		}
	}

	private void displayDetails() {
		System.out.print("Enter computer ID:\n>>");
		long id = sc.nextLong();
		Computer c = DatabaseDAO.findComputer(id);
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

	private void addComputer() {
		System.out.println("Please enter computer informations, ignore if not applicable:");
		System.out.print("Enter computer name (<=255 characters):\n>>");
		String name = sc.nextLine();
		if (name.trim().length()>255) {
			name = name.trim().substring(0,255);
		}
		System.out.print("Enter date of introduction (Format: yyyy-[m]m-[d]d hh:mm:ss[.f...]:\n>>");
		String introduction = sc.nextLine();
		Timestamp intro = Timestamp.valueOf(introduction);
		System.out.print("Enter date of end (Format: yyyy-[m]m-[d]d hh:mm:ss[.f...]:\n>>");
		String discontinuation = sc.nextLine();
		Timestamp ds = Timestamp.valueOf(discontinuation);			
		System.out.print("Enter company id:\n>>");
		long id = sc.nextLong();
		Computer c = new Computer(name,intro,ds,id);
		DatabaseDAO.addComputer(c);
	}

	private void displayCompanies() {
		List<Company> companies = DatabaseDAO.getCompanies();
		System.out.printf("| %-3s   %-50s |\n","ID","NAME");
		for (int i =0; i <10; i++) {
			System.out.printf("| %-3d   %-50s |\n",companies.get(i).getId(),companies.get(i).getName());
		}
	}

	private void displayComputers() {
		List<Computer> computers = DatabaseDAO.getComputers();
		System.out.printf("| %-3s   %-50s |\n","ID","NAME");
		for (int i =0; i <10; i++) {
			System.out.printf("| %-3d   %-50s |\n",computers.get(i).getId(),computers.get(i).getName());
		}
	}
}
