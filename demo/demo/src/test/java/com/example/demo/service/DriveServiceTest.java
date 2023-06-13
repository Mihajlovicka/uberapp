package com.example.demo.service;

import com.example.demo.exception.DriveNotFoundException;
import com.example.demo.exception.EmailNotFoundException;
import com.example.demo.exception.NotDrivePassengerException;
import com.example.demo.exception.TransactionIdDoesNotExistException;
import com.example.demo.fakeBank.BankService;
import com.example.demo.fakeBank.BankTransaction;
import com.example.demo.fakeBank.ClientsBankAccount;
import com.example.demo.model.*;
import com.example.demo.repository.DriveRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class DriveServiceTest {

    @Mock
    private NotificationService notificationService;
    @Mock
    private DriveRepository driveRepository;
    @Mock
    private BankService bankService;
    @Mock
    private RideSimulationService rideSimulationService;
    @Mock
    private UserService userService;
    @Spy
    @InjectMocks
    private DriveService driveService;

    DriversAccount driver;
    User user;
    String email = "test@example.com";
    List<User> users = new ArrayList<>();
    Drive d;
    RealAddress stop1, stop2;


    @BeforeEach
    public void setUp() {
        driver = new DriversAccount();
        user = new User();
        user.setEmail(email);
        driver.setId(1L);
        driver.setUser(user);
        d = new Drive();
        d.setId(1L);
    }
//start drive
    @Test
    public void shouldStartDrive() throws EmailNotFoundException {
        Passenger p1 = new Passenger();
        p1.setPassengerEmail("passenger");
        ClientsAccount clientsAccount1 = new ClientsAccount();
        Set passengers = new HashSet<Passenger>();
        passengers.add(p1);
        d.setPassengers(passengers);
        d.setDriveType(DriveType.NOW);

        ClientsAccount clientsAccountOwner = new ClientsAccount();
        d.setOwner(clientsAccountOwner);
        d.setOwnerTransactionId(1L);
        d.setSplitBill(false);

        doReturn(driver).when(userService).getLoggedDriver();
        doReturn(d).when(driveRepository).save(d);
        doReturn(new RideSimulation()).when(rideSimulationService).createRideSim(any());
        doReturn(new BankTransaction()).when(bankService).transactionFinalized(1L);
        doReturn(Arrays.asList(d)).when(driveRepository).findByDriver(driver);
        doReturn(clientsAccountOwner).when(userService).saveCurrent(d.getOwner());
        doReturn(clientsAccount1).when(userService).findClientsAccount("passenger");
        doReturn(clientsAccount1).when(userService).saveCurrent(clientsAccount1);
        doNothing().when(userService).updateDriverStatus(DriverStatus.BUSY);

        driveService.startDrive();

        assertEquals(d.getDriveStatus(), DriveStatus.DRIVE_STARTED);
        assertNotNull(d.getStartDate());
        assertTrue(d.getOwner().isInDrive());

        verify(driveRepository, times(1)).save(any());
        verify(userService, times(2)).saveCurrent(any());
        verify(bankService, times(1)).transactionFinalized(any());
        verify(userService, times(1)).updateDriverStatus(any());
    }

    @Test
    public void shouldNotStartDriveIsNull() throws EmailNotFoundException {

        doReturn(driver).when(userService).getLoggedDriver();
        doReturn(new ArrayList<>()).when(driveRepository).findByDriver(driver);

        driveService.startDrive();

        verify(bankService, times(0)).transactionFinalized(any());
        verify(driveRepository, times(0)).save(d);
        verify(userService, times(0)).updateDriverStatus(any());
        verify(userService, times(1)).getLoggedDriver();
        verify(userService, times(0)).saveCurrent(any());

    }

    @Test
    public void shouldReturnDrive() throws DriveNotFoundException {
        doReturn(Optional.of(d)).when(driveRepository).findById(1L);

        Drive newDrive = driveService.getDrive(1L);

        assertNotNull(newDrive);
        assertEquals(newDrive.getId(), 1L);
    }

    @Test
    public void shouldThrowExceptionNoDrive() {
        doReturn(Optional.empty()).when(driveRepository).findById(1L);

        assertThrows(DriveNotFoundException.class, () -> driveService.getDrive(1L));

    }
 //FindAvailableDriver
    @Test
    public void shouldFindAvailableDriverForCurrentDrive() throws IOException, InterruptedException, TransactionIdDoesNotExistException, URISyntaxException {
        d.setDriveType(DriveType.NOW);

        makeLocation();

        doReturn(new ArrayList<>()).when(driveRepository).findByDriver(driver);
        doReturn(d).when(driveRepository).save(d);
        doReturn(Arrays.asList(driver)).when(userService).getDriversByStatusAndAvailability(true, DriverStatus.AVAILABLE);
        doReturn(driver).when(userService).getDriver(anyString());
        doReturn(new Notification()).when(notificationService).addNotification(any());
        doNothing().when(notificationService).addNotificationMultiple(any(), any());

        HttpClient mockHttpClient = mock(HttpClient.class);
        HttpResponse<String> mockResponse = mock(HttpResponse.class);
        when(mockResponse.body()).thenReturn(getResponse());

        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        Drive newDrive = driveService.findAvailableDriver(d);

        assertEquals(d.getDriver().getUser().getEmail(), newDrive.getDriver().getUser().getEmail());

        verify(driveRepository, times(2)).save(any());
        verify(notificationService, times(1)).addNotification(any());
        verify(notificationService, times(1)).addNotificationMultiple(any(), any());
    }

    @Test
    public void shouldFindAvailableDriverForFutureDrive() throws IOException, InterruptedException, TransactionIdDoesNotExistException, URISyntaxException {
        d.setDriveType(DriveType.FUTURE);

        makeLocation();

        doReturn(new ArrayList<>()).when(driveRepository).findByDriver(driver);
        doReturn(d).when(driveRepository).save(d);
        doReturn(Arrays.asList(driver)).when(userService).getDrivers();
        doReturn(driver).when(userService).getDriver(anyString());
        doReturn(new Notification()).when(notificationService).addNotification(any());
        doNothing().when(notificationService).addNotificationMultiple(any(), any());

        HttpClient mockHttpClient = mock(HttpClient.class);
        HttpResponse<String> mockResponse = mock(HttpResponse.class);
        when(mockResponse.body()).thenReturn(getResponse());

        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        Drive newDrive = driveService.findAvailableDriver(d);

        assertEquals(d.getDriver().getUser().getEmail(), newDrive.getDriver().getUser().getEmail());

        verify(driveRepository, times(1)).save(any());
        verify(notificationService, times(1)).addNotification(any());
        verify(notificationService, times(1)).addNotificationMultiple(any(), any());
    }

    @Test
    public void shouldMakeFailedDriveNoDriverFound() throws IOException, InterruptedException, TransactionIdDoesNotExistException, URISyntaxException {
        d.setDriveType(DriveType.FUTURE);

        makeLocation();

        Drive oldDrive = new Drive();
        oldDrive.setDate(new Date(System.currentTimeMillis() + 7 * 1000));
        oldDrive.setDuration(5.0);

        doReturn(Arrays.asList(oldDrive)).when(driveRepository).findByDriver(driver);
        doReturn(d).when(driveRepository).save(d);
        doReturn(Arrays.asList(driver)).when(userService).getDrivers();
        doNothing().when(notificationService).addNotificationMultiple(any(), any());
        doReturn(new BankTransaction()).when(bankService).declineTransaction(any(), anyString());

        Drive newDrive = driveService.findAvailableDriver(d);

        assertNull(newDrive.getDriver());
        assertEquals(newDrive.getDriveStatus(), DriveStatus.DRIVE_FAILED);

        verify(driveRepository, times(1)).save(any());
        verify(notificationService, times(1)).addNotificationMultiple(any(), any());
        verify(bankService, times(1)).declineTransaction(any(), any());

    }
//
    @Test
    public void shouldCancelDriveByClient() {

        doNothing().when(notificationService).notifyCanceledDrive(d);
        doReturn(d).when(driveRepository).save(d);

        Drive newDrive = driveService.cancelDrive(d);

        verify(notificationService, times(1)).notifyCanceledDrive(any());
        verify(driveRepository, times(1)).save(any());

        assertEquals(newDrive.getDriveStatus(), DriveStatus.DRIVE_FAILED);

    }

    @Test
    public void shouldCancelDrive() throws EmailNotFoundException {
        ClientsAccount c = new ClientsAccount();
        Passenger p = new Passenger();
        HashSet<Passenger> set = new HashSet<>();
        c.setId(1L);
        p.setId(2L);
        set.add(p);
        Notification n = new Notification();
        d.setDriver(driver);
        d.setOwner(c);
        d.setPassengers(set);
        d.setDriveType(DriveType.NOW);
        List<Drive> drives = List.of(d);


        doReturn(driver).when(userService).getLoggedDriver();
        doReturn(drives).when(driveRepository).findByDriver(driver);
        doNothing().when(notificationService).addNotificationMultiple(n, users);
        doReturn(new BankTransaction()).when(bankService).transactionFinalized(c.getId());
        doReturn(d).when(driveRepository).save(d);
        doNothing().when(userService).changeDriverStatus(any(), any());
        doReturn(new ArrayList<>()).when(userService).getAdmins();

        driveService.cancelDrive("");

        verify(notificationService, times(2)).addNotificationMultiple(any(), any());
        verify(bankService, atLeast(1)).transactionFinalized(any());
        verify(driveRepository, times(1)).save(d);
        verify(userService, times(1)).changeDriverStatus(any(), any());
        verify(userService, times(1)).getLoggedDriver();

        assertEquals(d.getDriveStatus(), DriveStatus.DRIVE_REJECTED);
        assertEquals(d.getDriveType(), DriveType.PAST);
    }

    @Test
    public void shouldDoNothingDriveIsNull() throws EmailNotFoundException {

        doReturn(driver).when(userService).getLoggedDriver();
        doReturn(new ArrayList<>()).when(driveRepository).findByDriver(driver);

        driveService.cancelDrive("");

        verify(notificationService, times(0)).addNotificationMultiple(any(), any());
        verify(bankService, atLeast(0)).transactionFinalized(any());
        verify(driveRepository, times(0)).save(d);
        verify(userService, times(0)).changeDriverStatus(any(), any());
        verify(userService, times(1)).getLoggedDriver();

    }

    @AfterEach
    private void tear() {
    }

    private void makeLocation() {
        stop1 = new RealAddress();
        //1l, "Cankreva 22 Novi Sad 21000", 45.2322, 65.99, "Cankreva 22 Novi Sad 21000"
        stop1.setId(1);
        stop1.setAddress("Brace Dronjak ");
        stop1.setLocation(new Location(45.254125, 19.796972));
        stop2 = new RealAddress();
        //1l, "Cankreva 22 Novi Sad 21000", 45.2322, 65.99, "Cankreva 22 Novi Sad 21000"
        stop2.setId(2);
        stop2.setAddress("Cankareva ");
        stop2.setLocation(new Location(45.257101, 19.812107));

        ClientsAccount clientsAccount = new ClientsAccount();
        User u = new User();
        u.setEmail("neki@neki.com");
        clientsAccount.setUser(u);
        ClientsBankAccount account = new ClientsBankAccount();
        account.setAccountNumber("number");
        clientsAccount.setClientsBankAccount(account);

        Car car = new Car();
        car.setId(1L);
        car.setCurrentLocation(new Location(45.254125, 19.796972));

        driver.setCar(car);

        d.setPassengers(new HashSet<>());
        d.setOwner(clientsAccount);
        d.setDuration(5.0);
        d.setStops(Arrays.asList(stop1, stop2));
        d.setDate(new Date(System.currentTimeMillis() + 5 * 1000));
    }

    private String getResponse() {

        return "{\"code\":\"Ok\",\"distances\":[[0]],\"destinations\":[{\"hint\":\"w_GagSssboimAwAAkgQAAF4WAAC0QQAAlYiyRIVE30TyrghGjcjIRqYDAACSBAAAXhYAALRBAAAfGwAAHNixAj1KLwHthbIC7BMuAQMA7wMHTzZ8\",\"distance\":9953.393487108,\"name\":\"\",\"location\":[45.209628,19.876413]}],\"durations\":[[0]],\"sources\":[{\"hint\":\"w_GagSssboimAwAAkgQAAF4WAAC0QQAAlYiyRIVE30TyrghGjcjIRqYDAACSBAAAXhYAALRBAAAfGwAAHNixAj1KLwHthbIC7BMuAQMA7wMHTzZ8\",\"distance\":9953.393487108,\"name\":\"\",\"location\":[45.209628,19.876413]}]}";
    }

    //declineDriveParticipation
    @Test
    public void shouldThrowNotPassengerDeclineParticipation() {

        d.setPassengers(new HashSet<>());

        doReturn(user).when(userService).getLoggedUser();
        doReturn(Optional.of(d)).when(driveRepository).findById(any());

        assertThrows(NotDrivePassengerException.class, () -> driveService.declineDriveParticipation(1L));

    }

    @Test
    public void shouldDoNothingNotEveryoneAnsweredDeclineDriveParticipation() throws DriveNotFoundException, NotDrivePassengerException, TransactionIdDoesNotExistException {
        User u = new User();
        u.setEmail("passenger");
        Passenger p1 = new Passenger();
        p1.setPassengerEmail("passenger");
        Passenger p2 = new Passenger();
        p2.setPassengerEmail("passenger2");
        p2.setContribution(DrivePassengerStatus.WAITING);
        Set passengers = new HashSet<Passenger>();
        passengers.add(p1);
        passengers.add(p2);
        d.setPassengers(passengers);


        doReturn(u).when(userService).getLoggedUser();
        doReturn(Optional.of(d)).when(driveRepository).findById(any());
        doReturn(d).when(driveRepository).save(d);


        Drive changed = driveService.declineDriveParticipation(1L);

        for (Passenger pas : changed.getPassengers()) {
            if (pas.getPassengerEmail().equals("passenger")) {
                assertEquals(pas.getContribution(), DrivePassengerStatus.REJECTED);
                assertEquals(pas.getPayment(), PaymentPassengerStatus.REJECTED);
            }
        }
        assertNotEquals(changed.getDriveStatus(), DriveStatus.OWNER_PAYMENT_WAITING);
        verify(driveRepository, times(1)).save(any());
        verify(bankService, never()).declineTransaction(any(), any());

    }

    @Test
    public void shouldDeclineDriveParticipation() throws DriveNotFoundException, NotDrivePassengerException, TransactionIdDoesNotExistException {
        User u = new User();
        u.setEmail("passenger");
        Passenger p1 = new Passenger();
        p1.setPassengerEmail("passenger");
        Passenger p2 = new Passenger();
        p2.setPassengerEmail("passenger2");
        p2.setContribution(DrivePassengerStatus.ACCEPTED);
        p2.setPayment(PaymentPassengerStatus.WAITING);
        Set passengers = new HashSet<Passenger>();
        passengers.add(p1);
        passengers.add(p2);
        d.setPassengers(passengers);


        ClientsBankAccount clientsBankAccount = new ClientsBankAccount();
        clientsBankAccount.setAccountNumber("111");
        ClientsAccount owner = new ClientsAccount();
        owner.setClientsBankAccount(clientsBankAccount);
        owner.setUser(u);
        d.setOwner(owner);
        d.setPrice(500.0);

        doReturn(u).when(userService).getLoggedUser();
        doReturn(Optional.of(d)).when(driveRepository).findById(any());
        doReturn(d).when(driveRepository).save(d);
        doReturn(new BankTransaction()).when(bankService).declineTransaction(any(),any());
        doNothing().when(notificationService).notifyOwnerDriveChanged(any(),any());

        Drive changed = driveService.declineDriveParticipation(1L);

        for (Passenger pas : changed.getPassengers()) {
            if (pas.getPassengerEmail().equals("passenger")) {
                assertEquals(pas.getContribution(), DrivePassengerStatus.REJECTED);
                assertEquals(pas.getPayment(), PaymentPassengerStatus.REJECTED);
            }
        }
        assertEquals(changed.getDriveStatus(), DriveStatus.OWNER_PAYMENT_WAITING);
        verify(driveRepository, times(3)).save(any());
        verify(bankService, times(1)).declineTransaction(any(), any());
        verify(notificationService, times(1)).notifyOwnerDriveChanged(any(), any());

    }
    // continueWithDrive
    @Test
    public void shouldContinueWithDriveOwnerCanAfford() throws EmailNotFoundException {
        ClientsAccount cl = new ClientsAccount();
        cl.setUser(user);
        d.setOwner(cl);
        d.setOwnerDebit(500.0);
        BankTransaction bankTransaction = new BankTransaction();
        bankTransaction.setId(1L);

        doReturn(true).when(userService).canAffordDrive(user.getEmail(), d.getOwnerDebit());
        doReturn(bankTransaction).when(bankService).requestOwnerPayment(any());
        doReturn(d).when(driveRepository).save(d);

        Drive newDrive = driveService.continueWithDrive(d);

        assertEquals(newDrive.getDriveStatus(), DriveStatus.OWNER_PAYMENT_WAITING);
        assertEquals(newDrive.getOwnerTransactionId(), 1L);
        verify(driveRepository, times(1)).save(d);

    }

    @Test
    public void shouldNotContinueWithDriveOwnerCantAfford() throws EmailNotFoundException {
        ClientsAccount cl = new ClientsAccount();
        cl.setUser(user);
        d.setOwner(cl);
        d.setOwnerDebit(500.0);

        doReturn(false).when(userService).canAffordDrive(user.getEmail(), d.getOwnerDebit());
        doReturn(d).when(driveRepository).save(d);
        doNothing().when(notificationService).paymentFailedDriveCanceledNotify(anyString());

        Drive newDrive = driveService.continueWithDrive(d);

        assertEquals(newDrive.getDriveStatus(), DriveStatus.DRIVE_FAILED);
        verify(driveRepository, times(1)).save(d);

    }
    //acceptDriveParticipation
    @Test
    public void shouldThrowErrorAcceptDriveParticipation() {

        d.setPassengers(new HashSet<>());

        doReturn(user).when(userService).getLoggedUser();
        doReturn(Optional.of(d)).when(driveRepository).findById(any());

        assertThrows(NotDrivePassengerException.class, () -> driveService.acceptDriveParticipation(1L));

    }

    @Test
    public void shouldDoNothingAcceptDriveParticipation() throws EmailNotFoundException, DriveNotFoundException, NotDrivePassengerException, URISyntaxException, IOException, TransactionIdDoesNotExistException, InterruptedException {
        User user = new User();
        user.setEmail("email@email.com");
        Passenger p = new Passenger();
        p.setPassengerEmail("email@email.com");
        Passenger p1 = new Passenger();
        p1.setPassengerEmail("nekidrugi.com");
        p1.setContribution(DrivePassengerStatus.WAITING);
        Set passengers = new HashSet<Passenger>();
        passengers.add(p);
        passengers.add(p1);
        d.setPassengers(passengers);

        doReturn(user).when(userService).getLoggedUser();
        doReturn(Optional.of(d)).when(driveRepository).findById(any());
        doReturn(d).when(driveRepository).save(d);

        Drive newDrive = driveService.acceptDriveParticipation(1L);

        for (Passenger pas : newDrive.getPassengers()) {
            if (pas.getPassengerEmail().equals("email@email.com"))
                assertEquals(pas.getContribution(), DrivePassengerStatus.ACCEPTED);
        }

        verify(driveRepository, times(1)).save(any());
    }

    @Test
    public void acceptDriveParticipationEverybodyAcceptedNoSplitBill() throws EmailNotFoundException, DriveNotFoundException, NotDrivePassengerException, URISyntaxException, IOException, TransactionIdDoesNotExistException, InterruptedException {
        User user = new User();
        user.setEmail("email@email.com");
        Passenger p = new Passenger();
        p.setPassengerEmail("email@email.com");
        Passenger p1 = new Passenger();
        p1.setPassengerEmail("nekidrugi.com");
        p1.setContribution(DrivePassengerStatus.ACCEPTED);
        Set passengers = new HashSet<Passenger>();
        passengers.add(p);
        passengers.add(p1);
        d.setPassengers(passengers);

        d.setSplitBill(false);

        doReturn(user).when(userService).getLoggedUser();
        doReturn(Optional.of(d)).when(driveRepository).findById(any());
        doReturn(d).when(driveRepository).save(d);
        doReturn(d).when(driveService).findAvailableDriver(d);

        Drive newDrive = driveService.acceptDriveParticipation(1L);

        for (Passenger pas : newDrive.getPassengers()) {
            if (pas.getPassengerEmail().equals("email@email.com"))
                assertEquals(pas.getContribution(), DrivePassengerStatus.ACCEPTED);
        }
        assertEquals(newDrive.getDriveStatus(), DriveStatus.DRIVER_WAITING);
        verify(driveRepository, times(2)).save(any());


    }

    @Test
    public void acceptDriveParticipationEverybodyAcceptedSplitBillNoPoor() throws EmailNotFoundException, DriveNotFoundException, NotDrivePassengerException, URISyntaxException, IOException, TransactionIdDoesNotExistException, InterruptedException {
        User user = new User();
        user.setEmail("email@email.com");
        Passenger p = new Passenger();
        p.setPassengerEmail("email@email.com");
        Passenger p1 = new Passenger();
        p1.setPassengerEmail("nekidrugi.com");
        p1.setContribution(DrivePassengerStatus.ACCEPTED);
        Set passengers = new HashSet<Passenger>();
        passengers.add(p);
        passengers.add(p1);
        d.setPassengers(passengers);

        d.setSplitBill(true);

        ClientsBankAccount bank1 = new ClientsBankAccount();
        bank1.setBalance(1000.0);
        ClientsAccount cl1 = new ClientsAccount();
        cl1.setClientsBankAccount(bank1);
        p.setDebit(500.0);
        ClientsBankAccount bank2 = new ClientsBankAccount();
        bank2.setBalance(1000.0);
        ClientsAccount cl2 = new ClientsAccount();
        cl2.setClientsBankAccount(bank2);
        p1.setDebit(500.0);
        p.setPayment(PaymentPassengerStatus.ACCEPTED);
        p1.setPayment(PaymentPassengerStatus.ACCEPTED);
        BankTransaction bankTransaction = new BankTransaction();
        bankTransaction.setId(1L);

        doReturn(bankTransaction).when(bankService).requestPassengerPayment(any(), any());
        doReturn(cl1).when(userService).findClientsAccount(p.getPassengerEmail());
        doReturn(cl2).when(userService).findClientsAccount(p1.getPassengerEmail());

        doReturn(user).when(userService).getLoggedUser();
        doReturn(Optional.of(d)).when(driveRepository).findById(any());
        doReturn(d).when(driveRepository).save(d);
        doReturn(d).when(driveService).findAvailableDriver(d);

        Drive newDrive = driveService.acceptDriveParticipation(1L);

        for (Passenger pas : newDrive.getPassengers()) {
            assertEquals(pas.getTransactionId(), 1L);
            if (pas.getPassengerEmail().equals("email@email.com"))
                assertEquals(pas.getContribution(), DrivePassengerStatus.ACCEPTED);
        }
        assertEquals(newDrive.getDriveStatus(), DriveStatus.PAYMENT_WAITING);
        verify(driveRepository, times(2)).save(any());


    }

    @Test
    public void acceptDriveParticipationEverybodyAcceptedSplitBillPoor() throws EmailNotFoundException, DriveNotFoundException, NotDrivePassengerException, URISyntaxException, IOException, TransactionIdDoesNotExistException, InterruptedException {
        User user = new User();
        user.setEmail("email@email.com");
        Passenger p = new Passenger();
        p.setPassengerEmail("email@email.com");
        Passenger p1 = new Passenger();
        p1.setPassengerEmail("nekidrugi.com");
        p1.setContribution(DrivePassengerStatus.ACCEPTED);
        Set passengers = new HashSet<Passenger>();
        passengers.add(p);
        passengers.add(p1);
        d.setPassengers(passengers);
        d.setPrice(400.0);
        d.setSplitBill(true);

        ClientsBankAccount bank1 = new ClientsBankAccount();
        bank1.setBalance(1000.0);
        ClientsAccount cl1 = new ClientsAccount();
        cl1.setClientsBankAccount(bank1);
        p.setDebit(500.0);
        ClientsBankAccount bank2 = new ClientsBankAccount();
        bank2.setBalance(200.0);
        ClientsAccount cl2 = new ClientsAccount();
        cl2.setClientsBankAccount(bank2);
        p1.setDebit(500.0);
        p.setPayment(PaymentPassengerStatus.ACCEPTED);
        p1.setPayment(PaymentPassengerStatus.ACCEPTED);
        BankTransaction bankTransaction = new BankTransaction();
        bankTransaction.setId(1L);
        d.setOwner(cl1);

        doReturn(bankTransaction).when(bankService).requestPassengerPayment(any(), any());
        doReturn(cl1).when(userService).findClientsAccount(p.getPassengerEmail());
        doReturn(cl2).when(userService).findClientsAccount(p1.getPassengerEmail());
        doReturn(new BankTransaction()).when(bankService).declineTransaction(any(), any());

        doReturn(bankTransaction).when(bankService).requestOwnerPayment(any());
        doReturn(user).when(userService).getLoggedUser();
        doReturn(Optional.of(d)).when(driveRepository).findById(any());
        doReturn(d).when(driveRepository).save(d);
        doReturn(d).when(driveService).findAvailableDriver(d);

        Drive newDrive = driveService.acceptDriveParticipation(1L);

        for (Passenger pas : newDrive.getPassengers()) {
            if (pas.getPassengerEmail().equals("email@email.com"))
                assertEquals(pas.getContribution(), DrivePassengerStatus.ACCEPTED);
            if (pas.getPassengerEmail().equals("nekidrugi.com"))
                assertEquals(pas.getPayment(), PaymentPassengerStatus.REJECTED);
        }
        verify(driveRepository, times(5)).save(any());
    }


    @Test
    public void acceptDriveParticipationNotEverybodyAccepted() throws EmailNotFoundException, DriveNotFoundException, NotDrivePassengerException, URISyntaxException, IOException, TransactionIdDoesNotExistException, InterruptedException {
        User user = new User();
        user.setEmail("email@email.com");
        Passenger p = new Passenger();
        p.setPassengerEmail("email@email.com");
        p.setPayment(PaymentPassengerStatus.ACCEPTED);
        Passenger p1 = new Passenger();
        p1.setPassengerEmail("nekidrugi.com");
        p1.setContribution(DrivePassengerStatus.REJECTED);
        p1.setPayment(PaymentPassengerStatus.ACCEPTED);
        Set passengers = new HashSet<Passenger>();
        passengers.add(p);
        passengers.add(p1);
        d.setPassengers(passengers);

        d.setSplitBill(true);
        d.setPrice(500.0);
        ClientsBankAccount bank1 = new ClientsBankAccount();
        bank1.setAccountNumber("aa");
        ClientsAccount cl1 = new ClientsAccount();
        cl1.setClientsBankAccount(bank1);
        d.setOwnerTransactionId(1L);
        d.setOwner(cl1);

        doReturn(new BankTransaction()).when(bankService).declineTransaction(any(), any());
        doNothing().when(notificationService).notifyOwnerDriveChanged(any(), any());
        doReturn(user).when(userService).getLoggedUser();
        doReturn(Optional.of(d)).when(driveRepository).findById(any());
        doReturn(d).when(driveRepository).save(d);
        doReturn(d).when(driveService).findAvailableDriver(d);

        Drive newDrive = driveService.acceptDriveParticipation(1L);

        assertEquals(newDrive.getDriveStatus(), DriveStatus.OWNER_PAYMENT_WAITING);
        verify(driveRepository, times(3)).save(any());
    }

}
