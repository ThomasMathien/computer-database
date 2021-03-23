package com.excilys.computerDatabase.webUI;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver; 
import org.openqa.selenium.firefox.FirefoxDriver; 

public class DashboardTest {

	private final String URL = "http://localhost:8080/CDB/dashboard";
	
	WebDriver driver;
	@Before
	public void setup() {
		driver = new FirefoxDriver();
		driver.get(URL);

	}
	
	@Test
	public void shouldHaveAHomeButton() {
		driver.findElement(By.id("home-button")).click();
	}
	
	 @After
	 public void end(){
		 if (driver != null) {
			 driver.quit();
		 }
	 }
}
