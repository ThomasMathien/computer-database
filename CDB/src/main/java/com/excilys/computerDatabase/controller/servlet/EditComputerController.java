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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.excilys.computerDatabase.dto.CompanyDTO;
import com.excilys.computerDatabase.dto.EditComputerFormDTO;
import com.excilys.computerDatabase.exception.FailedSQLRequestException;
import com.excilys.computerDatabase.mapper.CompanyMapper;
import com.excilys.computerDatabase.mapper.ComputerMapper;
import com.excilys.computerDatabase.model.Company;
import com.excilys.computerDatabase.model.Computer;
import com.excilys.computerDatabase.service.CompanyService;
import com.excilys.computerDatabase.service.ComputerService;
import com.excilys.computerDatabase.validator.EditComputerFormDTOValidator;

@Controller
@RequestMapping(value = "/editComputer")
public class EditComputerController {
	
	private static final String REDIRECT_VIEW_NAME_AFTER_EDITING_COMPUTER = "redirect:/dashboard";
	private static final String VIEW_NAME =  "editComputer";
	private static final String MODEL_ATTRIBUTE = "computer";
	
	private static final String COMPUTER_ID_PARAMETER = "id";
	private static final String COMPANIES_LIST_ATTRIBUTE = "companies";
	
	private Logger logger = LoggerFactory.getLogger(EditComputerController.class);

	CompanyService companyService;
	ComputerService computerService;
	CompanyMapper companyMapper;
	ComputerMapper computerMapper;
	EditComputerFormDTOValidator computerValidator;
	
	public EditComputerController(CompanyService companyService, ComputerService computerService,
			CompanyMapper companyMapper, ComputerMapper computerMapper, EditComputerFormDTOValidator computerValidator) {
		this.companyService = companyService;
		this.computerService = computerService;
		this.companyMapper = companyMapper;
		this.computerMapper = computerMapper;
		this.computerValidator = computerValidator;
	}
	
	@GetMapping
	public ModelAndView doGet(@RequestParam(name = COMPUTER_ID_PARAMETER) String computerId) {
		EditComputerFormDTO dto = new EditComputerFormDTO();
		dto.setId(computerId);
		ModelAndView mv = getFormMV(dto);
		mv.getModel().put(COMPUTER_ID_PARAMETER, computerId);
		return mv;
	}
	
	@PostMapping
	public ModelAndView submitForm(@ModelAttribute(name = MODEL_ATTRIBUTE) @Valid EditComputerFormDTO dto, BindingResult result) {
		computerValidator.validate(dto, result);
		if (!result.hasErrors()) {
			Optional<Computer> editedComputer = computerMapper.toComputer(dto);
			try {
				computerService.updateComputer(editedComputer.orElseThrow().getId(), editedComputer.get());
				logger.info("Updating computer: " + editedComputer.get().toString());
			} catch (FailedSQLRequestException e) {
				logger.error("Couldn't update computer:" + editedComputer.get().toString(), e);
			} catch (NoSuchElementException e) {
				logger.warn("Computer couldn't be properly mapped from DTO");
			}
			return new ModelAndView(REDIRECT_VIEW_NAME_AFTER_EDITING_COMPUTER);
		} else {
			logger.info("Editing Computer Form Error:"+result.getAllErrors());
			ModelAndView mv = getFormMV((EditComputerFormDTO) result.getTarget());
			mv.getModel().put(COMPUTER_ID_PARAMETER, dto.getId());
			return mv;
		}
	}
	
	private ModelAndView getFormMV(EditComputerFormDTO dto) {
		ModelAndView mv = new ModelAndView(VIEW_NAME);
		List<Company> companies = companyService.getCompanies();
		List<CompanyDTO> dtos  = companies.stream().map(c -> companyMapper.toCompanyDTO(Optional.of(c)))
				.collect(Collectors.toList());
		mv.getModel().put(COMPANIES_LIST_ATTRIBUTE, dtos);
		mv.getModel().put(MODEL_ATTRIBUTE, dto);
		return mv;
	}

}
