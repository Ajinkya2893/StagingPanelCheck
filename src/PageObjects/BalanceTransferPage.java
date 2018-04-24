package PageObjects;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class BalanceTransferPage {
	
	@FindBy(id = "amount")
    private WebElement balanceTransferField;
	
	@FindBy(id = typeDropdown_ID)
    private WebElement typeDropdown;
	
	
	@SuppressWarnings("unused")
	private final static String balanceTransferField_ID = "amount";
	private final static String typeDropdown_ID = "label";
	
	

}
