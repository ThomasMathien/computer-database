package com.excilys.computerDatabase.exception.invalidValuesException;

public class InvalidNameException extends InvalidValuesException{

	private static final long serialVersionUID = 6212298941009517054L;

	public InvalidNameException(String message) {
		super("This Name doesn't conform to validation: " + message);
	}
	
}
