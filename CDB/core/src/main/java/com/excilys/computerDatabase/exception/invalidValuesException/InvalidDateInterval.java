package com.excilys.computerDatabase.exception.invalidValuesException;

public class InvalidDateInterval extends InvalidValuesException{

	private static final long serialVersionUID = 7265762267279109053L;

	public InvalidDateInterval(String message) {
		super("This Date interval doesn't conform to validation: " + message);
	}
}
