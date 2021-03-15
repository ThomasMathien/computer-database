package main.java.com.excilys.computerDatabase.exception;

public class IncompleteResultSetException extends Exception {

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
