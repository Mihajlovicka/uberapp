package com.example.demo.Selenium.tests;


import com.example.demo.Selenium.pages.HomePage;
import org.testng.annotations.Test;

public class LoginTest extends TestBase {

    @Test
    public void LoginSuccesful() throws InterruptedException {
        HomePage home = new HomePage(driver);


        home.openLogIn();

        Thread.sleep(1000);
        home.insertUsername("srki0505@gmail.com");

        Thread.sleep(1000);
        home.insertPassword("123");
        Thread.sleep(1000);
        String res = home.doLogIn();

        assert(res.equals("SVE JE OK"));
        System.out.println(res);
        Thread.sleep(3000);

    }

    @Test
    public void LoginUnSuccesful() throws InterruptedException {
        HomePage home = new HomePage(driver);


        home.openLogIn();
        Thread.sleep(1000);
        home.insertUsername("srki0505@gmail.com");
        Thread.sleep(1000);
        home.insertPassword("pogresna lozinka");
        Thread.sleep(1000);
        String res = home.doLogIn();
        assert(res.equals("ERROR"));
        System.out.println(res);
        Thread.sleep(3000);



    }



}
