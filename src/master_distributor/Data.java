package master_distributor;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import lib.Config;
import lib.ExcelConfig;
import lib.Utility;

public class Data extends BaseClass{

	//Create config object
	Config c=new Config();

	//Create page objects object
	PageObjects_MD p = new PageObjects_MD();

	private WebDriver driver;
	private WebDriverWait w;

	final static String md_amount = "2500";
	final static String d_amount = "50";

	@BeforeTest
	public void setUp(){

		//Call browser specific method and instantiate driver 
		driver=c.openBrowser();	

		//Wait 
		w=new WebDriverWait(driver,30);

		//PARALLEL EXECUTION
		try{
			driver.get(Config.baseUrl);
		}
		catch(Exception e){
			org.testng.Assert.fail("Couldn't navigate to the URL provided");
		}

		//maximize window
		driver.manage().window().maximize();

		//Wait 
		w=new WebDriverWait(driver,30);

		//LOGIN
		//change to super dist for creating data of sd to d
		c.Login(driver, "Distributor");

		//Wait
		w.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(p.transfer_link)));

	}

	@Test
	public void transfer() throws InterruptedException
	{
		//Call data generator function

		//getDistributorList();
		//getRetailerList();

		//For distributor 
		dist_data();

		//For super distributor
		//md_data();
	}

	public void dist_data() throws InterruptedException
	{			

		//Set excel path
		ExcelConfig excel = new ExcelConfig(Config.excelpath_demo);
		//int rCount	=	
		for(int i=1; i <=45  ; i++)
		{

			//Wait and type amount
			w.until(ExpectedConditions.visibilityOfElementLocated(By.id("amount")));
			driver.findElement(By.id("amount")).sendKeys(d_amount);

			//Get data from excel
			String data = excel.getData("Sheet1", i, 1);

			driver.findElement(By.id("shop1")).clear();
			driver.findElement(By.id("shop1")).sendKeys(data);

			//Wait
			w.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(data)));

			//Select option
			driver.findElement(By.linkText(data)).click();

			//Wait for txn's to load
			w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='lastTxns']/div")));

			driver.findElement(By.xpath(".//*[@id='typeRadio'][1]")).click();

			int random = (int)(Math.random() * 10000 + 1);

			//Send TXN ID
			driver.findElement(By.id("description")).sendKeys(""+random);

			//Submit request
			driver.findElement(By.id("sub")).click();

			//wait
			w.until(ExpectedConditions.visibilityOfElementLocated(By.id("confirm")));

			//Confirm amt
			driver.findElement(By.id("confirm")).sendKeys(d_amount);

			//Confirm 
			driver.findElement(By.id("tran_confirm")).click();

			//
			//Thread.sleep(2000);

		}
	}


	public void md_data() throws InterruptedException{

		//Set excel path
		ExcelConfig excel = new ExcelConfig(Config.excelpath_demo);

		/*driver.findElement(By.id("shop_select")).clear();
			driver.findElement(By.id("shop_select")).sendKeys(" ");
			w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='ui-id-1']/li/a")));

			List <WebElement> l = driver.findElements(By.xpath(".//*[@id='ui-id-1']/li/a"));


				for(WebElement e:l)
				{
					System.out.println(e.getText());
				}

				System.out.println("Done");*/

		for(int i=1; i<= 50; i++)
		{

			//Go to transfer page
			driver.get(Config.baseUrl+"/shops/transfer");

			//Wait and type amount
			w.until(ExpectedConditions.visibilityOfElementLocated(By.id("amount")));
			driver.findElement(By.id("amount")).sendKeys(md_amount);

			//Get data from excel
			String data = excel.getData("Sheet1", i, 0);

			//Enter space in the field to get auto select options
			driver.findElement(By.id("shop_select")).clear();
			driver.findElement(By.id("shop_select")).sendKeys(" ");

			//Wait
			w.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(data)));

			//Select option
			driver.findElement(By.linkText(data)).click();

			//Wait for txn's to load
			w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='lastTxns']/div")));

			//Select Bank Name
			Select s = new Select(driver.findElement(By.id("bank_name")));
			s.selectByVisibleText("internal");

			int random = (int)(Math.random() * 10000 + 1);

			//Send TXN ID
			driver.findElement(By.id("description")).sendKeys(""+random);

			//Submit request
			driver.findElement(By.id("sub")).click();

			try{
				//Wait for confirmation message (MD LOGIN)
				w.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));

				//Confirm amt
				driver.findElement(By.id("amount")).sendKeys(md_amount);

				//Confirm password
				driver.findElement(By.id("password")).sendKeys("limitpwd");

				//Confirm 
				driver.findElement(By.id("tran_confirm")).click();
			}
			catch(Exception e)
			{
				System.out.println(data+" is a closed distributor.");
			}
		}
	}

	public void getDistributorList()
	{
		//for MD login
		driver.findElement(By.id("shop_select")).clear();
		driver.findElement(By.id("shop_select")).sendKeys(" ");

		//for D login
		/*driver.findElement(By.id("shop1")).clear();
			driver.findElement(By.id("shop1")).sendKeys(" ");*/

		w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='ui-id-1']/li/a")));

		//Store all distributors from list
		List <WebElement> l = driver.findElements(By.xpath(".//*[@id='ui-id-1']/li/a"));

		for(WebElement e:l)
		{
			System.out.println(e.getText());
		}

	}


	public	void getRetailerList(){

		driver.findElement(By.id("shop1")).clear();
		driver.findElement(By.id("shop1")).sendKeys(" ");

		w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='ui-id-1']/li/a")));

		//Store all retailers from list
		List <WebElement> l = driver.findElements(By.xpath(".//*[@id='ui-id-1']/li/a"));


		for(WebElement e:l)
		{
			System.out.println(e.getText());
		}
	}


	@AfterMethod
	public void resultCheck(ITestResult result){
		if(ITestResult.FAILURE==result.getStatus())
		{
			Utility.captureScreenshot(driver,""+result.getName(), test);
		}
		else{
			//DO NOTHING
		} 

	}

	@AfterTest
	public void breakdown(){
		driver.quit();
	}

}



