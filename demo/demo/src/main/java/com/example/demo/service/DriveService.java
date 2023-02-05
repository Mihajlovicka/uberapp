package com.example.demo.service;

import com.example.demo.exception.*;



import com.example.demo.exception.NotDrivePassengerException;
import com.example.demo.fakeBank.BankService;
import com.example.demo.fakeBank.BankTransaction;
import com.example.demo.fakeBank.ClientsBankAccount;
import com.example.demo.model.*;
import com.example.demo.converter.UserConverter;
import com.example.demo.exception.EmailNotFoundException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.help.ResponseRouteHelp;
import com.example.demo.model.help.ResponseTableHelp;
import com.example.demo.model.Drive;
import com.example.demo.model.DriveStatus;
import com.example.demo.model.Passenger;
import com.example.demo.model.User;
import com.example.demo.repository.DriveRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;



@Component
public class DriveService {
    @Autowired
    DriveRepository driveRepository;

    @Autowired
    UserService userService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    BankService bankService;

    private UserConverter conv;

    @Autowired
    private CarService carService;

    @Autowired
    private RideSimulationService rideSimulationService;

    public Drive driveFailed(Drive drive){
        drive.setDriveStatus(DriveStatus.DRIVE_FAILED);
        return driveRepository.save(drive);
    }

    public Drive saveDrive(Drive drive) throws EmailNotFoundException {

        //driver se stavlja na null
        drive.setDriver(null);


        //jel owner ima para

        //nema --> jbg //status je failed
        if (!userService.canAffordDrive(drive.getOwner().getUser().getEmail(), drive.getOwnerDebit())) {
            notificationService.paymentFailedDriveCanceledNotify(drive.getOwner().getUser().getEmail());
            return driveFailed(drive);
        }


        drive.setDriveStatus(DriveStatus.OWNER_PAYMENT_WAITING);

        Drive saved = driveRepository.save(drive);

        //napravi transakciju
        BankTransaction bt = bankService.requestOwnerPayment(saved);
        saved.setOwnerTransactionId(bt.getId());



        return driveRepository.save(saved);

    }

    public Drive driveWaitingForPassengers(Drive drive) throws EmailNotFoundException {
        drive.setDriveStatus(DriveStatus.PASSENGERS_WAITING);
        notificationService.addedToDriveNotify(drive.getPassengers(), drive.getId());


        return driveRepository.save(drive);

    }

    @Transactional
    public Drive findAvailableDriver(Drive drive) throws URISyntaxException, IOException, InterruptedException, TransactionIdDoesNotExistException, EmailNotFoundException {
        String foundDriverEmail="";
        if(drive.getDriveType().equals(DriveType.NOW)){
            drive.setDriveType(DriveType.FUTURE);
            drive = driveRepository.save(drive);
            foundDriverEmail = getNextDriverForCurrentRide(drive);
        }

        if(drive.getDriveType().equals(DriveType.FUTURE)){
            foundDriverEmail = getNextDriverForFutureRide(drive);
        }



        if (foundDriverEmail.equals("")) {
            notificationService.addNotificationMultiple(new Notification("Voznja odbijena", "Cao! Nazalost, vaza voznja je odbijena. Trenutno nema slobodnih vozila.",null,""), makeUsersFromPassengersForNotification(drive));
            //vratiti pare na racun
            bankService.declineTransaction(drive.getOwnerTransactionId(), drive.getOwner().getClientsBankAccount().getAccountNumber());
            drive.setDriveStatus(DriveStatus.DRIVE_FAILED);
            return driveRepository.save(drive);
            //throw new NotFoundException("Nema trenutno slobodnog vozaca.");
        }


        DriversAccount driver = userService.getDriver(foundDriverEmail);
        drive.setDriver(driver);
        notificationService.addNotification(new Notification("Nova voznja", "Dodeljena vam je nova voznja. " + drive.getDate(), driver.getUser(),"localhost:4200/rides-dr"));


        notificationService.addNotificationMultiple(new Notification("Voznja odobrena", "Vasa voznja je odobrena. ", null,""), makeUsersFromPassengersForNotification(drive));


        return driveRepository.save(drive);

    }

    public Drive paymentDone(Drive drive) throws URISyntaxException, IOException, InterruptedException, TransactionIdDoesNotExistException, EmailNotFoundException {
        drive.setDriveStatus(DriveStatus.DRIVER_WAITING);
        Drive saved = driveRepository.save(drive);


        return findAvailableDriver(saved);

    }


    public boolean canPassengersAfford(Drive drive) throws EmailNotFoundException {
        for(Passenger passenger: drive.getPassengers()) {
            ClientsAccount clientsAccount = userService.findClientsAccount(passenger.getPassengerEmail());
            if(clientsAccount.getClientsBankAccount().getBalance()-passenger.getDebit()<0) return false;
        }

        return true;
    }

    public Drive findPoorParticipants(Drive drive) throws EmailNotFoundException {
        for (Passenger passenger:
             drive.getPassengers()) {
            ClientsAccount clientsAccount = userService.findClientsAccount(passenger.getPassengerEmail());
            if(clientsAccount.getClientsBankAccount().getBalance()-passenger.getDebit()<0){
                passenger.setPayment(PaymentPassengerStatus.REJECTED);
            }
        }

        return driveRepository.save(drive);
    }

    public void ownerPaymentAccepted(BankTransaction transaction) throws EmailNotFoundException, URISyntaxException, IOException, InterruptedException, TransactionIdDoesNotExistException {
        Drive drive = driveRepository.findByOwner_User_EmailAndOwnerTransactionId(transaction.getSender(), transaction.getId());


        if (drive.getPassengers().size() != 0) {


        //ako ima passengere
        if (isParticipationAnswered(drive.getPassengers())) {

            if(drive.isSplitBill()){
                if (!canPassengersAfford(drive)) {
                    //promeniit status onima koji ne mogu
                    drive = findPoorParticipants(drive);
                    //ponistiti ownerovu transakciju
                    bankService.declineTransaction(drive.getOwnerTransactionId(), drive.getOwner().getClientsBankAccount().getAccountNumber());
                    //drive.setOwnerTransactionId((long) -1);


                    //ponovo izracunati cenu
                    drive = calculateNewPriceForDrive(drive);


                    //nova transakcija owneru? i mejl logicno
                    drive.setOwnerTransactionId(bankService.requestOwnerPayment(drive).getId());
                }
                if (canPassengersAfford(drive)) {
                    //ako jesu
                    for (Passenger passenger :
                            drive.getPassengers()) {
                        if (passenger.getPayment().equals(PaymentPassengerStatus.WAITING) || passenger.getPayment().equals(PaymentPassengerStatus.ACCEPTED)) {
                            BankTransaction passengersTransaction = bankService.requestPassengerPayment(drive, userService.findClientsAccount(passenger.getPassengerEmail()));
                            passenger.setTransactionId(passengersTransaction.getId());
                        }
                    }
                    drive.setDriveStatus(DriveStatus.PAYMENT_WAITING);
                    drive = driveRepository.save(drive);

                }
            }




        }

        if (!isParticipationAnswered(drive.getPassengers())) {
            if (drive.getPassengers().size() > 0) drive = driveWaitingForPassengers(drive);

        }
    }
        //ako nema passengere
        if (drive.getPassengers().size() == 0 || !drive.isSplitBill()) drive = paymentDone(drive);
    }

    public Drive findPassengerDrive(String passengerEmail, Long passengerTransactionId){
        for (Drive drive: driveRepository.findAll()){
            for (Passenger passenger:
                 drive.getPassengers()) {
                if(passenger.getPassengerEmail().equals((passengerEmail)) && passenger.getTransactionId().equals(passengerTransactionId)){
                    return drive;
                }
            }
        }

        return null;
    }


    public boolean isPaymentAnswered(Set<Passenger> passengers){
        for (Passenger passenger:
                passengers) {
            if(passenger.getPayment().equals(PaymentPassengerStatus.WAITING)) return false;
        }
        return true;
    }

    public boolean isPaymentAccepted(Set<Passenger> passengers){
        for (Passenger passenger:
                passengers) {
            if(passenger.getPayment().equals(PaymentPassengerStatus.REJECTED) || passenger.getPayment().equals(PaymentPassengerStatus.WAITING)) return false;
        }

        return true;
    }

    public Drive passengerDeclinedPayment(Drive drive, BankTransaction transaction) throws TransactionIdDoesNotExistException, EmailNotFoundException {
        for (Passenger passenger: drive.getPassengers()) {
            if (passenger.getPassengerEmail().equals(transaction.getSender())) {
                passenger.setPayment(PaymentPassengerStatus.REJECTED);
            }
        }
        Drive saved = driveRepository.save(drive);

        if(isPaymentAnswered(drive.getPassengers())){
            //ako su svi odg..vec znamo da nisu svi prihvatili
            //ponistiti ownerovu transakciju
            bankService.declineTransaction(saved.getOwnerTransactionId(), saved.getOwner().getClientsBankAccount().getAccountNumber());
            //saved.setOwnerTransactionId((long) -1);

            //ponistiti svim passengerima transakcije
            for (Passenger passenger: saved.getPassengers()){
                if(passenger.getPayment().equals(PaymentPassengerStatus.ACCEPTED)){
                    bankService.declineTransaction(passenger.getTransactionId(),userService.findClientsAccount(passenger.getPassengerEmail()).getClientsBankAccount().getAccountNumber());
                    //passenger.setTransactionId((long)-1);
                    if(passenger.getPayment().equals(PaymentPassengerStatus.WAITING)){
                        passenger.setPayment(PaymentPassengerStatus.WAITING);
                    }
                }


            }

            //ponovo izracunati cenu
            saved = calculateNewPriceForDrive(saved);


            //nova transakcija owneru? i mejl logicno
            saved.setOwnerTransactionId(bankService.requestOwnerPayment(saved).getId());

        }
        //ako nisu svi odg nikome nista
        return driveRepository.save(saved);
    }



    public Drive passengerAcceptedPayment(Drive drive, BankTransaction transaction) throws URISyntaxException, IOException, InterruptedException, TransactionIdDoesNotExistException, EmailNotFoundException {
        for (Passenger passenger: drive.getPassengers()) {
            if (passenger.getPassengerEmail().equals(transaction.getSender())) {
                passenger.setPayment(PaymentPassengerStatus.ACCEPTED);

            }
        }
            Drive saved = driveRepository.save(drive);

            //proveravam da li su svi odg
            if(isPaymentAnswered(saved.getPassengers())){
                //ako jesu gledam da li su svi prihvatili
                if(isPaymentAccepted(saved.getPassengers())){
                    saved = paymentDone(saved);
                }

                if(!isPaymentAccepted(saved.getPassengers())){
                    //ako nisu svi prihvatili neko odbio - problem
                    //ponistiti ownerovu transakciju
                    bankService.declineTransaction(saved.getOwnerTransactionId(), saved.getOwner().getClientsBankAccount().getAccountNumber());
                    //saved.setOwnerTransactionId((long) -1);

                    //ponistiti svim passengerima transakcije

                    for (Passenger passenger: saved.getPassengers()) {
                        if (passenger.getPayment().equals(PaymentPassengerStatus.ACCEPTED)) {
                            bankService.declineTransaction(passenger.getTransactionId(), userService.findClientsAccount(passenger.getPassengerEmail()).getClientsBankAccount().getAccountNumber());
                            passenger.setTransactionId((long) -1);
                            if (passenger.getPayment().equals(PaymentPassengerStatus.WAITING)) {
                                passenger.setPayment(PaymentPassengerStatus.WAITING);
                            }
                        }

                    }

                    //ponovo izracunati cenu
                    saved = calculateNewPriceForDrive(saved);


                    //nova transakcija owneru? i mejl logicno
                    saved.setOwnerTransactionId(bankService.requestOwnerPayment(saved).getId());
                }
            }

        return driveRepository.save(saved);
    }

    public void paymentAccepted(BankTransaction transaction) throws EmailNotFoundException, URISyntaxException, IOException, InterruptedException, TransactionIdDoesNotExistException {
        //ko je zapravo prhvatio ovo
        //preko sendera nadjemo ko je to
        Drive drive = driveRepository.findByOwner_User_EmailAndOwnerTransactionId(transaction.getSender(), transaction.getId());
        if(drive != null) {System.out.println("ovnerry");
            ownerPaymentAccepted(transaction);}

        if(drive==null){
            Drive drivee = findPassengerDrive(transaction.getSender(), transaction.getId());
            if(drivee != null){
                Drive passengersDrive = passengerAcceptedPayment(drivee, transaction);//od ovoga kurtona je
            }
            //ako je null napravi neki exc i baci ga da ne psotoji voznja za ovu transakciju i da je greska
            if(drivee==null) throw new NotFoundException("Drive for transaction does not exist!");
        }

    }

    public void paymentDeclined(BankTransaction transaction) throws EmailNotFoundException, TransactionIdDoesNotExistException {
        //ko je zapravo prhvatio ovo
        //preko sendera nadjemo ko je to
        Drive drive = driveRepository.findByOwner_User_EmailAndOwnerTransactionId(transaction.getSender(), transaction.getId());
        if(drive != null) {
            //owner odbio
            driveFailedMoneyTransactionRejected(transaction);
        }
        if(drive==null){
            Drive drivee = findPassengerDrive(transaction.getSender(), transaction.getId());
            if(drivee!= null){
                //passenger je odbio
                Drive passengersDrive = passengerDeclinedPayment(drivee, transaction);
            }
            if(drivee==null) throw new NotFoundException("Drive for transaction does not exist!");
        }
    }


    /**
    public Drive saveDrive(Drive drive) throws URISyntaxException, IOException, InterruptedException, EmailNotFoundException {

        //driver se stavlja na null
        drive.setDriver(null);
        //drive startus se stavlja na waiting passengers
        drive.setDriveStatus(DriveStatus.PASSENGERS_WAITING);
        drive.setDriveType(DriveType.NOW);
        String foundDriverEmail = getNextDriverForCurrentRide(drive);
//        Long foundDriverId = getNextDriverForFutureRide(drive);
        if (foundDriverEmail.equals("")) {
            notificationService.addNotificationMultiple(new Notification("Obavestenje", "Vasa voznja je odbijena. Nema slobodnih vozila.",null,""), makeUsersFromPassengersForNotification(drive));
            throw new NotFoundException("Nema trenutno slobodnog vozaca.");

        }

//        Drive saved = driveRepository.save(drive);
//
//
//        notificationService.addedToDriveNotify(drive.getPassengers(), saved.getId());
//
//
//        return saved;

        DriversAccount driver = userService.getDriver(foundDriverEmail);
        drive.setDriver(driver);
        notificationService.addNotification(new Notification("Nova voznja", "Dodeljena vam je nova voznja. " + drive.getDate(), driver.getUser(),"localhost:4200/rides-dr"));
        notificationService.addNotificationMultiple(new Notification("Nova voznja", "Vasa voznja je odobrena. ", null,""), makeUsersFromPassengersForNotification(drive));
        return driveRepository.save(drive);

    }**/



    public List<Drive> getDrivesForUser(String email, boolean past) throws EmailNotFoundException {
        List<Drive> drives = new ArrayList<Drive>();
        User user = userService.getByEmail(email);
        for(Drive drive : driveRepository.findAll()){
            if(user.getRole().getName().equals("ROLE_CLIENT")) {
                if (drive.getOwner().getUser().getEmail().equals(email)) {
                    if(!past) {
                        drives.add(drive);
                    }
                    else{
                        if(drive.getDriveStatus() == DriveStatus.DRIVE_ENDED ||
                                drive.getDriveStatus() == DriveStatus.DRIVE_REJECTED ||
                                drive.getDriveStatus() == DriveStatus.DRIVE_FAILED ){
                            drives.add(drive);
                        }
                    }
                } else {
                    for (Passenger passenger : drive.getPassengers()) {
                        if (passenger.getPassengerEmail().equals(email)) {
                            if(!past) {
                                drives.add(drive);
                            }
                            else{
                                if(drive.getDriveStatus() == DriveStatus.DRIVE_ENDED ||
                                        drive.getDriveStatus() == DriveStatus.DRIVE_REJECTED ||
                                        drive.getDriveStatus() == DriveStatus.DRIVE_FAILED ){
                                    drives.add(drive);
                                }
                            }
                        }
                    }
                }
            }
            else{
                if(drive.getDriver() != null) {
                    if (drive.getDriver().getUser().getEmail().equals(email)) {
                        if(!past) {
                            drives.add(drive);
                        }
                        else{
                            if(drive.getDriveStatus() == DriveStatus.DRIVE_ENDED ||
                                    drive.getDriveStatus() == DriveStatus.DRIVE_REJECTED ||
                                    drive.getDriveStatus() == DriveStatus.DRIVE_FAILED ){
                                drives.add(drive);
                            }
                        }
                    }
                }
            }
        }
        return drives;
    }

    public Drive getDrive(int driveID) {
        for (Drive drive: driveRepository.findAll()) {
            if(drive.getId() == driveID) return drive;
        }
        return null;
    }

    public Drive getDrive(Long id) throws DriveNotFoundException {
        Drive drive =  driveRepository.findById(id).orElse(null);
        if(drive==null)throw new DriveNotFoundException("Drive does not exist!");
        return drive;
    }

    @Transactional(propagation=Propagation.REQUIRED)
    public String getNextDriverForFutureRide(Drive drive) {
        List<DriversAccount> drivers = this.userService.getDrivers();
        if (drivers.size() > 0) {
            DriversAccount d = getDriversDistancesFuture(drivers, drive);
            return d == null ? "" : d.getUser().getEmail();
        }
        return "";
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public String getNextDriverForCurrentRide(Drive drive) throws URISyntaxException, IOException, InterruptedException {
        List<DriversAccount> drivers = this.userService.getDriversByStatus(DriverStatus.AVAILABLE);
        drivers = getAvailableDrivers(drivers);
        Map<String, Map<String, Double>> distances = new HashMap<>();
        Location startLocation = drive.getStops().get(0).getLocation();
        if (drivers.size() > 0) {
            //proveriti da li su slobodni celu voznju
            //od njegove lokacije do starta do kraja vreme i moze da otskace 5min
            distances = getFreeDriversDistancesNow(drivers, drive);
        }
        if (distances.size() == 0) {
            drivers = this.userService.getDriversByStatus(DriverStatus.BUSY);
            drivers = getAvailableDrivers(drivers);
            distances = getBusyDriversDistancesNow(drivers, drive);
        }
        if (distances.size() == 0) return "";
        return getMinDistanceDriver(distances);
    }

    private List<DriversAccount> getAvailableDrivers(List<DriversAccount> drivers) {
        List<DriversAccount> driversFiltered = new ArrayList<>();
        for(DriversAccount d: drivers){
            if(d.getDriversAvailability()){
                driversFiltered.add(d);
            }
        }
        return driversFiltered;
    }

    private Map<String, Map<String, Double>> getFreeDriversDistancesNow(List<DriversAccount> drivers, Drive drive) throws IOException, URISyntaxException, InterruptedException {
        Location newStart = drive.getStops().get(0).getLocation();
        Map<String, Map<String, Double>> driversDistances = new HashMap<>();
        for (DriversAccount driver : drivers) {
            Location carCurrentLocation = driver.getCar().getCurrentLocation();
            HashMap<String, Double> result = makeRequestForRide(carCurrentLocation, newStart, null);
            double time = result.get("duration") + drive.getDuration() * 60;//u s
            //da li ima voznju koja pocinje pre vocoga vremena now + time
            long millis = System.currentTimeMillis();
            Date newRideStart = new Date(millis); //od sad
            Date newRideEnd = new Date((long) (millis + time * 1000));
            if (isDriverFreeInPeriod(newRideStart, newRideEnd, driver)) {
                driversDistances.put(driver.getUser().getEmail(), result);
            }
        }
        return driversDistances;
    }

    private Map<String, Map<String, Double>> getBusyDriversDistancesNow(List<DriversAccount> drivers, Drive drive) throws URISyntaxException, IOException, InterruptedException {
        Location newStart = drive.getStops().get(0).getLocation();
        Map<String, Map<String, Double>> driversDistances = new HashMap<>();
        for (DriversAccount driver : drivers) {
            Location carCurrentLocation = driver.getCar().getCurrentLocation();
            Location endLocation = getCarStartEndStop(driver.getCar().getId(), false);
            HashMap<String, Double> result = makeRequestForRide(carCurrentLocation, endLocation, newStart);
            double totalTime = result.get("duration") + drive.getDuration() * 60;
            long millis = System.currentTimeMillis();
            Date newRideStart = new Date(millis); //od sad
            Date newRideEnd = new Date((long) (millis + totalTime * 1000)); // do kraja nove voznje
            if (isDriverFreeInPeriod(newRideStart, newRideEnd, driver)) {
                driversDistances.put(driver.getUser().getEmail(), result);
            }
        }
        return driversDistances;
    }

    private DriversAccount getDriversDistancesFuture(List<DriversAccount> drivers, Drive drive) {
        for (DriversAccount driver : drivers) {
            double totalTime = drive.getDuration() * 60 + 5 * 60;//za dolaak na mesto?
            Date newRideStart = new Date(drive.getDate().getTime()); //od sad
            Date newRideEnd = new Date((long) (newRideStart.getTime() + totalTime * 1000)); // do kraja nove voznje
            if (isDriverFreeInPeriod(newRideStart, newRideEnd, driver)) {
                return driver;
            }
        }
        return null;
    }

    private boolean isDriverFreeInPeriod(Date start, Date end, DriversAccount driver) {
        for (Drive drive : driveRepository.findByDriver(driver)) {
            Date oldStartDate = drive.getDate();
            Date oldEndDate = new Date((long) (drive.getDate().getTime() + drive.getDuration() * 60 * 1000));
            if (oldStartDate.before(end) && oldStartDate.after(start) ||
                    oldEndDate.after(start) && oldEndDate.before(end))
                return false;
        }
        return true;
    }

    private String getMinDistanceDriver(Map<String, Map<String, Double>> distances) {
        String id = "";
        double min = -1;
        for (String i : distances.keySet()) {
            if (id.equals("")) {
                id = i;
                min = distances.get(i).get("distance");
            } else {
                if (min > distances.get(i).get("distance")) {
                    id = i;
                    min = distances.get(i).get("distance");
                }
            }
        }
        return id;

    }


    private HashMap<String, Double> makeRequestForRide(Location start, Location end, Location middle) throws IOException, InterruptedException, URISyntaxException {

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(makeURL(start, end, middle)))
                .GET()
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        ResponseTableHelp rh = new ObjectMapper().readValue(response.body(), ResponseTableHelp.class);
        HashMap<String, Double> m = new HashMap<>();
        if (middle == null) {
            m.put("distance", rh.getDistances().get(0).get(0));
            m.put("duration", rh.getDurations().get(0).get(0));
        } else {
            double distance = rh.getDistances().get(0).get(1) + rh.getDistances().get(1).get(2);
            double duration = rh.getDurations().get(0).get(1) + rh.getDurations().get(1).get(2);
            m.put("distance", distance);
            m.put("duration", duration);
        }
        return m;
    }

    private String makeURL(Location start, Location end, Location middle) {
        if (middle == null) {
            return "http://router.project-osrm.org/table/v1/driving/" +
                    start.getLatitude() + "," + start.getLongitude() + ";" +
                    end.getLatitude() + "," + end.getLongitude() + "?" +
                    "sources=0&destinations=1&" +
                    "annotations=distance,duration";
        }
        return "http://router.project-osrm.org/table/v1/driving/" +
                start.getLatitude() + "," + start.getLongitude() + ";" +
                middle.getLatitude() + "," + middle.getLongitude() + ";" +
                end.getLatitude() + "," + end.getLongitude() + "?" +
                "annotations=distance,duration";
    }

    public Drive getCurrentDrive(){
        DriversAccount driver = userService.getLoggedDriver();
        for (Drive d : driveRepository.findByDriver(driver)) {
            if(d.getDriveType().equals(DriveType.NOW))
                return d;
        }
        return null;
    }

    public Drive getFirstFutureDrive() {
        List<Drive> future = new ArrayList<>();
        DriversAccount driver = userService.getLoggedDriver();
        for (Drive d : driveRepository.findByDriver(driver)) {
            if(d.getDriveType().equals(DriveType.FUTURE))
                future.add(d);
        }
        Collections.sort(future, new Comparator<Drive>() {
            @Override
            public int compare(Drive d1, Drive d2) {
                return d1.getDate().compareTo(d2.getDate());
            }
        });
        if (future.size() > 0) {
            return future.get(0);
        }
        return null;
    }

    public Map<String, Object> getFirstFutureDriveStops() {
        Map<String, Object> resp = new HashMap<>();
        Drive drive = getFirstFutureDrive();
        if (drive != null) {
            resp.put("stops", drive.getStops());
            resp.put("date", drive.getDate());
        }
        return resp;
    }

    public String endDrive() throws EmailNotFoundException {
        Drive drive = getCurrentDrive();
        if(drive != null){
            drive.setDriveType(DriveType.PAST);
            userService.updateDriverStatus(DriverStatus.AVAILABLE);
            drive.setDriveStatus(DriveStatus.DRIVE_ENDED);
            drive.setEndDate(new Date());

            //promeniti owneru da je u voznji
            //sacuvati ovoga u userrepo
            drive.getOwner().setInDrive(false);
            userService.saveCurrent(drive.getOwner());

            //pasengeri u voznji
            passengerNotInDriveStatus(drive.getPassengers());


            driveRepository.save(drive);
            return "Voznja zavrsena";
        }
        else
        {
            return "Nema trenutne voznje.";
        }
    }

    public List<RealAddress> goToNextDrive() {
        Drive drive = getFirstFutureDrive();
        if(drive != null){
            drive.setDriveType(DriveType.NOW);
            userService.updateDriverStatus(DriverStatus.GOING_TO_LOCATION);
            driveRepository.save(drive);
            return drive.getStops();
        }
        return new ArrayList<>();
    }


    public void finalizePassengersTransactions(Set<Passenger> passengers){
        for(Passenger passenger: passengers){
            bankService.transactionFinalized(passenger.getTransactionId());
        }
    }

    public void passengerInDriveStatus(Set<Passenger> passengers) throws EmailNotFoundException {
        for (Passenger passenger:
                passengers) {
            // i sacuvati ovo u user repo
            ClientsAccount clientsAccount = userService.findClientsAccount(passenger.getPassengerEmail());
            clientsAccount.setInDrive(true);
            userService.saveCurrent(clientsAccount);
        }

    }

    public void passengerNotInDriveStatus(Set<Passenger> passengers) throws EmailNotFoundException {
        for (Passenger passenger:
                passengers) {
            // i sacuvati ovo u user repo
            ClientsAccount clientsAccount = userService.findClientsAccount(passenger.getPassengerEmail());
            clientsAccount.setInDrive(false);
            userService.saveCurrent(clientsAccount);

        }
    }

    public String startDrive() throws EmailNotFoundException {
        Drive drive = getCurrentDrive();
        if(drive != null){
            drive.setDriveStatus(DriveStatus.DRIVE_STARTED);
            drive.setStartDate(new Date());

            //promeniti owneru da je u voznji
            //sacuvati ovoga u userrepo
            drive.getOwner().setInDrive(true);
            userService.saveCurrent(drive.getOwner());

            //pasengeri u voznji
            passengerInDriveStatus(drive.getPassengers());

            drive = driveRepository.save(drive);

            //ovde promeniti status transakcije ownera
            bankService.transactionFinalized(drive.getOwnerTransactionId());

            //promeniti status transakcije passengera
            if(drive.isSplitBill()){
                finalizePassengersTransactions(drive.getPassengers());
            }


            userService.updateDriverStatus(DriverStatus.BUSY);
            rideSimulationService.createRideSim(drive);
            return "Zapoceta nova voznja.";
        }
        return "Nema voznje.";
    }

    public Location getCarStartEndStop(Long id,boolean first) throws JsonProcessingException {
        Car c = carService.getCar(id);
        for(Drive d: driveRepository.findByDriveType(DriveType.NOW)){
            if(d.getDriver().getCar().getId().equals(c.getId())){
                ResponseRouteHelp route = new ObjectMapper().readValue(d.getRouteJSON(), ResponseRouteHelp.class);
                ArrayList<ArrayList<Double>> coords = route.getMetadata().getQuery().getCoordinates();
                int i = coords.size()-1;
                if(first) i = 0;
                return new Location(coords.get(i).get(1),coords.get(i).get(0));
            }
        }
        throw new NotFoundException("Car current location not found. No current drive.");
    }


    public Drive getClientCurrentDrive(){
        User user = userService.getLoggedIn();
        for(Drive d: driveRepository.findByDriveType(DriveType.NOW)){
            if(d.getOwner().getUser().getEmail().equals(user.getEmail()))
                return d;
            for(Passenger passenger:d.getPassengers()){
                if(passenger.getPassengerEmail().equals(user.getEmail()))
                    return d;
            }
        }
        throw new NotFoundException("No client current ride.");
    }

    public List<User> makeUsersFromPassengersForNotification(Drive drive) throws EmailNotFoundException {
        List<User> users = new ArrayList<>();
        users.add(drive.getOwner().getUser());
        for(Passenger p: drive.getPassengers()){

            users.add(userService.findByEmail(p.getPassengerEmail()));

        }
        return users;
    }

    public Drive getCarCurrentDrive(int id) {
        for(Drive d: driveRepository.findByDriveType(DriveType.NOW)){
            if(d.getDriver().getCar().getId() == id){
                return d;
            }
        }
        return null;

    }


    public void notifyPassengers() throws EmailNotFoundException {
        Drive drive = getCurrentDrive();
        if(drive != null){
            notificationService.addNotificationMultiple(new Notification("Vozilo je stiglo", "Vase vozilo ceka.", null,""), makeUsersFromPassengersForNotification(drive));
        }
    }

    public void cancelDrive(String reason) throws EmailNotFoundException {
        Drive drive = getCurrentDrive();
        if(drive != null){
            notificationService.addNotificationMultiple(new Notification("Voznja otkazana", "Mnogo se izvinjavamo vasa voznja je otkazana.", null,""), makeUsersFromPassengersForNotification(drive));
            drive.setDriveStatus(DriveStatus.DRIVE_REJECTED);
            //promeniti status transakcije na finalized
            bankService.transactionFinalized(drive.getOwnerTransactionId());
            //promeniti pasengerima status transakcije na finalized
            finalizePassengersTransactions(drive.getPassengers());
            drive.setDriveType(DriveType.PAST);
            driveRepository.save(drive);
            userService.changeDriverStatus(drive.getDriver(), DriverStatus.AVAILABLE);
            notificationService.addNotificationMultiple(new Notification(
                            "Voznja otkazana",
                            "Vozac " + drive.getDriver().getUser().getName() + " " + drive.getDriver().getUser().getSurname()+
                            " je otkazao vonju zbog: " + reason + ".",
                            null,
                            ""
                    ),userService.getAdmins()
            );
        }
    }

    public List<Drive> getAllDrives(){
        return driveRepository.findAll();
    }

    public void cancelFutureDrives(DriversAccount driver) throws EmailNotFoundException {
        for(Drive drive:driveRepository.findByDriver(driver)){
            if(drive.getDriveType().equals(DriveType.FUTURE)){
                notificationService.addNotificationMultiple(new Notification("Voznja otkazana", "Mnogo se izvinjavamo vasa voznja je otkazana.", null,""), makeUsersFromPassengersForNotification(drive));
                drive.setDriveStatus(DriveStatus.DRIVE_REJECTED);
                drive.setDriveType(DriveType.PAST);
                driveRepository.save(drive);
                userService.changeDriverStatus(drive.getDriver(), DriverStatus.AVAILABLE);
            }
        }
    }

    public List<Drive> getDriveByType(DriveType type) {
        return driveRepository.findByDriveType(type);
    }


    public boolean isPassengerInDrive(String passengersEmail, Drive drive){
        for (Passenger passenger:
             drive.getPassengers()) {
            if(passenger.getPassengerEmail().equals(passengersEmail)) return true;
        }

        return false;
    }

    public boolean isParticipationAnswered(Set<Passenger> passengers){
        boolean status = true;
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

    public Drive declineDriveParticipation(Long driveId) throws DriveNotFoundException, NotDrivePassengerException, TransactionIdDoesNotExistException {
        String passengersEmail = userService.getLoggedUser().getEmail();
        Drive drive = getDrive(driveId);

        if(!isPassengerInDrive(passengersEmail, drive)) throw new NotDrivePassengerException("Not drive participant.");

        for(Passenger passenger: drive.getPassengers()){
            if(passenger.getPassengerEmail().equals(passengersEmail)){
                passenger.setContribution(DrivePassengerStatus.REJECTED);
                passenger.setPayment(PaymentPassengerStatus.REJECTED);
            }
        }

        drive = driveRepository.save(drive);

        if(isParticipationAnswered(drive.getPassengers())){
            //trenutna jedina kreirana transakcija je ownerova koja ima status waiting_for_finalization
            //nadjem tu njegovu transakciju
            //ponistim je
            bankService.declineTransaction(drive.getOwnerTransactionId(), drive.getOwner().getClientsBankAccount().getAccountNumber());

            //passenger transakcije nisu kreirane
            //tako racunaj novu cenu
            Drive calculated = calculateNewPriceForDrive(drive);

            //drive status je waiting for owner
            calculated.setDriveStatus(DriveStatus.OWNER_PAYMENT_WAITING);

            //poslati mu notifikaciju sa novim infom o voznji
            driveChangedOwnerNotify(drive);
        }



        return drive;

    }




    public Drive cancelDrive(Drive drive){
        drive.setDriveStatus(DriveStatus.DRIVE_FAILED);

        notificationService.notifyCanceledDrive(drive);


        return driveRepository.save(drive);
    }

    public Drive continueWithDrive(Drive drive) throws EmailNotFoundException {
        drive.setDriveStatus(DriveStatus.OWNER_PAYMENT_WAITING);
        //trigerovati mejl za placanje

        //ako nema para jadnik
        if (!userService.canAffordDrive(drive.getOwner().getUser().getEmail(), drive.getOwnerDebit())) {
            notificationService.paymentFailedDriveCanceledNotify(drive.getOwner().getUser().getEmail());
            return driveFailed(drive);
        }

        //i treba se trigerovati trazenje vozaca
        BankTransaction bt = bankService.requestOwnerPayment(drive);
        drive.setOwnerTransactionId(bt.getId());



        return driveRepository.save(drive);
    }

    public Drive calculateNewPriceForDrive(Drive drive){
        int partitions = 0;
        //pronaci prvo koliko njih placa
        for (Passenger passenger:
             drive.getPassengers()) {
            if(passenger.getContribution().equals(DrivePassengerStatus.ACCEPTED)){
                if(passenger.getPayment().equals(PaymentPassengerStatus.WAITING) || passenger.getPayment().equals(PaymentPassengerStatus.ACCEPTED)){
                    //znaci samo oni koji placaju i koji jos nisu odbili lol
                    partitions+=1;
                }
            }
        }

        if(partitions>0){
            Double newDebit = drive.getPrice()/partitions;
            drive.setOwnerDebit(newDebit);
            for (Passenger passenger:
                    drive.getPassengers()) {
                if(passenger.getContribution().equals(DrivePassengerStatus.ACCEPTED)){
                    if(!passenger.getPayment().equals(PaymentPassengerStatus.NOT_PAYING) || !passenger.getPayment().equals(PaymentPassengerStatus.REJECTED)){
                        //znaci samo oni koji placaju i koji jos nisu odbili lol
                        passenger.setDebit(newDebit);
                    }
                }
            }
        }
        else{
            drive.setSplitBill(false);
            drive.setOwnerDebit(drive.getPrice());
        }


        return driveRepository.save(drive);
    }

    //tu ce imati opciju da otkaze voznju i da prihvati da se odrzava
    //ako prihvati kreira mu se trasakcija
    //stize mu pitanje da li hoce da se odrzi
    //ako nema sad ovoliko para fail
    //ako nece voznja failed
    //ako hoce
    //ponavljam pitanje da li hoce da ucestvuju tj saljem im one info o voznji opet samo txt notifikacije drugaciji

    public Drive acceptDriveParticipation(Long driveId) throws DriveNotFoundException, NotDrivePassengerException, EmailNotFoundException, URISyntaxException, IOException, InterruptedException, TransactionIdDoesNotExistException {
        String passengersEmail = userService.getLoggedUser().getEmail();
        Drive drive = getDrive(driveId);

        if(!isPassengerInDrive(passengersEmail, drive)) throw new NotDrivePassengerException("Not drive participant.");

        for(Passenger passenger: drive.getPassengers()){
            if(passenger.getPassengerEmail().equals(passengersEmail)){
                passenger.setContribution(DrivePassengerStatus.ACCEPTED);
            }
        }

        drive = driveRepository.save(drive);

        //da li su svi prosli
        if(isParticipationAnswered(drive.getPassengers())){
            //da li su svi prihvatili

            if(allAccepted(drive.getPassengers())){
                //ako jesu
                if(drive.isSplitBill()){
                    //proveriti jel mogu svi da priuste

                    if(canPassengersAfford(drive)){
                        //ako mogu ovo
                        for (Passenger passenger:
                                drive.getPassengers()) {
                            if(!passenger.getPayment().equals(PaymentPassengerStatus.NOT_PAYING)){
                                BankTransaction passengersTransaction = bankService.requestPassengerPayment(drive, userService.findClientsAccount(passengersEmail));
                                passenger.setTransactionId(passengersTransaction.getId());
                            }
                        }
                        drive.setDriveStatus(DriveStatus.PAYMENT_WAITING);
                        drive = driveRepository.save(drive);
                    }
                    else{
                        drive = findPoorParticipants(drive);
                        drive = driveRepository.save(drive);

                        //ponistiti ownerovu transakciju
                        bankService.declineTransaction(drive.getOwnerTransactionId(), drive.getOwner().getClientsBankAccount().getAccountNumber());
                        //drive.setOwnerTransactionId((long) -1);

                        //ponistiti svim passengerima transakcije
                        for (Passenger passenger: drive.getPassengers()){
                            if(passenger.getPayment().equals(PaymentPassengerStatus.ACCEPTED)){
                                bankService.declineTransaction(passenger.getTransactionId(),userService.findClientsAccount(passenger.getPassengerEmail()).getClientsBankAccount().getAccountNumber());
                                //passenger.setTransactionId((long)-1);
                                if(passenger.getPayment().equals(PaymentPassengerStatus.WAITING)){
                                    passenger.setPayment(PaymentPassengerStatus.WAITING);
                                }
                            }


                        }

                        //ponovo izracunati cenu
                        drive = calculateNewPriceForDrive(drive);


                        //nova transakcija owneru? i mejl logicno
                        drive.setOwnerTransactionId(bankService.requestOwnerPayment(drive).getId());
                        return driveRepository.save(drive);
                    }


                }
                if(!drive.isSplitBill()){
                    drive = paymentDone(drive);
                }
            }

            if(!allAccepted(drive.getPassengers())){
                //trenutna jedina kreirana transakcija je ownerova koja ima status waiting_for_finalization
                //nadjem tu njegovu transakciju
                //ponistim je
                bankService.declineTransaction(drive.getOwnerTransactionId(), drive.getOwner().getClientsBankAccount().getAccountNumber());
                //drive.setOwnerTransactionId((long) -1);

                //passenger transakcije nisu kreirane
                //tako racunaj novu cenu
                Drive calculated = calculateNewPriceForDrive(drive);

                //drive status je waiting for owner
                calculated.setDriveStatus(DriveStatus.OWNER_PAYMENT_WAITING);

                //poslati mu notifikaciju sa novim infom o voznji
                driveChangedOwnerNotify(drive);

            }
        }

        //ako nisu nikome nista
        return drive;
    }

    public Drive driveChangedOwnerNotify(Drive drive){
        drive.setDriveStatus(DriveStatus.OWNER_PAYMENT_WAITING);
        notificationService.notifyOwnerDriveChanged(drive.getId(), drive.getOwner().getUser());

        return driveRepository.save(drive);
    }

    public void driveFailedMoneyTransactionRejected(BankTransaction transaction)  {

        //ovo je owenr voznje, zato sto us sutini placanje se odbija finalno s njim...tek kad on odbije, ili nema para voznja je fail
        Drive drive = driveRepository.findByOwner_User_EmailAndOwnerTransactionId(transaction.getSender(), transaction.getId());

        drive.setDriveStatus(DriveStatus.DRIVE_FAILED);

        driveRepository.save(drive);
        notificationService.paymentFailedDriveCanceledNotify(transaction.getSender());
    }

}
