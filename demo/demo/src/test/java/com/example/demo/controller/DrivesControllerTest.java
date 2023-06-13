package com.example.demo.controller;

import com.example.demo.converter.UserConverter;
import com.example.demo.dto.*;
import com.example.demo.exception.DriveNotFoundException;
import com.example.demo.exception.EmailNotFoundException;
import com.example.demo.fakeBank.BankService;
import com.example.demo.fakeBank.ClientsBankAccount;
import com.example.demo.model.*;
import com.example.demo.service.DriveService;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DrivesControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private String accessToken;

    @Autowired
    private DriveService driveService;

    @Autowired
    private UserService userService;

    @Autowired
    private BankService bankService;

    @Autowired
    private UserConverter userConverter;


    HttpHeaders headers = new HttpHeaders();

    @Nested
    @DisplayName("Client tests")
    class Group1Client {
        
        @BeforeEach
        public void login() {
            ResponseEntity<UserTokenState> responseEntity =
                    restTemplate.postForEntity("/auth/login",
                            new JwtAuthenticationRequest("klijent1@com", "nekasifra123"),
                            UserTokenState.class);
            // preuzmemo token jer ce nam trebati za testiranje REST kontrolera
            UserTokenState resp = responseEntity.getBody();
            accessToken = resp.getAccessToken();
            headers.add("Authorization","Bearer " + accessToken);
        }

        @Test
        public void shouldCancelDrive() throws DriveNotFoundException {

            HttpEntity<String> httpEntity = new HttpEntity(1, headers);

            ResponseEntity responseEntity = restTemplate.exchange("/api/ownerCancelDrive/1",
                    HttpMethod.POST,
                    httpEntity,
                    new ParameterizedTypeReference<Object>() {});

            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

            Drive d = driveService.getDrive(1L);

            assertEquals(DriveStatus.DRIVE_FAILED, d.getDriveStatus());

        }

        @Test
        public void shouldContinueWithDrive() throws DriveNotFoundException {

            HttpEntity<String> httpEntity = new HttpEntity(1, headers);

            ResponseEntity<DriveDTO> responseEntity = restTemplate.exchange("/api/continueWithDrive/1",
                    HttpMethod.POST,
                    httpEntity,
                    new ParameterizedTypeReference<>() {});

            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

            DriveDTO d = responseEntity.getBody();

            assertEquals(DriveStatus.OWNER_PAYMENT_WAITING, d.getDriveStatus());

        }

        @Test
        public void shouldNotContinueNoEnoughMoney() throws DriveNotFoundException {

            ClientsBankAccount clientsBankAccount = bankService.findByAccountNumber("111");
            clientsBankAccount.setBalance(400.0);
            bankService.create(clientsBankAccount);

            HttpEntity<String> httpEntity = new HttpEntity(1, headers);

            ResponseEntity<DriveDTO> responseEntity = restTemplate.exchange("/api/continueWithDrive/1",
                    HttpMethod.POST,
                    httpEntity,
                    new ParameterizedTypeReference<>() {});

            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            DriveDTO d = responseEntity.getBody();
            assertEquals(DriveStatus.DRIVE_FAILED, d.getDriveStatus());

        }

        @Test
        public void shouldCreateDrive() throws DriveNotFoundException, EmailNotFoundException {

            HttpEntity<String> httpEntity = new HttpEntity(getDrive(false), headers);

            ResponseEntity<DriveDTO> responseEntity = restTemplate.exchange("/api/createDriveReservation",
                    HttpMethod.POST,
                    httpEntity,
                    new ParameterizedTypeReference<DriveDTO>() {});

            assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

            DriveDTO d = responseEntity.getBody();

        }

        @Test
        public void shouldNotCreateDriveOwnerHasNoMoney() throws DriveNotFoundException, EmailNotFoundException {

            HttpEntity<String> httpEntity = new HttpEntity(getDrive(true), headers);

            ResponseEntity<DriveDTO> responseEntity = restTemplate.exchange("/api/createDriveReservation",
                    HttpMethod.POST,
                    httpEntity,
                    new ParameterizedTypeReference<DriveDTO>() {});

            assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

            DriveDTO d = responseEntity.getBody();
            assertEquals(d.getDriveStatus(), DriveStatus.DRIVE_FAILED);

        }


        private CreateDriveReservationDTO getDrive(boolean noMoney) throws EmailNotFoundException {
            RealAddress stop1 = new RealAddress();
            stop1.setName("ime 1");
            stop1.setAddress("Brace Dronjak ");
            stop1.setLocation(new Location(45.254125, 19.796972));
            RealAddress stop2 = new RealAddress();
            stop2.setName("ime 2");
            stop2.setAddress("Cankareva ");
            stop2.setLocation(new Location(45.257101, 19.812107));

            ClientsAccount clientsAccount = userService.findClientsAccount("klijent1@com");
            ClientAccountDTO owner = userConverter.toDTO(clientsAccount);
            if(noMoney){
                ClientsBankAccount bank = clientsAccount.getClientsBankAccount();
                bank.setBalance(200.0);
                bankService.create(bank);
            }

            return new CreateDriveReservationDTO(Arrays.asList(stop1, stop2),5.5,10.25,500.0,new HashSet<>(),0,0,0,0,owner,"{}",false,"",500.0);
        }

    }

    @Nested
    @DisplayName("Participant tests")
    class Group1Participant {


        @BeforeEach
        public void login() {
            ResponseEntity<UserTokenState> responseEntity =
                    restTemplate.postForEntity("/auth/login",
                            new JwtAuthenticationRequest("klijent2@com", "nekasifra123"),
                            UserTokenState.class);
            // preuzmemo token jer ce nam trebati za testiranje REST kontrolera
            UserTokenState resp = responseEntity.getBody();
            accessToken = resp.getAccessToken();
            headers.add("Authorization", "Bearer " + accessToken);
        }

        //one accepted one waiting for
        @Test
        public void shouldAcceptDriveParticipation() throws DriveNotFoundException, EmailNotFoundException {

            HttpEntity<String> httpEntity = new HttpEntity(1, headers);

            ResponseEntity<DriveDTO> responseEntity = restTemplate.exchange("/api/acceptDrive/1",
                    HttpMethod.POST,
                    httpEntity,
                    new ParameterizedTypeReference<>() {
                    });

            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            DriveDTO d = responseEntity.getBody();

            Drive dr = driveService.getDrive(1L);

            assertEquals(DriveStatus.PASSENGERS_WAITING, dr.getDriveStatus());


        }

        //accepted but does not have money
        @Test
        public void shouldAcceptAndDriveFailDriveParticipation() throws DriveNotFoundException, EmailNotFoundException {

            HttpEntity<String> httpEntity = new HttpEntity(3, headers);

            ResponseEntity<DriveDTO> responseEntity = restTemplate.exchange("/api/acceptDrive/3",
                    HttpMethod.POST,
                    httpEntity,
                    new ParameterizedTypeReference<>() {
                    });

            DriveDTO d = responseEntity.getBody();

            Drive dr = driveService.getDrive(3L);

            assertEquals(DriveStatus.DRIVE_FAILED, dr.getDriveStatus());


        }


        //decline one other not
        @Test
        public void shouldDeclineDriveParticipation() throws DriveNotFoundException, EmailNotFoundException {

            HttpEntity<String> httpEntity = new HttpEntity(1, headers);

            ResponseEntity<DriveDTO> responseEntity = restTemplate.exchange("/api/declineDrive/1",
                    HttpMethod.POST,
                    httpEntity,
                    new ParameterizedTypeReference<>() {
                    });

            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            DriveDTO d = responseEntity.getBody();

            Drive dr = driveService.getDrive(1L);


            assertEquals(DriveStatus.PASSENGERS_WAITING, dr.getDriveStatus());

        }


        //decline only one, owner needs to pay debit again
        @Test
        public void shouldDeclineDriveOwnerNeedsToPayParticipation() throws DriveNotFoundException, EmailNotFoundException {

            HttpEntity<String> httpEntity = new HttpEntity(3, headers);

            ResponseEntity<DriveDTO> responseEntity = restTemplate.exchange("/api/declineDrive/3",
                    HttpMethod.POST,
                    httpEntity,
                    new ParameterizedTypeReference<>() {
                    });

            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            DriveDTO d = responseEntity.getBody();

            Drive dr = driveService.getDrive(3L);


            assertEquals(DriveStatus.OWNER_PAYMENT_WAITING, dr.getDriveStatus());

        }
    }

    @Nested
    @DisplayName("Driver tests")
    class Group1Driver {
        @BeforeEach
        public void login() {
            ResponseEntity<UserTokenState> responseEntity =
                    restTemplate.postForEntity("/auth/login",
                            new JwtAuthenticationRequest("driver@com", "nekasifra123"),
                            UserTokenState.class);
            // preuzmemo token jer ce nam trebati za testiranje REST kontrolera
            UserTokenState resp = responseEntity.getBody();
            accessToken = resp.getAccessToken();
            headers.add("Authorization","Bearer " + accessToken);
        }

        @Test
        public void shouldCancelDrive() throws DriveNotFoundException, EmailNotFoundException {
            String reason = "zato sto";
            HttpEntity<String> httpEntity = new HttpEntity(reason, headers);

            ResponseEntity responseEntity = restTemplate.exchange("/cancelRide",
                    HttpMethod.POST,
                    httpEntity,
                    new ParameterizedTypeReference<Object>() {});

            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

            Drive d = driveService.getDrive(2L);

            assertEquals(DriveStatus.DRIVE_REJECTED, d.getDriveStatus());
            assertEquals(DriveType.PAST, d.getDriveType());
            assertEquals(DriverStatus.AVAILABLE, d.getDriver().getDriverStatus());
        }

        @Test
        public void shouldNotCancelDrive() throws DriveNotFoundException {
            Drive d = driveService.getDrive(2L);
            d.setDriveType(DriveType.FUTURE);
            driveService.save(d);

            String reason = "zato sto";
            HttpEntity<String> httpEntity = new HttpEntity(reason, headers);

            ResponseEntity responseEntity = restTemplate.exchange("/cancelRide",
                    HttpMethod.POST,
                    httpEntity,
                    new ParameterizedTypeReference<Object>() {});

            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

            d = driveService.getDrive(2L);

            assertNotEquals(d.getDriveType(), DriveType.PAST);

        }

        @Test
        public void shouldStartDrive() throws DriveNotFoundException, EmailNotFoundException {
            Drive d = driveService.getDrive(2L);
            d.setSplitBill(true);

            HttpEntity<String> httpEntity = new HttpEntity(null, headers);

            ResponseEntity<DriveDTO> responseEntity = restTemplate.exchange("/ride/startRide",
                    HttpMethod.POST,
                    httpEntity,
                    new ParameterizedTypeReference<>() {});

            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            d = driveService.getDrive(2L);
            assertEquals(DriveStatus.DRIVE_STARTED, d.getDriveStatus());
            assertEquals(DriverStatus.BUSY, d.getDriver().getDriverStatus());
        }

        @Test
        public void shouldNotStartDrive() throws DriveNotFoundException {
            Drive d = driveService.getDrive(2L);
            d.setDriveType(DriveType.FUTURE);
            driveService.save(d);

            HttpEntity<String> httpEntity = new HttpEntity(null, headers);

            ResponseEntity<DriveDTO> responseEntity = restTemplate.exchange("/ride/startRide",
                    HttpMethod.POST,
                    httpEntity,
                    new ParameterizedTypeReference<>() {});

            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            d = driveService.getDrive(2L);
            assertNotEquals(DriveStatus.DRIVE_STARTED, d.getDriveStatus());
            assertNotEquals(DriverStatus.BUSY, d.getDriver().getDriverStatus());

        }
    }

}
