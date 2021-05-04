package com.excilys.computerDatabase.exception;

public class CompanyNotFoundException extends Exception {

	private static final long serialVersionUID = -5650020188460637290L;

	public CompanyNotFoundException (long id) {
		super("Couldn't find company with id:"+id);
	}
	
}
