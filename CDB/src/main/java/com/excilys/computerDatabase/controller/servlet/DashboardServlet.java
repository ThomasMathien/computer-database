package com.excilys.computerDatabase.controller.servlet;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import com.excilys.computerDatabase.controller.page.Page;
import com.excilys.computerDatabase.dto.ComputerDTO;
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

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getSession().setAttribute("displayedRowsPerPage", request.getParameter("displayedRowsPerPage"));
		processRequest(request, response);
	}

	public void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int rowsPerPage = getRequestedRowsPerPage(request);
		int currentPageIndex = getRequestedPageIndex(request);
		
		List<Computer> computers = ComputerService.getInstance().getComputers(currentPageIndex * rowsPerPage, rowsPerPage);
		List<ComputerDTO> dtos = computers.stream().map(c -> ComputerMapper.getInstance().toComputerDTO(Optional.of(c))).collect(Collectors.toList());
		
		Page<ComputerDTO> page = new Page<ComputerDTO>(dtos);
		
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
