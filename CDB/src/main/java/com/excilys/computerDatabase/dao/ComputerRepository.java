package com.excilys.computerDatabase.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.excilys.computerDatabase.model.Computer;

@Repository
public interface ComputerRepository extends JpaRepository<Computer, Long> {

	//Page<Computer> getComputers(long from, long amount);
	//
	@Query("SELECT computer.id FROM Computer computer WHERE computer.company.id = :companyId")
	long[] getComputersIdFromCompany(@Param("companyId") long companyId);
	
	@Query("""
			SELECT computer FROM Computer computer WHERE computer.id = :id
			""")
	Computer findComputer(@Param("id") long id);
}
