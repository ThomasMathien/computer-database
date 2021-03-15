package main.java.com.excilys.computerDatabase.ui.view;

import main.java.com.excilys.computerDatabase.model.Company;
import main.java.com.excilys.computerDatabase.model.Computer;

public class DetailsDisplayComputer implements Displayable {

	private Computer computer;
	
	public DetailsDisplayComputer(Computer c) {
		this.computer = c;
	}

	@Override
	public void displayHeader() {

	}

	@Override
	public void display() {
		String displayedName = computer.getName() != null ? computer.getName() : "unknown";
		String displayedIntro = computer.getIntroduced() != null ? computer.getIntroduced().toString() : "unknown";
		String displayedDisc = computer.getDiscontinued() != null ? computer.getDiscontinued().toString() : "unknown";
		Company company = computer.getCompany();
		String displayedCompanyId = "unknown";
		String displayedCompanyName = "unknown";
		if(company != null) {
			displayedCompanyId = String.valueOf(company.getId());
			displayedCompanyName = company.getName();
		}
		System.out.println("Computer Details:");
		System.out.printf("--- %s (%d) ---\n",displayedName,computer.getId());
		System.out.printf("** Produced from %s to %s \n",displayedIntro,displayedDisc);
		System.out.printf("** Company: %s (%s)\n",displayedCompanyName,displayedCompanyId);
	}

	@Override
	public void displayFooter() {

	}

}
