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

	public static boolean isValidId(long id) {
		return id >=0;
	}
	
	public static boolean isValidName(String name) {
		return name.length()<=255;
	}
	
	public static boolean isValidTimestampInterval(Timestamp from, Timestamp until) {
		if (from == null ^ until == null) {
			return true;
		}
		return from != null && from.before(until);
	}
}
