package main.java.com.excilys.computerDatabase.ui;

import java.util.List;

import main.java.com.excilys.computerDatabase.exception.PageOutOfBoundException;

public class Page {
	public final static int MAX_LINES_PER_PAGE = 10;

	private List<Displayable> content;
	private int currentPage;
	private int maxPages;
	
	
	public Page(List<Displayable> content) {
		this.content = content;
		this.currentPage = 0;
		this.maxPages = content.size() % MAX_LINES_PER_PAGE;
	}
	
	public void print(int pageIndex) {
		if (!content.isEmpty()) {
			content.get(0).displayHeader();
			for (int i = pageIndex * MAX_LINES_PER_PAGE; i < MAX_LINES_PER_PAGE; i++ ) {
				if ( i < content.size() && content.get(i) != null) {
					content.get(i).display();
				}
			}
			content.get(0).displayFooter();
		}
	}
	
	public void feedContent(List<Displayable> content) {
		this.content.addAll(content);
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
