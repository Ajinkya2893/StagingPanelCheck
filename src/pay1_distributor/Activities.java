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

/**
 * @author Dev Shah
 */

public class Activities extends BaseClass{

	//Create config object
	Config c=new Config();
	//Create page objects object
	PageObjects p = new PageObjects();

	private WebDriver driver;
	private WebDriverWait w;

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

	@Test(priority=1,enabled=true)
	public void activities()
	{
		test.log(LogStatus.INFO, "Verifying the Activity Link Section");
		driver.findElement(By.linkText(p.activities_link)).click();

		//Verify correct login
		String temp=driver.findElement(By.xpath(p.distinfo_topright_element)).getText();

		String mobile=temp.substring(temp.indexOf("+") + 4, temp.indexOf("+") + 14);

		Assert.assertEquals(mobile, Config.userMobile);

		//Check page breaks
		c.assertFunc(driver);

		try{
			//Verify all left links of activities tab are displayed
			driver.findElement(By.linkText(p.transfer_link)).isDisplayed();
			driver.findElement(By.linkText(p.salesmenList_link)).isDisplayed();
			driver.findElement(By.linkText(p.createSalesman_link)).isDisplayed();
			driver.findElement(By.linkText(p.retList_link)).isDisplayed();
			driver.findElement(By.linkText(p.createRetailer_link)).isDisplayed();
			driver.findElement(By.linkText(p.deletedRetailers_link)).isDisplayed();
			driver.findElement(By.linkText(p.topup_link)).isDisplayed();

		}
		catch(Exception e){
			test.log(LogStatus.FAIL, "Error while displaying activities Left links");
			Assert.fail("Error while displaying activities Left links");
		}
	}

	@Test(priority=2, enabled=false)
	public void sCollections(){

		//Click on salesmen collection list
		test.log(LogStatus.INFO, "Click on salesmen collection list");
		try{
			driver.findElement(By.linkText(p.salesmanCollections_link)).click();
		}
		catch(Exception e){
			test.log(LogStatus.FAIL, "Issue occured while navigating to salesman collections page.");
			Assert.fail("Issue occured while navigating to salesman collections page.");
		}
		//Wait
		w.until(ExpectedConditions.visibilityOfElementLocated(By.id(p.from_datepicker)));

		//Call assert function
		c.assertFunc(driver);

		//Select from date
		try{
			driver.findElement(By.id(p.from_datepicker)).click();

			//Select month
			driver.findElement(By.xpath(p.selectMonth_calendarpicker)).click();
			driver.findElement(By.xpath(p.month_choice)).click();

			//Select date
			driver.findElement(By.xpath(p.fromDate_choice)).click();


			//Select to date process
			driver.findElement(By.id(p.to_datepicker)).click();

			//Select month
			driver.findElement(By.xpath(p.selectMonth_calendarpicker)).click();
			driver.findElement(By.xpath(p.month_choice)).click();

			//Select date
			driver.findElement(By.xpath(p.toDate_choice)).click();

		}
		catch(Exception e){
			test.log(LogStatus.FAIL, "Problem occured while choosing date on Salesman Collection page");
			Assert.fail("Problem occured while choosing date on Salesman Collection page");
		}

		//Click on Search
		driver.findElement(By.id(p.searchButton)).click();

		//wait
		w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(p.totalSalesmanCollection_field)));		

		//Return/display total collection
		try{
			String total=driver.findElement(By.xpath(p.totalSalesmanCollection_field)).getText();

			System.out.println("Total Topup:" +total);
		}
		catch(Exception e){
			test.log(LogStatus.FAIL, "Error occured while getting Salesman total topup value");
			Assert.fail("Error occured while getting Salesman total topup value");
		}
	}

	@Test(priority=3,enabled=true)
	public void sList(){		
		//Activities re-navigation
		try{
			test.log(LogStatus.INFO, "Checking the Salesman List");
			//Navigate
			driver.findElement(By.linkText(p.activities_link)).click();

			//Wait
			w.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(p.salesmenList_link)));

		}
		catch(Exception e){
			test.log(LogStatus.FAIL, "Issue occured while re-navigating to Activities page.");
			Assert.fail("Issue occured while re-navigating to Activities page.");
		}


		try{
			//Click on salesman list
			driver.findElement(By.linkText(p.salesmenList_link)).click();

			//Wait
			w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'"+p.salesmenList_link+"')]")));
		}
		catch(Exception e){
			test.log(LogStatus.FAIL, "Issue occured while navigating to Salesmen List page.");
			Assert.fail("Issue occured while navigating to Salesmen List page.");
		}

		//Check page breaks
		c.assertFunc(driver);

		//Verify Salesmen list page is displayed by checking the header text
		Assert.assertEquals("Salesmen List", driver.findElement(By.xpath("//div[contains(text(),'"+p.salesmenList_link+"')]")).getText());


		/////////////////// ---------     CODE FOR OLD SYSTEM 

		//Display Pending TopUp
		/*String topUp= driver.findElement(By.xpath(p.pendingTopup_element)).getText();
		System.out.println("Pending TopUp"+topUp);*/

		/*try{
		//Edit Salesman
		driver.findElement(By.xpath(".//tr[1]/td[7]/a")).click();

		//Wait
		w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'Edit Salesman')]")));
		}
		catch(Exception e){
			org.testng.Assert.fail("Issue occured while editing salesman.");	
		}

		//Check page breaks
		c.assertFunc(driver);

		//Text should match
		Assert.assertEquals("Edit Salesman", driver.findElement(By.xpath("//div[contains(text(),'Edit Salesman')]")).getText());

		//Variables to store limits
		String initial_limit=driver.findElement(By.id("smTranLimit")).getAttribute("value");
		String newLimit="151";

		try{
		//Change limit to new limit
		driver.findElement(By.id("smTranLimit")).clear();
		driver.findElement(By.id("smTranLimit")).sendKeys(newLimit);

		//Save
		driver.findElement(By.xpath(".//input[@class='retailBut enabledBut']")).click();

		//Wait
		w.until(ExpectedConditions.alertIsPresent());

		//Handle alert
		Alert alert = driver.switchTo().alert();
		alert.accept();
		driver.switchTo().defaultContent();

		//Wait and refresh
		w.until(ExpectedConditions.presenceOfElementLocated(By.linkText("Salesmen List")));

		driver.findElement(By.linkText("Salesmen List")).click();
		driver.navigate().refresh();
		}
		catch(Exception e)
		{
			org.testng.Assert.fail("Issue occured while changing/modifying salesman limit");	
		}

		//Verify new limit is reflecting for the salesman
		String s=driver.findElement(By.xpath(".//tr[1]/td[4]")).getText();
		String s1= s.substring(s.indexOf("(") + 1, s.indexOf(")"));

		s = s.substring(s.indexOf("(") + 1);
		s = s.substring(0, s.indexOf(")"));
		System.out.println(s1);

		Assert.assertEquals(s1, newLimit);

		////REVERT LIMIT BACK

		//Click on salesman list
		driver.findElement(By.linkText("Salesmen List")).click();

		//Wait
		w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'Salesmen List')]")));

		//Edit Salesman
		driver.findElement(By.xpath(".//tr[1]/td[7]/a")).click();		

		//Wait
		w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'Edit Salesman')]")));

		//Check page breaks
		c.assertFunc(driver);

		try{
		//Change limit to new limit
		driver.findElement(By.id("smTranLimit")).clear();
		driver.findElement(By.id("smTranLimit")).sendKeys(initial_limit);

		//Save
		driver.findElement(By.xpath(".//input[@class='retailBut enabledBut']")).click();

		//Wait
		w.until(ExpectedConditions.alertIsPresent());

		//Handle alert
		Alert alert = driver.switchTo().alert();
		alert.accept();
		driver.switchTo().defaultContent();

		//Wait and refresh
		w.until(ExpectedConditions.presenceOfElementLocated(By.linkText("Salesmen List")));
		driver.findElement(By.linkText("Salesmen List")).click();
		driver.navigate().refresh();
		}
		catch(Exception e){
			org.testng.Assert.fail("Error occured while reverting back the salesman limit");	
		}


		//Verify old limit is reflecting for the salesman
		String temp=driver.findElement(By.xpath(".//tr[1]/td[4]")).getText();
		String s2= temp.substring(temp.indexOf("(") + 1, temp.indexOf(")"));

		s = s.substring(s.indexOf("(") + 1);
		s = s.substring(0, s.indexOf(")"));

		Assert.assertEquals(s2, initial_limit);*/

	}

	@SuppressWarnings("deprecation")
	@Test(priority=4,enabled=true)
	public void retailerList(){
		//To-Do

		try{
			test.log(LogStatus.INFO, "Navigating to Retailer List");
			//Click on retailers list link
			driver.findElement(By.linkText(p.retList_link)).click();

			//Wait
			w.until(ExpectedConditions.visibilityOfElementLocated(By.id(p.filter)));	
		}
		catch(Exception e){
			test.log(LogStatus.INFO, "Error occured while navigating to retailers list page");
			Assert.fail("Error occured while navigating to retailers list page");	
		}

		//Check page breaks
		c.assertFunc(driver);

		//Display total retailers count
		System.out.println(driver.findElement(By.id(p.totalRetailers_count)).getText());

		try{
			//Filter
			Select s2=new Select(driver.findElement(By.id(p.filter)));
			s2.selectByVisibleText("Top Transacting in last 7 days");

			//Display retailer count acc to filter
			w.until(ExpectedConditions.textToBePresentInElement(By.id(p.totalRetailers_count),"Top Transacting in last 7 days"));
			System.out.println(driver.findElement(By.id(p.totalRetailers_count)).getText());

			//Select salesman
			Select s=new Select(driver.findElement(By.id(p.salesman_filter_dropdown)));
			s.selectByIndex(1);

			//Select retailer
			Select s1=new Select(driver.findElement(By.id(p.retailer_filter_dropdown)));
			s1.selectByIndex(1);

			//Search
			driver.findElement(By.id(p.searchButton)).click();		
		}
		catch(Exception e)
		{
			test.log(LogStatus.FAIL, "Error occured while enforcing search criteria on retailers limit page");
			Assert.fail("Error occured while enforcing search criteria on retailers limit page");	
		}
		//wait
		w.until(ExpectedConditions.presenceOfElementLocated(By.id(p.totalRetailers_count)));

		//Display retailer count 
		System.out.println(driver.findElement(By.id(p.totalRetailers_count)).getText());

		try{
			//Verify all columns and its headers
			for(int i=1;i<13;i++)
			{
				driver.findElement(By.xpath(".//tr[@id='head']/th["+i+"]")).isDisplayed();
				System.out.println(driver.findElement(By.xpath(".//tr[@id='head']/th["+i+"]")).getText());
			}
		}
		catch(Exception e){
			test.log(LogStatus.FAIL, "Error occured while verifying all columns exist on the retailers list page");
			Assert.fail("Error occured while verifying all columns exist on the retailers list page");	
		}

		//Select dropdown
		Select sb=new Select(driver.findElement(By.id(p.salesman_dropdown)));

		try{
			//Block salesman

			sb.selectByVisibleText("Fully Blocked");

			//Handle alert
			w.until(ExpectedConditions.alertIsPresent());

			//Confirm blocking
			Alert a=driver.switchTo().alert();
			a.accept();

			//Click OK
			w.until(ExpectedConditions.alertIsPresent());
			a.accept();
		}

		catch(Exception e){			
			test.log(LogStatus.FAIL, "Error occured while blocking the salesman.");
			Assert.fail("Error occured while blocking the salesman.");	
		}

		try{
			//Unblock salesman
			sb.selectByVisibleText("None");

			//Handle alert
			w.until(ExpectedConditions.alertIsPresent());

			Alert a1=driver.switchTo().alert();
			a1.accept();

			w.until(ExpectedConditions.alertIsPresent());
			a1.accept();
		}
		catch(Exception e){			
			test.log(LogStatus.FAIL, "Error occured while unblocking the salesman.");
			Assert.fail("Error occured while unblocking the salesman.");	
		}	
	}

	//@Test(priority=5,enabled=true)
	public void deletedRetailerPage(){

		//To-Do
		try{
			test.log(LogStatus.INFO, "Navigating  to Delete Retailer page");
			driver.findElement(By.linkText(p.deletedRetailers_link)).click();
		}
		catch(Exception e){
			test.log(LogStatus.FAIL, "Error occured while navigating to the deleted retailer page");
			Assert.fail("Error occured while navigating to the deleted retailer page");	
		}

		//Check page breaks
		c.assertFunc(driver);

		//Print retailer count
		System.out.println(driver.findElement(By.id(p.totalRetailers_count)).getText());

		//Verify elements are present
		try{
			for(int i=1;i<=10;i++)
			{
				driver.findElement(By.xpath(".//*[@id='head']/th["+i+"]")).isDisplayed();
			}		

			//Verify all options are present
			driver.findElement(By.linkText(p.deletedretailer_edit_link)).isDisplayed();
			driver.findElement(By.linkText(p.deletedretailer_revert_link)).isDisplayed();
			driver.findElement(By.linkText(p.deletedretailer_analyze_link)).isDisplayed();
			driver.findElement(By.linkText(p.deletedretailer_change_link)).isDisplayed();
		}
		catch(Exception e)
		{	
			test.log(LogStatus.FAIL, "Error occured while checking whether all columns and options are present on the deleted retailer page");
			Assert.fail("Error occured while checking whether all columns and options are present on the deleted retailer page");	
		}
	}

	@Test(priority=6, enabled=true)
	public void deleteRevertRetailer(){
		// DELETE RETAILER

		try{
			test.log(LogStatus.INFO, "Deleting the Retailer");
			//Navigate to retailers page
			driver.findElement(By.linkText(p.retList_link)).click();
		}
		catch(Exception e)
		{
			test.log(LogStatus.FAIL, "Error occured while navigating to retailers list");
			Assert.fail("Error occured while navigating to retailers list");		
		}

		//DELETE
		try{

			//Select retailer
			String retailernum=c.retailer.substring(c.retailer.indexOf("-")+1, c.retailer.indexOf("-") + 11);

			Select s1=new Select(driver.findElement(By.id(p.retailer_filter_dropdown)));

			System.out.println(retailernum);
			s1.selectByVisibleText("Test("+retailernum+")");


			//Search
			driver.findElement(By.id(p.searchButton)).click();

			//Delete retailer
			driver.findElement(By.xpath("//*[@id='ret_113065']/td[15]/a")).click();

			//Wait
			w.until(ExpectedConditions.alertIsPresent());

			//Confirm
			Alert a=driver.switchTo().alert();
			a.accept();
			test.log(LogStatus.PASS, "Successfully Deleted the Retailer");
		}
		catch(Exception e)
		{
			test.log(LogStatus.FAIL, "Error occured while deleting retailer");
			Assert.fail("Error occured while deleting retailer");		
		}

		//w.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(".//a[contains(text(),'"+c.retailer_num+"')]")));

		//VERIFY DELETION
		try{
			test.log(LogStatus.INFO, "Verifying the Deleted Retailer");
			driver.findElement(By.linkText(p.transfer_link)).click();
		}catch(Exception e)
		{
			test.log(LogStatus.FAIL, "Error occured while navigating to transfer page");
			Assert.fail("Error occured while navigating to transfer page");		
		}

		//Wait
		w.until(ExpectedConditions.visibilityOfElementLocated(By.id(p.transfer_amount_element)));

		//Type retailer number
		driver.findElement(By.id(p.transfer_number_element)).sendKeys(c.retailer_num);

		//Verify the dropdown doesn't show the retailer
		Assert.assertTrue(driver.findElements(By.linkText(c.retailer)).isEmpty(),"Retailer still exists");

		System.out.println("Retailer deleted successfully");
		test.log(LogStatus.PASS, "Retailer deleted successfully");

		//REVERT
		try{
			test.log(LogStatus.INFO, "Reverting the Deleted Retailer");
			//Navigate to deleted page 
			driver.findElement(By.linkText(p.deletedRetailers_link)).click();

			//Revert deletion
			driver.findElement(By.xpath("//*[@id='ret_113065']/td[12]/a")).click();
			///html/body/div[2]/div[3]/div/div[2]/div/div[2]/fieldset/table/tbody/tr/td[12]/a
			//wait
			w.until(ExpectedConditions.alertIsPresent());

			//Accept popup
			Alert a=driver.switchTo().alert();
			a.accept();

			//Switch to default content
			driver.switchTo().defaultContent();

			w.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(".//a[contains(text(),'"+c.retailer_num+"')]")));
			test.log(LogStatus.PASS, "Successfully reverted the Deleted Retailer");
		}
		catch(Exception e)
		{
			test.log(LogStatus.FAIL, "Revert unsuccessful!");
			Assert.fail("Revert unsuccessful!");	
		}
		driver.findElement(By.linkText(p.transfer_link)).click();

		//Wait
		w.until(ExpectedConditions.visibilityOfElementLocated(By.id(p.transfer_amount_element)));

		//Type retailer number
		driver.findElement(By.id("shop1")).sendKeys(c.retailer_num);

		try{
			driver.findElement(By.linkText(c.retailer));
			test.log(LogStatus.PASS, "Retailer reverted successfully");
			System.out.println("Retailer reverted successfully");
		}catch(Exception e){
			test.log(LogStatus.FAIL, "Retailer doesn't exist.. Revert unsuccessful!");
			Assert.fail("Retailer doesn't exist.. Revert unsuccessful!");
		}

	}

	@Test(priority=7,enabled=true)
	public void topUpRequest(){
		try{
			test.log(LogStatus.INFO, "Verifying the Activity Links");
			driver.findElement(By.linkText(p.activities_link)).click();

			//To-Do
			driver.findElement(By.linkText(p.topup_link)).click();

			//Wait for elements
			w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(p.topuprequest_header)));
		}
		catch(Exception e){
			test.log(LogStatus.FAIL, "Error occured while navigating to the topup request page");
			Assert.fail("Error occured while navigating to the topup request page");	
		}

		//Check page breaks
		c.assertFunc(driver);

		//Verify all elements are displayed
		try{
			test.log(LogStatus.INFO, "Verifying all the Link displayed ");
			driver.findElement(By.xpath(p.topuprequest_bankacc_label)).isDisplayed();
			driver.findElement(By.xpath(p.topuprequest_transfertype_label)).isDisplayed();
			driver.findElement(By.xpath(p.topuprequest_amount_label)).isDisplayed();
			driver.findElement(By.xpath(p.topuprequest_branch_label)).isDisplayed();
			driver.findElement(By.xpath(p.topuprequest_branchcode_label)).isDisplayed();
			driver.findElement(By.xpath(p.topuprequest_id_label)).isDisplayed();
			driver.findElement(By.xpath(p.topuprequest_slip_label)).isDisplayed();
			driver.findElement(By.id(p.topuprequest_send_button)).isDisplayed();
		}
		catch(Exception e){
			test.log(LogStatus.FAIL, "Error occured while verifying whether all elements are present or not");
			Assert.fail("Error occured while verifying whether all elements are present or not");		
		}

		//Send topup request

		try{
			test.log(LogStatus.INFO, "Entering the Amount details");
			//Select bank
			Select s=new Select(driver.findElement(By.id(p.topuprequest_bankacc_field)));
			s.selectByVisibleText("ICICI Bank:6714");

			//Select mode
			Select s1=new Select(driver.findElement(By.id(p.topuprequest_transfertype_field)));
			s1.selectByVisibleText("NEFT/RTGS");

			//Type amount
			driver.findElement(By.xpath(p.topuprequest_amount_field)).sendKeys("10");

			//Type trans id
			driver.findElement(By.id(p.topuprequest_id_field)).sendKeys("Testing");

			//Type branch code
			driver.findElement(By.id(p.topuprequest_branchcode_field)).sendKeys("Testing");

			//Type branch name
			driver.findElement(By.id(p.topuprequest_branch_field)).sendKeys("Testing");

			//Submit
			driver.findElement(By.id(p.topuprequest_send_button)).click();

			test.log(LogStatus.PASS, "Successfully Transfered the Amount");
			w.until(ExpectedConditions.invisibilityOfElementLocated(By.id(p.topuprequest_bankacc_field)));
		}
		catch(Exception e){
			test.log(LogStatus.FAIL, "Error occured while sending/submitting a topup request page");
			Assert.fail("Error occured while sending/submitting a topup request page");	
		}
	}

	@Test(priority=8,enabled=true)
	public void createRetailer(){

		try{
			//Navigate to create retailer
			driver.findElement(By.linkText(p.createRetailer_link)).click();
		}
		catch(Exception e){
			test.log(LogStatus.FAIL, "Error occured while navigating to the CREATE RETAILER page");
			Assert.fail("Error occured while navigating to the CREATE RETAILER page");	
		}

		//Check page breaks
		c.assertFunc(driver);

		try{
			test.log(LogStatus.INFO, "Entering the Retailer Name And details");
			//Send mobile number
			driver.findElement(By.id(p.createretailer_mobile)).sendKeys(Config.create_retailer_salesman);

			//Type shop name
			driver.findElement(By.id(p.createretailer_shopname)).sendKeys("Test - One");

			//Submit
			driver.findElement(By.id(p.searchButton)).click();

			Utility.takeScreenShot(driver, "Tried to Create Retailer", test);
			//Wait
			//w.until(ExpectedConditions.visibilityOfElementLocated(By.id(p.createretailer_otp)));
		}
		catch(Exception e)
		{
			test.log(LogStatus.FAIL, "Error occured while creating a new retailer request");
			Assert.fail("Error occured while creating a new retailer request");	
		}

		/*try{
			test.log(LogStatus.INFO, "Entering the Otp Details");
			//Enter false otp
			driver.findElement(By.id(p.createretailer_otp)).sendKeys("123456");

			//Check number
			Assert.assertTrue(driver.findElement(By.xpath(p.createretailer_confirmnumber)).getText().contains(Config.create_retailer_salesman));

			//CONFIRm
			driver.findElement(By.id(p.searchButton)).click();
			test.log(LogStatus.PASS, "Clicked on Search Button");

			w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(p.flashmessage)));
		}
		catch(Exception e)
		{
			test.log(LogStatus.FAIL, "Error occured while confirming OTP and retailer number");
			Assert.fail("Error occured while confirming OTP and retailer number");	
		}

		Assert.assertEquals(driver.findElement(By.xpath(p.flashmessage)).getText(), "Please enter correct OTP.");*/	
	}

	@Test(priority=9,enabled=true)
	public void createSalesman(){

		try{
			//Navigate to create retailer
			driver.findElement(By.linkText(p.createSalesman_link)).click();
		}
		catch(Exception e){
			test.log(LogStatus.FAIL, "Error occured while navigating to the CREATE RETAILER page");
			Assert.fail("Error occured while navigating to the CREATE RETAILER page");	
		}

		//Check page breaks
		c.assertFunc(driver);

		try{
			test.log(LogStatus.INFO, "Creating a SalesMan");
			//Write username
			driver.findElement(By.id(p.createsalesman_name)).sendKeys("Test Salesman 1");

			//Send mobile number
			driver.findElement(By.xpath(p.createsalesman_number)).sendKeys("7101000990");

			//Set limit
			//driver.findElement(By.xpath(".//*[@id='mobile'][@name='data[Salesman][tran_limit]']")).sendKeys("100");

			//Type shop name
			driver.findElement(By.id(p.createsalesman_address)).sendKeys("XYZ");
			test.log(LogStatus.INFO, "Entered the Salesman Details");
			
			//Submit
			driver.findElement(By.id(p.searchButton)).click();
			test.log(LogStatus.INFO, "Clicked on Search Button");
			
			//Wait
			w.until(ExpectedConditions.visibilityOfElementLocated(By.id(p.createretailer_otp)));
		}
		catch(Exception e){
			test.log(LogStatus.FAIL, "Error occured while creating a new salesman request");
			Assert.fail("Error occured while creating a new salesman request");	
		}

		try{
			//Enter false otp
			driver.findElement(By.id(p.createretailer_otp)).sendKeys("123456");
			test.log(LogStatus.INFO, "Entering the OTP Number");

			//Check number
			Assert.assertTrue(driver.findElement(By.xpath(p.createsalesman_confirmnumber)).getText().contains(Config.create_retailer_salesman));

			//CONFIRm
			driver.findElement(By.id(p.searchButton)).click();
			test.log(LogStatus.INFO, "Clicked on Created Button");
			
			w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(p.flashmessage)));
		}
		catch(Exception e)
		{
			test.log(LogStatus.FAIL, "Error occured while confirming OTP and retailer number");
			Assert.fail("Error occured while confirming OTP and retailer number");	
		}
	}

	@AfterMethod
	public void resultCheck(ITestResult result){
		if(ITestResult.FAILURE==result.getStatus())
			Utility.captureScreenshot(driver,""+result.getName(), test);
	}

	@AfterTest
	public void tearDown(){
		rep.endTest(test);
		//rep.flush();
		driver.quit();
		System.out.println("END OF ------------Activites TEST-----------");
	}
}