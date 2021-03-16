package main.java.com.excilys.computerDatabase.controller.cli;

import main.java.com.excilys.computerDatabase.exception.CommandNotFoundException;

public enum MenuOption {
	DISPLAY_ALL_COMPUTERS("1","List computers"),
	DISPLAY_ALL_COMPANIES("2","List companies"),
	DISPLAY_COMPUTER_DETAILS_BY_ID("3","Show computer details"),
	ADD_COMPUTER("4","Add new computer"),
	UPDATE_COMPUTER("5","Update existing computer"),
	DELETE_COMPUTER("6","Delete existing computer"),
	EXIT_MENU("7","Exit");
	
	private final String command;
	private final String message;
	
	private MenuOption(String command, String message) {
		this.command = command;
		this.message = message;
	}

	public String getCommand() {
		return command;
	}

	public String getMessage() {
		return message;
	}
	
	public static MenuOption fromCommand(String command) throws CommandNotFoundException {
	    for (MenuOption option : values()) {
	        if (option.getCommand().equals(command)) {
	            return option;
	        }
	    }
	    throw new CommandNotFoundException("Command "+ command+ "not found");
	}
}