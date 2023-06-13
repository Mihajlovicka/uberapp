package com.example.demo.Selenium.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class HomePage {
    private WebDriver driver;

    private static String PAGE_URL = "http://localhost:4200";

    @FindBy(xpath = "//body")
    WebElement page;

    private String transactionID;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        driver.get(PAGE_URL);

        PageFactory.initElements(driver, this);
    }

    public void openLogIn() {
        WebElement logInBtn = page.findElement(By.id("prijavaBtn"));

        (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.elementToBeClickable(logInBtn)).click();
    }

    public void insertUsername(String username) {
        WebElement usernameInput = page.findElement(By.xpath("//input[@name=\"username\"]"));
        usernameInput.clear();
        (new WebDriverWait(driver, Duration.ofSeconds(5)))
                .until(ExpectedConditions.elementToBeClickable(usernameInput)).sendKeys(username);


    }

    public void insertPassword(String password) {
        WebElement passwordInput = page.findElement(By.xpath("//input[@name=\"password\"]"));
        passwordInput.clear();
        (new WebDriverWait(driver, Duration.ofSeconds(5)))
                .until(ExpectedConditions.elementToBeClickable(passwordInput)).sendKeys(password);

    }


    public String doLogIn() {
        WebElement logInBtn = page.findElement(By.id("doLogInBtn"));

        (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.elementToBeClickable(logInBtn)).click();
        List<WebElement> pelements = page.findElements(By.xpath("//app-error-dialog//p"));

        for (WebElement e : pelements)
            if (e.getText().contains("Korisnicko ime ili lozinka nisu ispravni.")) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                closeError();
                return "ERROR";
            }

        return "SVE JE OK";
    }

    public void closeError() {
        WebElement okBtn = page.findElement(By.xpath("//app-error-dialog//button"));


        List<WebElement> pelements = page.findElements(By.xpath("//app-error-dialog//p"));

        for (WebElement e : pelements)
            if (e.getText().contains("Korisnicko ime ili lozinka nisu ispravni.")) {
                System.out.println("GRESKAAA");
                (new WebDriverWait(driver, Duration.ofSeconds(10)))
                        .until(ExpectedConditions.elementToBeClickable(okBtn)).click();
            }
    }

    private void clickButtonByXPath(String xpath) {
        WebElement button = page.findElements(By.xpath(xpath)).get(0);

        (new WebDriverWait(driver, Duration.ofSeconds(50)))
                .until(ExpectedConditions.visibilityOf(button)).click();
    }

    private void clickButtonByXPath(String xpath, int elementIndex) {
        WebElement button = page.findElements(By.xpath(xpath)).get(elementIndex);

        (new WebDriverWait(driver, Duration.ofSeconds(50)))
                .until(ExpectedConditions.visibilityOf(button)).click();
    }

    public void goToRide() throws InterruptedException {
        clickButtonByXPath("//button//span[contains(text(),'Kreni na adresu')]");

        Thread.sleep(1500);
        clickButtonByXPath("//button//span[contains(text(),'Stigao na odrediste')]");

        Thread.sleep(1500);
        clickButtonByXPath("//button//span[contains(text(),'Pocni voznju')]");

        Thread.sleep(1500);
        WebElement okBtn = page.findElement(By.xpath("//button//span[contains(text(), 'Ok')]"));
        (new WebDriverWait(driver, Duration.ofSeconds(30)))
                .until(ExpectedConditions.elementToBeClickable(okBtn)).click();

        clickButtonByXPath("//button//span[contains(text(),'Zavrsi voznju')]");
    }

    public boolean isRideOver() {
        WebElement webElement = page.findElements(By.xpath("//app-error-dialog//p")).get(0);
        WebElement okBtn = page.findElement(By.xpath("//button//span[contains(text(), 'Ok')]"));
        (new WebDriverWait(driver, Duration.ofSeconds(30)))
                .until(ExpectedConditions.elementToBeClickable(okBtn)).click();
        return webElement.getText().equals("Zavrsena voznja.");
    }
    public void insertAddress1(String adresa1, int addresCount) {
        WebElement appAddressItem1 = page.findElements(By.xpath("//app-address-item//input")).get(addresCount);

        appAddressItem1.clear();
        (new WebDriverWait(driver, Duration.ofSeconds(50)))
                .until(ExpectedConditions.elementToBeClickable(appAddressItem1)).sendKeys(adresa1);
    }
    public void selectAddress(String street) {
        WebElement address = page.findElements(By.xpath("//mat-option//span[contains(text(),'" + street + "')]")).get(0);

        (new WebDriverWait(driver, Duration.ofSeconds(50)))
                .until(ExpectedConditions.visibilityOf(address)).click();
    }
    public void showRoute() {
        clickButtonByXPath("//button//span[contains(text(),'Prikazi putanju')]");
    }
    public void openRoutes() {
        clickButtonByXPath("//button//span[contains(text(),'Izaberi putanju')]");
    }

    public void selectRoute() {
        clickButtonByXPath("//span[contains(text(),'Optimizuj put vremenski')]");
    }
    public void confirmRoute() {
        clickButtonByXPath("//button//span[contains(text(),'Potvrdi')]");
    }

    public void chooseRoute() {
        openRoutes();
        selectRoute();
        confirmRoute();
    }
    public void goToPayment() throws InterruptedException {
        clickButtonByXPath("//button//span[contains(text(),'Dalje')]");


        //clickButtonByXPath("//mat-card-title[contains(text(),'Hatchback')]");

        //clickButtonByXPath("//mat-panel-title[contains(text(),'Datum voznje')]");
        //clickButtonByXPath("//mat-panel-title[contains(text(),'Deca')]");
        //clickButtonByXPath("//mat-panel-title[contains(text(),'Kucni ljubimci')]");
        //clickButtonByXPath("//button//span[contains(text(),'Sledeci')]",3);

        Thread.sleep(1500);
        clickButtonByXPath("//mat-step-header//span",2);

        Thread.sleep(1500);
        clickButtonByXPath("//mat-step-header//span",3);


    }
    public void doPayment(){
        clickButtonByXPath("//button//span[contains(text(),'Potvrdi placanje')]");

    }
    public boolean getTransactionID(){
        WebElement message = page.findElement(By.xpath("//app-error-dialog//p"));
        if(message.getText().contains("neuspesno")) return false;
        WebElement okBtn = page.findElement(By.xpath("//button//span[contains(text(), 'Ok')]"));
        (new WebDriverWait(driver, Duration.ofSeconds(30)))
                .until(ExpectedConditions.visibilityOf(message)).click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.transactionID = message.getText().split(":")[1].strip();
        return true;
    }
    public void acceptPayment(boolean isOwnerPaying) throws InterruptedException {
        if(!isOwnerPaying)
            this.transactionID = String.valueOf(Integer.valueOf(transactionID) + 1);
        driver.get("http://localhost:4200/passenger/confirmPayment/"+transactionID);

        Thread.sleep(1500);
        clickButtonByXPath("//button[contains(text(),'Odobri')]");
    }
    public void openAllNotifications() throws InterruptedException {
        clickButtonByXPath("//mat-icon[contains(text(),'notifications')]");
        Thread.sleep(1500);
    }
    public boolean checkNotificationsForNotEnoughMoney() throws InterruptedException {
        openAllNotifications();

        WebElement notifikacija = page.findElements(By.xpath("//app-notifications//mat-card-subtitle")).get(0);
        clickButtonByXPath("//app-notifications//mat-card-subtitle",0);
        return notifikacija.getText().contains("placanje nije izvrseno");
    }

    public void addPeopleToRide(String email) throws InterruptedException {
        clickButtonByXPath("//button//span[contains(text(),'Dalje')]");
        Thread.sleep(1500);
        clickButtonByXPath("//mat-step-header//span",2);
        WebElement appAddressItem1 = page.findElements(By.xpath("//mat-form-field//input[contains(@class,'pretragaKlijenta')]")).get(0);

        appAddressItem1.clear();
        Thread.sleep(1500);
        (new WebDriverWait(driver, Duration.ofSeconds(50)))
                .until(ExpectedConditions.elementToBeClickable(appAddressItem1)).sendKeys(email);

        clickButtonByXPath("//mat-option//span[contains(text(),'"+ email +"')]");

        Thread.sleep(1500);
        clickButtonByXPath("//mat-step-header//span",3);

    }

    public void doLogOut() throws InterruptedException {
        Thread.sleep(800);
        clickButtonByXPath("//button//mat-icon[contains(text(),'menu')]");
        Thread.sleep(800);

        clickButtonByXPath("//button[contains(text(),'Odjava')]");
    }
    public void openNovaVoznjaNotification() throws InterruptedException {
        openAllNotifications();
        clickButtonByXPath("//mat-card-title[contains(text(),'Nova voznja')]",0);
    }
    public void acceptRide(){
        clickButtonByXPath("//button//span[contains(text(),'Prihvatam voznju')]");
    }

}



