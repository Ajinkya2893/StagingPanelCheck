
package pay1_distributor;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
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

public class SalesmanTransfer extends BaseClass{
	private WebDriver driver;
	private WebDriverWait w;

	//
	String temp;
	String s_bal;
	String post_s_bal;
	String temp_txn_id;

	Config c = new Config();
	PageObjects p = new PageObjects();

	@BeforeTest
	public void setUp(){
		
		rep = Utility.getInstance(Config.panelReport);
		test = rep.startTest(this.getClass().getSimpleName());

		test.log(LogStatus.INFO, "Setting Up the Test");
		
		//Select browser method call
		driver=c.openBrowser();
		test.log(LogStatus.INFO, "Opened a browser instance");
		//URL
		try{
			driver.get(Config.baseUrl);
			test.log(LogStatus.PASS, "Successfully Navigated to provided URL");
		}
		catch(Exception e){
			test.log(LogStatus.FAIL, "Couldn't navigate to the URL provided");
			Assert.fail("Couldn't navigate to the URL provided");
		}

		//maximize window
		driver.manage().window().maximize();

		//Create Wait object 
		w=new WebDriverWait(driver,30);

		//LOGIN
		c.Login(driver, "Distributor");
		test.log(LogStatus.PASS, "Successfully Logged into System");
		
		//Wait
		w.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Balance Transfer")));

		//Set implicit wait
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);

		//Get distributor balance
		temp=driver.findElement(By.xpath(p.user_balance_element)).getText();	

		driver.findElement(By.linkText(p.activities_link)).click();
	}

	@Test(priority=0,enabled=true)
	public void getSalesmanBalance(){
		try{
			//Click on retailers list link
			driver.findElement(By.linkText(p.salesmenList_link)).click();

			//Wait
			w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(p.page_title)));	
		}
		catch(Exception e){
			test.log(LogStatus.FAIL, "Error occured while navigating to salesmen list page");
			Assert.fail("Error occured while navigating to salesmen list page");	
		}
		//Assert page breaks, Calls assert function
		c.assertFunc(driver);
		try{
			//Get salesman balance
			s_bal = driver.findElement(By.xpath(".//td[contains(text(),'"+c.salesman_num+"')]/following::td[2]")).getText();
		}
		catch(Exception e)
		{
			test.log(LogStatus.FAIL, "Error occured while extracting salesman balance");
			Assert.fail("Error occured while extracting salesman balance");	
		}
	}

	@Test(priority=1,enabled=true)
	public void stransfer(){

		try{
			//Navigate to balance transfer
			driver.findElement(By.linkText(p.transfer_link)).click();
			test.log(LogStatus.INFO, "Transfer Amount Page");
			//Wait
			w.until(ExpectedConditions.visibilityOfElementLocated(By.id(p.transfer_amount_element)));
		}
		catch(Exception e){
			test.log(LogStatus.FAIL, "Issue occured while navigating to Balance Transfer page.");
			Assert.fail("Issue occured while navigating to Balance Transfer page.");
		}

		try{
			test.log(LogStatus.INFO , "Transfering the Salesman Amount");
			//Enter amount
			driver.findElement(By.id(p.transfer_amount_element)).sendKeys(c.s_amount);

			Select s=new Select(driver.findElement(By.id(p.transfer_type)));
			s.selectByVisibleText("Salesman");

			//Enter salesman
			driver.findElement(By.id(p.transfer_number_element)).sendKeys(c.salesman_num);
			w.until(ExpectedConditions.visibilityOfElementLocated(By.partialLinkText(c.salesman_num)));

			driver.findElement(By.partialLinkText(c.salesman_num)).click();

			//Select option 1-Cash, 2-NEFT,	3-ATM,	4-Cheque
			driver.findElement(By.xpath("//div[@class='fieldLabelSpace1']/input[@id='typeRadio']["+c.s_txnType+"]")).click();

			//Txn ID
			driver.findElement(By.id(p.txn_id)).sendKeys(c.s_txnid);
			test.log(LogStatus.PASS, "Successfully transfer the salesman Amount");
		}
		catch(Exception e)
		{
			test.log(LogStatus.FAIL, "Issue occured while entering balance transfer details");
			Assert.fail("Issue occured while entering balance transfer details");	
		}

		try{
			//Confirm last transactions is displayed
			driver.findElement(By.xpath(p.lasttransactions)).isDisplayed();
			test.log(LogStatus.INFO, "Checking the Transfered Amount");
		}
		catch(Exception e){
			test.log(LogStatus.FAIL, "Issue occured while viewing last transactions element");
			Assert.fail("Issue occured while viewing last transactions element");	
		}

		if(!driver.findElements(By.xpath(p.lasttransactions_header)).isEmpty())
			Assert.assertTrue(!driver.findElements(By.xpath(p.lasttransactions_rows)).isEmpty());
		else
			Assert.assertTrue(driver.findElements(By.xpath(p.lasttransactions_rows)).isEmpty());

		try{
			//Submit
			driver.findElement(By.id(p.searchButton)).click();

			//wait
			w.until(ExpectedConditions.visibilityOfElementLocated(By.id(p.confirmtransaction_button)));
		}
		catch(Exception e){
			test.log(LogStatus.FAIL, "Error occured while submitting balance transfer request");
			Assert.fail("Error occured while submitting balance transfer request");	
		}
		//Confirm TRANSFER
		try{
			//re enter amount
			driver.findElement(By.id(p.confirmamount_field)).sendKeys(c.s_amount);

			//Confirm
			driver.findElement(By.id(p.confirmtransaction_button)).click();

			//Messages
			w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(p.flashmessage)));
		}
		catch(Exception e){
			test.log(LogStatus.FAIL, "Error occured while CONFIRMING balance transfer request");
			Assert.fail("Error occured while CONFIRMING balance transfer request");		
		}	

		String a=driver.findElement(By.xpath(p.flashmessage)).getText();

		temp_txn_id=a.substring(60);

		String temp1=driver.findElement(By.xpath(p.user_balance_element)).getText();
		//Check balance consistency and whether is back to the same balance

		if(!temp1.equals(temp))
		{
			test.log(LogStatus.PASS, "Updated balance post Transfer");
		}
		else
		{
			test.log(LogStatus.FAIL, "Error in updating balance post transfer");
			Assert.fail("Error in updating balance post transfer");
		}
	}

	@Test(priority=2,enabled=true)
	public void verifySalesmanBalance(){
		//Confirm retailer balance
		try{
			//Click on retailers list link
			driver.findElement(By.linkText(p.salesmenList_link)).click();
			test.log(LogStatus.INFO, "Salesman Balance Transfer Page");
			//Wait
			w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(p.page_title)));	
		}
		catch(Exception e){
			test.log(LogStatus.FAIL, "Error occured while navigating to salesman list page");
			Assert.fail("Error occured while navigating to salesman list page");	
		}
		//Confirm balance	
		post_s_bal=driver.findElement(By.xpath("//td[contains(text(),'"+c.salesman_num+"')]/following::td[2]")).getText();

		//post_s_bal=temp.substring(0, 2);
		if(s_bal.equals(post_s_bal)){
			//Fail Test case
			test.log(LogStatus.FAIL, "Salesman balance not reflecting post transaction or balance transfer");
			Assert.fail("Salesman balance not reflecting post transaction or balance transfer");
		}
		else{
			test.log(LogStatus.PASS, "Successful Transfer");
			System.out.println("Successful Transfer");
		}
	}

	@Test(priority=3, enabled=true)
	public void lastTransactions(){

		test.log(LogStatus.INFO, "Navigating to Balance Report Page");
		//Balance transfer
		driver.findElement(By.linkText(p.transfer_link)).click();

		//Select salesman
		Select s=new Select(driver.findElement(By.id(p.transfer_type)));
		s.selectByVisibleText("Salesman");

		//Type salesman to get last transactions
		driver.findElement(By.id(p.transfer_number_element)).sendKeys(c.salesman_num);

		//Click on salesman
		driver.findElement(By.partialLinkText(c.salesman_num)).click();

		//get elements
		String temp =driver.findElement(By.xpath(p.lasttransactions_latesttxn)).getText();

		if(temp.equalsIgnoreCase(temp_txn_id))
		{
			test.log(LogStatus.INFO, "Showing up in recent transactions");
			System.out.println("Showing up in recent transactions");
		}
		else{
			test.log(LogStatus.FAIL, "Transfer entry not present in the salesman's recent transactions");
			Assert.fail("Transfer entry not present in the salesman's recent transactions.");
		}
	}			

	@Test(priority=4,enabled=true)
	//BEFORE PULLBACK
	public void mainReport(){

		driver.get(Config.baseUrl+"/shops/mainReport/1");
		test.log(LogStatus.INFO, "Navigating to Main Report Page");

		String topup_sold_day= driver.findElement(By.xpath(p.topup_sold)).getText();

		if(!c.s_amount.equals(topup_sold_day))
		{
			test.log(LogStatus.PASS, "Main Report displaying values for topup sold/day correctly");
			System.out.println("Main Report displaying values for topup sold/day correctly");
		}
		else{
			test.log(LogStatus.FAIL, "Main report not correctly displaying the top up sold/day as per latest transaction");
			Assert.fail("Main report not correctly displaying the top up sold/day as per latest transaction");
		}

		String topup_unique=driver.findElement(By.xpath(p.topup_unique)).getText();
		if(topup_unique.equals("0"))
		{
			test.log(LogStatus.PASS, "Main Report displaying values for unique topups correctly");
			System.out.println("Main Report displaying values for unique topups correctly");
		}
		else{
			test.log(LogStatus.FAIL, "Main report fails to correctly displaying the unique topup count as per latest transaction");
			Assert.fail("Main report fails to correctly displaying the unique topup count as per latest transaction");
		}
	}

	@Test(priority=5, enabled=true, dependsOnMethods={"stransfer"})
	public void spullback(){
		//PULLBACK FLOW
		try{
			//Navigate to reports
			driver.findElement(By.linkText(p.reports_link)).click();

			//Wait
			w.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(p.salesmanreport_link)));

			//Go to salesman report
			driver.findElement(By.linkText(p.salesmanreport_link)).click();
			test.log(LogStatus.INFO, "Navigated to Salesman Report Page");
		}
		catch(Exception e)
		{
			test.log(LogStatus.FAIL, "Error occured while navigating to REPORTS tab");
			Assert.fail("Error occured while navigating to REPORTS tab");			
		}

		//SEARCH TXNs
		try{
			//Select required salesman
			driver.findElement(By.xpath("//td[contains(text(),'"+temp_txn_id+"')]/following::td[12]/a")).click();
		}
		catch(Exception e)
		{
			test.log(LogStatus.FAIL, "Error occured while SEARCHING for salesman transaction");
			Assert.fail("Error occured while SEARCHING for salesman transaction");			
		}

		try{
			w.until(ExpectedConditions.alertIsPresent());

			//Handle two alerts
			Alert a=driver.switchTo().alert();
			a.accept();

			w.until(ExpectedConditions.alertIsPresent());
			a.accept();

			//Switch to window
			driver.switchTo().defaultContent();

			//Refresh page
			driver.navigate().refresh();

			//Wait
			w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(p.pullback_message)));

			//Assert pullback is successful
			Assert.assertTrue(driver.findElement(By.xpath(p.pullback_message)).isDisplayed());
			test.log(LogStatus.PASS, "PullBack is successfully done");
		}
		catch(Exception e)
		{
			test.log(LogStatus.FAIL, "Error occured while completing pullback request");
			Assert.fail("Error occured while completing pullback request");			
		}

		String temp1=driver.findElement(By.xpath(p.user_balance_element)).getText();

		//Check balance consistency and whether is back to the same balance
		Assert.assertEquals(temp1, temp);
		test.log(LogStatus.PASS, "Balance is intact, transaction successful");
		System.out.println("Balance is intact, transaction successful");
	}

	@Test(priority=6, enabled=true, dependsOnMethods={"spullback"})
	public void accHistory1()
	{
		try{
			//Navigate to acc history page
			driver.get(Config.baseUrl+"/shops/accountHistory/1");
			test.log(LogStatus.INFO, "Navigating the Accouunt History page");
		}
		catch(Exception e){
			test.log(LogStatus.FAIL, "Error while navigating to the page");
			Assert.fail("Error while navigating to the page");
		}

		//SET TODAY'S DATE
		try{
			driver.navigate().to(Config.baseUrl+"/shops/accountHistory/0/1/1");
			w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(p.from_datepicker)));
			//Navigate to date control
			driver.findElement(By.xpath(p.from_datepicker)).click();
			driver.findElement(By.xpath(p.from_datepicker)).click();

			//Select today's date
			driver.findElement(By.xpath(p.todaydate_datepicker)).click();

			//Navigate to date control
			driver.findElement(By.xpath(p.to_datepicker)).click();
			driver.findElement(By.xpath(p.to_datepicker)).click();

			//Select today's date
			driver.findElement(By.xpath(p.todaydate_datepicker)).click();

			//Search
			driver.findElement(By.id(p.searchButton)).click();
			test.log(LogStatus.INFO, "Searching for the Current date");
		}
		catch(Exception e1)
		{
			test.log(LogStatus.FAIL, "Error while searching for transaction");
			Assert.fail("Error while searching for transaction");
		}

		//CONFIRM ENTRY
		try{
			//Check transaction entry
			driver.findElement(By.xpath(".//td[contains(text(),'"+temp_txn_id+"')]/following::td[1][contains(text(),'Topup')]")).isDisplayed();
			test.log(LogStatus.INFO, "Verifying the Transaction Entry");

			//Check pullback entry
			driver.findElement(By.xpath(".//td[contains(text(),'Pullback')][contains(text(),'"+temp_txn_id+"')]/following::td[2][contains(text(),'"+c.s_amount+"')]")).isDisplayed();
			test.log(LogStatus.INFO, "Verifying the PullBack Entry");

			//Compare opening and closing balance
			test.log(LogStatus.INFO, "Comparing the Opening and Closing Balance");

			//Get opening
			String debit=driver.findElement(By.xpath(".//td[contains(text(),'"+temp_txn_id+"')]/following::td[1][contains(text(),'Topup')]/following::td[1]")).getText();
			test.log(LogStatus.INFO, "Opening Balance is "+debit);

			//Get closing
			String credit=driver.findElement(By.xpath("//td[contains(text(),'Pullback')][contains(text(),'"+temp_txn_id+"')]/following::td[2]")).getText();
			test.log(LogStatus.INFO, "Closing Balance is " +credit);

			//Check consistency in balancetem
			Assert.assertEquals(debit, credit, "Discrepancy while matching balances post pullback");
			test.log(LogStatus.PASS, "Balance is verifyed and pull back is successfull");
		}
		catch(Exception e)
		{
			test.log(LogStatus.FAIL, "Error while confirming pullback transaction");
			Assert.fail("Error while confirming pullback transaction");
		}
	}

	@Test(priority=7, enabled=true)
	public void salesman_listchanges(){
		//Navigate
		driver.get(Config.baseUrl+"/shops/salesmanListing/");

		test.log(LogStatus.INFO, "Navigating to Salesman List Page");
		//Verify salesman collecton is removed
		Assert.assertTrue(driver.findElements(By.linkText(p.salesmanCollections_link)).isEmpty());

		//Edit salesman
		driver.findElement(By.xpath(".//td[contains(text(),'"+c.salesman_num+"')]/following::td[3]/a")).click();

		//Wait
		w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='smMobile']")));

		//Check limit field is disappeared.
		Assert.assertTrue(driver.findElements(By.xpath(p.salesman_limit_field)).isEmpty());
	}

	@AfterMethod
	public void resultCheck(ITestResult result){
		if(ITestResult.FAILURE==result.getStatus())
			Utility.captureScreenshot(driver,""+result.getName()+System.currentTimeMillis(), test);
	}	

	@AfterTest
	public void tearDown(){
		rep.endTest(test);
		//rep.flush();
		driver.quit();
		System.out.println("END OF ------------SalesMan Transfer TEST-----------");
	}
}

