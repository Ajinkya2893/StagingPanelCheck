package master_distributor;

import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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
import lib.Utility;

/**
 * @author "Dev Shah"
 **/

public class Reports extends BaseClass{

	private WebDriver driver;
	private WebDriverWait w;

	//Create object
	Config c=new Config();
	PageObjects_MD p=new PageObjects_MD();


	@BeforeTest
	public void setUp(){

		rep = Utility.getInstance(Config.panelReport);
		test = rep.startTest("MD "+this.getClass().getSimpleName());

		test.log(LogStatus.INFO, "Setting Up the Test");

		try{
			//Call browser specific method and instantiate driver 
			driver=c.openBrowser();	

			//Wait 
			w=new WebDriverWait(driver,30);

			//PARALLEL EXECUTION
			driver.get(Config.baseUrl);

			//maximize window
			driver.manage().window().maximize();

			//Wait 
			w=new WebDriverWait(driver, 45);
		}
		catch(Exception e){
			test.log(LogStatus.FAIL, "Unable to create a browser instance or Couldn't navigate to the URL provided");
			Assert.fail("Couldn't navigate to the URL provided");
		}

		try {
			//LOGIN
			c.Login(driver, "Super Distributor");
			test.log(LogStatus.PASS, "Successfully Logged into System as Super Distributor");

			//Wait
			w.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(p.transfer_link)));
			driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		}catch (Exception e) {
			test.log(LogStatus.FAIL, "Issue while login in to system");
			Assert.fail("Issue while login in to system");
		}
	}

	@Test(priority=1,enabled=true)
	public void mainReport(){

		test.log(LogStatus.INFO, "Navigating to Main Reports Page");
		driver.navigate().to(Config.baseUrl+"/shops/mainReport");

		//Wait
		w.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(p.mainreport_link)));

		//Navigate to Main Report
		driver.findElement(By.linkText(p.mainreport_link)).click();

		//Assert page is not breaking
		c.assertFunc(driver);

		test.log(LogStatus.INFO, "Verfying all the Record is present or not");
		//Verify all columns exist
		driver.findElement(By.xpath("//*[contains(text(),'Today')]")).isDisplayed();
		driver.findElement(By.xpath("//*[contains(text(),'Yesterday')]")).isDisplayed();
		driver.findElement(By.xpath("//*[contains(text(),'Last 7 days')]")).isDisplayed();
		driver.findElement(By.xpath("//*[contains(text(),'Last 30 days')]")).isDisplayed();

		//Wait until graph loads
		w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(p.mainreport_charts)));

		//Verify all graphs are present
		driver.findElement(By.xpath(p.mainreport_charts)).isDisplayed();

		//List
		List<WebElement> e=	driver.findElements(By.xpath(p.mainreport_charts));
		System.out.println(e.size());
		//Assert size is 4, i.e. 4 charts exist for distributor
		Assert.assertEquals(5, e.size());	
		test.log(LogStatus.PASS, "Verified all the data is present");

	}

	@Test(priority=2, enabled=true)
	public void accountHistory(){    	

		try{
			test.log(LogStatus.INFO, "Navigating to Reports Page");
			//Navigate to Reports
			driver.findElement(By.linkText(p.acchistory_link)).click();

			//Wait
			w.until(ExpectedConditions.visibilityOfElementLocated(By.id(p.from_datepicker)));
		}
		catch(Exception e)
		{
			test.log(LogStatus.FAIL, "Issue occured while navigating to account history");
			Assert.fail("Issue occured while navigating to account history");
		}

		//Call Page breaks method
		c.assertFunc(driver);

		//Store title name
		String title_msg=driver.findElement(By.xpath(p.page_title)).getText();

		try{
			//Call set date function
			p.pickDates(driver, p.todaydate_datepicker, p.todaydate_datepicker);

		}
		catch(Exception e)
		{
			test.log(LogStatus.FAIL, "Issue occured while selecting date range");
			Assert.fail("Issue occured while selecting date range");
		}
		//wait
		w.until(ExpectedConditions.invisibilityOfElementLocated(By.id(p.loader_element)));	

		try{
			//Verify if all table columns are displayed
			driver.findElement(By.xpath(p.acchistory_txnid_column)).isDisplayed();
			driver.findElement(By.xpath(p.acchistory_particulars_column)).isDisplayed();
			driver.findElement(By.xpath(p.acchistory_debit_column)).isDisplayed();
			driver.findElement(By.xpath(p.acchistory_credit_column)).isDisplayed();
			driver.findElement(By.xpath(p.acchistory_opening_column)).isDisplayed();
			driver.findElement(By.xpath(p.acchistory_closing_column)).isDisplayed();
			test.log(LogStatus.PASS, "All the Rows and Columns are present");
		}
		catch(Exception e)
		{
			test.log(LogStatus.FAIL, "Issue occured while verifying presence of columns");
			Assert.fail("Issue occured while verifying presence of columns");	
		}

		try{
			//Rows presence check
			driver.findElement(By.xpath(p.acchistory_rows)).isDisplayed();	
			test.log(LogStatus.PASS, "Account history is present");
		}catch(Exception e){
			test.log(LogStatus.FAIL, "Error occured while viewing entries in account history");
			Assert.fail("Error occured while viewing entries in account history");
		}

		//To cross check the title is displaying the period along with the title.
		String title_msg1=driver.findElement(By.xpath(p.page_title)).getText();

		//Assert the table is updated with entries
		Assert.assertEquals("Page title not matching", !title_msg.equals(title_msg1));

		//Get last closing from latest record.
		String closing_bal=driver.findElement(By.xpath(p.acchistory_closingbal)).getText();
		String current_bal=driver.findElement(By.xpath(p.user_balance_element)).getText();

		if(current_bal.equals(closing_bal))
			test.log(LogStatus.PASS, "Balance Matched as per to Latest Acc Record");
		else
			test.log(LogStatus.FAIL, "Balance not matching as per to latest acc record");
		Assert.assertEquals("Balance not matching as per to latest acc record", current_bal, closing_bal);
	}

	@Test(priority=3, enabled=true)
	public void buyReport(){

		test.log(LogStatus.INFO, "Verifying the Buy Report Link");
		//Navigate to Reports
		driver.findElement(By.linkText(p.buyreport_link)).click();

		//Wait
		w.until(ExpectedConditions.visibilityOfElementLocated(By.id(p.from_datepicker)));

		//Call - Check page breaks function
		c.assertFunc(driver);

		//Call absolute date picker function		
		p.pickDates_absolute(driver, p.fromDate_choice1, p.toDate_choice1);

		//Wait
		//w.until(ExpectedConditions.visibilityOfElementLocated(By.id("loader2")));
		w.until(ExpectedConditions.invisibilityOfElementLocated(By.id(p.loader_element)));

		//Check whether data/records exist or not and print accordingly
		if(driver.findElements(By.xpath(p.noresults_message)).isEmpty())
		{
			test.log(LogStatus.PASS, "Latest top ups are displayed");
			System.out.println("Latest top ups are displayed");
		}
		else{
			test.log(LogStatus.FAIL, "No Results Found");
			System.out.println("No Results Found");
		}

	}

	@Test(priority=4, enabled=true)
	public void balanceReport(){

		//
		test.log(LogStatus.INFO, "Checking the Balance Report");
		driver.findElement(By.linkText(p.balancereport_link)).click();

		//Check page breaks
		c.assertFunc(driver);

		//Select required retailer
		driver.findElement(By.xpath(".//option[contains(text(),'"+c.retailer_num+"')]"));

		//Search
		driver.findElement(By.id(p.searchButton));
		test.log(LogStatus.INFO, "Clicking on Search Button");

		//Download old data
		driver.findElement(By.xpath("//*[@id='innerDiv']/div[1]/a")).click();

		//Check if any active transfer is present or not
		if(!driver.findElements(By.xpath(".//fieldset/span")).isEmpty())
		{
			//No active transfer present
			test.log(LogStatus.FAIL, "NO active transfer or transaction present");
			System.out.println("NO active transfer or transaction present");
		}
		else{
			//Active trans present so check if pullback link is present
			driver.findElement(By.linkText(p.pullback_link));
			test.log(LogStatus.PASS, "Active transactions present");
			System.out.println("Active transactions present");
		}
	}

	/*
    @Test(priority=7, enabled=true)
    public void checkChart(){

    	ReadChart rc=new ReadChart();
    	rc.navigate(driver);
    	//rc.chart1(driver);
    	rc.chart2(driver);
    	rc.chart3(driver);
    	rc.chart4(driver);
    	rc.chart5(driver);

    }
	 */

	@AfterMethod
	public void resultCheck(ITestResult result){
		if(ITestResult.FAILURE==result.getStatus())
			Utility.captureScreenshot(driver,""+result.getName(), test);
	}

	@AfterTest
	public void breakdown(){
		rep.endTest(test);
		driver.quit();
	}
}