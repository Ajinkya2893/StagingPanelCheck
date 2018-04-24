package lib;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Functions {

	PageObjects p=new PageObjects();
	//private WebDriverWait w;

	public void handleAlert(WebDriver driver)
	{
		WebDriverWait w= new WebDriverWait(driver, 15);
		w.until(ExpectedConditions.alertIsPresent());

		//Handle two alerts
		Alert a=driver.switchTo().alert();
		a.accept();

		//Switch to window
		driver.switchTo().defaultContent();

	}

	public void handleDropdown(WebDriver driver, String selector, String text){

		Select s = new Select(driver.findElement(By.id(selector)));
		s.selectByVisibleText(text);
	}

	public void handleDropdown(WebDriver driver, String selector, int id){

		Select s = new Select(driver.findElement(By.id(selector)));
		s.selectByIndex(id);
	}


	public void handleDatePicker(WebDriver driver, String from, String to){

		//Select from date
		driver.findElement(By.id(p.from_datepicker)).click();

		//Clear
		driver.findElement(By.xpath(p.clear_datepicker)).click();

		//CLose
		driver.findElement(By.xpath(p.close_datepicker)).click();

		//Enter date
		driver.findElement(By.id(p.from_datepicker)).sendKeys(from);

		//Select to date
		driver.findElement(By.id(p.to_datepicker)).click();

		//Clear
		driver.findElement(By.xpath(p.clear_datepicker)).click();

		//CLose
		driver.findElement(By.xpath(p.close_datepicker)).click();

		//Enter date
		driver.findElement(By.id(p.to_datepicker)).sendKeys(to);	

		//Click on Search
		driver.findElement(By.id(p.searchButton)).click();	
	}
}
