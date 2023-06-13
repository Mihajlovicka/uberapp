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
}



