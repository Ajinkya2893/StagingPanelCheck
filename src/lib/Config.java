package lib;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.SkipException;
/**
 * @author Dev Shah
 */

public class Config {

	private WebDriver driver;
	PageObjects p = new PageObjects();
	//private WebDriverWait w;

	//DRIVER PATHS
	public static String chrome_path = System.getProperty("user.dir")+"//Dependencies//chromedriver";
	public static String ff_path = System.getProperty("user.dir")+"/Dependencies/geckodriver";
	public static String ie_path = "./Dependencies/IEdriverServer.exe";

	//Report Path
	public static String panelReport = System.getProperty("user.dir")+"/Reports/";

	//Excel Path
	public static String excelPath = "./Dependencies/Test_Data.xlsx";
	public static String excelpath_demo = "C:\\Users\\Administrator.pay1lap-71-PC\\Desktop\\Book1.xlsx";

	//Website Title used for ASSERTION
	public static String title = "Pay1 - Distributor Portal | Cash to Digital Network";

	//Retailer Website
	public static String retailer_title = "Panel for Retailers | Retail Network";


	//Pick data from excel - VARIABLES
	public static ExcelConfig excel = new ExcelConfig(Config.excelPath);

	public final static String baseUrl = excel.getData("Variables", 1, 1); 
	public final static String userMobile = excel.getData("Variables", 3, 1);
	public final static String password = excel.getData("Variables", 5, 1);
	public final static String browser = excel.getData("Variables", 7, 1);
	public int system;
	public final static String MduserMobile = excel.getData("Variables", 11, 1);
	public final static String Mdpassword = excel.getData("Variables", 13, 1);

	//TRANSFER BALANCE
	public final String retailer = excel.getData("TestData", 2, 1);
	public final String retailer_num = retailer.substring(5,15);
	public final String amount =  excel.getrawData("TestData", 3, 1);
	public final String txnid = excel.getrawData("TestData", 4, 1);
	public final String txnType = excel.getData("TestData", 5, 1);

	//MD TRANSFER BALANCE
	public final String distributor_id = excel.getData("TestData", 14, 1);
	public final String amount1 = excel.getData("TestData", 15, 1);

	//SALESMAN TFR
	public final String salesman_num = excel.getData("TestData", 20, 1);
	public final String s_amount = excel.getData("TestData", 21, 1);
	public final String s_txnid = excel.getrawData("TestData", 22, 1);
	public final String s_txnType = excel.getData("TestData", 23, 1);

	//Create Case
	public final static String create_retailer_salesman = excel.getData("TestData", 11, 1);

	//PANEL LOGIN Parameters
	public final static String panel_id = excel.getData("TestData", 7, 1);
	public final static String panel_pwd = excel.getData("TestData", 8, 1);


	//ARRAYS
	public String[] retailersite_links = {"Complaint Status", "Request Complaint", "DTH", "Bill Payment", "Utility Bill Payment", "Wallet Topup", "C2D Payment" , "Complaint", "Reports", "Support"};



	/*public int user_system()
	{
		if(baseUrl.equals("http://cc.pay1.in/") || baseUrl.equals("http://panel.pay1.in"))
		{
			system = 0;
		}
		else{
			system = 1;
		}

		return system;
	}*/


	///////////REUSABLE METHODS	


	//	LOGIN AS
	public void Login(WebDriver driver, String user_group){
		if(user_group.equals("Distributor")) {
			try{
				driver.findElement(By.id(p.dist_username_field)).sendKeys(userMobile);
				driver.findElement(By.id(p.dist_password_field)).sendKeys(password);
				Select dd = new Select(driver.findElement(By.id("userGroup")));
				dd.selectByVisibleText(user_group);
				driver.findElement(By.xpath(p.login_button)).click();
				//w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(p.user_balance_element)));
				driver.navigate().to(baseUrl+"/shops/transfer");
			}
			catch(Exception e){
				Assert.fail("Issue occured while logging in");
			}
		}
		else {
			try{
				driver.findElement(By.id(p.dist_username_field)).sendKeys(MduserMobile);
				driver.findElement(By.id(p.dist_password_field)).sendKeys(Mdpassword);
				Select dd = new Select(driver.findElement(By.id("userGroup")));
				dd.selectByVisibleText(user_group);
				driver.findElement(By.xpath(p.login_button)).click();
				//w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(p.user_balance_element)));
				driver.navigate().to(baseUrl+"/shops/transfer");
			}
			catch(Exception e){
				Assert.fail("Issue occured while logging in");
			}
		}
	}


	// LOGIN 
	public void retailerLogin(WebDriver driver, String userMobile, String password){

		try{
			driver.findElement(By.id(p.retailer_username_field)).sendKeys(userMobile);
			driver.findElement(By.id(p.retailer_password_field)).sendKeys(password);
			driver.findElement(By.id(p.retailer_login_button)).click();
		}catch(Exception e){
			org.testng.Assert.fail("Issue occured while logging in");
		}
	}

	//Panel Case
	public void panelLogin(WebDriver driver, String user_group){
		try{
			driver.findElement(By.id(p.dist_username_field)).sendKeys(panel_id);
			driver.findElement(By.id(p.dist_password_field)).sendKeys(panel_pwd);
			Select dd = new Select(driver.findElement(By.id("userGroup")));
			dd.selectByVisibleText(user_group);
			driver.findElement(By.xpath(p.login_button)).click();
			//w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(p.user_balance_element)));
			driver.navigate().to(baseUrl+"/shops/transfer");
		}
		catch(Exception e){
			org.testng.Assert.fail("Issue occured while logging in");
		}
	}

	//Assert function
	public void assertFunc(WebDriver driver){
		try{

			if(baseUrl.equals("http://shop.pay1.in/")){

				Assert.assertEquals(driver.getTitle(), retailer_title);
				Assert.assertTrue(!driver.getCurrentUrl().contains("error"));
				Assert.assertTrue(!driver.getCurrentUrl().contains("404"));
				Assert.assertTrue(!driver.getCurrentUrl().contains("er404"));
			}

			else{	
				Assert.assertEquals(driver.getTitle(), title);
				Assert.assertTrue(!driver.getCurrentUrl().contains("error"));
				Assert.assertTrue(!driver.getCurrentUrl().contains("404"));
				Assert.assertTrue(!driver.getCurrentUrl().contains("er404"));
			}
		}
		catch(Exception e){
			org.testng.Assert.fail("Issue occured while asserting.");
		}
	}


	//Check Image
	public void checkImage(WebDriver driver)
	{
		//Brand logo appears
		Assert.assertTrue(driver.findElement(By.xpath(p.images_id)).isDisplayed());	

		//Verify image is loading successfully
		List<WebElement> l=driver.findElements(By.xpath(p.images_id));

		try{
			for(WebElement img:l)
			{
				Boolean result = (Boolean) ((JavascriptExecutor)driver).executeScript(
						"return arguments[0].complete && typeof arguments[0].naturalWidth != \"undefined\" && arguments[0].naturalWidth > 0",
						img);

				if(result==true)
				{
					//To-Do
				}
				else{
					System.out.println(result);
					throw new SkipException("Image not loaded");
				}
			}	

		}
		catch(Exception e){
			org.testng.Assert.fail("Issue occured while loading image.");
		}

	}


	public WebDriver openBrowser(){

		if(driver==null)
		{
			if(Config.browser.equalsIgnoreCase("Chrome"))
			{
				//FOR Chrome driver
				/*System.setProperty("webdriver.chrome.driver", Config.chrome_path);
					driver=new ChromeDriver();*/

				System.setProperty("webdriver.chrome.driver", Config.chrome_path);
				ChromeOptions ChromeOptions = new ChromeOptions();
				//ChromeOptions.addArguments("--headless", "window-size=1024,768", "--no-sandbox");
				driver = new ChromeDriver(ChromeOptions);
			}
			else if(Config.browser.equalsIgnoreCase("Firefox"))
			{
				//FOR FIREFOX driver (SELENIUM 3.0)
				/*System.setProperty("webdriver.gecko.driver", Config.ff_path);
				driver=new FirefoxDriver();*/
				FirefoxBinary firefoxBinary = new FirefoxBinary();
				//firefoxBinary.addCommandLineOptions("--headless");
				System.setProperty("webdriver.gecko.driver",Config.ff_path);
				System.setProperty(FirefoxDriver.SystemProperty.DRIVER_USE_MARIONETTE,"true");
				System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE,"/dev/null");
				FirefoxOptions firefoxOptions = new FirefoxOptions();
				firefoxOptions.setBinary(firefoxBinary);
				driver = new FirefoxDriver(firefoxOptions);
			}
			else{
				//FOR IE driver
				System.setProperty("webdriver.ie.driver", Config.ie_path);
				driver=new InternetExplorerDriver();
			}

			/*try{
			driver.get(Config.baseUrl);
			}
			catch(Exception e){
				org.testng.Assert.fail("Couldn't navigate to the URL provided");
			}

			//Create Wait object 
			w=new WebDriverWait(driver,30);

			//LOGIN
			distLogin(driver);

			//Wait
			w.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(p.transfer_link)));

			//Call check image function
			checkImage(driver);

			//maximize window
			driver.manage().window().maximize();
			//Set implicit wait
			driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);*/	
		}
		return driver;

	}


}
