package main.java.com.excilys.computerDatabase.validator;

import java.sql.Timestamp;

public abstract class InputValidator {
	
	public static boolean isValidTimestamp(String s){
		try {
			Timestamp.valueOf(s);
			return true;
		}
		catch(IllegalArgumentException e) {
			return false;
		}
	}
	
}
