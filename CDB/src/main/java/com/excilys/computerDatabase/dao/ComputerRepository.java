package com.excilys.computerDatabase.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.excilys.computerDatabase.dto.ComputerDashboardDTO;
import com.excilys.computerDatabase.model.Computer;

@Repository
public interface ComputerRepository extends JpaRepository<Computer, Long> {

	@Query("SELECT c.name AS name, c.id AS id, c.introduced AS introduced, c.discontinued AS discontinued, company.name AS companyName FROM Computer c LEFT JOIN Company company ON c.company = company WHERE c.name LIKE %:filter%")
	Page<ComputerDashboardDTO> getComputers(Pageable pageable, @Param(value = "filter") String filter);

	@Query("SELECT c.id FROM Computer c WHERE c.company.id = :companyId")
	long[] getComputersIdFromCompany(@Param("companyId") long companyId);
	
	@Query("SELECT computer FROM Computer computer WHERE computer.id = :id")
	Computer findComputer(@Param("id") long id);
	
	List<Computer> findAll();
	
}
