package com.excilys.computerDatabase.controller.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.computerDatabase.dto.CompanyDTO;
import com.excilys.computerDatabase.exception.FailedSQLRequestException;
import com.excilys.computerDatabase.mapper.ComputerMapper;
import com.excilys.computerDatabase.model.Computer;
import com.excilys.computerDatabase.service.CompanyService;
import com.excilys.computerDatabase.service.ComputerService;

public class AddComputerServlet extends HttpServlet {

		@Override
		public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			List<CompanyDTO> companies = CompanyService.getInstance().getAsPageable();
			request.setAttribute("companies", companies);

			this.getServletContext().getRequestDispatcher("/WEB-INF/views/addComputer.jsp").forward(request, response);
		}
		
		@Override
		public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			String name = request.getParameter("computerName");
			String introduced = request.getParameter("introduced");
			String discontinued = request.getParameter("discontinued");
			String companyId = request.getParameter("companyId");
			Computer newComputer = ComputerMapper.getInstance().toComputer(name, introduced, discontinued, companyId);
			try {
				ComputerService.getInstance().addComputer(newComputer);
			} catch (FailedSQLRequestException e) {
				e.printStackTrace();
			}
			response.sendRedirect("dashboard");
		}

}
