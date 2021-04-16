package com.excilys.computerDatabase.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.excilys.computerDatabase.dao.ComputerDAO;
import com.excilys.computerDatabase.dao.ComputerRepository;
import com.excilys.computerDatabase.dto.ComputerDashboardDTO;
import com.excilys.computerDatabase.exception.FailedSQLRequestException;
import com.excilys.computerDatabase.mapper.ComputerMapper;
import com.excilys.computerDatabase.model.Computer;

@Service
public class ComputerService {
	
	ComputerDAO computerDAO;
	ComputerRepository repository;
	ComputerMapper computerMapper;
	
	public ComputerService(ComputerDAO computerDAO, ComputerRepository repository, ComputerMapper computerMapper) {
		this.computerDAO = computerDAO;
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
		return computerDAO.findComputer(id);
	}
	
	public void addComputer(Computer computer) throws FailedSQLRequestException {
		computerDAO.addComputer(computer);
	}
	
	public void deleteComputer(long id) throws FailedSQLRequestException {
		computerDAO.deleteComputer(id);
	}
	
	public void updateComputer(long id, Computer computer) throws FailedSQLRequestException {
		computerDAO.updateComputer(id, computer);
	}

	public int getComputerCount() {
		return computerDAO.getComputerCount();
	}

	public int getComputerCount(String search) {
		return computerDAO.getComputerCount(search);
	}


}
