package com.excilys.computerDatabase.mapper;

import java.util.Map;

import com.excilys.computerDatabase.exception.InvalidColumnNameException;

public class ColumnsNameMapper {
	
	private static final Map<String, String> DB_COLS_FOR_FRONT_NAME = Map.of(
			"computerName", "computer.name",
			"introduced", "computer.introduced",
			"discontinued", "computer.discontinued",
			"companyName", "company.name"
			);
	
	public static String getDbColumnName(String frontColumnName) throws InvalidColumnNameException {
		if (frontColumnName != null && DB_COLS_FOR_FRONT_NAME.containsKey(frontColumnName)) {
			return DB_COLS_FOR_FRONT_NAME.get(frontColumnName);
		} else {
			throw new InvalidColumnNameException(frontColumnName);
		}
	}
}
