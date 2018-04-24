package lib;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

public class ReportCharts {

	WebDriver driver;
	Config c=new Config();	
	String date= getDate();


	@AfterTest
	public void teardown(){

		driver.quit();
	}

	public String getDate()
	{

		DateFormat modifiedDate= new SimpleDateFormat("yyyy-MM-dd");

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -2);  

		return modifiedDate.format(cal.getTime());

	}


	@Test(priority=0, enabled=true)
	public void navigate(WebDriver driver){

		WebDriverWait w=new WebDriverWait(driver, 30);


		/*//Go to main report
		driver.get(c.baseUrl);

		//Login
		c.distLogin(driver);

		//Wai
		w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='shopViewForm']/fieldset/div[1]")));*/

		//Go to reports
		driver.findElement(By.linkText("Reports")).click();
		w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='test2']/div/div[1]/div/*[name()='svg']/*[name()='g'][1]/*[name()='text']")));


		WebElement e1 = driver.findElement(By.xpath(".//*[@id='test2']/div/div[1]/div/*[name()='svg']/*[name()='g'][1]/*[name()='text']"));

		//Scroll
		((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", e1);

	}


	@Test(priority=1, enabled=true)
	public void chart1(WebDriver driver)
	{
		//Chart1
		/*for(int i=1;i<=3;i++)
			{
				WebElement e=driver.findElement(By.xpath(".//div[@id='test1']/div/div/div/div/table/tbody/tr/td[contains(text(),'"+date+"')]/following::td["+i+"]"));
				System.out.println(((JavascriptExecutor)driver).executeScript("return arguments[0].innerHTML;", e));
			}*/

		//Get all latest chart values
		WebElement Topup_sold_today=driver.findElement(By.xpath(".//div[@id='test1']/div/div/div/div/table/tbody/tr/td[contains(text(),'"+date+"')]/following::td[1]"));
		WebElement Topup_buy_today=driver.findElement(By.xpath(".//div[@id='test1']/div/div/div/div/table/tbody/tr/td[contains(text(),'"+date+"')]/following::td[2]"));	
		WebElement Tertiary_sale=driver.findElement(By.xpath(".//div[@id='test1']/div/div/div/div/table/tbody/tr/td[contains(text(),'"+date+"')]/following::td[3]"));

		//Print 
		System.out.println("Topup sold - " +((JavascriptExecutor)driver).executeScript("return arguments[0].innerHTML;", Topup_sold_today));
		System.out.println("Topup Buy - " +((JavascriptExecutor)driver).executeScript("return arguments[0].innerHTML;", Topup_buy_today));
		System.out.println("Tertiary Sale - "+((JavascriptExecutor)driver).executeScript("return arguments[0].innerHTML;", Tertiary_sale));

		System.out.println("");
	}


	@Test(priority=2, enabled=true)
	public void chart2(WebDriver driver)
	{
		//Chart1
		WebElement transacting_Retailers=driver.findElement(By.xpath(".//div[@id='test2']/div/div/div/div/table/tbody/tr/td[contains(text(),'"+date+"')]/following::td[1]"));
		System.out.println("Transacting retailers are - "+((JavascriptExecutor)driver).executeScript("return arguments[0].innerHTML;", transacting_Retailers).toString());
		System.out.println("");
	}

	@Test(priority=3, enabled=true)
	public void chart3(WebDriver driver)
	{
		//Chart1
		WebElement new_Outlets=driver.findElement(By.xpath(".//div[@id='test3']/div/div/div/div/table/tbody/tr/td[contains(text(),'"+date+"')]/following::td[1]"));
		System.out.println("New outlets opened - "+((JavascriptExecutor)driver).executeScript("return arguments[0].innerHTML;", new_Outlets).toString());
		System.out.println("");
	}


	@Test(priority=4, enabled=true)
	public void chart4(WebDriver driver)
	{
		//Chart1
		WebElement unique_Topups=driver.findElement(By.xpath(".//div[@id='test4']/div/div/div/div/table/tbody/tr/td[contains(text(),'"+date+"')]/following::td[1]"));
		System.out.println("Unique TopUps/Day - "+((JavascriptExecutor)driver).executeScript("return arguments[0].innerHTML;", unique_Topups).toString());
		System.out.println("");
	}

	@Test(priority=5, enabled=true)
	public void chart5(WebDriver driver)
	{
		//Chart1
		WebElement avg_Sale=driver.findElement(By.xpath(".//div[@id='test6']/div/div/div/div/table/tbody/tr/td[contains(text(),'"+date+"')]/following::td[1]"));
		System.out.println("Average Sale/Retailer - "+((JavascriptExecutor)driver).executeScript("return arguments[0].innerHTML;", avg_Sale).toString());
		System.out.println("");
	}
}
