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
	
		@Override
		public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			List<CompanyDTO> companies = CompanyService.getInstance().getAsPageable();
			request.setAttribute("companies", companies);
			this.getServletContext().getRequestDispatcher("/WEB-INF/views/addComputer.jsp").forward(request, response);
		}
		
		@Override
		public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			String name = request.getParameter("computerName").trim();
			String introduced = request.getParameter("introduced");
			String discontinued = request.getParameter("discontinued");
			String companyId = request.getParameter("companyId");
			try {
				InputValidator.validateNewComputer(name, introduced, discontinued, companyId);
				Computer newComputer = ComputerMapper.getInstance().toComputer(name, introduced, discontinued, companyId);
				try {
					ComputerService.getInstance().addComputer(newComputer);
					logger.info("Adding new computer success: "+newComputer.toString());
				} catch (FailedSQLRequestException e) {
					logger.error("Adding new computer failed",e);
				}
			} catch (InvalidValuesException e) {
				logger.error("New computer paramterers not valid",e);
			}
			response.sendRedirect("dashboard");
		}

}
