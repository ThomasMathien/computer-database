package com.excilys.computerDatabase.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.excilys.computerDatabase.model.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

	@Query("SELECT c FROM Company c")
	List<Company> getCompanies();
	
	@Query("SELECT c FROM Company c ORDER BY c.id")
	List<Company> getCompanies(Pageable pageable);
	
	@Query("SELECT c FROM Company c WHERE id = :id")
	Optional<Company> findCompany(@Param(value = "id") long id);
	
	@Query("SELECT COUNT(c) FROM Company c")
	int getCompanyCount();
	
}
