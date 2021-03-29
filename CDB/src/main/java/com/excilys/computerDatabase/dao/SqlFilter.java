package com.excilys.computerDatabase.dao;

public class SqlFilter {
	
	private static final String DEFAULT_COLUMN = "computer.id";
	private static final String DEFAULT_SEARCH = "%";
	private static final String DEFAULT_ORDER = "ASC";
	
	String column;
	String order;
	String search;
	
	public SqlFilter(String column, String order) {
		this.column = column;
		this.order = order;
	}
	
	public SqlFilter(String column, String parsedSortCriterias, String search) {
		this.column = column;
		this.order = parsedSortCriterias;
		this.search = search;
	}
	
	public String getColumn() { return this.column  == null ? DEFAULT_COLUMN : this.column; }
	public String getSQLOrder() { return order == null ? DEFAULT_ORDER : this.order; }
	public String getSearch() { return this.search == null ? DEFAULT_SEARCH : this.search; }
	public void setSearch(String search) { this.search = search; }
	
}
