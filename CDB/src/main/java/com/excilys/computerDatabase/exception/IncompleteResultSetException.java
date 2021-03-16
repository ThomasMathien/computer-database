package com.excilys.computerDatabase.exception;

public class IncompleteResultSetException extends Exception {

	private static final long serialVersionUID = -4647265899397472295L;

	public  IncompleteResultSetException(String message) {
		super(message);
	}
	
	public IncompleteResultSetException(Throwable cause) {
		super(cause);
	}
	
	public IncompleteResultSetException(String message, Throwable cause) {
		super(message,cause);
	}
}
