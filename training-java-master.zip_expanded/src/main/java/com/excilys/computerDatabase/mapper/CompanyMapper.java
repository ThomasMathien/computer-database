package main.java.com.excilys.computerDatabase.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import main.java.com.excilys.computerDatabase.exception.IncompleteResultSet;
import main.java.com.excilys.computerDatabase.model.Company;

public abstract class CompanyMapper {
	
	public static Company toCompany(ResultSet rs) throws IncompleteResultSet{
		return CompanyMapper.toCompany(rs,"id","name");
	}
	
	public static Company toCompany(ResultSet rs, String companyIdColumnName,String companyNameColumnName) throws IncompleteResultSet{
		try {
			long id = rs.getLong(companyIdColumnName);
			String name =  rs.getString(companyNameColumnName);
			if (id == 0) {
				throw new IncompleteResultSet("ResultSet unsufficient to create a Company object");
			}
			return new Company(id,name);
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}