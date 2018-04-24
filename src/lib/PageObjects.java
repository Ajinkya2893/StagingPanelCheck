package lib;

public class PageObjects {
		
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
		public String transfer_link="Balance Transfer";
		public String profile_link = "My Profile";
		public String reports_link = "Reports";
		public String support_link = "Support";
		
		public String salesmenList_link="Salesmen List";
		public String createSalesman_link="Create Salesman";
		public String retList_link="Retailers List";
		public String createRetailer_link="Create Retailer";
		public String deletedRetailers_link="Deleted Retailer";
		public String topup_link="TopUp Request";
		public String salesmanCollections_link="Salesman Collections";
		
		public String retailertransaction_link =  "Retailer Transactions";
		public String mainreport_link = "Main Report";
		public String acchistory_link= "Account History";
		public String buyreport_link = "Buy Report";
		public String retailersalereport_link = "Retailers Sale Report";
		public String salesmanreport_link="Salesman Report";
		public String pullback_link = "Pull Back";
		
		//Search and date control
		public String from_datepicker = "//*[@id='fromDate']";
		public String to_datepicker = "//*[@id='toDate']";
		public String selectMonth_calendarpicker = "//td[@class='monthHeadingRow']//tbody//td[3]";
		public String todaydate_datepicker = ".//td[@class='todayDateCell_']";
		public String month_choice = "//td[contains(text(),'Jan')]";
		public String fromDate_choice = "//table[@class='calendarTable_']//tr[5]/td[contains(text(),'5')]";
		public String toDate_choice = "//tr[9]/td[@class='normalDateCell_'][contains(text(),'3')]";
		public String fromDate_choice1 = "01-01-2017";
		public String toDate_choice1 = "07-01-2017"; 
		public String searchButton = "sub";
		public String clear_datepicker = ".//td[@class='titleRow_']/a[contains(text(),'C')]";
		public String close_datepicker = ".//td[@class='titleRow_']/a[contains(text(),'X')]";
		
		//Page specific collective values
		public String pendingTopup_element = ".//td/b[contains(text(),'Total')]/following::td[4]/b";
		public String totalSalesmanCollection_field = "//b[contains(text(),'Total')]/following::td[1]";
		public String totalRetailers_count = "count";
	
		//Filter element
		public String filter= "filter";
	
		//Other
		public String salesman_filter_dropdown="id";
		public String salesman_dropdown="block_salesmanDD";
		public String retailer_filter_dropdown="retailerid";
	
		//Deleted Retailer case
		public String deletedretailer_edit_link="edit";
		public String deletedretailer_revert_link="revert";
		public String deletedretailer_analyze_link="Analyze";
		public String deletedretailer_change_link="Change Number";
		
		//public String retailer_element = ".//a[contains(text(),'"+c.retailer_num+"')]";
		//public String retailer_element1 = ".//option[contains(text(),'"+c.retailer_num+"')]";
		//public String retailer_element_link = ".//a[contains(text(),'"+c.retailer_num+"')]/following::td[10]/a";	
		
		//TopUp page
		public String topuprequest_header = ".//div[contains(text(),'Pay1 TopUp Request')]";
		public String topuprequest_bankacc_label = "//label[contains(text(),'Bank Acc')]";
		public String topuprequest_transfertype_label = "//label[contains(text(),'Transfer')]";
		public String topuprequest_branch_label = "//label[contains(text(),'Branch')]";
		public String topuprequest_slip_label = "//label[contains(text(),'Bank Slip')]";
		public String topuprequest_amount_label = "//label[contains(text(),'Amount')]";
		public String topuprequest_id_label = "//label[contains(text(),'Bank Trans')]";
		public String topuprequest_branchcode_label = "//label[contains(text(),'Branch Code')]";
		public String topuprequest_send_button= "send_top_up_req";

		public String topuprequest_bankacc_field = "bank_acc_id";
		public String topuprequest_transfertype_field = "trans_type_id";
		public String topuprequest_branch_field = "branch_name";
		public String topuprequest_amount_field = ".//input[@id='mobile'][@type='text']";
		public String topuprequest_id_field = "bank_trans_id";
		public String topuprequest_branchcode_field = "branch_code";
		
		//Myprofile case
		public String myprofile_password_label = "//legend[contains(text(),'Change Password')]";
		public String myprofile_password_field1 = "pass1";
		public String myprofile_password_field2 = "pass2";
		public String myprofile_password_field3 = "pass3";
		public String myprofile_submit_button = "//div[@class='submit']/input";
		public String myprofile_confirm_message = ".//*[@id='innerDiv'][contains(text(),'Password changed successfully')]";

		//Create Retailer Case
		public String createretailer_mobile = "mobile";
		public String createretailer_shopname = "shopname";
		public String createretailer_otp = "otp";
		public String createretailer_confirmnumber = "//label[contains(text(),'Mobile')]/following::div[1][@class='fieldLabelSpace1 strng']";
		
		//Create Salesman Case
		public String createsalesman_name = "username";
		public String createsalesman_number = ".//*[@id='mobile'][@name='data[Salesman][mobile]']";
		public String createsalesman_address = "address";
		public String createsalesman_confirmnumber = ".//div[@class='fieldLabel1 leftFloat']/label[contains(text(),'Mobile')]/following::div[1]";
		
		//Limits case
		public String limits_element  = ".//*[@id='chats']/span/div[1]";
		public String limits_element1  = ".//*[@id='chats']/span/div[2]";
		public String limits_element2  = ".//*[@id='chats']/span/div[3]";
		public String limits_element3 = "//*[@id='chats']/span/div[3]/div[1]";
		
		//Panel case
		public String marquee_element = "notice";
		public String changepassword_link = "Change Password";
		public String panel_search_element = "//*[contains(text(),'Search')]";
		public String panel_number_element = "rMobNo";
		
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
		public String transfer_amount_element = "amount";
		public String transfer_number_element =	"shop1";
		public String transfer_type = "type";
		public String retlist_balance_element = ".//th[contains(text(),'Balance')]/following::tr[2]/td[4]";
		public String txn_id="description";
		public String lasttransactions = ".//div[@id='lastTxns']";
		public String lasttransactions_rows = ".//*[@id='lastTxns']/div/table/tbody/tr";
		public String lasttransactions_latesttxn = ".//*[@id='lastTxns']/div/table/tbody/tr[1]/td[1]";
		public String lasttransactions_header = "//div[contains(text(),'Last Transferred')]";
		public String confirmtransaction_button ="tran_confirm";
		public String confirmamount_field ="confirm";
		public String newsys_transfer_dropdown = "label";
		public String pullback_message = "//span[@class='success']";
		
		public String salesman_limit_field="//*[contains(text(),'Transaction Limit')]";
		
		//Main report
		public String topup_sold = ".//b[contains(text(),'Topup sold')]/following::td[1]";
		public String topup_unique = "//b[contains(text(),'Unique topups')]/following::td[1]";
		
		
		
		
	//----------RETAILER PANEL--------------
		public String retailer_username_field = "mobile_no";
		public String retailer_password_field = "pin";
		public String retailer_login_button = "login_modal_submit";
		public String login_form = "//*[@class='form-signin-heading']";
	}

