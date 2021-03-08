package main.java.com.excilys.computerDatabase.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import main.java.com.excilys.computerDatabase.model.Computer;

public abstract class ComputerMapper {
	public static Computer toComputer(ResultSet rs) throws SQLException{
		Computer c = new Computer(rs.getString("name"));
		c.setIntroduced(rs.getTimestamp("introduced"));
		c.setDiscontinued(rs.getTimestamp("discontinued"));
		return c;
	}
}
