package com.excilys.computerDatabase.controller.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import com.excilys.computerDatabase.controller.page.Page;
import com.excilys.computerDatabase.dto.ComputerDTO;
import com.excilys.computerDatabase.service.ComputerService;

public class DashboardServlet extends HttpServlet {

	private static final long serialVersionUID = 8233813063630626361L;
	
	private int displayedRowsPerPage = 10;
	private int currentPageIndex = 1;
	private int maxPages;
	private int totalComputers;

	
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
		actualizeProperties(request);
		
		Page<ComputerDTO> page = new Page<ComputerDTO>(ComputerService.getInstance().getAsPageable(currentPageIndex, displayedRowsPerPage));
		
		request.setAttribute("computers", page.getContent());
		request.setAttribute("totalComputers", totalComputers);
		request.setAttribute("maxPages", maxPages);
		request.setAttribute("pageIndex", currentPageIndex);
		
		this.getServletContext().getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);
	}
	
	public void actualizeProperties(HttpServletRequest request) {
		String rowsPerPargeRequest = (String) request.getSession().getAttribute("displayedRowsPerPage");
		if (rowsPerPargeRequest != null) {
			displayedRowsPerPage = Integer.parseInt(rowsPerPargeRequest);
		}
		String pageIndex = request.getParameter("pageIndex");
		if (pageIndex != null) {
			currentPageIndex = Integer.parseInt(pageIndex);
		}
		totalComputers = ComputerService.getInstance().getComputerCount();
		maxPages = (int) Math.ceil(totalComputers / displayedRowsPerPage);
	}
	
	
}
