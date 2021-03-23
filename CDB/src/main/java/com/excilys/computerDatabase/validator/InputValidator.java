package com.excilys.computerDatabase.validator;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public abstract class InputValidator {
	
	public static boolean isValidDate(String s){
		try {
			LocalDate.parse(s);
			return true;
		}
		catch (DateTimeParseException e) {
			return false;
		}
	}

	public static boolean isValidId(long id) {
		return id >= 0;
	}
	
	public static boolean isValidName(String name) {
		return name.length() <= 255 && !name.isBlank();
	}
	
	public static boolean isValidDateInterval(LocalDate from, LocalDate until) {
		if ((from == null) || (until == null)) {
			return true;
		}
		else {
			return from.isBefore(until);
		}
	}
}
