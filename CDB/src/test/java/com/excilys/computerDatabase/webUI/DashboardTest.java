package com.excilys.computerDatabase.webUI;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver; 
import org.openqa.selenium.firefox.FirefoxDriver; 

public class DashboardTest {

	private final String URL = "http://localhost:8080/CDB/dashboard";
	
	static WebDriver driver;
	
	@BeforeClass
	public static void setup() {
		driver = new FirefoxDriver();
	}
	
	@Before
	public void reset() {
		driver.get(URL);
	}
	
	@Test
	public void shouldHaveAHomeButton() {
		driver.findElement(By.id("home-button")).click();
	}
	
	 @AfterClass
	 public static void end(){
		 if (driver != null) {
			 driver.quit();
		 }
	 }
}
