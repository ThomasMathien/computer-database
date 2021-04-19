package com.excilys.computerDatabase.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

import com.excilys.computerDatabase.exception.InvalidColumnNameException;

@Component
public class ComputerSortMapper {

	private static final Direction DEFAULT_DIRECTION = Sort.Direction.ASC;
	private static final String DEFAULT_COLUMN = "name";
	
	ColumnsNameMapper columnsNameMapper;
	
	@Autowired
	public void setColumnsNameMapper(ColumnsNameMapper columnsNameMapper) {
		this.columnsNameMapper = columnsNameMapper;
	}
	
	public Sort toSort(String direction, String... properties) {
		Direction parsedDirection = DEFAULT_DIRECTION;
		if (direction != null && "desc".equals(direction.toLowerCase())) {
			parsedDirection = Sort.Direction.DESC;
		}
		List<String> parsedProperties = new ArrayList<String>();
		for (String prop: properties) {
			try {
				parsedProperties.add(columnsNameMapper.getDbColumnName(prop));
			} catch (InvalidColumnNameException e) {
				if (parsedProperties.isEmpty()) {
					parsedProperties.add(DEFAULT_COLUMN);
				}
			}
		}
		return Sort.by(parsedDirection, parsedProperties.toArray(new String[0]));
	}
}
