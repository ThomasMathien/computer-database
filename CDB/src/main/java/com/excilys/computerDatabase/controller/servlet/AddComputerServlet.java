package com.excilys.computerDatabase.controller.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.computerDatabase.dto.CompanyDTO;
import com.excilys.computerDatabase.exception.FailedSQLRequestException;
import com.excilys.computerDatabase.exception.InvalidValuesException;
import com.excilys.computerDatabase.mapper.ComputerMapper;
import com.excilys.computerDatabase.model.Computer;
import com.excilys.computerDatabase.service.CompanyService;
import com.excilys.computerDatabase.service.ComputerService;
import com.excilys.computerDatabase.validator.InputValidator;

public class AddComputerServlet extends HttpServlet {

	private static final long serialVersionUID = -195965979475821843L;
	
		private Logger logger = LoggerFactory.getLogger(AddComputerServlet.class);
		
		private final String REDIRECT_PAGE_AFTER_ADDING_COMPUTER = "dashboard";
		private final String VIEW_PATH = "/WEB-INF/views/addComputer.jsp";
		
		private final String COMPANIES_LIST_ATTRIBUTE = "companies";
		private final String COMPUTER_NAME_ATTRIBUTE = "computerName";
		private final String INTRODUCED_DATE_ATTRIBUTE = "introduced";
		private final String DISCONTINUED_DATE_ATTRIBUTE = "discontinued";
		private final String COMPANY_ID_ATTRIBUTE = "companyId";
		
		@Override
		public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			List<CompanyDTO> companies = CompanyService.getInstance().getAsPageable();
			request.setAttribute(COMPANIES_LIST_ATTRIBUTE, companies);
			this.getServletContext().getRequestDispatcher(VIEW_PATH).forward(request, response);
		}
		
		@Override
		public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			String name = request.getParameter(COMPUTER_NAME_ATTRIBUTE).trim();
			String introduced = request.getParameter(INTRODUCED_DATE_ATTRIBUTE);
			String discontinued = request.getParameter(DISCONTINUED_DATE_ATTRIBUTE);
			String companyId = request.getParameter(COMPANY_ID_ATTRIBUTE);
			try {
				InputValidator.validateNewComputer(name, introduced, discontinued, companyId);
				Computer newComputer = ComputerMapper.getInstance().toComputer(name, introduced, discontinued, companyId);
				try {
					ComputerService.getInstance().addComputer(newComputer);
					logger.info("Adding new computer: " + newComputer.toString());
				} catch (FailedSQLRequestException e) {
					logger.error("Couldn't add new computer", e);
				}
			} catch (InvalidValuesException e) {
				logger.warn("New computer parameters not valid", e);
			}
			response.sendRedirect(REDIRECT_PAGE_AFTER_ADDING_COMPUTER);
		}

}
