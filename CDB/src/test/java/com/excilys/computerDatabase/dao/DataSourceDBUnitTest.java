package com.excilys.computerDatabase.dao;

import javax.sql.DataSource;

import org.dbunit.DataSourceBasedDBTestCase;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.Test;


public class DataSourceDBUnitTest extends DataSourceBasedDBTestCase {
	
	@Override
	protected DataSource getDataSource() {
		JdbcDataSource dataSource = new JdbcDataSource();
		dataSource.setURL("jdbc:h2:mem:computer-database-db-test;DB_CLOSE_DELAY=-1;INIT=RUNSCRIPT FROM 'classpath:dbScripts/schema.sql'");
		dataSource.setUser("sa");
		dataSource.setPassword("sa");
		return dataSource;
	}

	@Override
	protected IDataSet getDataSet() throws Exception {
		return new FlatXmlDataSetBuilder().build(getClass().getClassLoader().getResourceAsStream("dbScripts/data.xml"));
	}

	@Override
	protected DatabaseOperation getSetUpOperation() {
		return DatabaseOperation.REFRESH;
	}

	@Override
	protected DatabaseOperation getTearDownOperation() {
		return DatabaseOperation.DELETE_ALL;
	}
	
	@Test
	public void testShouldLoadData() throws Exception {
		IDataSet databaseDataSet = getConnection().createDataSet();
		ITable computerTable = databaseDataSet.getTable("COMPUTER");
		assertEquals(15, computerTable.getRowCount());
		ITable companyTable = databaseDataSet.getTable("COMPANY");
		assertEquals(5, companyTable.getRowCount());
	}

}
