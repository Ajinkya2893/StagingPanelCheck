package pay1_distributor;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import lib.Config;
import lib.PageObjects;
import lib.Utility;

/**
 * @author Dev Shah
 */

public class Support extends BaseClass{

	private WebDriver driver;
	private WebDriverWait w;

	Config c=new Config();
	PageObjects p = new PageObjects();

	@BeforeTest
	public void setUp(){

		rep = Utility.getInstance(Config.panelReport);
		test = rep.startTest(this.getClass().getSimpleName());

		test.log(LogStatus.INFO, "Setting Up the Test");
		try{
			//Select browser method call
			driver=c.openBrowser();

			test.log(LogStatus.INFO, "Opened a browser instance");
			//URL
			driver.get(Config.baseUrl);

			test.log(LogStatus.PASS, "Successfully Navigated to provided URL");
			//maximize window
			driver.manage().window().maximize();

			//Create Wait object 
			w=new WebDriverWait(driver,30);
		}
		catch(Exception e){
			test.log(LogStatus.FAIL, "Unable to create a browser instance or Couldn't navigate to the URL provided");
			Assert.fail("Couldn't navigate to the URL provided");
		}
		try {
			//LOGIN
			c.Login(driver, "Distributor");
			test.log(LogStatus.PASS, "Successfully Logged into System");

			//Wait
			w.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(p.transfer_link)));
			driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		}catch (Exception e) {
			test.log(LogStatus.FAIL, "Issue while login in to system");
			Assert.fail("Issue while login in to system");
		}
	}

	@Test(priority=1, enabled=true)
	public void support(){
		try{
			driver.get(Config.baseUrl+"/shops/bankDetails");
			test.log(LogStatus.INFO, "Navigating to Bank Details page");
			w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='appTitle alignCenter']")));
		}
		catch(Exception e){
			org.testng.Assert.fail("Exception occured while navigating to support page");	
		}
		//Cross check page title
		Assert.assertEquals(driver.getTitle(), Config.title);
		Assert.assertTrue(!driver.getCurrentUrl().contains("error"));
		Assert.assertTrue(!driver.getCurrentUrl().contains("404"));
		Assert.assertTrue(!driver.getCurrentUrl().contains("er404"));

		//Bank forms should be visible
		try{
			driver.findElement(By.xpath(".//*[@id='innerDiv']/form/fieldset[1]"));
			driver.findElement(By.xpath(".//*[@id='innerDiv']/form/fieldset[2]"));
			driver.findElement(By.xpath(".//*[@id='innerDiv']/form/fieldset[3]"));
			driver.findElement(By.xpath(".//*[@id='innerDiv']/form/fieldset[4]"));

			//All bank names are accurately displayed
			driver.findElement(By.xpath("//*[contains(text(),'State Bank of India')]")).isDisplayed();
			driver.findElement(By.xpath("//*[contains(text(),'ICICI')]")).isDisplayed();
			driver.findElement(By.xpath("//*[contains(text(),'Bank of Maharashtra')]")).isDisplayed();
			driver.findElement(By.xpath("//*[contains(text(),'Axis')]")).isDisplayed();
			test.log(LogStatus.PASS, "All the Bank details are displayed Correctly");
		}
		catch(Exception e){
			test.log(LogStatus.FAIL, "Exception occured while verifying all elements exist on Support page");
			Assert.fail("Exception occured while verifying all elements exist on Support page");	
		}
	}

	@Test(priority=2, enabled=true)
	public void otherPages(){
		try{
			test.log(LogStatus.INFO, "Navigating to Distributors Help Desk Page");
			//DISTRIBUTORS HELP DESK
			driver.findElement(By.linkText("Distributors Help Desk")).click();
		}catch(Exception e){
			test.log(LogStatus.FAIL, "Exception occured while navigating to Distributors Help Desk page");
			Assert.fail("Exception occured while navigating to Distributors Help Desk page");	
		}
		//Check page breaks
		c.assertFunc(driver);

		Assert.assertEquals(driver.findElement(By.xpath(p.page_title)).getText(), "Distributor Help Desk");
		test.log(LogStatus.PASS, "Distributor Help desk Page is displayed");
		//Limit department no.
		try{
			driver.findElement(By.linkText("Limit Department No.")).click();
		}catch(Exception e){
			test.log(LogStatus.FAIL, "Exception occured while navigating to Limit Department No. page");
			Assert.fail("Exception occured while navigating to Limit Department No. page");	
		}

		Assert.assertEquals(driver.getTitle(), Config.title);
		Assert.assertEquals(driver.findElement(By.xpath(p.page_title)).getText(), "Limit Department");
		test.log(LogStatus.PASS, "Limit Department text is displayed");
		//Customer Care
		try{
			driver.findElement(By.linkText("Customer Care No.(For Retail Partners)")).click();
		}catch(Exception e){
			test.log(LogStatus.FAIL, "Exception occured while navigating to Customer Care page");
			Assert.fail("Exception occured while navigating to Customer Care page");	
		}
		Assert.assertEquals(driver.getTitle(), Config.title);
	}

	@AfterMethod
	public void resultCheck(ITestResult result){
		if(ITestResult.FAILURE==result.getStatus())
			Utility.captureScreenshot(driver,""+result.getName()+System.currentTimeMillis(), test);
	}

	@AfterTest
	public void aftertest(){
		rep.endTest(test);
		//rep.flush();
		driver.quit();
	}
}
