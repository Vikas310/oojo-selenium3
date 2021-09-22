package LandingPage;

import common.Helper;
import common.Path;
import constants.Constants;
import constants.FlightCodes;
import constants.TestData;
import org.junit.experimental.theories.Theories;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pageObjects.*;
import selenium.BaseClass;
import selenium.BaseTest;

import java.awt.print.Book;

public class SearchResultPageTests extends BaseTest {

    BaseClass baseClass;
    HomePageObject homePageObject;
    SearchFlightObject searchFlightObject;
    SearchResultPageObject searchResultPageObject;
    PqTripDetailedViewPageObject pqTripDetailedViewPageObject;
    BookPageObject bookPageObject;
    HeaderPageObject headerPageObject;
    PaymentInfoPageObject paymentInfoPageObject;


    @BeforeMethod(alwaysRun = true)
    public void initiate() {
        driver = getDriver();
        baseClass = new BaseClass(driver);
        baseClass.getEnvironment();
        homePageObject = new HomePageObject(driver);
        searchFlightObject = new SearchFlightObject(driver);
        searchResultPageObject = new SearchResultPageObject(driver);
        pqTripDetailedViewPageObject = new PqTripDetailedViewPageObject(driver);
        bookPageObject = new BookPageObject(driver);
        headerPageObject = new HeaderPageObject(driver);
        paymentInfoPageObject = new PaymentInfoPageObject(driver);


    }

    @Test
    public void searchResultFlight() {

        String name = "Gennadijs";
        String surName = "Strushkins";
        String email = name + surName+"@Dynatech.com";
        int pqNumber = 0;

        String customDate = Helper.getDateWithSpecificMonthsInFuture(Constants.FOUR_MONTHS,"yyyy-MM-dd");
        String fullUrl = Path.OOJO_URL+Helper.getFlightSearchResultOneWay(
                FlightCodes.DALLAS_CODE,
                FlightCodes.MANCHESTER_CODE,
                customDate);

        logWrite.info("Open direct search url " + fullUrl);
        baseClass.openPage(
                fullUrl);

        searchResultPageObject.waitForSearchLoad();
        logWrite.info("Accept cookies if there are any");
        headerPageObject.acceptCookies();

        String pQFlightPrice = searchResultPageObject.getTripOptionPriceByIndex(pqNumber).getText();
        logWrite.info("Select trip");
        searchResultPageObject.selectTripOptionPq(pqNumber);
        logWrite.info("Assert that price from the list is equal with the price in overview screen");
        Assert.assertEquals(pqTripDetailedViewPageObject.getDetailedViewFlightPrice().getText(),pQFlightPrice);

        logWrite.info("Click on book flight");
        pqTripDetailedViewPageObject.clickBookFlight();
        headerPageObject.waitForLoadingBeeToLoad();
        logWrite.info("Click on check more flights");

        logWrite.info("Fill clients info");
        bookPageObject.fillName(name)
                .fillLastName(TestData.nameOnCardJarvis)
                .fillEmail(email)
                .selectGender("male")
                .fillPhone(TestData.phoneNumber)
                .clickOnDateOfBirth()
                .fillDateOfBirth("Jan","15","2000")
                .selectNoToPriceDropAssurance();

        baseClass.switchToiFrame(PaymentInfoPageObject.iFrame);
        logWrite.info("Fill card information & other client data");
        paymentInfoPageObject.fillCardNumber(TestData.cardNumber)
                .fillCardName(TestData.nameOnCardJarvis)
                .fillCardExpDate(TestData.cardExpirationDate)
                .fillSecurityCode(TestData.cardSecurityCode)
                .fillAddress(TestData.streetAddress)
                .fillCity(TestData.cityLosAngelos)
                .fillPaymentEmail(TestData.carHolderSkyWalker)
                .fillZipCode(TestData.zipPostalCode)
                .fillState(TestData.stateCalifornia)
                .fillBillingPhone(TestData.phoneNumber);

        baseClass.switchToParentFrame();
        logWrite.info("Click on agree terms & conditions");
        bookPageObject.clickAgreeOnTerms()
                .clickBook();
        headerPageObject.waitForLoadingBeeToLoad();
        Assert.assertTrue(bookPageObject.getBookSuccessMessage().isDisplayed(), "Book success message was not present");



    }

    @AfterMethod
    public void quit() {
        driver.quit();
    }
}