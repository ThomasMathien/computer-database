package com.excilys.computerDatabase.controller.servlet;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;

import com.excilys.computerDatabase.controller.page.Page;
import com.excilys.computerDatabase.dao.SqlFilter;
import com.excilys.computerDatabase.dto.ComputerFormDTO;
import com.excilys.computerDatabase.exception.FailedSQLRequestException;
import com.excilys.computerDatabase.mapper.ComputerMapper;
import com.excilys.computerDatabase.model.Computer;
import com.excilys.computerDatabase.service.ComputerService;

@Configuration
public class DashboardServlet extends SpringServlet {

	private static final long serialVersionUID = 8233813063630626361L;
	
	private static final String ROWS_PER_PAGE_ATTRIBUTE = "displayedRowsPerPage";
	private static final String PAGE_INDEX_ATTRIBUTE = "pageIndex";
	private static final String TOTAL_COMPUTERS_ATTRIBUTE = "totalComputers";
	private static final String COMPUTER_LIST_ATTRIBUTE = "computers";
	private static final String MAX_PAGES_ATTRIBUTE = "maxPages";
	private static final String SELECTED_COMPUTERS_ATTRIBUTE = "selection";
	private static final String SELECTED_COMPUTER_DELIMITER = ",";
	private static final String SEARCH_PARAMETER = "search";
	private static final String SORTING_CRITERIA_ATTRIBUTE = "sortCriteria";
	private static final String SORTING_CRITERIA_DELIMITER = ";";
	
	private static final String VIEW_PATH = "/WEB-INF/views/dashboard.jsp";
	
	Logger logger = LoggerFactory.getLogger(DashboardServlet.class);
	
	private final int DEFAULT_PAGE_INDEX = 1;
	private final int DEFAULT_ROWS_PER_PAGE = 10;
	
	@Autowired
	ComputerService computerService;
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		displayResults(request, response);
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		handleMultiplePosts(request);
		displayResults(request, response);
	}
	
	private void handleMultiplePosts(HttpServletRequest request) {
		if (request.getParameter(ROWS_PER_PAGE_ATTRIBUTE) != null) {
			setSessionAttributeFromParam(request, ROWS_PER_PAGE_ATTRIBUTE);
		} else if (request.getParameter(SORTING_CRITERIA_ATTRIBUTE) != null) {
			setSessionAttributeFromParam(request, SORTING_CRITERIA_ATTRIBUTE);
		} else if (request.getParameter(SELECTED_COMPUTERS_ATTRIBUTE) != null) {
			deleteComputer(Stream.of(request.getParameter(SELECTED_COMPUTERS_ATTRIBUTE).split(SELECTED_COMPUTER_DELIMITER)).mapToLong(Long::parseLong).toArray());
		}
	}
	
	private void deleteComputer(long[] computersId) {
		for (long id : computersId) {
			try {
				computerService.deleteComputer(id);
			} catch (FailedSQLRequestException e) {
				logger.warn(e.getMessage());
			}
		}
	}
	
	private void setSessionAttributeFromParam(HttpServletRequest request, String attributeName) {
		request.getSession().setAttribute(attributeName, request.getParameter(attributeName));
	}

	public void displayResults(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int rowsPerPage = getRequestedRowsPerPage(request);
		int currentPageIndex = getRequestedPageIndex(request);
		String search = request.getParameter(SEARCH_PARAMETER);
		String sortingCriteria = (String) request.getSession().getAttribute(SORTING_CRITERIA_ATTRIBUTE);
		String[] parsedSortCriterias = parseOrderBy(sortingCriteria);
		SqlFilter filter = new SqlFilter(parsedSortCriterias[0], parsedSortCriterias[1], search);
		List<Computer> computers = computerService.getComputers((currentPageIndex - 1) * rowsPerPage, rowsPerPage, filter);
		List<ComputerFormDTO> dtos = computers.stream().map(c -> ComputerMapper.getInstance().toComputerFormDTO(c)).collect(Collectors.toList());
		
		Page<ComputerFormDTO> page = new Page<ComputerFormDTO>(dtos);
		
		int totalComputers = computerService.getComputerCount(search);
		
		request.setAttribute(COMPUTER_LIST_ATTRIBUTE, page.getContent());
		request.setAttribute(TOTAL_COMPUTERS_ATTRIBUTE, totalComputers);
		request.setAttribute(MAX_PAGES_ATTRIBUTE, (int) Math.ceil(totalComputers / rowsPerPage));
		request.setAttribute(PAGE_INDEX_ATTRIBUTE, currentPageIndex);

		this.getServletContext().getRequestDispatcher(VIEW_PATH).forward(request, response);
	}
	
	private String[] parseOrderBy(String criterias) {
		String [] result = new String[2];
		if (criterias != null) {
			result =  criterias.split(SORTING_CRITERIA_DELIMITER);
		}
		return result;
	}
	
	private int getRequestedRowsPerPage(HttpServletRequest request) {
		String rowsPerPargeRequest = (String) request.getSession().getAttribute(ROWS_PER_PAGE_ATTRIBUTE);
		try {
			if (rowsPerPargeRequest != null) {
				return Integer.parseInt(rowsPerPargeRequest);
			}
		} catch (NumberFormatException e) {
			logger.warn("Parameter " + ROWS_PER_PAGE_ATTRIBUTE + ":" + rowsPerPargeRequest + " could'nt be conveted to an Int", e);
		}
		return DEFAULT_ROWS_PER_PAGE;
	}
	
	private int getRequestedPageIndex(HttpServletRequest request) {
		String pageIndex = request.getParameter(PAGE_INDEX_ATTRIBUTE);
		try {
			if (pageIndex != null) {
				return Integer.parseInt(pageIndex);
			}
		} catch (NumberFormatException e) {
			logger.warn("Parameter " + PAGE_INDEX_ATTRIBUTE + ":" + pageIndex + " could'nt be conveted to an Int", e);
		}
		return DEFAULT_PAGE_INDEX;
	}
}
