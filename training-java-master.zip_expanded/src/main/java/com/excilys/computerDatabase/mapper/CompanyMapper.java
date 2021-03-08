package main.java.com.excilys.computerDatabase.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import main.java.com.excilys.computerDatabase.model.Company;

public abstract class CompanyMapper {
	public static Company toCompany(ResultSet rs) throws SQLException{
		Company c = new Company(rs.getString("name"));
		return c;
	}
}
