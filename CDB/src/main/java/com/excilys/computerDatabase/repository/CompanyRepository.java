package com.excilys.computerDatabase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.excilys.computerDatabase.model.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

}
