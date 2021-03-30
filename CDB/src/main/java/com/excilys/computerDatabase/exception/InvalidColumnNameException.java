package com.excilys.computerDatabase.exception;

public class InvalidColumnNameException extends Exception {
	
	private static final long serialVersionUID = 2984193431830153249L;

	public InvalidColumnNameException(String columnName) {
		super("Column name "+columnName+ "unkown");
	}
}
