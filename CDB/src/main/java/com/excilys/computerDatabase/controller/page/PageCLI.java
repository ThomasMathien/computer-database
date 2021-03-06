package com.excilys.computerDatabase.controller.page;

import java.util.List;

import com.excilys.computerDatabase.exception.PageOutOfBoundException;
import com.excilys.computerDatabase.ui.view.Displayable;

public class PageCLI {
	public final static int MAX_LINES_PER_PAGE = 10;

	private List<Displayable> content;
	private int currentPage;
	private int maxPages;
	private int maxToFetch;
	
	public PageCLI(List<Displayable> content, int maxToFetch) {
		this.content = content;
		this.maxToFetch = maxToFetch;
		this.currentPage = 0;
		this.maxPages = (int) Math.ceil(maxToFetch / MAX_LINES_PER_PAGE);
	}
	
	public int size() {
		return content.size();
	}
	
	public void print() {
		if (!content.isEmpty()) {
			content.get(0).displayHeader();
			for (int i = 0 ; i < MAX_LINES_PER_PAGE; i++ ) {
				int itemIndex = currentPage * MAX_LINES_PER_PAGE + i;
				if ( itemIndex < content.size() && content.get(itemIndex) != null) {
					content.get(itemIndex).display();
				}
			}
			content.get(0).displayFooter();
			System.out.println("      --- Page " + currentPage+ " / "+ maxPages + " ---");
		}
	}
	
	public boolean needsFeeding() {
		if (hasNextPage()) {
			int itemsNeeded = (currentPage + 2 )* MAX_LINES_PER_PAGE;
			if (content.size() >= itemsNeeded || content.size() == maxToFetch ) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}
	
	public int getCurrentPage() {
		return this.currentPage;
	}
	
	public void feedContent(List<Displayable> content) {
		this.content.addAll(content);
	}
	
	public void nextPage() throws PageOutOfBoundException {
		if (!hasNextPage()) {
			throw new PageOutOfBoundException("No more next page to go to");
		}
		currentPage++;
	}
	public void previousPage() throws PageOutOfBoundException {
		if (!hasPreviousPage()) {
			throw new PageOutOfBoundException("No more next page to go to");
		}
		currentPage--;
	}
	
	public boolean hasNextPage() {
		return currentPage < maxPages;
	}
	
	public boolean hasPreviousPage() {
		return currentPage > 0 ;
	}
	
	@Override
	public String toString() {
		return "Page [content=" + content + ", currentPage=" + currentPage + ", maxPages=" + maxPages + ", maxToFetch="
				+ maxToFetch + "]";
	}
}
