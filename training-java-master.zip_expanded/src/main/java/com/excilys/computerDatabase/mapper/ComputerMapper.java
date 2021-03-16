package main.java.com.excilys.computerDatabase.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import main.java.com.excilys.computerDatabase.exception.IncompleteResultSetException;
import main.java.com.excilys.computerDatabase.model.Company;
import main.java.com.excilys.computerDatabase.model.Computer;
import main.java.com.excilys.computerDatabase.model.builder.ComputerBuilder;

public class ComputerMapper {
	
	public final static String ID_COLUMN = "id";
	public final static String NAME_COLUMN = "name";
	public final static String INTRODUCED_COLUMN = "introduced";
	public final static String DISCONTINUED_COLUMN = "discontinued";
	
	
	private ComputerMapper() {}
	private static ComputerMapper instance;
	
	public static ComputerMapper getInstance() {
		if (instance == null) {
			instance = new ComputerMapper();
		}
		return instance;
	}
	
	public Optional<Computer> toComputer(ResultSet rs) throws IncompleteResultSetException{
		if (rs == null) {
			throw new IllegalArgumentException();
		}
		Optional<Computer> computer = Optional.empty();
		try {
		String name = rs.getString(NAME_COLUMN);
		long id = rs.getLong(ID_COLUMN);
		if (id == 0) {
			throw new IncompleteResultSetException("ResultSet requires Id to create a Computer object");
		}
		computer = Optional.of(new ComputerBuilder(name)
				.setId(id)
				.setIntroduced(rs.getTimestamp(INTRODUCED_COLUMN))
				.setDiscontinued(rs.getTimestamp(DISCONTINUED_COLUMN))
				.build());
		if (computer.isPresent()) {
			if (rs.getLong(CompanyMapper.ID_COLUMN) != 0) {
				Optional<Company> company = CompanyMapper.getInstance().toCompany(rs);
				if (company.isPresent()) {
					((Computer) computer.orElseThrow()).setCompany(company.orElseThrow());
				}
			}
		}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return computer;
	}
	
}
