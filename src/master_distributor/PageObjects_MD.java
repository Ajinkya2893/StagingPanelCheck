package master_distributor;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

import lib.Config;

public class PageObjects_MD {

	Config c = new Config();
	//----------DISTRIBUTOR PANEL------------
	String dist_username_field = "userMobile";
	String dist_password_field = "userPassword";
	String login_button = ".//*[@id='loginSignIn']/input";

	String images_id= "//a/img";
	public String distinfo_topright_element="//*[@id='rightHeaderSpace']/div/div/div[1]/div/ul/li[1]";
	public String footer = ".//*[@id='footer']";
	public String user_balance_element = ".//*[@id='UserBalance']";
	public String page_title="//div[@class='appTitle']";
	public String noresults_message = "//span[contains(text(),'No Results')]";
	public String flashmessage = ".//*[@id='flashMessage']/div";
	public String loader_element="loader2";

	//Links
	public String activities_link="Activities";
	public String profile_link = "My Profile";
	public String reports_link = "Reports";
	public String transfer_link="Balance Transfer";
	public String kits_transfer_link = "Debit System";
	public String dist_list_link="List Distributors";
	public String createdist_link="Create Distributor";
	public String createrm_link = "Create RM";
	public String retailer_list_link="Retailers List";
	//

	public String mainreport_link = "Main Report";
	public String acchistory_link= "Account History";
	public String buyreport_link = "Buy Report";
	public String retailersalereport_link = "Retailers Sale Report";
	public String balancereport_link="Balance Transfer Report";
	public String pullback_link = "Pull Back";

	//KITS TRANSFER CASE
	public String amount_element = "amount";
	public String kits_distributor_dropdown = "shop";
	public String select_distributor = "distributor";
	public String select_service = "service";
	public String kits_element = "kits";
	public String per_kits_element = "per_kit";
	public String discount_per_kits_element = "discount_per_kit";


	//DISTRIBUTOR LIST CASE
	public String dlist_column_headers = "//tr[@class='noAltRow altRow']/th";

	//Search and date control
	public String from_datepicker = "fromDate";
	public String to_datepicker = "toDate";
	public String selectMonth_calendarpicker = "//td[@class='monthHeadingRow']//tbody//td[3]";
	public String todaydate_datepicker = ".//td[@class='todayDateCell_']";
	public String month_choice = "//td[contains(text(),'Jan')]";
	public String fromDate_choice = "//table[@class='calendarTable_']//tr[5]/td[contains(text(),'1')]";
	public String toDate_choice = "//tr[9]/td[@class='normalDateCell_'][contains(text(),'3')]";
	public String fromDate_choice1 = "01-01-2017";
	public String toDate_choice1 = "05-01-2017"; 
	public String searchButton = "sub";
	public String clear_datepicker = ".//td[@class='titleRow_']/a[contains(text(),'C')]";
	public String close_datepicker = ".//td[@class='titleRow_']/a[contains(text(),'X')]";

	//Page specific collective values
	public String pendingTopup_element = ".//td/b[contains(text(),'Total')]/following::td[4]/b";
	public String totalSalesmanCollection_field = "//b[contains(text(),'Total')]/following::td[1]";
	public String totalRetailers_count = "count";

	//Filter element
	public String filter= "filter";

	//Myprofile case
	public String myprofile_password_label = "//legend[contains(text(),'Change Password')]";
	public String myprofile_password_field1 = "pass1";
	public String myprofile_password_field2 = "pass2";
	public String myprofile_password_field3 = "pass3";
	public String myprofile_submit_button = "//div[@class='submit']/input";
	public String myprofile_confirm_message = ".//*[@id='innerDiv'][contains(text(),'Password changed successfully')]";

	//Create RM Case
	public String rm_name = "username";
	public String rm_number = "mobile";
	public String rm_name_confirm_field = ".//label[contains(text(),'Name')]/following::div[1]";

	//Main report case
	public String mainreport_charts = "//*[local-name() = 'svg']";

	//Acc history case
	public String acchistory_txnid_column = ".//th[contains(text(),'Txn Id')]";
	public String acchistory_particulars_column = ".//th[contains(text(),'Particulars')]";
	public String acchistory_debit_column = ".//th[contains(text(),'Debit')]";
	public String acchistory_credit_column = ".//th[contains(text(),'Credit')]";
	public String acchistory_opening_column = ".//th[contains(text(),'Opening')]";
	public String acchistory_closing_column = ".//th[contains(text(),'Closing')]";
	public String acchistory_rows = ".//tr[@class='altRow']";
	public String acchistory_closingbal = ".//th[contains(text(),'Closing')]/following::tbody/tr[1]/td[6][@class='number']";

	//Transfer case
	public String transfer_number_element =	"shop_select";
	public String transfer_type = "type";
	public String txn_id="description";
	public String lasttransactions = ".//div[@id='lastTxns']";
	public String lasttransactions_rows = ".//*[@id='lastTxns']/div/table/tbody/tr";
	public String lasttransactions_latesttxn = ".//*[@id='lastTxns']/div/table/tbody/tr[1]/td[1]";
	public String lasttransactions_header = "//div[contains(text(),'Last Transferred')]";
	public String confirmtransaction_button ="tran_confirm";
	public String bank_field ="bank_name";
	public String confirm_pwd_field = "password";
	public String pullback_message = "//span[@class='success']";

	//Main report
	public String topup_sold = ".//b[contains(text(),'Topup sold')]/following::td[1]";
	public String topup_buy = ".//b[contains(text(),'Topup buy')]/following::td[1]";
	public String topup_unique = "//b[contains(text(),'Unique topups')]/following::td[1]";
	public String dist_dropdown = "shop";


	public void search_distributor(WebDriver driver, String dist_name){

		//Navigate
		driver.get(Config.baseUrl+"/shops/mainReport");

		Select s= new Select(driver.findElement(By.id(dist_dropdown)));
		s.selectByVisibleText(dist_name);

		//Search
		driver.findElement(By.id(searchButton)).click();

	}

	public void pickDates(WebDriver driver, String from, String to){

		//Navigate 'from' date control
		driver.findElement(By.id(from_datepicker)).click();
		driver.findElement(By.id(from_datepicker)).click();

		//Select today's date
		driver.findElement(By.xpath(from)).click();

		//Navigate 'to' date control
		driver.findElement(By.id(to_datepicker)).click();
		driver.findElement(By.id(to_datepicker)).click();

		//Select today's date
		driver.findElement(By.xpath(to)).click();

		//Search
		driver.findElement(By.id(searchButton)).click();

	}


	public void pickDates_absolute(WebDriver driver, String from, String to){

		//Select from date
		driver.findElement(By.id(from_datepicker)).click();

		//Clear
		driver.findElement(By.xpath(clear_datepicker)).click();

		//CLose
		driver.findElement(By.xpath(close_datepicker)).click();

		//Enter date
		driver.findElement(By.id(from_datepicker)).sendKeys(fromDate_choice1);

		//Select to date
		driver.findElement(By.id(to_datepicker)).click();

		//Clear
		driver.findElement(By.xpath(clear_datepicker)).click();

		//CLose
		driver.findElement(By.xpath(close_datepicker)).click();

		//Enter date
		driver.findElement(By.id(to_datepicker)).sendKeys(toDate_choice1);	

		//Click on Search
		driver.findElement(By.id(searchButton)).click();	
	}

}

