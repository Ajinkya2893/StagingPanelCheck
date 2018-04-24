package master_distributor;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import lib.Config;
import lib.PageObjects;
import lib.Utility;

/**
 * @author Dev Shah
 */
public class MyProfile extends BaseClass{

	private WebDriver driver;
	private WebDriverWait w;	

	Config c=new Config();
	//Create page objects object
	PageObjects p = new PageObjects();
		
    @BeforeTest
	public void setUp(){
	
    	//Call select browser method
		driver=c.openBrowser();
	
		/*try{
		//URL
		driver.get(Config.baseUrl);
		}
		catch(Exception e){
			org.testng.Assert.fail("Exception occured while navigating to the site");	
		}
		
		//maximize window
		driver.manage().window().maximize();
		
		//Login
		c.distLogin(driver);
		*/
		//Wait object
		w=new WebDriverWait(driver,30);
		/*w.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(p.transfer_link)));
		
		//Check image
		c.checkImage(driver);
		*/
		//Set implicit wait
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		
		}
	
    @Test(priority=1, enabled=true)
	public void myProfile(){
		
    	try{
    	//CLick on my profile link
		driver.findElement(By.linkText(p.profile_link)).click();
		
		//Wait object
    	w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(p.myprofile_password_label)));
    	}
    	catch(Exception e){
			org.testng.Assert.fail("Exception occured while navigating to My Profile page");	
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
    	driver.findElement(By.id(p.myprofile_password_field1)).sendKeys(Config.password);
    	
    	driver.findElement(By.id(p.myprofile_password_field2)).sendKeys("zxcv1234");
    	driver.findElement(By.id(p.myprofile_password_field3)).sendKeys("zxcv1234");
    	
    	driver.findElement(By.xpath(p.myprofile_submit_button)).click();
    	}
    	catch(Exception e){
    			org.testng.Assert.fail("Exception occured while changing password");			
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
			//  Auto-generated catch block
			e.printStackTrace();
		}*/
    	
    	//Verify success message is present
      	Assert.assertTrue(driver.findElement(By.xpath(".//*[@id='innerDiv'][contains(text(),'Password changed successfully')]")).isDisplayed(),"Error occured while changing password");
      	
      	//Refresh
      	driver.navigate().refresh();
      	
      	//Wait
      	w.until(ExpectedConditions.visibilityOfElementLocated(By.id("pass1")));
      	
      	try{
      	//Change password back
    	driver.findElement(By.id(p.myprofile_password_field1)).sendKeys("zxcv1234");
    	
    	driver.findElement(By.id(p.myprofile_password_field2)).sendKeys(Config.password);
    	driver.findElement(By.id(p.myprofile_password_field3)).sendKeys(Config.password);
    	
    	driver.findElement(By.xpath(p.myprofile_submit_button)).click();
      	
    	w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(p.myprofile_confirm_message)));
      	}
    	catch(Exception e){
			org.testng.Assert.fail("Exception occured while reverting the back the password.");	
		}
      	}
    
    
    
    @Test(priority=3,enabled=true)
    public void limits(){
    	
    driver.get(Config.baseUrl+"/limits");
    
    driver.findElement(By.xpath("html/body/h4")).isDisplayed();
    
    //Wait until the limits are loaded
    w.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(p.limits_element)));
    
    //Verify the elements
    driver.findElement(By.xpath(p.limits_element)).isDisplayed();
    driver.findElement(By.xpath(p.limits_element1)).isDisplayed();
    driver.findElement(By.xpath(p.limits_element2)).isDisplayed();
    
    driver.findElement(By.xpath(p.limits_element3)).isDisplayed();
    
    }
    	
    
    @AfterMethod
	public void resultCheck(ITestResult result){
		if(ITestResult.FAILURE==result.getStatus())
		{
			Utility.captureScreenshot(driver,""+result.getName()+System.currentTimeMillis(), test);
		}
		else{
			//DO NOTHING
		} 
		
	}
    
    /*@AfterTest
    public void tearDown(){
		System.out.println("END OF ------------MY PROFILE TEST-----------");
	}
    @AfterSuite
    public void tearDown(){
	
    	driver.quit();

        }*/
}
