package com.excilys.computerDatabase.controller.servlet;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.excilys.computerDatabase.dto.CompanyDTO;
import com.excilys.computerDatabase.dto.ComputerToDatabaseDTO;
import com.excilys.computerDatabase.dto.builder.ComputerToDatabaseDTOBuilder;
import com.excilys.computerDatabase.exception.FailedSQLRequestException;
import com.excilys.computerDatabase.exception.invalidValuesException.InvalidValuesException;
import com.excilys.computerDatabase.mapper.CompanyMapper;
import com.excilys.computerDatabase.mapper.ComputerMapper;
import com.excilys.computerDatabase.model.Company;
import com.excilys.computerDatabase.model.Computer;
import com.excilys.computerDatabase.service.CompanyService;
import com.excilys.computerDatabase.service.ComputerService;
import com.excilys.computerDatabase.validator.ComputerValidator;

@Component
@WebServlet(urlPatterns="/editComputer")
public class EditComputerServlet extends SpringServlet {

	private static final long serialVersionUID = -8675864653659504656L;	
	
	private static final String REDIRECT_PAGE_AFTER_ADDING_COMPUTER = "dashboard";
	private static final String VIEW_PATH =  "/WEB-INF/views/editComputer.jsp";
	
	private static final String COMPUTER_ID_ATTRIBUTE = "id";
	private static final String COMPUTER_ID_PARAMETER = "id";
	private static final String COMPANIES_LIST_ATTRIBUTE = "companies";
	private static final String COMPUTER_NAME_ATTRIBUTE = "computerName";
	private static final String INTRODUCED_DATE_ATTRIBUTE = "introduced";
	private static final String DISCONTINUED_DATE_ATTRIBUTE = "discontinued";
	private static final String COMPANY_ID_ATTRIBUTE = "companyId";
	
	private Logger logger = LoggerFactory.getLogger(EditComputerServlet.class);

	@Autowired
	CompanyService companyService;
	@Autowired
	ComputerService computerService;
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter(COMPUTER_ID_PARAMETER);
		request.setAttribute(COMPUTER_ID_ATTRIBUTE, id);
		List<Company> companies = companyService.getCompanies();
		List<CompanyDTO> dtos  = companies.stream().map(c -> CompanyMapper.getInstance().toCompanyDTO(Optional.of(c))).collect(Collectors.toList());
		request.setAttribute(COMPANIES_LIST_ATTRIBUTE, dtos);
		this.getServletContext().getRequestDispatcher(VIEW_PATH).forward(request, response);
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name = request.getParameter(COMPUTER_NAME_ATTRIBUTE).trim();
		String introduced = request.getParameter(INTRODUCED_DATE_ATTRIBUTE);
		String discontinued = request.getParameter(DISCONTINUED_DATE_ATTRIBUTE);
		String companyId = request.getParameter(COMPANY_ID_ATTRIBUTE);
		String computerId = request.getParameter(COMPUTER_ID_ATTRIBUTE);
		try {
			ComputerToDatabaseDTO dto = new ComputerToDatabaseDTOBuilder()
					.setId(computerId)
					.setCompanyId(companyId)
					.setDiscontinued(discontinued)
					.setIntroduced(introduced)
					.setName(name)
					.build();
			ComputerValidator.getInstance().validateComputerDTO(dto);
			Optional<Computer> computer = ComputerMapper.getInstance().toComputer(dto);
			try {
				computerService.updateComputer(Long.parseLong(computerId), computer.orElseThrow());
				logger.info("Updating computer: " + computer.get().toString());
			} catch (FailedSQLRequestException e) {
				logger.error("Couldn't update computer:" + computer.get().toString(), e);
			} catch (NoSuchElementException e) {
				logger.warn("Computer couldn't be properly mapped from DTO");
			}
		} catch (InvalidValuesException e) {
			logger.warn("Updated computer parameters not valid", e);
		}
		response.sendRedirect(REDIRECT_PAGE_AFTER_ADDING_COMPUTER);
	}
}
