package com.excilys.computerDatabase.repository.company;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.excilys.computerDatabase.model.Company;

@Repository
public interface CompanyRepository extends CompanyManagement, JpaRepository<Company, Long> {

	void deleteById(long id);
	
}
