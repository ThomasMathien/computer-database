package main.java.com.excilys.computerDatabase.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import main.java.com.excilys.computerDatabase.exception.IncompleteResultSet;
import main.java.com.excilys.computerDatabase.model.Computer;

public abstract class ComputerMapper {
	
	public static Computer toComputer(ResultSet rs, String companyIdColumnName,String companyNameColumnName) throws IncompleteResultSet{
		try {
		String name = rs.getString("name");
		long id = rs.getLong("id");
		if (id == 0) {
			throw new IncompleteResultSet("ResultSet unsufficient to create a Computer object");
		}
		Computer computer = new Computer(name);
		computer.setId(id);
		computer.setIntroduced(rs.getTimestamp("introduced"));
		computer.setDiscontinued(rs.getTimestamp("discontinued"));
		if (rs.getLong(companyIdColumnName) != 0) {
			computer.setCompany(CompanyMapper.toCompany(rs, companyIdColumnName, companyNameColumnName));
		}
		return computer;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
