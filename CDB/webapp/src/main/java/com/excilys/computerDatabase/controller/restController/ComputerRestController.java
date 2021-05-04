package com.excilys.computerDatabase.controller.restController;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.excilys.computerDatabase.exception.ComputerNotFoundException;
import com.excilys.computerDatabase.exception.FailedSQLRequestException;
import com.excilys.computerDatabase.model.Computer;
import com.excilys.computerDatabase.service.ComputerService;

@RestController
@RequestMapping("/api/computer")
public class ComputerRestController {

	ComputerService computerService;
	
	public ComputerRestController(ComputerService computerService) {
		this.computerService = computerService;
	}
	
	@GetMapping(value = "/get")
	public List<Computer> getComputers(){
		return computerService.getComputers();
	}
	
	@GetMapping(value = "/get", params = {"id"},  produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> findComputer(@RequestParam("id") long id) throws ComputerNotFoundException {
		return new ResponseEntity<Object>(computerService.findComputer(id), HttpStatus.OK);
	}
	 
	@GetMapping(value = "/get", params = {"page", "size"})
	public Page<Computer> getComputers(@RequestParam("page") int page,@RequestParam("size") int size) {
		return computerService.getComputers(page, size);
	}
	 
	@GetMapping("/getIds")
	public long[] getComputersIdFromCompany(@RequestParam("companyId") long companyId) {
		return computerService.getComputersIdFromCompany(companyId);
	}
	
	@GetMapping("/count")
	public long getComputerCount() {
		return computerService.getComputerCount();
	}
	
	@GetMapping(value = "/count", params = {"nameFilter"})
	public long getComputerCount(@RequestParam("nameFilter") String search) {
		return computerService.getComputerCount(search);
	}
	
	@PostMapping("/delete")
	public ResponseEntity<Integer> deleteComputer(@RequestParam long id) {
			computerService.deleteComputer(id);
			return new ResponseEntity<>(HttpStatus.OK);
	}
	 /*	
		public void addComputer(Computer computer) throws FailedSQLRequestException {
			repository.save(computer);
		}
		
		public void deleteComputer(long id) throws FailedSQLRequestException {
			repository.deleteById(id);
		}
		
		public void updateComputer(long id, Computer computer) throws FailedSQLRequestException {
			computer.setId(id);
			repository.save(computer);
		}
*/
	
}
