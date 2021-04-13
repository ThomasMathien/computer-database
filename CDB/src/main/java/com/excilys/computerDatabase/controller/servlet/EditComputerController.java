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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

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

@Controller
@RequestMapping(value = "/editComputer")
public class EditComputerController {
	
	private static final String REDIRECT_PAGE_AFTER_ADDING_COMPUTER = "dashboard";
	private static final String VIEW_NAME =  "editComputer";
	
	private static final String COMPUTER_ID_ATTRIBUTE = "id";
	private static final String COMPUTER_ID_PARAMETER = "id";
	private static final String COMPANIES_LIST_ATTRIBUTE = "companies";
	private static final String COMPUTER_NAME_ATTRIBUTE = "computerName";
	private static final String INTRODUCED_DATE_ATTRIBUTE = "introduced";
	private static final String DISCONTINUED_DATE_ATTRIBUTE = "discontinued";
	private static final String COMPANY_ID_ATTRIBUTE = "companyId";
	
	private Logger logger = LoggerFactory.getLogger(EditComputerController.class);

	CompanyService companyService;
	ComputerService computerService;
	CompanyMapper companyMapper;
	ComputerMapper computerMapper;
	ComputerValidator computerValidator;
	
	public EditComputerController(CompanyService companyService, ComputerService computerService,
			CompanyMapper companyMapper, ComputerMapper computerMapper, ComputerValidator computerValidator) {
		this.companyService = companyService;
		this.computerService = computerService;
		this.companyMapper = companyMapper;
		this.computerMapper = computerMapper;
		this.computerValidator = computerValidator;
	}
	
	@GetMapping
	public ModelAndView doGet(@RequestParam(name = COMPUTER_ID_PARAMETER) String computerId) {
		ModelAndView mv = new ModelAndView(VIEW_NAME);
		
		List<Company> companies = companyService.getCompanies();
		List<CompanyDTO> dtos  = companies.stream().map(c -> companyMapper.toCompanyDTO(Optional.of(c))).collect(Collectors.toList());
		mv.getModel().put(COMPUTER_ID_ATTRIBUTE, computerId);
		mv.getModel().put(COMPANIES_LIST_ATTRIBUTE, dtos);
		return mv;
	}
	
	@PostMapping
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name = request.getParameter(COMPUTER_NAME_ATTRIBUTE).trim();
		String introduced = request.getParameter(INTRODUCED_DATE_ATTRIBUTE);
		String discontinued = request.getParameter(DISCONTINUED_DATE_ATTRIBUTE);
		String companyId = request.getParameter(COMPANY_ID_ATTRIBUTE);
		String computerId = request.getParameter(COMPUTER_ID_ATTRIBUTE);

			ComputerToDatabaseDTO dto = new ComputerToDatabaseDTOBuilder()
					.setId(computerId)
					.setCompanyId(companyId)
					.setDiscontinued(discontinued)
					.setIntroduced(introduced)
					.setName(name)
					.build();
		//	computerValidator.validateComputerDTO(dto);
			Optional<Computer> computer = computerMapper.toComputer(dto);
			try {
				computerService.updateComputer(Long.parseLong(computerId), computer.orElseThrow());
				logger.info("Updating computer: " + computer.get().toString());
			} catch (FailedSQLRequestException e) {
				logger.error("Couldn't update computer:" + computer.get().toString(), e);
			} catch (NoSuchElementException e) {
				logger.warn("Computer couldn't be properly mapped from DTO");
			}
		response.sendRedirect(REDIRECT_PAGE_AFTER_ADDING_COMPUTER);
	}
}
