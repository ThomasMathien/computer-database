package com.excilys.computerDatabase.exception.invalidValuesException;

public class CompanyNotExistantException extends InvalidValuesException {

	private static final long serialVersionUID = -4790464129231810539L;

	public CompanyNotExistantException(long id) {
		super("No company could'nt be found matching this id: " + id);
	}
	
}
