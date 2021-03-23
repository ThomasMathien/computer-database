package com.excilys.computerDatabase.webUI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

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
		List<WebElement> computerName = driver.findElements(By.xpath("//input[@id='computerName']"));
		List<WebElement> introduced = driver.findElements(By.xpath("//input[@id='introduced']"));
		List<WebElement> discontinued = driver.findElements(By.xpath("//input[@id='discontinued']"));
		List<WebElement> companyId = driver.findElements(By.xpath("//select[@id='companyId']"));
		List<WebElement> submit = driver.findElements(By.xpath("//input[@type='submit']"));
		assertEquals(1,form.size());
		assertEquals(1,computerName.size());
		assertEquals(1,introduced.size());
		assertEquals(1,discontinued.size());
		assertEquals(1,companyId.size());
		assertEquals(1,submit.size());
	}
	
	@Test
	public void cancelButtonShouldRedirect() {
		List<WebElement> cancelButton = driver.findElements(By.id("cancelButton"));
		assertEquals(1,cancelButton.size());
		cancelButton.get(0).click();
		assertTrue(driver.getCurrentUrl().contains("dashboard"));
	}
	
	@Test
	public void companiesShouldBeListOfId() {
		List<WebElement> companies = driver.findElements(By.xpath("//option"));
		assertFalse(companies.isEmpty());
		for (WebElement option : companies) {
			try {
				Long.parseLong(option.getAttribute("value"));
			} catch (NumberFormatException e) {
				fail();
			}
		}
	}
	
	@Test
	public void shouldAddComputerOnSubmit() {
		WebElement computerName = driver.findElement(By.xpath("//input[@id='computerName']"));
		WebElement introduced = driver.findElement(By.xpath("//input[@id='introduced']"));
		WebElement discontinued = driver.findElement(By.xpath("//input[@id='discontinued']"));
		WebElement companyId = driver.findElement(By.xpath("//select[@id='companyId']"));
		WebElement submit = driver.findElement(By.xpath("//input[@type='submit']"));
		//computerName.sendKeys("Super PC 5000");
		introduced.sendKeys("09252012");
		discontinued.sendKeys("09252012");
		new Select(companyId).selectByVisibleText("Apple Inc.");
		//submit.click();
		assertFalse(driver.getCurrentUrl().contains("addComputer"));
	}
	
	@Test
	public void shouldHaveValidation() {
		
	}
	
	 @After
	 public void end(){
		 if (driver != null) {
			 driver.quit();
		 }
	 }
}
