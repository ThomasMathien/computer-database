package main.java.com.excilys.computerDatabase.exception;

public class FailedSQLRequestException extends Exception {

	private static final long serialVersionUID = -121460926827582755L;

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
