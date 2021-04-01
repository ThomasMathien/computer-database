package com.excilys.computerDatabase.dao;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import com.excilys.computerDatabase.exception.FailedSQLRequestException;
import com.excilys.computerDatabase.model.Company;
import com.excilys.computerDatabase.model.Computer;
import com.excilys.computerDatabase.model.builder.ComputerBuilder;


public class ComputerDAOTest extends DataSourceDBUnitTest {
	
	private final int COMPUTERS_AMOUNT = 15;
	@Autowired
	static ComputerDAO dao ;
	
	@Test
	public void testGetComputers() {
		List<Computer> computers = new ArrayList<>();
		computers = dao.getComputers();
		assertEquals(COMPUTERS_AMOUNT,computers.size());
		for (Computer c: computers) {
			assertTrue(c != null);
			assertEquals(c, dao.findComputer(c.getId()).orElseThrow());
		}	
	}
	
	@Test
	public void testGetComputersWithArguments() {
		List<Computer> computers = new ArrayList<>();
		computers = dao.getComputers(0, COMPUTERS_AMOUNT);
		assertEquals(dao.getComputers(), computers);
		final int offset = 2;
		computers = dao.getComputers(offset, 5);
		assertEquals(5, computers.size());
		for (int i = 0; i < computers.size(); i++) {
			assertEquals(dao.findComputer(i + 1 + offset).orElseThrow(), computers.get(i));
		}
	}
	
	
	@Test
	public void testFindComputer() {
		Optional<Computer> testComputer = Optional.of(new ComputerBuilder("")
				.setIntroduced(LocalDate.parse("1980-05-01"))
				.setDiscontinued(LocalDate.parse("1984-04-01"))
				.setCompany(1L)
				.setId(12L)
				.build());
		
		Optional<Computer> foundComputer = dao.findComputer(12);
		assertTrue(foundComputer.isPresent());
		assertNotEquals(foundComputer, testComputer);
		
		testComputer.orElseThrow().setName("Apple III");
		
		assertEquals(foundComputer, testComputer);
		assertTrue(dao.findComputer(145).isEmpty());
		assertTrue(dao.findComputer(0).isEmpty());	
		assertTrue(dao.findComputer(-2).isEmpty());
	}
	
	@Test
	public void testAddComputer() {
		
		try {
			Computer newComputer = new ComputerBuilder("TEST").build();
			int count = dao.getComputerCount();
			dao.addComputer(newComputer);
			assertEquals(++count, dao.getComputerCount());
			assertTrue(dao.findComputer(COMPUTERS_AMOUNT+1).isPresent());
			assertNotEquals(dao.findComputer(COMPUTERS_AMOUNT+1).orElseThrow(), newComputer);
			
			newComputer.setId(COMPUTERS_AMOUNT+1);
			
			assertEquals(dao.findComputer(COMPUTERS_AMOUNT+1).orElseThrow(), newComputer);
		} catch (FailedSQLRequestException e) {
			fail();
		}
		
		assertThrows(NullPointerException.class, () -> {
			dao.addComputer(null);
		});
	}	
	
	@Test
	public void testDeleteComputer() {
		assertThrows(FailedSQLRequestException.class, () -> {
			dao.deleteComputer(0);
		});
		try {
			dao.deleteComputer(5);
		} catch (FailedSQLRequestException e) {
			fail();
		}
		assertTrue(dao.findComputer(5).isEmpty());
		assertThrows(FailedSQLRequestException.class, () -> {
			dao.deleteComputer(5);
		});
	}
	

	
	@Test
	public void testUpdateComputer() {
		Computer formerComputer = dao.findComputer(3).orElseThrow();
		Computer newComputer = new Computer(formerComputer);
		newComputer.setName("New name");
		try {
			dao.updateComputer(3, newComputer);
			assertEquals(newComputer, dao.findComputer(3).orElseThrow());
		} catch (FailedSQLRequestException e) {
			fail();
		}
		assertThrows(FailedSQLRequestException.class, () -> {
			dao.updateComputer(0, newComputer);
		});
		assertThrows(FailedSQLRequestException.class, () -> {
			dao.updateComputer(18, newComputer);
		});
		assertThrows(NullPointerException.class, () -> {
			dao.updateComputer(5, null);
		});
	}	

}
