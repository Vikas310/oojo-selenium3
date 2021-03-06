package pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import selenium.BaseClass;

public class HeaderPageObject extends BaseClass {

    public HeaderPageObject(WebDriver driver){
        super(driver);
    }

    @FindBy(xpath = "//*[@id='onetrust-accept-btn-handler']")
    public WebElement acceptCookiesButtonInput;

    @FindBy (xpath = "//a[contains(text(),'No thanks')]")
    public WebElement cancelMemberOfferInput;

    @FindBy(xpath = "//div[contains(@class,'loaderLarge__IconStyle')]")
    public WebElement bookLoaderBeeInput;

    @FindBy(xpath = "//*[contains(@class,'qa-mainLogo')]")
    public WebElement oojoMainLogoInput;

    @FindBy (xpath = "//*[contains(@class,'MuiCircularProgress-indeterminate')]")
    public WebElement verificationTypeSpinner;

    By loadingBee = By.xpath("//*[contains(@class,'loaderLarge__IconStyle')]");

    private WebElement getLoadingBeeIcon() {
        try{
            return driver.findElement(loadingBee);
        } catch (NoSuchElementException e) {
            return driver.findElement(loadingBee);
        }

    }

    public void acceptCookies(){
        if(this.waitForElementVisibility(acceptCookiesButtonInput, TIMEOUT_5)){
            logWrite.info("Cookies are present - accept cookies");
            acceptCookiesButtonInput.click();
        }
    }

    public void navigateToMainPage(){
        this.waitForElementToBeClickable(oojoMainLogoInput,TIMEOUT_10);
        oojoMainLogoInput.click();
    }

    public void cancelMemberOffer(){
        if(this.waitForElementVisibility(cancelMemberOfferInput,TIMEOUT_10)) {
            logWrite.info("Click No thanks in member offer popover");
            cancelMemberOfferInput.click();
        }
    }

    public void waitForLoadingBeeToLoad() {
        //this.waitForElementVisibility(this.getLoadingBeeIcon(),TIMEOUT_5);
        try {
            this.waitForElementInvisibility(this.getLoadingBeeIcon(),TIMEOUT_120);
        } catch (NoSuchElementException e) {

        }
    }

    public void waitForLoadingBeeToLoadAfterBook() {
        this.waitForElementVisibility(this.getLoadingBeeIcon(),TIMEOUT_10);
//        this.waitForElementInvisibility(this.getLoadingBeeIcon(),TIMEOUT_80);
    }

    public void waitForVerificationSpinnerToStop(){
        this.waitForElementInvisibility(verificationTypeSpinner,TIMEOUT_30);
    }
}
