package com.excilys.computerDatabase.controller.cli;

import java.time.LocalDate;
import java.util.Scanner;
import java.util.regex.Pattern;

import com.excilys.computerDatabase.validator.ComputerValidator;

public abstract class InputParser {

	public static String takeString(Scanner sc) {
		System.out.print(">>"); 
		return sc.nextLine().trim();
	}
	public static String takeNameInput(Scanner sc) {
		String name;
		//do { 
			name = takeString(sc);;
		//} while (!ComputerValidator.isValidName(name));
		return name;
	}
	
	public static long takeIdInput(Scanner sc) {
		long input;
		//do { 
			System.out.print(">>");
			input =  sc.nextLong();
		//} while (!ComputerValidator.isValidId(input));
		sc.nextLine();
		return input;
	}
	
	public static LocalDate takeLocalDateInput(Scanner sc) {
		String input;
	//	do { 
			System.out.print(">>");
			input = sc.nextLine();
	//	} while (!ComputerValidator.isValidDate(input));
		return LocalDate.parse(input);
	}
	
}
