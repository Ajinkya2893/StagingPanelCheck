package lib;

import java.io.File;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.relevantcodes.extentreports.DisplayOrder;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class Utility {

	//No return
	public static void captureScreenshot(WebDriver driver,String screenshotName, ExtentTest test)
	{
		try 
		{
			TakesScreenshot ts=(TakesScreenshot)driver;	 
			File source=ts.getScreenshotAs(OutputType.FILE);

			String dest = System.getProperty("user.dir")+"/Screenshots/"+screenshotName+".png";
			
			FileUtils.copyFile(source, new File(dest));
			
			test.log(LogStatus.INFO, test.addScreenCapture(dest));
			System.out.println("Screenshot taken");

			//Reporter.log("<br> <img src=./Screenshots/"+screenshotName+".png /> <br>");
		}
		catch (Exception e)
		{
			test.log(LogStatus.FAIL, "Exception while taking screenshot "+e.getMessage());
			System.out.println("Exception while taking screenshot "+e.getMessage());
		}
	}


	//Return string
	public static String takeScreenShot(WebDriver driver,String screenShotName, ExtentTest test) throws Exception
	{
		TakesScreenshot ts = (TakesScreenshot)driver;

		File source = ts.getScreenshotAs(OutputType.FILE);

		String dest = "./Screenshots/"+screenShotName+".png";

		File destination = new File(dest);

		FileUtils.copyFile(source, destination);
		
		test.log(LogStatus.INFO, test.addScreenCapture(dest));

		return dest;
	}

	private static ExtentReports extent;
	//Creates a report object and a file; appends the current date and time to the file name.
	public static ExtentReports getInstance(String path) {
		if (extent == null) {
			Date d=new Date();
			String fileName=d.toString().replace(":", "_").replace(" ", "_")+".html";
			extent = new ExtentReports(path+fileName, true, DisplayOrder.NEWEST_FIRST );
			extent.loadConfig(new File(System.getProperty("user.dir")+"//ReportsConfig.xml"));
			// optional
			extent.addSystemInfo("Selenium Version", "3.11.1").addSystemInfo(
					"Environment", "QA");
		}
		return extent;
	}

}