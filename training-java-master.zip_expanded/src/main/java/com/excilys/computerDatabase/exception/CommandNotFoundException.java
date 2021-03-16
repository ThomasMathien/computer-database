package main.java.com.excilys.computerDatabase.exception;

public class CommandNotFoundException extends Exception {

	private static final long serialVersionUID = 5437972229671079256L;

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
	