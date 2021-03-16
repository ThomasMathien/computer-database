package com.excilys.computerDatabase.dao;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.excilys.computerDatabase.exception.FailedSQLRequestException;
import com.excilys.computerDatabase.model.Computer;
import com.excilys.computerDatabase.model.builder.ComputerBuilder;

public class ComputerDatabaseDAOTest {

	@Test
	public void testGetComputers() {
		List<Computer> computers = new ArrayList<>();
		computers = ComputerDatabaseDAO.getInstance().getComputers();
		
		
	}
	
	@Test
	public void testAddComputer() {
		try {
			ComputerDatabaseDAO.getInstance().addComputer(new ComputerBuilder("TEST").build());
			System.out.print("TEST PASSED");
		} catch (FailedSQLRequestException e) {
			e.printStackTrace();
		}
	}
}
