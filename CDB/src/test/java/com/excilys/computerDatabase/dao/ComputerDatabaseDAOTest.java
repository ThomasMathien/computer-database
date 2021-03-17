package com.excilys.computerDatabase.dao;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;

import com.excilys.computerDatabase.exception.FailedSQLRequestException;
import com.excilys.computerDatabase.model.Computer;
import com.excilys.computerDatabase.model.builder.ComputerBuilder;

public class ComputerDatabaseDAOTest {
	
	@Test
	public void testGetComputers() {
		List<Computer> computers = new ArrayList<>();
		computers = ComputerDatabaseDAO.getInstance().getComputers();
		assertTrue(computers.size() == 15);
		for (Computer c: computers) {
			assertTrue(c != null);
			assertEquals(c, ComputerDatabaseDAO.getInstance().findComputer(c.getId()).orElseThrow());
		}	
	}
	
	@Test
	public void testGetComputersWithArguments() {
		List<Computer> computers = new ArrayList<>();
		computers = ComputerDatabaseDAO.getInstance().getComputers(0, 15);
		assertEquals(ComputerDatabaseDAO.getInstance().getComputers(), computers);
		final int offset = 2;
		computers = ComputerDatabaseDAO.getInstance().getComputers(offset, 5);
		assertTrue(computers.size() == 5);
		for (int i = 0; i < computers.size(); i++) {
			assertEquals(ComputerDatabaseDAO.getInstance().findComputer(i + 1 + offset).orElseThrow(), computers.get(i));
		}
	}
	
	
	@Test
	public void testFindComputer() {
		Optional<Computer> testComputer = Optional.of(new ComputerBuilder("")
				.setIntroduced(Timestamp.valueOf("1980-05-01 00:00:00"))
				.setDiscontinued(Timestamp.valueOf("1984-04-01 00:00:00"))
				.setCompany(1L)
				.setId(12L)
				.build());
		
		Optional<Computer> foundComputer = ComputerDatabaseDAO.getInstance().findComputer(12);
		assertTrue(foundComputer.isPresent());
		assertNotEquals(foundComputer, testComputer);
		
		testComputer.orElseThrow().setName("Apple III");
		
		assertEquals(foundComputer, testComputer);
		assertTrue(ComputerDatabaseDAO.getInstance().findComputer(145).isEmpty());
		assertTrue(ComputerDatabaseDAO.getInstance().findComputer(0).isEmpty());	
		assertTrue(ComputerDatabaseDAO.getInstance().findComputer(-2).isEmpty());
	}
	
	@Test
	public void testAddComputer() {
		try {
			Computer newComputer = new ComputerBuilder("TEST").build();
			int count = ComputerDatabaseDAO.getInstance().getComputerCount();
			ComputerDatabaseDAO.getInstance().addComputer(newComputer);
			assertEquals(++count, ComputerDatabaseDAO.getInstance().getComputerCount());
			assertTrue(ComputerDatabaseDAO.getInstance().findComputer(16).isPresent());
			assertNotEquals(ComputerDatabaseDAO.getInstance().findComputer(16).orElseThrow(), newComputer);
			
			newComputer.setId(16);
			
			assertEquals(ComputerDatabaseDAO.getInstance().findComputer(16).orElseThrow(), newComputer);
		} catch (FailedSQLRequestException e) {
			fail();
		}
		
		Computer computerIdTaken = new ComputerBuilder("TEST")
				.setIntroduced(Timestamp.valueOf("1980-05-01 00:00:00"))
				.setDiscontinued(Timestamp.valueOf("1984-04-01 00:00:00"))
				.setId(12L)
				.setCompany(1L).build();
		
		assertThrows(FailedSQLRequestException.class, () -> {
			ComputerDatabaseDAO.getInstance().addComputer(computerIdTaken);
		});
		
		Computer computerWrongId = new ComputerBuilder("TEST")
				.setIntroduced(Timestamp.valueOf("1980-05-01 00:00:00"))
				.setDiscontinued(Timestamp.valueOf("1984-04-01 00:00:00"))
				.setId(18L)
				.setCompany(1L).build();
		assertThrows(FailedSQLRequestException.class, () -> {
			ComputerDatabaseDAO.getInstance().addComputer(computerWrongId);
		});
		
		assertThrows(FailedSQLRequestException.class, () -> {
			ComputerDatabaseDAO.getInstance().addComputer(null);
		});
	}	
	
	@Test
	public void testDeleteComputer() {
		assertThrows(FailedSQLRequestException.class, () -> {
			ComputerDatabaseDAO.getInstance().deleteComputer(0);
		});
		try {
			ComputerDatabaseDAO.getInstance().deleteComputer(5);
		} catch (FailedSQLRequestException e) {
			fail();
		}
		assertTrue(ComputerDatabaseDAO.getInstance().findComputer(5).isEmpty());
		assertThrows(FailedSQLRequestException.class, () -> {
			ComputerDatabaseDAO.getInstance().deleteComputer(5);
		});
	}
	
	@Test
	public void testUpdateComputer() {
		Computer formerComputer = ComputerDatabaseDAO.getInstance().findComputer(3).orElseThrow();
		Computer newComputer = new Computer(formerComputer);
		newComputer.setName("New name");
		try {
			ComputerDatabaseDAO.getInstance().updateComputer(3, newComputer);
			assertEquals(newComputer, ComputerDatabaseDAO.getInstance().findComputer(3).orElseThrow());
		} catch (FailedSQLRequestException e) {
			fail();
		}
		assertThrows(FailedSQLRequestException.class, () -> {
			ComputerDatabaseDAO.getInstance().updateComputer(0, newComputer);
		});
		assertThrows(FailedSQLRequestException.class, () -> {
			ComputerDatabaseDAO.getInstance().updateComputer(18, newComputer);
		});
		assertThrows(FailedSQLRequestException.class, () -> {
			ComputerDatabaseDAO.getInstance().updateComputer(5, null);
		});
	}
		
		

}
