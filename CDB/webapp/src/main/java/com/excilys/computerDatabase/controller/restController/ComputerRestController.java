package com.excilys.computerDatabase.controller.restController;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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
	
	@GetMapping(value = "/get", params = {"id"})
	public Computer findComputer(@RequestParam("id") long id) {
		return computerService.findComputer(id).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Computer not found"));
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
