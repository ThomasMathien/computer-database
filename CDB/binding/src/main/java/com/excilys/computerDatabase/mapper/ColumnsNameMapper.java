package com.excilys.computerDatabase.mapper;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.excilys.computerDatabase.exception.InvalidColumnNameException;

@Component
public class ColumnsNameMapper {
	
	private static final Map<String, String> DB_COLS_FOR_FRONT_NAME = Map.of(
			"computerName", "name",
			"introduced", "introduced",
			"discontinued", "discontinued",
			"companyName", "company.name"
			);
	
	public String getDbColumnName(String frontColumnName) throws InvalidColumnNameException {
		if (frontColumnName != null && DB_COLS_FOR_FRONT_NAME.containsKey(frontColumnName)) {
			return DB_COLS_FOR_FRONT_NAME.get(frontColumnName);
		} else {
			throw new InvalidColumnNameException(frontColumnName);
		}
	}
}
