package com.excilys.computerDatabase.controller.servlet;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import com.excilys.computerDatabase.controller.page.Page;
import com.excilys.computerDatabase.dto.ComputerFormDTO;
import com.excilys.computerDatabase.exception.FailedSQLRequestException;
import com.excilys.computerDatabase.mapper.ComputerMapper;
import com.excilys.computerDatabase.model.Computer;
import com.excilys.computerDatabase.service.ComputerService;

public class DashboardServlet extends HttpServlet {

	private static final long serialVersionUID = 8233813063630626361L;

	Logger logger = LoggerFactory.getLogger(DashboardServlet.class);
	
	private final String VIEW_PATH = "/WEB-INF/views/dashboard.jsp";
	
	private final int DEFAULT_PAGE_INDEX = 1;
	private final int DEFAULT_ROWS_PER_PAGE = 10;
	
	private final String ROWS_PER_PAGE_ATTRIBUTE = "displayedRowsPerPage";
	private final String PAGE_INDEX_ATTRIBUTE = "pageIndex";
	private final String TOTAL_COMPUTERS_ATTRIBUTE = "totalComputers";
	private final String COMPUTER_LIST_ATTRIBUTE = "computers";
	private final String MAX_PAGES_ATTRIBUTE = "maxPages";
	private final String SELECTED_COMPUTERS_ATTRIBUTE = "selection";
	private final String SELECTED_COMPUTER_DELIMITER = ",";

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
		if (request.getParameter("ROWS_PER_PAGE_ATTRIBUTE") != null) {
			setRowsPerPage(request);
		} else if (request.getParameter(SELECTED_COMPUTERS_ATTRIBUTE) != null) {
			deleteComputer(Stream.of(request.getParameter(SELECTED_COMPUTERS_ATTRIBUTE).split(SELECTED_COMPUTER_DELIMITER)).mapToLong(Long::parseLong).toArray());
		}
	}
	
	private void deleteComputer(long[] computersId) {
		for (long id : computersId) {
			try {
				ComputerService.getInstance().deleteComputer(id);
			} catch (FailedSQLRequestException e) {
				logger.warn(e.getMessage());
			}
		}
	}
	
	private void setRowsPerPage(HttpServletRequest request) {
		request.getSession().setAttribute("ROWS_PER_PAGE_ATTRIBUTE", request.getParameter("ROWS_PER_PAGE_ATTRIBUTE"));
	}

	public void displayResults(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int rowsPerPage = getRequestedRowsPerPage(request);
		int currentPageIndex = getRequestedPageIndex(request);
		
		List<Computer> computers = ComputerService.getInstance().getComputers(currentPageIndex * rowsPerPage, rowsPerPage);
		List<ComputerFormDTO> dtos = computers.stream().map(c -> ComputerMapper.getInstance().toComputerFormDTO(c)).collect(Collectors.toList());
		
		Page<ComputerFormDTO> page = new Page<ComputerFormDTO>(dtos);
		
		int totalComputers = ComputerService.getInstance().getComputerCount();
		
		request.setAttribute(COMPUTER_LIST_ATTRIBUTE, page.getContent());
		request.setAttribute(TOTAL_COMPUTERS_ATTRIBUTE, totalComputers);
		request.setAttribute(MAX_PAGES_ATTRIBUTE, (int) Math.ceil(totalComputers / rowsPerPage));
		request.setAttribute(PAGE_INDEX_ATTRIBUTE, currentPageIndex);
		
		this.getServletContext().getRequestDispatcher(VIEW_PATH).forward(request, response);
	}
	
	private int getRequestedRowsPerPage(HttpServletRequest request) {
		String rowsPerPargeRequest = (String) request.getSession().getAttribute(ROWS_PER_PAGE_ATTRIBUTE);
		try {
			if (rowsPerPargeRequest != null) {
				return Integer.parseInt(rowsPerPargeRequest);
			}
		} catch (NumberFormatException e) {
			logger.warn("Parameter " +ROWS_PER_PAGE_ATTRIBUTE+":"+rowsPerPargeRequest+" could'nt be conveted to an Int", e);
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
			logger.warn("Parameter "+PAGE_INDEX_ATTRIBUTE+":"+pageIndex+" could'nt be conveted to an Int", e);
		}
		return DEFAULT_PAGE_INDEX;
	}
}
