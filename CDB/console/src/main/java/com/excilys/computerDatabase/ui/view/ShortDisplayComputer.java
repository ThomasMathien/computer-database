package com.excilys.computerDatabase.ui.view;

import com.excilys.computerDatabase.model.Computer;

public class ShortDisplayComputer  implements Displayable {

	private Computer computer;
	
	public ShortDisplayComputer(Computer c) {
		if (c == null) {
			throw new IllegalArgumentException();
		}
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
