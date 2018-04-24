package pay1_distributor;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.SkipException;
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
public class MyProfile extends BaseClass{

	private WebDriver driver;
	private WebDriverWait w;	

	Config c =new Config();
	//Create page objects object
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
	public void myProfile(){

		try{
			test.log(LogStatus.INFO, "Navigating to Profile Link");
			//CLick on my profile link
			driver.findElement(By.linkText(p.profile_link)).click();

			//Wait object
			w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(p.myprofile_password_label)));
		}
		catch(Exception e){
			e.printStackTrace();
			test.log(LogStatus.FAIL, "Exception occured while navigating to My Profile page");
			Assert.fail("Exception occured while navigating to My Profile page");	
		}

		//Verify page opens fine
		c.assertFunc(driver);

		//Get all text fields for password
		List<WebElement> l=driver.findElements(By.xpath("//label[contains(text(),'password')]"));

		//Print all fields name and verify if three fields exist.
		/*for(WebElement x:l)
		{
		System.out.println(x.getText());	
		}*/
		//Assert 3 fields exist on the page
		Assert.assertTrue(l.size()==3, "Three fields not present on myProfile page");

	}

	@Test(priority=2,enabled=true)
	public void changePassword(){

		try{
			test.log(LogStatus.INFO, "Changing the profile Password");
			driver.findElement(By.id(p.myprofile_password_field1)).sendKeys(Config.password);

			driver.findElement(By.id(p.myprofile_password_field2)).sendKeys("zxcv1234");
			driver.findElement(By.id(p.myprofile_password_field3)).sendKeys("zxcv1234");

			driver.findElement(By.xpath(p.myprofile_submit_button)).click();
			test.log(LogStatus.PASS, "Changed the Password Successfully");
		}
		catch(Exception e){
			test.log(LogStatus.FAIL, "Exception occured while changing password");
			Assert.fail("Exception occured while changing password");			
		}
		//to do
		//Wait for confirmation of password change

		/*    	//Verify no error message exists
    	try{
    	Assert.assertTrue(driver.findElements(By.id("err_pname")).isEmpty());
    	}
    	catch(Exception e){
    	throw new SkipException("Error while changing password");
    	}
		 */
		/*    	
    	try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

		//Verify success message is present
		Assert.assertTrue(driver.findElement(By.xpath(".//*[@id='innerDiv'][contains(text(),'Password changed successfully')]")).isDisplayed(),"Error occured while changing password");

		//Refresh
		driver.navigate().refresh();

		//Wait
		w.until(ExpectedConditions.visibilityOfElementLocated(By.id("pass1")));

		try{
			test.log(LogStatus.INFO, "ReChanging the Previous Password");
			//Change password back
			driver.findElement(By.id(p.myprofile_password_field1)).sendKeys("zxcv1234");

			driver.findElement(By.id(p.myprofile_password_field2)).sendKeys(Config.password);
			driver.findElement(By.id(p.myprofile_password_field3)).sendKeys(Config.password);

			driver.findElement(By.xpath(p.myprofile_submit_button)).click();

			w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(p.myprofile_confirm_message)));
			test.log(LogStatus.PASS, "Successfully Changed the Password");
		}
		catch(Exception e){
			test.log(LogStatus.FAIL, "Exception occured while reverting the back the password.");
			Assert.fail("Exception occured while reverting the back the password.");	
		}
	}

	//Set true only if test not running on staging
	@Test(priority=3,enabled=true)
	public void limits(){

		test.log(LogStatus.INFO, "Verifying the Limits Page is displayed ");
		driver.get(Config.baseUrl+"/limits");

		driver.findElement(By.xpath("html/body/h4")).isDisplayed();
		try{
			//Wait until the limits are loaded
			w.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(p.limits_element)));

			//Verify the elements
			driver.findElement(By.xpath(p.limits_element)).isDisplayed();
			driver.findElement(By.xpath(p.limits_element1)).isDisplayed();
			driver.findElement(By.xpath(p.limits_element2)).isDisplayed();
			driver.findElement(By.xpath(p.limits_element3)).isDisplayed();
			test.log(LogStatus.PASS, "Verifyed all the links");
		}
		catch(Exception e)
		{
			test.log(LogStatus.SKIP, "Error in loading limits, maybe staging environment?");
			rep.endTest(test);
			rep.flush();
			throw new SkipException("Error in loading limits, maybe staging environment?");
		}
	}

	@AfterMethod
	public void resultCheck(ITestResult result){
		if(ITestResult.FAILURE==result.getStatus())
			Utility.captureScreenshot(driver,""+result.getName()+System.currentTimeMillis(), test);
	}

	@AfterTest
	public void tearDown(){
		rep.endTest(test);
	//	rep.flush();
		driver.quit();
		System.out.println("END OF ------------MyProfile TEST-----------");
	}
}
