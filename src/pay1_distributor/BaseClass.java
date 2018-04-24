package pay1_distributor;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;

public class BaseClass {

	public ExtentReports rep;
	public ExtentTest test;

	@BeforeSuite
	public void setup() {

	}
	
	@AfterSuite
	public void afterSuite() {
		try {
			rep.flush();
			/*ZipUtil.pack(new File(Constants.SrcPath), new File(Constants.DesPath));
			new SendMail().sendMail();*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
