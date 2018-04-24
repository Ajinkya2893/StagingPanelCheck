package pay1_retailer;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import lib.Config;
import lib.PageObjects;

public class Home {
	
		private WebDriver driver;
		private WebDriverWait w;

		//Create object
		Config c=new Config();
		PageObjects p = new PageObjects();
		
		String[] operator = {"Aircel", "Airtel", "BSNL", "Idea", "MTS", "Reliance CDMA","Reliance GSM","Tata Docomo", "Tata Indicom", "Uninor", "Videocon", "Vodafone", "MTNL"};
		
	@BeforeTest
	public void setUp(){
		
		driver = c.openBrowser();
		driver.get(Config.baseUrl);
		driver.manage().window().maximize();
		w = new WebDriverWait(driver,30);
	
	}
	
	@Test(priority=0,enabled=true)
	public void login()
	{	
		//Click on login

/*		JavascriptExecutor js=(JavascriptExecutor)driver;
		js.executeScript("arguments[0].click();", e);*/
		
		/*//Go to complaint status
		driver.findElement(By.linkText("Complaint Status")).click();
		
		//Pass
		w.until(ExpectedConditions.visibilityOfElementLocated(By.id("mobile_no")));
		*/
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		driver.findElement(By.xpath(".//a[@value='Login']")).click();
		
		w.until(ExpectedConditions.visibilityOfElementLocated(By.id(p.retailer_username_field)));
		
		//Login
		c.retailerLogin(driver, Config.userMobile, Config.password);
		
		//Wait to login
		w.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(p.login_form)));
	
		//.//*[@id='mobile']/a[1]/p
	}
	
	@Test(priority=1, enabled=true)
	public void checkpages() throws InterruptedException{
		
		//Check all pages
		
		for(int i=0;i<=c.retailersite_links.length-1;i++)
		{
			driver.findElement(By.linkText(c.retailersite_links[i])).isDisplayed();
			driver.findElement(By.linkText(c.retailersite_links[i])).click();
			
			c.assertFunc(driver);
		}
		
		Thread.sleep(3000);
		
	}

	@Test(priority=2, enabled=false)
	public void mobile(){
		
		//Navigate to mobile page
		driver.findElement(By.linkText("Mobile")).click();
		
		for(int i=0; i<=12; i++)
		{
		
		//Click on operator name
		w.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(operator[i])));
		
		driver.findElement(By.linkText(operator[i])).click();
		
		//Assert aircel recharge page
		driver.getPageSource().contains("> "+operator[i]+" > Recharge");
		
		//Send Data
		driver.findElement(By.id("mobileNo")).sendKeys("9833887517");
		driver.findElement(By.id("rechargeAmt")).sendKeys("10");

		//Check plans
		driver.findElement(By.linkText("Check Plan")).click();

		//Wait and assert display
		w.until(ExpectedConditions.visibilityOfElementLocated(By.id("mob_plan")));
		w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//table[@class='table table-hover']//tr/th[1]")));
		
		driver.navigate().back();
		driver.navigate().back();
		
		/*	//Accept popup
		Alert a= driver.switchTo().alert();
		a.accept();*/
		
		
		/*//Submit request
		driver.findElement(By.id("mobRechargeSubmit")).click();
		*/
		
		}
	}
	
	
	@AfterMethod
	public void resultCheck(ITestResult result){
		if(ITestResult.FAILURE==result.getStatus())
			{
			//Utility.captureScreenshot(driver,""+result.getName());
			}
		else{
			//DO NOTHING
			} 
		
		}
		
	
	@AfterTest
	public void logout(){
		
		System.out.println(1);
		
		WebElement e = driver.findElement(By.xpath(".//*[@id='absolute1']/div/div[3]/div/ul/li[3]/div/button"));	
		((JavascriptExecutor)driver).executeScript("arguments[0].click();", e);
		
		System.out.println(2);
		
		driver.findElement(By.linkText("Logout")).click();
		
	}
	
	}




