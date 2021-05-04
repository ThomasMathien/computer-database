package com.excilys.computerDatabase.controller.restController;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.excilys.computerDatabase.exception.CompanyNotFoundException;
import com.excilys.computerDatabase.model.Company;
import com.excilys.computerDatabase.service.CompanyService;

@RestController
@RequestMapping("/api/company")
public class CompanyRestController {

	CompanyService companyService;
		
	public CompanyRestController(CompanyService companyService) {
		this.companyService = companyService;
	}
	
	@GetMapping("/get")
	public List<Company> getCompanies() {
		return companyService.getCompanies();
	}
	
	@GetMapping(value = "/get", params = {"page", "size"})
	public Page<Company> getCompanies(@RequestParam int page, @RequestParam int size) {
		return companyService.getCompanies(page, size);
	}
	
	@GetMapping(value = "/get", params = {"id"})
	public Company findCompany(@RequestParam long id) throws CompanyNotFoundException {
		return companyService.findCompany(id);
	}
	
	@GetMapping(value = "/count")
	public long getCompanyCount() {
		return companyService.getCompanyCount();
	}
	 
}
