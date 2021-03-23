package com.excilys.computerDatabase.validator;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import com.excilys.computerDatabase.exception.InvalidValuesException;

public abstract class InputValidator {
	
	public static void validateNewComputer(String name, String introduced, String discontinued, String companyId) throws InvalidValuesException {
		if (!isValidName(name)) {
			throw new InvalidValuesException("Name not valid");
		}
		if (isValidDate(introduced) || isValidDate(discontinued)) {
			if (!isValidDateInterval(LocalDate.parse(introduced), LocalDate.parse(discontinued))) {
				throw new InvalidValuesException("Date precedence not valid");
			}
		}
		try {
			if (!isValidId(Long.parseLong(companyId))){
				throw new InvalidValuesException("Id not valid");
			}
		}
		catch (NumberFormatException e) {
			throw new InvalidValuesException("Id isn't a valid Long");
		}
	}
	
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
