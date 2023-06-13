package com.example.demo.Selenium.tests;

import com.example.demo.Selenium.pages.HomePage;
import org.testng.annotations.Test;

public class DriveReservationTest extends TestBase{
    @Test
    public void orderDriveForOneSuccessfully() throws InterruptedException {
        HomePage home = new HomePage(driver);


        home.openLogIn();
        home.insertUsername("srki0505@gmail.com");
        home.insertPassword("123");
        String res = home.doLogIn();

        assert(res.equals("SVE JE OK"));
        System.out.println(res);

        home.insertAddress1("futoska 11",0);

        Thread.sleep(1500);
        home.selectAddress("Futoska");

        home.insertAddress1("jevrejska",1);

        Thread.sleep(1500);
        home.selectAddress("Jevrejska");

        home.showRoute();
        Thread.sleep(1500);
        home.chooseRoute();
        Thread.sleep(1500);
        home.goToPayment();
        Thread.sleep(2000);
        home.doPayment();
        Thread.sleep(5000);
        //assert(home.isReserved());
        assert(home.getTransactionID());
        home.acceptPayment(true);
        Thread.sleep(2000);

    }

    @Test
    public void orderDriveForOneFail_NotEnoughMoney() throws InterruptedException {
        HomePage home = new HomePage(driver);


        home.openLogIn();
        home.insertUsername("srki0505@gmail.com");
        home.insertPassword("123");
        String res = home.doLogIn();

        assert(res.equals("SVE JE OK"));
        System.out.println(res);

        home.insertAddress1("futoska 11",0);

        Thread.sleep(1500);
        home.selectAddress("Futoska");

        home.insertAddress1("jevrejska",1);

        Thread.sleep(1500);
        home.selectAddress("Jevrejska");

        home.showRoute();
        Thread.sleep(1500);
        home.chooseRoute();
        Thread.sleep(1500);
        home.goToPayment();
        Thread.sleep(2000);
        home.doPayment();
        Thread.sleep(5000);
        //assert(home.isReserved());
        assert(home.getTransactionID()==false);
        assert(home.checkNotificationsForNotEnoughMoney());
    }

    @Test
    public void orderDriveForTwoPeopleOwnerPayingSuccessfully() throws InterruptedException {
        HomePage home = new HomePage(driver);


        home.openLogIn();
        home.insertUsername("srki0505@gmail.com");
        home.insertPassword("123");
        String res = home.doLogIn();

        assert(res.equals("SVE JE OK"));
        System.out.println(res);

        home.insertAddress1("futoska 11",0);

        Thread.sleep(1500);
        home.selectAddress("Futoska");

        home.insertAddress1("jevrejska",1);

        Thread.sleep(1500);
        home.selectAddress("Jevrejska");

        home.showRoute();
        Thread.sleep(1500);
        home.chooseRoute();
        Thread.sleep(1500);
        home.addPeopleToRide("pera@gmail.com");
        Thread.sleep(2000);
        home.doPayment();
        Thread.sleep(5000);
        //assert(home.isReserved());

        assert(home.getTransactionID());
        home.acceptPayment(true);
        Thread.sleep(2000);
        home.doLogOut();
        home.openLogIn();
        home.insertUsername("pera@gmail.com");
        home.insertPassword("123");
        res = home.doLogIn();

        assert(res.equals("SVE JE OK"));
        System.out.println(res);
        home.openNovaVoznjaNotification();
        home.acceptRide();
    }


    
    @Test
    public void orderDriveForTwoPeopleOwnerPayingSuccessfully_PassengerDeclinesRide() throws InterruptedException
    {
        HomePage home = new HomePage(driver);


        home.openLogIn();
        home.insertUsername("srki0505@gmail.com");
        home.insertPassword("123");
        String res = home.doLogIn();

        assert(res.equals("SVE JE OK"));
        System.out.println(res);

        home.insertAddress1("futoska 11",0);

        Thread.sleep(1500);
        home.selectAddress("Futoska");

        home.insertAddress1("jevrejska",1);

        Thread.sleep(1500);
        home.selectAddress("Jevrejska");

        home.showRoute();
        Thread.sleep(1500);
        home.chooseRoute();
        Thread.sleep(1500);
        home.addPeopleToRide("pera@gmail.com");
        Thread.sleep(2000);
        home.doPayment();
        Thread.sleep(5000);
        //assert(home.isReserved());

        assert(home.getTransactionID());
        home.acceptPayment(true);
        Thread.sleep(2000);
        home.doLogOut();
        home.openLogIn();
        home.insertUsername("pera@gmail.com");
        home.insertPassword("123");
        res = home.doLogIn();

        assert(res.equals("SVE JE OK"));
        System.out.println(res);
        home.openNovaVoznjaNotification();
        home.declineRide();
        Thread.sleep(1000);

        home.doLogOut();

        home.openLogIn();
        home.insertUsername("srki0505@gmail.com");
        home.insertPassword("123");
        Thread.sleep(800);
        res = home.doLogIn();

        assert(res.equals("SVE JE OK"));
        System.out.println(res);

        home.openPutnikOdustaoNotification();
        home.continueRide();
        Thread.sleep(2000);
    }

    @Test
    public void orderDriveForTwoPeopleSplitPaymentSuccessfully() throws InterruptedException {
        HomePage home = new HomePage(driver);
        home.openLogIn();
        home.insertUsername("srki0505@gmail.com");
        home.insertPassword("123");
        String res = home.doLogIn();

        assert(res.equals("SVE JE OK"));
        System.out.println(res);

        home.insertAddress1("futoska 11",0);

        Thread.sleep(1500);
        home.selectAddress("Futoska");

        home.insertAddress1("jevrejska",1);

        Thread.sleep(1500);
        home.selectAddress("Jevrejska");

        home.showRoute();
        Thread.sleep(1500);
        home.chooseRoute();
        Thread.sleep(1500);
        home.addPeopleToRide("pera@gmail.com");
        Thread.sleep(2000);
        home.splitPayment();
        home.doPayment();
        Thread.sleep(5000);
        //assert(home.isReserved());

        home.getTransactionID();
        home.acceptPayment(true);
        Thread.sleep(2000);
        home.doLogOut();
        home.openLogIn();
        home.insertUsername("pera@gmail.com");
        home.insertPassword("123");
        res = home.doLogIn();

        assert(res.equals("SVE JE OK"));
        System.out.println(res);
        home.openNovaVoznjaNotification();
        home.acceptRide();

        home.acceptPayment(false);
    }

   }
