package com.excilys.computerDatabase.controller.cli;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.Scanner;

import com.excilys.computerDatabase.controller.page.PageNavigator;
import com.excilys.computerDatabase.exception.CommandNotFoundException;
import com.excilys.computerDatabase.exception.FailedSQLRequestException;
import com.excilys.computerDatabase.model.Computer;
import com.excilys.computerDatabase.model.builder.ComputerBuilder;
import com.excilys.computerDatabase.service.ComputerService;
import com.excilys.computerDatabase.ui.view.DetailsDisplayComputer;

public class CLIController {
	
	private static CLIController instance;
	private Scanner sc;
	
	private CLIController() {
		 sc = new Scanner(System.in);
	}
	
	public static CLIController getCLIController() {
		if (instance == null) {
			instance = new CLIController();
		}
		return instance;
	}
	
	public void run(){
		while (true) {
			displayMainMenu();
			String command = InputParser.takeString(sc);
			MenuOption menuOption;
			try {
				menuOption = MenuOption.fromCommand(command);
				System.out.println("Please wait...");
				switch(menuOption) {
					case DISPLAY_ALL_COMPUTERS:
						displayComputers();
						break;
					case DISPLAY_ALL_COMPANIES:
						displayCompanies();
						break;
					case DISPLAY_COMPUTER_DETAILS_BY_ID: 
						displayDetails();
						break;
					case ADD_COMPUTER:
						addComputer();
						break;
					case UPDATE_COMPUTER:
						updateComputer();
						break;
					case DELETE_COMPUTER:
						deleteComputer();
						break;
					case EXIT_MENU:
						sc.close();
						return;
					default:
						throw new CommandNotFoundException("Command "+ command+ "not found");
				}
			} catch (CommandNotFoundException e) {
				System.out.println("Invalid command, please try again");
			}
		} 
	}

	private void displayMainMenu() {
		System.out.println("\n*** Please enter the desired command number ***");
		for (MenuOption option : MenuOption.values()) {
			System.out.printf("   %s-%s\n",option.getCommand(),option.getMessage());
		}
	}

	private void displayDetails() {
		System.out.print("Enter computer ID:\n>>");
		long id = sc.nextLong();
		Optional<Computer> c = ComputerService.getInstance().findComputer(id);
		if (c.isPresent()) {
			new DetailsDisplayComputer(c.orElseThrow()).display();
		}
		else {
			System.out.println("No such computer found, please kindly enter a proper computer's ID");
		}
	}
	
	private void addComputer() {
		Computer c = createComputerForm();
		System.out.println("+++Create:"+c.toString());
		try {
			ComputerService.getInstance().addComputer(c);
		}
		catch (FailedSQLRequestException e) {
			System.out.println("Computer not added");
		}
		System.out.println("Computer successfully created!");
	}
	
	private void deleteComputer() {
		System.out.print("+++Enter deleted computer id:\n>>");
		long computerId = InputParser.takeIdInput(sc);
		try {
			ComputerService.getInstance().deleteComputer(computerId);
		}
		catch (FailedSQLRequestException e) {
			System.out.println("Computer not deleted");
		}
		System.out.println("Computer "+computerId+" successfully deleted!");
	}
	
	private void updateComputer() {
		Computer c = createComputerForm();
		System.out.print("+++Enter replaced computer id:\n>>");
		long computerId = InputParser.takeIdInput(sc);
		System.out.println("+++Update computer "+computerId+" with:"+c.toString());
		try {
			ComputerService.getInstance().updateComputer(computerId,c);
		}
		catch (FailedSQLRequestException e) {
			System.out.println("Computer not updated");
		}
		System.out.println("Computer successfully updated!");
	}
	
	private Computer createComputerForm() {
		System.out.println("Please enter computer informations, ignore if not applicable:");
		System.out.println("+++Enter computer name (<=255 characters):");
		String name = InputParser.takeNameInput(sc);
		System.out.println("+++Enter date of introduction (Format: yyyy-[m]m-[d]d [hh:mm:ss[.f...]]:");
		Timestamp introducted = InputParser.takeTimestampInput(sc);
		System.out.println("+++Enter date of end (Format: yyyy-[m]m-[d]d [hh:mm:ss[.f...]]:");
		Timestamp discontinued = InputParser.takeTimestampInput(sc);
		System.out.print("+++Enter company id:\n>>");
		long companyId = InputParser.takeIdInput(sc);
		return new ComputerBuilder(name)
				.setIntroduced(introducted)
				.setDiscontinued(discontinued)
				.setCompany(companyId)
				.build();
	}

	private void displayCompanies() {
		PageNavigator.getInstance().run(sc, PageNavigator.GET_COMPANIES_REQUEST);
	}

	private void displayComputers() {
		PageNavigator.getInstance().run(sc, PageNavigator.GET_COMPUTERS_REQUEST);
	}
	
}
