package com.example.demo.service;

import com.example.demo.converter.UserConverter;
import com.example.demo.dto.MessageDTO;
import com.example.demo.exception.EmailNotFoundException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.*;
import com.example.demo.model.help.ResponseRouteHelp;
import com.example.demo.model.help.ResponseTableHelp;
import com.example.demo.repository.DriveRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Timestamp;
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
    private UserConverter conv;

    @Autowired
    private CarService carService;


    @Autowired
    private RideSimulationService rideSimulationService;

    public Drive saveDrive(Drive drive) throws URISyntaxException, IOException, InterruptedException, EmailNotFoundException {

        //driver se stavlja na null
        drive.setDriver(null);
        //drive startus se stavlja na waiting passengers
        drive.setDriveStatus(DriveStatus.PASSENGERS_WAITING);
        drive.setDriveTimeStatus(DriveTimeStatus.NOW);
        String foundDriverEmail = getNextDriverForCurrentRide(drive);
//        Long foundDriverId = getNextDriverForFutureRide(drive);
        if (foundDriverEmail.equals("")) {
            throw new NotFoundException("Nema trenutno slobodnog vozaca.");
        }
        DriversAccount driver = userService.getDriver(foundDriverEmail);

        drive.setDriver(driver);
        notificationService.addNotification(new Notification("Nova voznja", "Dodeljena vam je nova voznja. " + drive.getDate(), driver.getUser(),"localhost:4200/rides-dr"));
        notificationService.addNotificationMultiple(new Notification("Nova voznja", "Vasa voznja je odobrena. ", driver.getUser(),""), makeUsersFromPassengersForNotification(drive));
        return driveRepository.save(drive);

    }


    public String getNextDriverForFutureRide(Drive drive) {
        List<DriversAccount> drivers = this.userService.getDrivers();
        if (drivers.size() > 0) {
            DriversAccount d = getDriversDistancesFuture(drivers, drive);
            return d == null ? "" : d.getUser().getEmail();
        }
        return "";
    }


    public String getNextDriverForCurrentRide(Drive drive) throws URISyntaxException, IOException, InterruptedException {
        List<DriversAccount> drivers = this.userService.getDriversByStatus(DriverStatus.AVAILABLE);
        Map<String, Map<String, Double>> distances = new HashMap<>();
        Location startLocation = drive.getStops().get(0).getLocation();
        if (drivers.size() > 0) {
            //proveriti da li su slobodni celu voznju
            //od njegove lokacije do starta do kraja vreme i moze da otskace 5min
            distances = getFreeDriversDistancesNow(drivers, drive);
        }
        if (distances.size() == 0) {
            drivers = this.userService.getDriversByStatus(DriverStatus.BUSY);
            distances = getBusyDriversDistancesNow(drivers, drive);
        }
        if (distances.size() == 0) return "";
        return getMinDistanceDriver(distances);
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

    public List<RealAddress> getCurrentDriveStops() {
        Drive drive = getCurrentDrive();
        if(drive != null) return drive.getStops();
        return new ArrayList<>();
    }

    private Drive getCurrentDrive(){
        DriversAccount driver = userService.getLoggedDriver();
        for (Drive d : driveRepository.findByDriver(driver)) {
            if(d.getDriveTimeStatus().equals(DriveTimeStatus.NOW))
                return d;
        }
        return null;
    }



    public Drive getFirstFutureDrive() {
        List<Drive> future = new ArrayList<>();
        DriversAccount driver = userService.getLoggedDriver();
        for (Drive d : driveRepository.findByDriver(driver)) {
            if(d.getDriveTimeStatus().equals(DriveTimeStatus.FUTURE))
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


    public String endDrive() {
        Drive drive = getCurrentDrive();
        if(drive != null){
            drive.setDriveTimeStatus(DriveTimeStatus.PAST);
            userService.updateDriverStatus(DriverStatus.AVAILABLE);
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
            drive.setDriveTimeStatus(DriveTimeStatus.NOW);
            userService.updateDriverStatus(DriverStatus.GOING_TO_LOCATION);
            driveRepository.save(drive);
            return drive.getStops();
        }
        return new ArrayList<>();
    }

    public String startDrive() {
        Drive drive = getCurrentDrive();
        if(drive != null){
            userService.updateDriverStatus(DriverStatus.BUSY);
            rideSimulationService.createRideSim(drive);
            return "Zapoceta nova voznja.";
        }
        return "Nema voznje.";
    }

    public Location getCarStartEndStop(Long id,boolean first) throws JsonProcessingException {
        Car c = carService.getCar(id);
        for(Drive d: driveRepository.findByDriveTimeStatus(DriveTimeStatus.NOW)){
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

    public Car getClientCurrentCar(){
        User user = userService.getLoggedIn();
        for(Drive d: driveRepository.findByDriveTimeStatus(DriveTimeStatus.NOW)){
            if(d.getOwner().getUser().getEmail().equals(user.getEmail()))
                return d.getDriver().getCar();
            for(Passenger passenger:d.getPassengers()){
                if(passenger.getPassengerEmail().equals(user.getEmail()))
                    return d.getDriver().getCar();
            }
        }
        throw new NotFoundException("No client current ride.");
    }

    public List<RealAddress> getClientCurrentDriveStops() {
        User user = userService.getLoggedIn();
        for(Drive d: driveRepository.findByDriveTimeStatus(DriveTimeStatus.NOW)){
            if(d.getOwner().getUser().getEmail().equals(user.getEmail()))
                return d.getStops();
            for(Passenger passenger:d.getPassengers()){
                if(passenger.getPassengerEmail().equals(user.getEmail()))
                    return d.getStops();
            }
        }
        return new ArrayList<>();
    }

    private List<User> makeUsersFromPassengersForNotification(Drive drive){
        List<User> users = new ArrayList<>();
        users.add(drive.getOwner().getUser());
        for(Passenger p: drive.getPassengers()){
            User u = new User();
            u.setEmail(p.getPassengerEmail());
            users.add(u);
        }
        return users;
    }

    public Drive getCarCurrentDrive(int id) {
        for(Drive d: driveRepository.findByDriveTimeStatus(DriveTimeStatus.NOW)){
            if(d.getDriver().getCar().getId() == id){
                return d;
            }
        }
        return null;
    }
}
