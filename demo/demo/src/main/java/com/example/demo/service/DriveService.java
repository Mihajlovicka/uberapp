package com.example.demo.service;

import com.example.demo.exception.DoesNotHaveEnoughMoneyException;
import com.example.demo.exception.DriveNotFoundException;
import com.example.demo.exception.EmailNotFoundException;
import com.example.demo.exception.NotDrivePassengerException;
import com.example.demo.fakeBank.BankService;
import com.example.demo.model.*;
import com.example.demo.repository.DriveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;


@Component
public class DriveService {
    @Autowired
    DriveRepository driveRepository;

    @Autowired
    UserService userService;

    @Autowired
    NotificationService notificationService;

    @Autowired
    BankService bankService;


    //odbije --> jbg
    //status je failed
    //prihvati da plati
    //pravi se banka transakcija
    //menja se status waut for passengers
    //obavestavaju se ovi mali govnari
    //notificationService.addedToDriveNotify(drive.getPassengers(), saved.getId());







    public Drive driveFailed(Drive drive){
        drive.setDriveStatus(DriveStatus.DRIVE_FAILED);
        return driveRepository.save(drive);
    }

    public Drive saveDrive(Drive drive) throws EmailNotFoundException {

        //driver se stavlja na null
        drive.setDriver(null);


        //jel owner ima para

        //nema --> jbg //status je failed
        if(!userService.canAffordDrive(drive.getOwner().getUser().getEmail(), drive.getOwnerDebit())){
            notificationService.paymentFailedDriveCanceledNotify(drive.getOwner().getUser().getEmail());
            return driveFailed(drive);
        }


        drive.setDriveStatus(DriveStatus.OWNER_PAYMENT_WAITING);
        Drive saved = driveRepository.save(drive);

        //napravi transakciju
        bankService.requestOwnerPayment(saved);


        return saved;

    }




    public Drive getDrive(Long id) throws DriveNotFoundException {
       Drive drive =  driveRepository.findById(id).get();
       if(drive==null)throw new DriveNotFoundException("Drive does not exist!");
       return drive;
    }


    public boolean isPassengerInDrive(String passengersEmail, Drive drive){
        for (Passenger passenger:
             drive.getPassengers()) {
            if(passenger.getPassengerEmail().equals(passengersEmail)) return true;
        }

        return false;
    }

    public boolean isParticipationAnswered(Set<Passenger> passengers){
        boolean status = false;
        for (Passenger passenger:
                passengers) {
            if(passenger.getContribution().equals(DrivePassengerStatus.WAITING)) status = false;
        }

        return status;
    }


    public boolean allAccepted(Set<Passenger> passengers){
        for (Passenger passenger:
                passengers ) {
            if(passenger.getContribution().equals(DrivePassengerStatus.REJECTED)) return false;
        }

        return true;
    }


    public Drive declineDrivePartiticipation(Long driveId) throws DriveNotFoundException, NotDrivePassengerException {
        String passengersEmail = userService.getLoggedUser().getEmail();
        Drive drive = getDrive(driveId);

        if(!isPassengerInDrive(passengersEmail, drive)) throw new NotDrivePassengerException("Not drive participant.");

        for(Passenger passenger: drive.getPassengers()){
            if(passenger.getPassengerEmail().equals(passengersEmail)){
                passenger.setContribution(DrivePassengerStatus.REJECTED);
            }
        }

        drive = driveRepository.save(drive);

        if(isParticipationAnswered(drive.getPassengers()))
        {
            //msm ovde nema sluaja da su svi prihvatili buk je ovaj lik odbio lol
            //ako je neko odbio
            //racuna se opet
            //salje se not svima ii owneru sa novom sumom
            //owneru se vracaju pare na racun
            //i sve u krug za placanje

        }

        return drive;

    }

    public Drive acceptDriveParticipation(Long driveId) throws DriveNotFoundException, NotDrivePassengerException {
        String passengersEmail = userService.getLoggedUser().getEmail();
        Drive drive = getDrive(driveId);

        if(!isPassengerInDrive(passengersEmail, drive)) throw new NotDrivePassengerException("Not drive participant.");

        for(Passenger passenger: drive.getPassengers()){
            if(passenger.getPassengerEmail().equals(passengersEmail)){
                passenger.setContribution(DrivePassengerStatus.ACCEPTED);
            }
        }

        drive = driveRepository.save(drive);

        if(isParticipationAnswered(drive.getPassengers()))
        {
            if(allAccepted(drive.getPassengers())){
                // proveriti jesu svi prihvatili ako jesu onda promeni status i salji not za placanje
                drive.setDriveStatus(DriveStatus.PAYMENT_WAITING);
                //POSALJI NOTIFIKACIJE
                notificationService.notifyAboutPayment(drive.getPassengers(), driveId);
                //MEJL BANKA:)
            }
            //ako je neko odbio
            //racuna se opet
            //salje se not svima ii owneru sa novom sumom
           //owneru se vracaju pare na racun
            //i sve u krug za placanje

        }


        return drive;
    }
}
