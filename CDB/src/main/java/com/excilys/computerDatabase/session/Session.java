package com.excilys.computerDatabase.session;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.context.annotation.ScopedProxyMode;

@Component
@Scope(
	value = WebApplicationContext.SCOPE_SESSION,
	proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Session {
	
	private int rowsPerPage;
	private String sortingCriteria;
	
	public int getRowsPerPage() {
		return rowsPerPage;
	}
	public void setRowsPerPage(int rowsPerPage) {
		this.rowsPerPage = rowsPerPage;
	}
	public String getSortingCriteria() {
		return sortingCriteria;
	}
	public void setSortingCriteria(String sortingCriteria) {
		this.sortingCriteria = sortingCriteria;
	}
	
}
