package main.java.com.excilys.computerDatabase.ui;

import java.sql.Timestamp;
import java.util.Scanner;
import java.util.regex.Pattern;

import main.java.com.excilys.computerDatabase.validator.InputValidator;

public abstract class InputParser {

	public static String takeNameInput(Scanner sc) {
		System.out.print(">>");
		String input = sc.nextLine();
		if (input.trim().length()>255) {
			input = input.trim().substring(0,255);
		}
		return input;
	}
	
	public static long takeIdInput(Scanner sc) {
		long input;
		do { 
			System.out.print(">>");
			input =  sc.nextLong();
		} while (!InputValidator.isValidId(input));
		sc.nextLine();
		return input;
	}
	
	public static Timestamp takeTimestampInput(Scanner sc) {
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
	
}
