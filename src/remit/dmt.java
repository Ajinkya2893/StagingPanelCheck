package remit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import lib.Config;

public class dmt {
	
	private static WebDriver driver;
	private static WebDriverWait w;
	
	//Variables
	public static String username = "9819042543";
	public static String password = "zxcv1234";
	public static String remitter = "9833887517";
	public static String bene = "Mindsarray Acc One";
	public static String pin = "1234";
	
	public static void main(String[] args){
		
		ProfilesIni profile = new ProfilesIni();
	    FirefoxProfile myProfile = profile.getProfile("default");
	    myProfile.setPreference("geo.prompt.testing", true);
	    myProfile.setPreference("geo.prompt.testing.allow", true);
	    myProfile.setPreference("geo.wifi.uri", "https://location.services.mozilla.com/v1/geolocate?key=%MOZILLA_API_KEY%");
	    DesiredCapabilities capabilities = DesiredCapabilities.firefox();
	    capabilities.setCapability(FirefoxDriver.PROFILE,myProfile);
		
		//Launch driver
		//driver = new FirefoxDriver((Capabilities) myProfile);
		
		//Maximize window
		driver.manage().window().maximize();
		
		//Set wait
		w = new WebDriverWait(driver, 30);
		
		navigate();
		shopLogin();
		goToRemit();
		selectSenderBene();
		transfer();
	}
	
	
	public static void navigate(){
	
		
				//Navigate to shop
				driver.get("https://shop.pay1.in");
				
				//Wait
				w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//a[@value='Login']")));
				
				//Click on login
				driver.findElement(By.xpath(".//a[@value='Login']")).click();
				
				//Wait
				w.until(ExpectedConditions.visibilityOfElementLocated(By.id("mobile_no")));
				
	}
	
	
	public static void shopLogin(){
		
		Config c = new Config();
		c.retailerLogin(driver, username, password);
		
		w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//p[@class='balance']")));

	}
	
	
	public static void goToRemit(){

		driver.get("https://remit.pay1.in");
		
		w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//span[@class='balance']")));
	
		//System.out.println("Done!!");
	}
	
	
	public static void selectSenderBene(){
	
		//Input remitter mobile
				driver.findElement(By.id("remittermobile")).sendKeys(remitter);
				
				//Wait
				w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='existingsender']")));
				
				//Choose bene account
				driver.findElement(By.xpath("//*[contains(text(),'"+bene+"')]")).click();

	}
	
	public static void transfer(){
		
		//Type amount and pin
		driver.findElement(By.id("txnamt")).sendKeys("100");
		driver.findElement(By.id("txnpin")).sendKeys(pin);
		
		//Transfer
		driver.findElement(By.id("btntfr")).click();
		
		//Wait for txn to process
		w.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class='loader loader-default is-active']")));

	
		//check
		if(w.until(ExpectedConditions.alertIsPresent()) != null){
			System.out.println("Transaction failed");
		}
		else
		{
			System.out.println("Transaction successful");
		}
	}
	
	
	
	
	
	

}
