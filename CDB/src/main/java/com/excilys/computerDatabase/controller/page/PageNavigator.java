package com.excilys.computerDatabase.controller.page;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.computerDatabase.exception.PageOutOfBoundException;
import com.excilys.computerDatabase.service.CompanyService;
import com.excilys.computerDatabase.service.ComputerService;
import com.excilys.computerDatabase.ui.view.DisplayCompany;
import com.excilys.computerDatabase.ui.view.Displayable;
import com.excilys.computerDatabase.ui.view.ShortDisplayComputer;

public class PageNavigator {

	private final Logger logger = LoggerFactory.getLogger(PageNavigator.class);
	
	private final String NEXT_PAGE_COMMAND = "+";
	private final String PREVIOUS_PAGE_COMMAND = "-";
	private final String EXIT_COMMAND = "0";
	
	public final static int GET_COMPUTERS_REQUEST = 1;
	public final static int GET_COMPANIES_REQUEST = 2;
	PageCLI page;
	
	private static PageNavigator instance = null;
	
	private PageNavigator() {}
	
	public static PageNavigator getInstance() {
		if (instance == null) {
			instance = new PageNavigator();
		}
		return instance;
	}

	private int getTotalToFetch(int request) {
		switch(request) {
		case GET_COMPUTERS_REQUEST:
			return ComputerService.getInstance().getComputerCount();
		case GET_COMPANIES_REQUEST:
			return CompanyService.getInstance().getCompanyCount();
		default:
			return 0;
		}
	}
	
	private List<Displayable> getDisplayables(int request, int from, int amount) {
		switch(request) {
		case GET_COMPUTERS_REQUEST:
			return ComputerService.getInstance().getComputers(from,amount).stream().map( c -> new ShortDisplayComputer(c)).collect(Collectors.toList());
		case GET_COMPANIES_REQUEST:
			return CompanyService.getInstance().getCompanies(from, amount).stream().map( c -> new DisplayCompany(c)).collect(Collectors.toList());
		default:
			return null;
		}
	}
	
	public void run (Scanner sc, int request) {
		int totalToFetch = getTotalToFetch(request);
		List<Displayable> content = getDisplayables(request,0,PageCLI.MAX_LINES_PER_PAGE);
		PageCLI p = new PageCLI(content, totalToFetch);
		p.print();
		while (true) {
			System.out.println("***** Use "+PREVIOUS_PAGE_COMMAND+" and "+NEXT_PAGE_COMMAND+" to navigate and "+EXIT_COMMAND+" to exit *****");
			System.out.print(">>");	
			String command = sc.nextLine();
			switch(command) {
			case NEXT_PAGE_COMMAND:
				if (p.hasNextPage()) {
					try {
						if (p.needsFeeding()) {
							System.out.println("Loading ...");
							p.feedContent(getDisplayables(request, p.size(),PageCLI.MAX_LINES_PER_PAGE));
						}
						p.nextPage();
						p.print();
					} catch (PageOutOfBoundException e) {
						logger.error("Next Page Failed: from Page "+p.toString(),e);
					}
				}
				else {
					System.out.println("No more pages");
				}
				break;
			case PREVIOUS_PAGE_COMMAND:
				if (p.hasPreviousPage()) {
					try {
						p.previousPage();
						p.print();
					} catch (PageOutOfBoundException e) {
						logger.error("Previous Page Failed: from Page "+p.toString(),e);
					}
				}
				else {
					System.out.println("Already at first page");
				}
				break;
			case EXIT_COMMAND:
				return;
			default: 
				System.out.println("Invalid command, please try again");
			}
		}
	}


}
