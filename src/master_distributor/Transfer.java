package master_distributor;

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
import lib.Functions;
import lib.Utility;

public class Transfer extends BaseClass{

	//Variable declaration
	String opening;
	String closing;
	String temp;
	String temp_txn_id;
	String before;
	String dist_name;

	//Create config object
	Config c=new Config();
	Functions f=new Functions();

	//Create page objects object
	PageObjects_MD p = new PageObjects_MD();

	private WebDriver driver;
	private WebDriverWait w;

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

	@Test(priority=0, enabled=true)
	public void getdistbalance(){

		//Fetch page
		driver.get(Config.baseUrl+"/shops/allDistributor");
		test.log(LogStatus.INFO, "Verfying the Distributor Balance");
		//Find element
		opening = driver.findElement(By.xpath(".//table/tbody/tr/td[2][contains(text(),'"+c.distributor_id+"')]/following::td[9]")).getText();
		dist_name = driver.findElement(By.xpath(".//table/tbody/tr/td[2][contains(text(),'"+c.distributor_id+"')]/following::td[2]")).getText();
		//Call search function
		p.search_distributor(driver, dist_name);

		//Get topup buy
		before = driver.findElement(By.xpath(p.topup_buy)).getText();
		test.log(LogStatus.INFO, "Distributor Wallet Balance "+before+" before transfer");

	}

	@Test(priority=1, enabled=true)
	public void transfer() throws InterruptedException
	{
		test.log(LogStatus.INFO, "Transfering the Amount to Distributor");

		temp = driver.findElement(By.xpath(p.user_balance_element)).getText();

		driver.findElement(By.linkText(p.activities_link)).click();
		driver.findElement(By.linkText(p.transfer_link)).click();

		//Wait and type amount
		w.until(ExpectedConditions.visibilityOfElementLocated(By.id(p.amount_element)));
		driver.findElement(By.id(p.amount_element)).sendKeys(c.amount1);

		//Enter space in the field to get auto select options
		driver.findElement(By.id(p.transfer_number_element)).clear();
		driver.findElement(By.id(p.transfer_number_element)).sendKeys(c.distributor_id);

		//Wait
		w.until(ExpectedConditions.visibilityOfElementLocated(By.partialLinkText(c.distributor_id)));

		//Select option
		driver.findElement(By.partialLinkText(c.distributor_id)).click();

		//Wait for txn's to load
		w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(p.lasttransactions)));

		//Select radio button 
		driver.findElement(By.xpath("//div[@class='fieldLabelSpace1']/input[@id='typeRadio'][1]")).click();

		//Select Bank Name
		f.handleDropdown(driver, p.bank_field, "internal");

		int random = (int)(Math.random() * 10000 + 1);

		//Send TXN ID
		driver.findElement(By.id(p.txn_id)).sendKeys(""+random);

		//Submit request
		driver.findElement(By.id(p.searchButton)).click();

		Assert.assertTrue(driver.findElements(By.xpath(p.flashmessage)).isEmpty());

		//Wait for confirmation message (MD LOGIN)
		w.until(ExpectedConditions.visibilityOfElementLocated(By.id(p.confirm_pwd_field)));
		test.log(LogStatus.INFO, "Transfering the amount");
		
		//Confirm amt MD LOGIN
		driver.findElement(By.id(p.amount_element)).sendKeys(c.amount1);

		//Confirm password for MD LOGIN
		driver.findElement(By.id(p.confirm_pwd_field)).sendKeys("189818");

		//Confirm 
		driver.findElement(By.id(p.confirmtransaction_button)).click();
		
		//Wait
		w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(p.flashmessage)));							

		//GET TRANSACTION ID
		String a=driver.findElement(By.xpath(p.flashmessage)).getText();
		test.log(LogStatus.PASS, "Transfered the amount");
		
		temp_txn_id=a.substring(60);
	}

	@Test(priority=2, enabled=true)
	public void checkDist_balance()
	{
		test.log(LogStatus.INFO, "Verifying the Distributor Balance");
		//Fetch page
		driver.get(Config.baseUrl+"/shops/allDistributor");

		//Find element
		closing = driver.findElement(By.xpath(".//table/tbody/tr/td[2][contains(text(),'"+c.distributor_id+"')]/following::td[11]")).getText();

		if(opening.equals(closing))
			test.log(LogStatus.FAIL, "Dist balance not updated post transfer");
		else
			test.log(LogStatus.PASS, "Balance got updated post transfer");
		Assert.assertFalse(opening.equals(closing), "Dist balance not updated post transfer");

	}

	@Test(priority=3, enabled=true)
	public void lastTransactions(){

		test.log(LogStatus.INFO, "Verifying the Retailer Balance Before Transaction");
		//Balance transfer
		driver.get(Config.baseUrl+"/shops/transfer");
		//driver.findElement(By.linkText("Balance Transfer")).click();

		//Type retailer to get last transactions
		driver.findElement(By.id(p.transfer_number_element)).sendKeys(c.distributor_id);

		//WAIT
		w.until(ExpectedConditions.visibilityOfElementLocated(By.partialLinkText(c.distributor_id)));

		//Click on retailer
		driver.findElement(By.partialLinkText(c.distributor_id)).click();

		w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(p.lasttransactions_latesttxn)));

		//get elements
		String temp = driver.findElement(By.xpath(p.lasttransactions_latesttxn)).getText();

		try{

			//True condition
			Assert.assertTrue(temp.equalsIgnoreCase(temp_txn_id));
			System.out.println("Transaction exists:-"+temp+" "+temp_txn_id);
			test.log(LogStatus.PASS, "Transaction exists:-"+temp+" "+temp_txn_id);
		}

		catch(Exception e)
		{
			test.log(LogStatus.FAIL, "Transfer entry not present in the retailer's recent transactions.");
			Assert.fail("Transfer entry not present in the retailer's recent transactions.");
		}
	}

	@Test(priority=4, enabled=true)
	//BEFORE PULLBACK
	public void mainReport(){

		test.log(LogStatus.INFO, "Checking the Main Report Values");
		//Call dist search function
		p.search_distributor(driver, dist_name);

		//Get topup value
		String after = driver.findElement(By.xpath(p.topup_buy)).getText();

		double after1 = Double.parseDouble(after);
		double amt = Double.parseDouble(c.amount1);
		double before1 = Double.parseDouble(before);

		if(after1 == before1 + amt)
		{
			test.log(LogStatus.PASS, "Main Report displaying values for topup sold/day correctly");
			System.out.println("Main Report displaying values for topup sold/day correctly");
		}
		else{
			test.log(LogStatus.FAIL, "Main report not correctly displaying the top up buy/day as per latest transaction");
			Assert.fail("Main report not correctly displaying the top up buy/day as per latest transaction");
		}
	}

	@Test(priority=5, enabled=true)
	public void pullback(){

		test.log(LogStatus.INFO, "Navigating to PullBack Page");
		//PULLBACK FLOW
		try{
			//Navigate to reports
			driver.findElement(By.linkText(p.reports_link)).click();

			//Wait
			w.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(p.balancereport_link)));

			//Go to salesman report
			driver.findElement(By.linkText(p.balancereport_link)).click();
		}
		catch(Exception e)
		{
			test.log(LogStatus.FAIL, "Error occured while navigating to REPORTS tab");
			Assert.fail("Error occured while navigating to REPORTS tab");			
		}

		//Pullback
		try{
			test.log(LogStatus.INFO, "Clicking on PullBack Button");
			w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//td[contains(text(),'"+temp_txn_id+"')]/following::td[8]/a")));
			driver.findElement(By.xpath("//td[contains(text(),'"+temp_txn_id+"')]/following::td[8]/a")).click();

			//Call alert function
			f.handleAlert(driver);
			f.handleAlert(driver);

			//Refresh page
			driver.navigate().refresh();
		}
		catch(Exception e)
		{
			test.log(LogStatus.FAIL, "Error occured while carrying out pullback request");
			Assert.fail("Error occured while carrying out pullback request");			
		}

		String temp1=driver.findElement(By.xpath(p.user_balance_element)).getText();

		//Check balance consistency and whether is back to the same balance
		Assert.assertEquals(temp1, temp);
		test.log(LogStatus.PASS, "Balance is intact, transaction successful");
		System.out.println("Balance is intact, transaction successful");
	}

	@Test(priority=6, enabled=true, dependsOnMethods={"pullback"})
	public void accHistory()
	{
		try{
			test.log(LogStatus.INFO, "Verifying the account History After PullBack");
			//Navigate to acc history page
			driver.findElement(By.linkText(p.acchistory_link)).click();
		}
		catch(Exception e){
			test.log(LogStatus.FAIL, "Error while navigating to the page");
			Assert.fail("Error while navigating to the page");
		}

		//SET TODAY'S DATE
		try{
			//Call date set function
			p.pickDates(driver, p.todaydate_datepicker, p.todaydate_datepicker);
		}
		catch(Exception e1)
		{
			test.log(LogStatus.FAIL, "Error while searching for transaction");
			Assert.fail("Error while searching for transaction");
		}

		//CONFIRM ENTRY
		try{
			test.log(LogStatus.INFO, "Verifying the amount sent");
			//Check transaction entry
			driver.findElement(By.xpath(".//td[contains(text(),'"+temp_txn_id+"')]/following::td[1][contains(text(),'Topup')]")).isDisplayed();

			//Check pullback entry
			driver.findElement(By.xpath(".//td[contains(text(),'Pullback')][contains(text(),'"+temp_txn_id+"')]/following::td[2][contains(text(),'"+c.amount+"')]")).isDisplayed();

			//Compare opening and closing balance

			//Get opening
			String opening = driver.findElement(By.xpath(".//td[contains(text(),'"+temp_txn_id+"')]/following::td[1][contains(text(),'Topup')]/following::td[3]")).getText();
			test.log(LogStatus.INFO, "Opening balance is "+opening);

			//Get closing
			String closing1 = driver.findElement(By.xpath("//td[contains(text(),'Pullback')][contains(text(),'"+temp_txn_id+"')]/following::td[4]")).getText();
			test.log(LogStatus.INFO, "Closing balance is "+closing1);

			//Check consistency in balance
			Assert.assertEquals(opening, closing1, "Discrepancy in matching/asserting balance post pullback");
			test.log(LogStatus.PASS, "No Discrepancy in matching/asserting balance post pullback");
		}
		catch(Exception e)
		{
			test.log(LogStatus.FAIL, "Error while confirming pullback transaction");
			Assert.fail("Error while confirming pullback transaction");
		}
	}

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