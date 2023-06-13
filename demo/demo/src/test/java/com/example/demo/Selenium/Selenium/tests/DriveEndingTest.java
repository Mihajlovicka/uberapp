package com.example.demo.Selenium.tests;

import com.example.demo.Selenium.pages.HomePage;
import org.testng.annotations.Test;

public class DriveEndingTest extends TestBase {
    @Test
    public void endDriveSuccessfully() throws InterruptedException {
        HomePage home = new HomePage(driver);


        home.openLogIn();
        home.insertUsername("jelenamanojlovic27062000@gmail.com");
        home.insertPassword("123");
        String res = home.doLogIn();

        assert (res.equals("SVE JE OK"));
        System.out.println(res);

        home.goToRide();
        assert(home.isRideOver());
        //home.closeError();
        Thread.sleep(1000);
        driver.get("http://localhost:4200/driver");
        Thread.sleep(2500);


    }
}
