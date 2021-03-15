package main.java.com.excilys.computerDatabase.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import main.java.com.excilys.computerDatabase.exception.IncompleteResultSetException;
import main.java.com.excilys.computerDatabase.model.Company;
import main.java.com.excilys.computerDatabase.model.Computer;
import main.java.com.excilys.computerDatabase.model.builder.ComputerBuilder;

public abstract class ComputerMapper {
	
	public static Optional<Computer> toComputer(ResultSet rs) throws IncompleteResultSetException{
		if (rs == null) {
			throw new IllegalArgumentException();
		}
		Optional<Computer> computer = Optional.empty();
		try {
		String name = rs.getString("name");
		long id = rs.getLong("id");
		if (id == 0) {
			throw new IncompleteResultSetException("ResultSet requires Id to create a Computer object");
		}
		computer = Optional.of(new ComputerBuilder(name)
				.setId(id)
				.setIntroduced(rs.getTimestamp("introduced"))
				.setDiscontinued(rs.getTimestamp("discontinued"))
				.build());
		if (computer.isPresent()) {
			Optional<Company> company = CompanyMapper.toCompany(rs);
			if (company.isPresent()) {
				((Computer) computer.orElseThrow()).setCompany(company.orElseThrow());
			}
		}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return computer;
	}
	
}
