package com.excilys.computerDatabase.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.excilys.computerDatabase.dto.ComputerDashboardDTO;
import com.excilys.computerDatabase.exception.FailedSQLRequestException;
import com.excilys.computerDatabase.mapper.ComputerMapper;
import com.excilys.computerDatabase.model.Computer;
import com.excilys.computerDatabase.repository.ComputerRepository;

@Service
public class ComputerService {
	

	ComputerRepository repository;
	ComputerMapper computerMapper;
	
	public ComputerService(ComputerRepository repository, ComputerMapper computerMapper) {
		this.repository = repository;
		this.computerMapper = computerMapper;
	}
	
	public List<Computer> getComputers() {
		return repository.findAll();
	}
	
	public Page<Computer> getComputers(int page, int size) {
		Page<ComputerDashboardDTO> dtos = repository.getComputers(PageRequest.of(page, size), "");
		return dtos.map(dto -> computerMapper.toComputer(dto).orElseThrow());
	}

	public Page<Computer> getComputers(Pageable pageable, String filter) {
		Page<ComputerDashboardDTO> dtos = repository.getComputers(pageable, filter);
		return dtos.map(dto -> computerMapper.toComputer(dto).orElseThrow());
	}
	
	public long[] getComputersIdFromCompany(long companyId) {
		return repository.getComputersIdFromCompany(companyId);
	}
	
	public Optional<Computer> findComputer(long id) {
		return repository.findById(id);
	}
	
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

	public long getComputerCount() {
		return repository.count();
	}

	public long getComputerCount(String search) {
		return repository.countByName(search);
	}


}
