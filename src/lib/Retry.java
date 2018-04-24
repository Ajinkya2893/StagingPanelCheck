package lib;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class Retry implements IRetryAnalyzer {
    
	private int retryCount = 0;
    private int maxRetryCount = 1;

    public boolean retry(ITestResult result) {
        if (this.retryCount < this.maxRetryCount) {
            System.out.println("Retrying test " + result.getName() + " with status " + this.getResultStatusName(result.getStatus()) + " for the " + (this.retryCount + 1) + " time(s).");
            ++this.retryCount;
            return true;
        }
        return false;
    }

    public String getResultStatusName(int status) {
        String resultName = null;
        if (status == 1) {
            resultName = "SUCCESS";
        }
        if (status == 2) {
            resultName = "FAILURE";
        }
        if (status == 3) {
            resultName = "SKIP";
        }
        return resultName;
    }
}