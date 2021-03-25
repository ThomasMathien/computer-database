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

import com.excilys.computerDatabase.exception.FailedSQLRequestException;
import com.excilys.computerDatabase.model.Company;
import com.excilys.computerDatabase.model.Computer;
import com.excilys.computerDatabase.model.builder.ComputerBuilder;

public class ComputerDAOTest extends DataSourceDBUnitTest {
	
	private final int COMPUTERS_AMOUNT = 15;
	static ComputerDAO dao = ComputerDAO.getInstance();
	
	@Test
	public void testGetComputers() {
		List<Computer> computers = new ArrayList<>();
		computers = ComputerDAO.getInstance().getComputers();
		assertEquals(COMPUTERS_AMOUNT,computers.size());
		for (Computer c: computers) {
			assertTrue(c != null);
			assertEquals(c, ComputerDAO.getInstance().findComputer(c.getId()).orElseThrow());
		}	
	}
	
	@Test
	public void testGetComputersWithArguments() {
		List<Computer> computers = new ArrayList<>();
		computers = ComputerDAO.getInstance().getComputers(0, COMPUTERS_AMOUNT);
		assertEquals(ComputerDAO.getInstance().getComputers(), computers);
		final int offset = 2;
		computers = ComputerDAO.getInstance().getComputers(offset, 5);
		assertTrue(computers.size() == 5);
		for (int i = 0; i < computers.size(); i++) {
			assertEquals(ComputerDAO.getInstance().findComputer(i + 1 + offset).orElseThrow(), computers.get(i));
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
		
		Optional<Computer> foundComputer = ComputerDAO.getInstance().findComputer(12);
		assertTrue(foundComputer.isPresent());
		assertNotEquals(foundComputer, testComputer);
		
		testComputer.orElseThrow().setName("Apple III");
		
		assertEquals(foundComputer, testComputer);
		assertTrue(ComputerDAO.getInstance().findComputer(145).isEmpty());
		assertTrue(ComputerDAO.getInstance().findComputer(0).isEmpty());	
		assertTrue(ComputerDAO.getInstance().findComputer(-2).isEmpty());
	}
	
	@Test
	public void testAddComputer() {
		
		try {
			Computer newComputer = new ComputerBuilder("TEST").build();
			int count = ComputerDAO.getInstance().getComputerCount();
			ComputerDAO.getInstance().addComputer(newComputer);
			assertEquals(++count, ComputerDAO.getInstance().getComputerCount());
			assertTrue(ComputerDAO.getInstance().findComputer(COMPUTERS_AMOUNT+1).isPresent());
			assertNotEquals(ComputerDAO.getInstance().findComputer(COMPUTERS_AMOUNT+1).orElseThrow(), newComputer);
			
			newComputer.setId(COMPUTERS_AMOUNT+1);
			
			assertEquals(ComputerDAO.getInstance().findComputer(COMPUTERS_AMOUNT+1).orElseThrow(), newComputer);
		} catch (FailedSQLRequestException e) {
			fail();
		}
		
		Computer computerIdTaken = new ComputerBuilder("TEST")
				.setIntroduced(LocalDate.parse("1980-05-01"))
				.setDiscontinued(LocalDate.parse("1984-04-01"))
				.setId(12L)
				.setCompany(1L).build();
		
		assertThrows(FailedSQLRequestException.class, () -> {
			ComputerDAO.getInstance().addComputer(computerIdTaken);
		});
		
		Computer computerWrongId = new ComputerBuilder("TEST")
				.setIntroduced(LocalDate.parse("1980-05-01"))
				.setDiscontinued(LocalDate.parse("1984-04-01"))
				.setId(18L)
				.setCompany(1L).build();
		assertThrows(FailedSQLRequestException.class, () -> {
			ComputerDAO.getInstance().addComputer(computerWrongId);
		});
		
		assertThrows(FailedSQLRequestException.class, () -> {
			ComputerDAO.getInstance().addComputer(null);
		});
	}	
	
	@Test
	public void testDeleteComputer() {
		assertThrows(FailedSQLRequestException.class, () -> {
			ComputerDAO.getInstance().deleteComputer(0);
		});
		try {
			ComputerDAO.getInstance().deleteComputer(5);
		} catch (FailedSQLRequestException e) {
			fail();
		}
		assertTrue(ComputerDAO.getInstance().findComputer(5).isEmpty());
		assertThrows(FailedSQLRequestException.class, () -> {
			ComputerDAO.getInstance().deleteComputer(5);
		});
	}
	

	
	@Test
	public void testUpdateComputer() {
		Computer formerComputer = ComputerDAO.getInstance().findComputer(3).orElseThrow();
		Computer newComputer = new Computer(formerComputer);
		newComputer.setName("New name");
		try {
			ComputerDAO.getInstance().updateComputer(3, newComputer);
			assertEquals(newComputer, ComputerDAO.getInstance().findComputer(3).orElseThrow());
		} catch (FailedSQLRequestException e) {
			fail();
		}
		assertThrows(FailedSQLRequestException.class, () -> {
			ComputerDAO.getInstance().updateComputer(0, newComputer);
		});
		assertThrows(FailedSQLRequestException.class, () -> {
			ComputerDAO.getInstance().updateComputer(18, newComputer);
		});
		assertThrows(FailedSQLRequestException.class, () -> {
			ComputerDAO.getInstance().updateComputer(5, null);
		});
	}	

}
