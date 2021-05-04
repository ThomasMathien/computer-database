package com.excilys.computerDatabase.exception;

public class ComputerNotFoundException extends Exception {

	private static final long serialVersionUID = -1400507708051032800L;

	public ComputerNotFoundException (long id) {
		super("Couldn't find computer with id:"+id);
	}
	
}
