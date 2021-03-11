package main.java.com.excilys.computerDatabase.ui;

import main.java.com.excilys.computerDatabase.model.Computer;

public class DetailsDisplayComputer   implements Displayable {

	private Computer computer;
	
	public DetailsDisplayComputer(Computer c) {
		this.computer = c;
	}

	@Override
	public void displayHeader() {
		System.out.printf("| %-3s   %-50s |\n","ID","NAME");
	}

	@Override
	public void display() {
		System.out.printf("| %-3d   %-50s |\n",computer.getId(),computer.getName());
	}

	@Override
	public void displayFooter() {

	}

}
