
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

public class Panel extends BaseClass{
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

	@Test(priority=1)
	public void panel(){

		//Check for page breaks
		Assert.assertEquals(driver.getTitle(), Config.title);

		//Print all modules existence and name
		//List<WebElement> list=driver.findElements(By.xpath(".//*[@class='floating-box']"));
		try{
			for(int i=1;i<=47;i++)
			{
				//Get text
				driver.findElement(By.xpath(".//*[@class='floating-box']["+i+"]")).getText();

				//Click
				driver.findElement(By.xpath(".//*[@class='floating-box']["+i+"]")).click();

				//Go back
				driver.navigate().back();
			}
			test.log(LogStatus.PASS, "Verified the All the Elements present on the panels");
		}
		catch(Exception e){
			test.log(LogStatus.FAIL, "Exception occured while verifying elements/modules are present on the panels page");
			Assert.fail("Exception occured while verifying elements/modules are present on the panels page");	
		}

		//Assert the notice marquee exist
		driver.findElement(By.id(p.marquee_element)).isDisplayed();

		//Verify the change password link exist
		driver.findElement(By.linkText(p.changepassword_link)).isDisplayed();

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
		System.out.println("END OF ------------PANEL TEST-----------");
	}
}
