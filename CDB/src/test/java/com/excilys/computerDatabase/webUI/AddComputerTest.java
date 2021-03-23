package com.excilys.computerDatabase.webUI;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class AddComputerTest {

	private final String URL = "http://localhost:8080/CDB/addComputer";
	
	WebDriver driver;
	@Before
	public void setup() {
		driver = new FirefoxDriver();
		driver.get(URL);
	}
	
	@Test
	public void shouldHaveCompleteForm() {
		List<WebElement> form = driver.findElements(By.xpath("//form[@id='addComputerForm'][@action='addComputer'][@method='POST']"));
		assertEquals(1,form.size());
	}
	
	 @After
	 public void end(){
		 if (driver != null) {
			 driver.quit();
		 }
	 }
}
