package com.excilys.computerDatabase.exception;

public class InvalidValuesException extends Exception {

	private static final long serialVersionUID = -2098360565109740849L;

	public  InvalidValuesException(String message) {
		super(message);
	}
	
	public InvalidValuesException(Throwable cause) {
		super(cause);
	}
	
	public InvalidValuesException(String message, Throwable cause) {
		super(message,cause);
	}
}
