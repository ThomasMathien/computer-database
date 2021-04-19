package com.excilys.computerDatabase.exception;

public class PageOutOfBoundException extends Exception {
	
	private static final long serialVersionUID = -4546579324707943245L;

	public  PageOutOfBoundException(String message) {
		super(message);
	}
	
	public PageOutOfBoundException(Throwable cause) {
		super(cause);
	}
	
	public PageOutOfBoundException(String message, Throwable cause) {
		super(message,cause);
	}
}
