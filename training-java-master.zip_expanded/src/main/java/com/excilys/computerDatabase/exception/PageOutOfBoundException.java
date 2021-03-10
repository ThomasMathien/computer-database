package main.java.com.excilys.computerDatabase.exception;

public class PageOutOfBoundException extends Exception {
	
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
