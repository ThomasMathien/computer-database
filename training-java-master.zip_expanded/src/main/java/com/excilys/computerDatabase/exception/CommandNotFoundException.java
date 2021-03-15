package main.java.com.excilys.computerDatabase.exception;

public class CommandNotFoundException extends Exception {

	public  CommandNotFoundException(String message) {
		super(message);
	}
	
	public CommandNotFoundException(Throwable cause) {
		super(cause);
	}
	
	public CommandNotFoundException(String message, Throwable cause) {
		super(message,cause);
	}
}
	