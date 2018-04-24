package pay1_distributor;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
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


public class Transfer extends BaseClass{

	private WebDriver driver;
	private WebDriverWait w;

	String temp;
	String ret_bal;
	String post_ret_bal;
	String temp_txn_id;
	String temp_txn_id_new;
	boolean system_used;

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

	@Test(priority=0,enabled=true)
	public void getRetailerBalance(){
		try{
			//Click on retailers list link
			driver.findElement(By.linkText(p.retList_link)).click();

			//Wait
			w.until(ExpectedConditions.visibilityOfElementLocated(By.id(p.filter)));	
		}
		catch(Exception e){
			test.log(LogStatus.FAIL, "Error occured while navigating to retailers list page");
			Assert.fail("Error occured while navigating to retailers list page");	
		}

		//Assert page breaks, Calls assert function
		c.assertFunc(driver);

		try{
			//Select retailer
			String retailernum=c.retailer.substring(c.retailer.indexOf("-")+1, c.retailer.indexOf("-") + 11);

			Select s1=new Select(driver.findElement(By.id(p.retailer_filter_dropdown)));

			System.out.println(retailernum);
			s1.selectByVisibleText("Test("+retailernum+")");

			//Search
			driver.findElement(By.id(p.searchButton)).click();
		}
		catch(Exception e)
		{
			Assert.fail("Error occured while enforcing search criteria on retailers limit page");	
		}
		//Confirm balance
		try{
			ret_bal=driver.findElement(By.xpath(p.retlist_balance_element)).getText();
			System.out.println(ret_bal);
		}
		catch(Exception e){
			test.log(LogStatus.FAIL, "Error occured while extracting retailer balance");
			Assert.fail("Error occured while extracting retailer balance");
		}
	}

	@Test(priority=1,enabled=true)
	public void transfer(){

		//Get distributor balance
		temp=driver.findElement(By.xpath(p.user_balance_element)).getText();

		try{
			test.log(LogStatus.INFO, "Transfering the amount");
			//Navigate to balance transfer
			driver.findElement(By.linkText(p.transfer_link)).click();

			//Wait
			w.until(ExpectedConditions.visibilityOfElementLocated(By.id(p.transfer_amount_element)));

		}
		catch(Exception e){
			org.testng.Assert.fail("Issue occured while navigating to Balance Transfer page.");
		}

		try{
			//Enter amount
			driver.findElement(By.id(p.transfer_amount_element)).sendKeys(c.amount);

			//Enter retailer
			driver.findElement(By.id(p.transfer_number_element)).sendKeys(c.retailer);
			w.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(c.retailer)));

			driver.findElement(By.linkText(c.retailer)).click();

			//Select option 1-Cash, 2-NEFT,	3-ATM,	4-Cheque
			driver.findElement(By.xpath("//*[@id='typeRadio'][@value="+c.txnType+"]")).click();

			//Txn ID//*[@id="typeRadio"] 
			driver.findElement(By.id(p.txn_id)).sendKeys(c.txnid);
		}

		catch(Exception e)
		{
			Assert.fail("Issue occured while entering balance transfer details");	
		}

		try{
			//Confirm last transactions is displayed
			driver.findElement(By.xpath(p.lasttransactions)).isDisplayed();
		}
		catch(Exception e){
			Assert.fail("Issue occured while viewing last transactions element");	

		}
		if(!driver.findElements(By.xpath(p.lasttransactions_header)).isEmpty())
		{
			Assert.assertTrue(!driver.findElements(By.xpath(p.lasttransactions_rows)).isEmpty());
		}
		else{	
			//To do
			Assert.assertTrue(driver.findElements(By.xpath(p.lasttransactions_rows)).isEmpty());
		}

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
		//re enter amount
		try{
			driver.findElement(By.id(p.confirmamount_field)).sendKeys(c.amount);
			//amount 

			//Confirm
			driver.findElement(By.id(p.confirmtransaction_button)).click();

			//Messages
			w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(p.flashmessage)));
			test.log(LogStatus.PASS, "CONFIRMING balance transfer request");
		}
		catch(Exception e){
			test.log(LogStatus.FAIL, "Error occured while CONFIRMING balance transfer request");
			Assert.fail("Error occured while CONFIRMING balance transfer request");		
		}

		//GET TRANSACTION ID

		String a=driver.findElement(By.xpath(p.flashmessage)).getText();
		temp_txn_id=a.substring(60);
		test.log(LogStatus.PASS, "Successfully sent the Amount");
		// Fetch actual ID
		driver.findElement(By.linkText(p.reports_link)).click();
		driver.findElement(By.linkText(p.salesmanreport_link)).click();

		//  
		String x=driver.findElement(By.xpath("//td[contains(text(),'"+temp_txn_id+"')]")).getText();
		temp_txn_id_new=x.substring(0, x.indexOf("/")-1);
	}

	@Test(priority=2,enabled=true)
	public void retailerBalanceCheck(){
		//Confirm retailer balance
		try{
			test.log(LogStatus.INFO, "Navigating to Retailers List Page");
			driver.navigate().to("https://panelstaging.pay1.in/shops/allRetailer");
			//Wait
			w.until(ExpectedConditions.visibilityOfElementLocated(By.id(p.filter)));	
		}
		catch(Exception e) {
			Assert.fail("Error occured while navigating to retailers list page");
		}

		try{
			String retailernum=c.retailer.substring(c.retailer.indexOf("-")+1, c.retailer.indexOf("-") + 11);

			Select s1=new Select(driver.findElement(By.id(p.retailer_filter_dropdown)));
			s1.selectByVisibleText("Test("+retailernum+")");

			//Search
			driver.findElement(By.id(p.searchButton)).click();
			test.log(LogStatus.INFO, "Successfully searched the Retailer");
		}
		catch(Exception e)
		{
			test.log(LogStatus.FAIL, "Error occured while enforcing search criteria on retailers limit page");
			Assert.fail("Error occured while enforcing search criteria on retailers limit page");	
		}
		//Confirm balance
		try{
			post_ret_bal=driver.findElement(By.xpath(p.retlist_balance_element)).getText();

			Assert.assertFalse(ret_bal.equals(post_ret_bal));
			test.log(LogStatus.PASS, "Confirmed the Amount has been Transfered");
		}
		catch(Exception e){
			test.log(LogStatus.FAIL, "Issue in balance transfer ");
			Assert.fail("Issue in balance transfer ");
			System.out.println("Issue in balance transfer ");
		}
	}	
	
	//@Test(priority=3, enabled=true)
	public void checkRequestOnPanel()
	{
		String MainWindowHandle = driver.getWindowHandle();

		checkPanel();

		//SWITCH BACK
		driver.switchTo().window(MainWindowHandle);	
	}

	@Test(priority=4, enabled=true)
	public void lastTransactions(){

		//Balance transfer
		driver.get(Config.baseUrl+"/shops/transfer");
		//driver.findElement(By.linkText("Balance Transfer")).click();
		test.log(LogStatus.INFO, "Verifying the Last made transaction amount");

		if(driver.findElements(By.id(p.newsys_transfer_dropdown)).isEmpty())//Old system
			system_used=true;
		else//New system
			system_used=false;

		//Type retailer to get last transactions
		driver.findElement(By.id(p.transfer_number_element)).sendKeys(c.retailer);

		//Click on retailer
		driver.findElement(By.linkText(c.retailer)).click();

		//get elements
		String temp =driver.findElement(By.xpath(p.lasttransactions_latesttxn)).getText();

		if(system_used) {
			try{
				//True condition
				Assert.assertTrue(temp.equalsIgnoreCase(temp_txn_id));
				test.log(LogStatus.PASS, "Transaction exists:-"+temp+" "+temp_txn_id);
				System.out.println("Transaction exists:-"+temp+" "+temp_txn_id);
			}
			catch(Exception e)
			{
				test.log(LogStatus.FAIL, "Transfer entry not present in the retailer's recent transactions.");
				Assert.fail("Transfer entry not present in the retailer's recent transactions.");
			}
		}
		else{
			try{
				//True condition
				Assert.assertTrue(temp.equalsIgnoreCase(temp_txn_id_new));
				test.log(LogStatus.PASS, "Transaction exists:-"+temp+" "+temp_txn_id_new);
				System.out.println("Transaction exists:-"+temp+" "+temp_txn_id_new);
			}
			catch(Exception e)
			{
				test.log(LogStatus.FAIL, "Transfer entry not present in the retailer's recent transactions");
				Assert.fail("Transfer entry not present in the retailer's recent transactions.");
			}
		}
	}			 

	@Test(priority=5,enabled=true)
	//BEFORE PULLBACK
	public void mainReport(){
		test.log(LogStatus.INFO, "Verifying the Sent amount before pulling back");
		driver.get(Config.baseUrl+"/shops/mainReport/1");

		String topup_sold_day= driver.findElement(By.xpath(p.topup_sold)).getText();

		if(c.amount.equals(topup_sold_day))
		{
			test.log(LogStatus.PASS, "Main Report displaying values for topup sold/day correctly");
			System.out.println("Main Report displaying values for topup sold/day correctly");
		}
		else{
			test.log(LogStatus.FAIL, "Main report not correctly displaying the top up sold/day as per latest transaction");
			Assert.fail("Main report not correctly displaying the top up sold/day as per latest transaction");
		}

		String topup_unique=driver.findElement(By.xpath(p.topup_unique)).getText();
		if(!topup_unique.equals("0"))
		{
			test.log(LogStatus.PASS, "Main Report displaying values for unique topups correctly");
			System.out.println("Main Report displaying values for unique topups correctly");
		}
		else{
			test.log(LogStatus.FAIL, "Main report fails to correctly displaying the unique topup count as per latest transaction");
			Assert.fail("Main report fails to correctly displaying the unique topup count as per latest transaction");
		}
	}

	@Test(priority=6,enabled=true)
	public void pullback(){
		//PULLBACK FLOW
		try{
			test.log(LogStatus.INFO, "Pulling Back the Amount Sent");

			//Navigate to reports
			driver.findElement(By.linkText(p.reports_link)).click();

			//Wait
			w.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(p.salesmanreport_link)));
			test.log(LogStatus.INFO, "Navigated to Salesman Report section");
			//Go to salesman report
			driver.findElement(By.linkText(p.salesmanreport_link)).click();
		}
		catch(Exception e)
		{
			test.log(LogStatus.FAIL, "Error occured while navigating to REPORTS tab");
			Assert.fail("Error occured while navigating to REPORTS tab");			
		}
		driver.findElement(By.xpath("//td[contains(text(),'"+temp_txn_id+"')]/following::td[12]/a")).click();
		test.log(LogStatus.PASS, "Clicked on Pullback Button");
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

		String temp1=driver.findElement(By.xpath(p.user_balance_element)).getText();

		//Check balance consistency and whether is back to the same balance
		Assert.assertEquals(temp1, temp);
		test.log(LogStatus.PASS, "Balance is intact, transaction successful");
		System.out.println("Balance is intact, transaction successful");
	}

	@Test(priority=7, enabled=true , dependsOnMethods = {"pullback"})
	public void accHistory()
	{
		try{
			test.log(LogStatus.INFO, "Navigating to MainReport to check the PullBack");
			driver.navigate().to("https://panelstaging.pay1.in/shops/mainReport");
			//Navigate to acc history page
			driver.findElement(By.linkText(p.acchistory_link)).click();
		}
		catch(Exception e){
			test.log(LogStatus.FAIL, "Error while navigating to the page");
			Assert.fail("Error while navigating to the page");
		}

		//SET TODAY'S DATE
		try{
			test.log(LogStatus.INFO, "Searching for Transaction");
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

		}
		catch(Exception e1)
		{
			test.log(LogStatus.INFO, "Error while searching for transaction");
			Assert.fail("Error while searching for transaction");
		}
		//CONFIRM ENTRY
		try{
			test.log(LogStatus.INFO, "Verifying the Pullback Money");
			//Check transaction entry
			driver.findElement(By.xpath(".//td[contains(text(),'"+temp_txn_id+"')]/following::td[1][contains(text(),'Topup')]")).isDisplayed();

			//Check pullback entry
			driver.findElement(By.xpath(".//td[contains(text(),'Pullback')][contains(text(),'"+temp_txn_id+"')]/following::td[2][contains(text(),'"+c.amount+"')]")).isDisplayed();

			//Compare opening and closing balance

			//Get opening
			String opening=driver.findElement(By.xpath(".//td[contains(text(),'"+temp_txn_id+"')]/following::td[1][contains(text(),'Topup')]/following::td[3]")).getText();

			//Get closing
			String closing1=driver.findElement(By.xpath("//td[contains(text(),'Pullback')][contains(text(),'"+temp_txn_id+"')]/following::td[4]")).getText();

			//Check consistency in balancetem
			Assert.assertEquals(opening, closing1, "Discrepancy in matching/asserting balance post pullback");
			test.log(LogStatus.PASS, "Discrepancy in matching/asserting balance post pullback");
		}
		catch(Exception e)
		{
			test.log(LogStatus.FAIL," Error while confirming pullback transaction");
			Assert.fail("Error while confirming pullback transaction");
		}
	}

	@AfterMethod
	public void resultCheck(ITestResult result){
		if(ITestResult.FAILURE==result.getStatus())
			Utility.captureScreenshot(driver,""+result.getName()+System.currentTimeMillis(), test);
		//DO NOTHING
	}

	public void checkPanel()
	{
		//NEW WINDOW
		WebDriver driver1=new FirefoxDriver();
		//test.log(Status.PASS, "Open new window");

		//Navigate
		driver1.get(Config.baseUrl);
		//test.log(Status.PASS, "Navigate to cc");

		//Login
		c.panelLogin(driver1, "Super Distributor");

		WebDriverWait w=new WebDriverWait(driver1, 30);
		w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(p.panel_search_element)));

		//Click on search
		driver1.findElement(By.xpath(p.panel_search_element)).click();

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try{
			JavascriptExecutor js=(JavascriptExecutor)driver1;
			js.executeScript("scroll(0,300)");


			//Enter retailer number
			driver1.findElement(By.id(p.panel_number_element)).sendKeys(c.retailer_num);
			driver1.findElement(By.id(p.panel_number_element)).sendKeys(Keys.ENTER);

			//Scroll to element
			js.executeScript("scroll(0,450)");

			//Print timestamp of transfer
			driver1.findElement(By.xpath("//*[contains(text(),'Amount transferred')]/following::table[1]//tr[2]/td[2]")).isDisplayed();
			System.out.println(driver1.findElement(By.xpath("//*[contains(text(),'Amount transferred')]/following::table[1]//tr[2]/td[3]")).getText());

			String x=driver1.findElement(By.xpath("//th[contains(text(),'Balance')]/following::td[1]")).getText();
			x=x.substring(0, x.indexOf("."));
			//Confirm balance
			Assert.assertEquals(post_ret_bal, x);

			driver1.quit();
		}
		catch(Exception e)
		{
			driver1.quit();
		}
	}

	@AfterTest
	public void tearDown(){
		rep.endTest(test);
		//rep.flush();
		driver.quit();
	}
}
