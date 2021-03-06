package com.excilys.computerDatabase.search;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;

import com.excilys.computerDatabase.exception.InvalidColumnNameException;
import com.excilys.computerDatabase.mapper.ColumnsNameMapper;


public class SqlFilter {
	
	private static final String DEFAULT_COLUMN = "computer.name";
	public static final String DEFAULT_SEARCH = "%";
	private static final String DEFAULT_ORDER = "ASC";
	
	private Logger logger = LoggerFactory.getLogger(SqlFilter.class);
	
	String sortedColmun;
	String sortOrder;
	String searchFilter;
	
	ColumnsNameMapper columnsNameMapper;
	
	@Autowired
	public void setColumnsNameMapper(ColumnsNameMapper columnsNameMapper) {
		this.columnsNameMapper = columnsNameMapper;
	}
	
	public SqlFilter(String column, String order) {
		this(column, order, DEFAULT_SEARCH);
	}
	
	public SqlFilter(String column, String parsedSortCriterias, String search) {
		try {
			this.sortedColmun = ColumnsNameMapper.getDbColumnName(column);
		} catch (InvalidColumnNameException e) {
			logger.warn(e.getMessage() + " assigning default column instead");
			this.sortedColmun = DEFAULT_COLUMN;
		}
		if (parsedSortCriterias != null && ("ASC".equals(parsedSortCriterias.toUpperCase()) || "DESC".equals(parsedSortCriterias.toUpperCase())) ) {
			this.sortOrder = parsedSortCriterias;
		} else {
			this.sortOrder = DEFAULT_ORDER;
		}
		this.searchFilter = search == null ? DEFAULT_SEARCH : search;
	}
	
	public String getSortedColumn() { return this.sortedColmun; }
	public String getSortOrder() { return this.sortOrder; }
	public String getSearchFilter() { return this.searchFilter; }
	public void setSearch(String search) { this.searchFilter = search == null ? DEFAULT_SEARCH : search; }
	

}
