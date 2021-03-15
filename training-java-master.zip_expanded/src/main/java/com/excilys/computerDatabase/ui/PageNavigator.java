package main.java.com.excilys.computerDatabase.ui;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import main.java.com.excilys.computerDatabase.dao.CompanyDatabaseDAO;
import main.java.com.excilys.computerDatabase.dao.ComputerDatabaseDAO;
import main.java.com.excilys.computerDatabase.exception.PageOutOfBoundException;
import main.java.com.excilys.computerDatabase.ui.view.DisplayCompany;
import main.java.com.excilys.computerDatabase.ui.view.Displayable;
import main.java.com.excilys.computerDatabase.ui.view.ShortDisplayComputer;

public class PageNavigator {

	public final static int GET_COMPUTERS_REQUEST = 1;
	public final static int GET_COMPANIES_REQUEST = 2;
	Page page;
	
	public PageNavigator() {

	}

	private int getTotalToFetch(int request) {
		switch(request) {
		case GET_COMPUTERS_REQUEST:
			return ComputerDatabaseDAO.getComputerCount();
		case GET_COMPANIES_REQUEST:
			return CompanyDatabaseDAO.getCompanyCount();
		default:
			return 0;
		}
	}
	
	private List<Displayable> getDisplayables(int request, int from, int amount) {
		switch(request) {
		case GET_COMPUTERS_REQUEST:
			return ComputerDatabaseDAO.getComputers(from,amount).stream().map( c -> new ShortDisplayComputer(c)).collect(Collectors.toList());
		case GET_COMPANIES_REQUEST:
			return CompanyDatabaseDAO.getCompanies(from, amount).stream().map( c -> new DisplayCompany(c)).collect(Collectors.toList());
		default:
			return null;
		}
	}
	
	public void run (Scanner sc, int request) {
		int totalToFetch = getTotalToFetch(request);
		List<Displayable> content = getDisplayables(request,0,Page.MAX_LINES_PER_PAGE);
		Page p = new Page(content, totalToFetch);
		p.print();
		while (true) {
			System.out.println("***** Use - and + to navigate and 0 to exit *****");
			System.out.print(">>");	
			String command = sc.nextLine();
			switch(command) {
			case "+":
				if (p.hasNextPage()) {
					try {
						if (p.needsFeeding()) {
							System.out.println("Loading ...");
							p.feedContent(getDisplayables(request, p.size(),Page.MAX_LINES_PER_PAGE));
						}
						p.nextPage();
						p.print();
					} catch (PageOutOfBoundException e) {
						e.printStackTrace();
					}
				}
				else {
					System.out.println("No more pages");
				}
				break;
			case "-":
				if (p.hasPreviousPage()) {
					try {
						p.previousPage();
						p.print();
					} catch (PageOutOfBoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else {
					System.out.println("Already at first page");
				}
				break;
			case "0":
				return;
			default: 
				System.out.println("Invalid command, please try again");
			}
		}
	}


}
