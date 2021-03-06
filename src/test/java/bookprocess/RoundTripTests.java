package bookprocess;

import common.Helper;
import constants.Constants;
import constants.FlightCodes;
import constants.TestData;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pageObjects.*;
import selenium.BaseClass;
import selenium.BaseTest;

public class RoundTripTests extends BaseTest {
    BaseClass baseClass;
    HomePageObject homePageObject;
    SearchFlightObject searchFlightObject;
    SearchResultPageObject searchResultPageObject;
    PqTripDetailedViewPageObject pqTripDetailedViewPageObject;
    BookPageObject bookPageObject;
    HeaderPageObject headerPageObject;
    PaymentInfoPageObject paymentInfoPageObject;
    BookSuccessPageObject bookSuccessPageObject;

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
        bookSuccessPageObject = new BookSuccessPageObject(driver);


    }

    @Test
    public void bookFlightNewYorkToLosAngelesAndBookTwoMonthAway() {

        String name = Helper.getRandomName();
        String surName = Helper.getRandomLastName();
        int flight = 0;

            String customDateDepart = Helper.getDateWithSpecificMonthsInFuture(Constants.TWO_MONTHS,"yyyy-MM-dd");
            String customDateReturn = Helper.getDateWithSpecificMonthsInFuture(Constants.THREE_MONTHS,"yyyy-MM-dd");
            String fullUrl = BaseClass.OOJO_URL+Helper.getFlightSearchResultRoundTrip(
                    FlightCodes.NEW_YORK_CODE,
                    FlightCodes.LOS_ANGELOS,
                    customDateDepart,
                    customDateReturn);

            logWrite.info("Open direct search url " + fullUrl);
            baseClass.openPage(
                    fullUrl);

            searchResultPageObject.waitForSearchLoad();
            logWrite.info("Accept cookies if there are any");
            headerPageObject.acceptCookies();
            headerPageObject.cancelMemberOffer();
            String pQFlightPrice = searchResultPageObject.getTripOptionPriceByIndex(flight).getText();
            logWrite.info("Flight price from the search screen: " + pQFlightPrice);
            String pQFlightDate = searchResultPageObject.getFlightStartDate(flight);
            logWrite.info("Flight date from the search screen: " + pQFlightDate);
            String pQFlightStartTime = searchResultPageObject.getBookScreenFlightStartTime(flight).getText();
            logWrite.info("Flight time from the search screen: " + pQFlightStartTime);
            logWrite.info("Select trip");
            headerPageObject.cancelMemberOffer();


            logWrite.info("Search stats: " + searchResultPageObject.getSearchStats());
            searchResultPageObject.selectTripOptionPq(flight);
            headerPageObject.cancelMemberOffer();
            logWrite.info("Assert that price from the list is equal with the price in overview screen");
            Assert.assertEquals(pqTripDetailedViewPageObject.getDetailedViewFlightPrice().getText(),pQFlightPrice);
            headerPageObject.cancelMemberOffer();
            logWrite.info("Click on book flight");
            pqTripDetailedViewPageObject.clickBookFlight();
            headerPageObject.waitForLoadingBeeToLoad();
            logWrite.info("Flight quick URL: " + baseClass.getCurrentUrl());

            String totalPrice = bookPageObject.getTotalPrice().getText();
            logWrite.info("Check that price from the search result matches the price on the book screen " + totalPrice);
            Assert.assertEquals(totalPrice,pQFlightPrice, "Price from flight form search result does not match price from book screen");

            logWrite.info("Fill clients info " + name + " "  + surName + " " + TestData.testEmailDynatech + " " + TestData.phoneNumber);
            bookPageObject.fillName(name)
                    .fillLastName(surName)
                    .fillEmail(TestData.testEmailDynatech)
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
                    .fillPaymentEmail(TestData.testEmailDynatech)
                    .fillZipCode(TestData.zipPostalCode)
                    .fillState(TestData.stateCalifornia)
                    .fillBillingPhone(TestData.phoneNumber);

            baseClass.switchToParentFrame();
            logWrite.info("Click on agree terms & conditions");
            //TODO: For now remove book
            bookPageObject.clickAgreeOnTerms()
                    .clickBook();
            logWrite.info("Cancel the protection");
            bookPageObject.confirmContacts(TestData.phoneNumber,TestData.testEmailDynatech);
            bookPageObject.cancelProtection();
            headerPageObject.waitForLoadingBeeToLoad();
            logWrite.info("Assert that book success message was shown");

            if (bookSuccessPageObject.getBookNumber().isDisplayed()){
                logWrite.info("New confirmation number:" + bookSuccessPageObject.getBookNumber().getText());
                Assert.assertEquals(bookSuccessPageObject.getBookNumber().getText(),"Something is not right...");
                Assert.assertEquals(bookSuccessPageObject.BookingNoText(),"Booking Reference #");
                Assert.assertTrue(bookSuccessPageObject.getBookNumber().isDisplayed());
            } else {
                logWrite.info("Old confirmation number: " + bookSuccessPageObject.getBookNumber().getText());
                Assert.assertEquals(bookSuccessPageObject.getBookNumber().getText(),"Hurray! Thank you for completing your booking!");
                Assert.assertTrue(bookSuccessPageObject.getBookNumber().isDisplayed());
            }

        }

    @AfterMethod
    public void quitDriver() {
        driver.quit();
    }
    }

