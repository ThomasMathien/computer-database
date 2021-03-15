package main.java.com.excilys.computerDatabase.exception;

public class FailedSQLRequestException extends Exception {

	public  FailedSQLRequestException(String message) {
		super(message);
	}
	
	public FailedSQLRequestException(Throwable cause) {
		super(cause);
	}
	
	public FailedSQLRequestException(String message, Throwable cause) {
		super(message,cause);
	}
	
}
