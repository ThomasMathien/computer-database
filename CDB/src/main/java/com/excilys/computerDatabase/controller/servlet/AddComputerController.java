package com.excilys.computerDatabase.controller.servlet;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.excilys.computerDatabase.dto.CompanyDTO;
import com.excilys.computerDatabase.dto.ComputerToDatabaseDTO;
import com.excilys.computerDatabase.exception.FailedSQLRequestException;
import com.excilys.computerDatabase.mapper.CompanyMapper;
import com.excilys.computerDatabase.mapper.ComputerMapper;
import com.excilys.computerDatabase.model.Company;
import com.excilys.computerDatabase.model.Computer;
import com.excilys.computerDatabase.service.CompanyService;
import com.excilys.computerDatabase.service.ComputerService;
import com.excilys.computerDatabase.validator.ComputerValidator;

@Controller
@RequestMapping(value = "/addComputer")
public class AddComputerController {

	private Logger logger = LoggerFactory.getLogger(AddComputerController.class);
	
	private final String VIEW_NAME = "addComputer";
	private final String REDIRECT_VIEW_NAME = "dashboard";
	
	private final String COMPANIES_LIST_ATTRIBUTE = "companies";
	private final String MODEL_ATTRIBUTE = "computer";
	
	CompanyService companyService;
	ComputerService computerService;
	CompanyMapper companyMapper;
	ComputerMapper computerMapper;
	ComputerValidator computerValidator;
	
	public AddComputerController(CompanyService companyService, ComputerService computerService,
			CompanyMapper companyMapper, ComputerMapper computerMapper, ComputerValidator computerValidator) {
		this.companyService = companyService;
		this.computerService = computerService;
		this.companyMapper = companyMapper;
		this.computerMapper = computerMapper;
		this.computerValidator = computerValidator;
	}
	
	@GetMapping
	public ModelAndView doGet() {
		return getFormMV((ComputerToDatabaseDTO) new ComputerToDatabaseDTO());
	}
	
	@PostMapping
	public ModelAndView submitForm(@ModelAttribute(name = MODEL_ATTRIBUTE) @Valid ComputerToDatabaseDTO dto, BindingResult result) {
		computerValidator.validate(dto, result);
		if (!result.hasErrors()) {
			Optional<Computer> newComputer = computerMapper.toComputer(dto);
			try {
				computerService.addComputer(newComputer.orElseThrow());
				logger.info("Adding new computer: " + newComputer.toString());
			} catch (FailedSQLRequestException e) {
				logger.error("Couldn't add new computer:" + newComputer.toString(), e);
			} catch (NoSuchElementException e) {
				logger.warn("Computer couldn't be properly mapped from DTO");
			}
			return new ModelAndView(REDIRECT_VIEW_NAME);
		} else {
			logger.info("Adding Computer Form Error:"+result.getAllErrors());
			return getFormMV((ComputerToDatabaseDTO) result.getTarget());
		}
	}
	
	private ModelAndView getFormMV(ComputerToDatabaseDTO dto) {
		ModelAndView mv = new ModelAndView(VIEW_NAME);
		List<Company> companies = companyService.getCompanies();
		List<CompanyDTO> dtos  = companies.stream().map(c -> companyMapper.toCompanyDTO(Optional.of(c)))
				.collect(Collectors.toList());
		mv.getModel().put(COMPANIES_LIST_ATTRIBUTE, dtos);
		mv.getModel().put(MODEL_ATTRIBUTE, dto);
		return mv;
	}

}
