package com.excilys.computerDatabase.controller.page;

import java.util.List;

import com.excilys.computerDatabase.dto.Pageable;

public class Page<T extends Pageable> {
	
	public static final String DEFAULT_PAGE_INDEX = "1";
	public static final String DEFAULT_ROWS_PER_PAGE = "10";
	
	private List<T> content;
	
	public Page(List<T> content) {
		this.content = content;
	}
	
	public List<T> getContent() {
		return this.content;
	}
	
}
