package main.java.com.excilys.computerDatabase.ui;

import main.java.com.excilys.computerDatabase.model.Company;

public class DisplayCompany  implements Displayable {

	private Company company;
	
	public DisplayCompany(Company c) {
		if (c == null) {
			throw new IllegalArgumentException();
		}
		this.company = c;
	}

	@Override
	public void displayHeader() {
		System.out.printf("| %-3s   %-50s |\n","ID","NAME");
	}

	@Override
	public void display() {
		System.out.printf("| %-3d   %-50s |\n",company.getId(),company.getName());
	}

	@Override
	public void displayFooter() {

	}

}
