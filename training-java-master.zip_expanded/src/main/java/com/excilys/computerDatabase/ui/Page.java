package main.java.com.excilys.computerDatabase.ui;

import java.util.List;

import main.java.com.excilys.computerDatabase.exception.PageOutOfBoundException;

public class Page {
	public final static int MAX_LINES_PER_PAGE = 10;

	private List<Displayable> content;
	private int currentPage;
	private int maxPages;
	private int maxToFetch;
	
	public Page(List<Displayable> content, int maxToFetch) {
		this.content = content;
		this.maxToFetch = maxToFetch;
		this.currentPage = 0;
		this.maxPages = (int) Math.ceil(content.size() / MAX_LINES_PER_PAGE);
	}
	
	public void print() {
		System.out.print("#### Content size = "+content.size());
		if (!content.isEmpty()) {
			content.get(0).displayHeader();
			System.out.println("#### Display from = "+currentPage * MAX_LINES_PER_PAGE +
					" to "+ (currentPage * MAX_LINES_PER_PAGE +MAX_LINES_PER_PAGE));
			for (int i = 0 ; i < MAX_LINES_PER_PAGE; i++ ) {
				int itemIndex = currentPage * MAX_LINES_PER_PAGE + i;
				if ( itemIndex < content.size() && content.get(itemIndex) != null) {
					content.get(itemIndex).display();
				}
			}
			content.get(0).displayFooter();
			System.out.println("      --- " + currentPage + " ---");
		}
	}
	
	public boolean needsFeeding() {
		if (hasNextPage()) {
			int itemsNeeded = (currentPage +1 )* MAX_LINES_PER_PAGE;
			if (content.size() >= itemsNeeded || content.size() == maxToFetch ) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}
	
	public void feedContent(List<Displayable> content) {
		this.content.addAll(content);
		this.maxPages = (int) Math.ceil(content.size() / MAX_LINES_PER_PAGE);
	}
	
	public void nextPage() throws PageOutOfBoundException {
		currentPage++;
		if (currentPage >= maxPages) {
			throw new PageOutOfBoundException("No more next page to go to");
		}
	}
	public void previousPage() throws PageOutOfBoundException {
		currentPage--;
		if (currentPage <0 ) {
			throw new PageOutOfBoundException("No more next page to go to");
		}
	}
	
	public boolean hasNextPage() {
		return currentPage +1 < maxPages;
	}
	
	public boolean hasPreviousPage() {
		return currentPage >= 1 ;
	}
}
