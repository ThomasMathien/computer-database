package com.excilys.computerDatabase.controller.page;

import java.util.List;

import com.excilys.computerDatabase.dto.Pageable;

public class Page<T extends Pageable> {
	
	private List<T> content;
	
	public Page(List<T> content) {
		this.content = content;
	}
	
	public List<T> getContent() {
		return this.content;
	}
	
}
