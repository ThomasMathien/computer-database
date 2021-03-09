package main.java.com.excilys.computerDatabase.exception;

public class IncompleteResultSet extends Exception {

	public  IncompleteResultSet(String message) {
		super(message);
	}
	
	public IncompleteResultSet(Throwable cause) {
		super(cause);
	}
	
	public IncompleteResultSet(String message, Throwable cause) {
		super(message,cause);
	}
}
