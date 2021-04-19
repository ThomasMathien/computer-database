package com.excilys.computerDatabase.exception.invalidValuesException;

public class InvalidIdException extends InvalidValuesException{

	private static final long serialVersionUID = -8084545690418300187L;

	public InvalidIdException(String message) {
		super("This Id doesn't conform to validation: " + message);
	}
}
