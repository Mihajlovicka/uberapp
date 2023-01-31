package com.example.demo.service;

import com.example.demo.exception.NotFoundException;
import com.example.demo.model.*;
import com.example.demo.model.help.ResponseTableHelp;
import com.example.demo.repository.DriveRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class DriveService {
    @Autowired
    DriveRepository driveRepository;

    @Autowired
    UserService userService;

    @Autowired
    private RideSimulationService rideSimulationService;

    public Drive saveDrive(Drive drive) throws URISyntaxException, IOException, InterruptedException {

        //driver se stavlja na null
        drive.setDriver(null);
        //drive startus se stavlja na waiting passengers
        drive.setDriveStatus(DriveStatus.PASSENGERS_WAITING);

        Long foundDriverId = getNextDriverForCurrentRide(drive);
        Long foundDriverId1 = getNextDriverForFutureRide(drive);
        if(foundDriverId == -1){
            throw new NotFoundException("Nema trenutno slobodnog vozaca.");
        }
        DriversAccount driver = userService.getDriver(foundDriverId);
        //TODO poslati notifikaciju vozacu da ce imati voznju
        drive.setDriver(driver);

        return driveRepository.save(drive);

    }

    public Long getNextDriverForFutureRide(Drive drive) throws URISyntaxException, IOException, InterruptedException {
        List<DriversAccount> drivers = this.userService.getDrivers();
        Map<Long, Map<String, Double>> distances = new HashMap<>();
        Location startLocation = drive.getStops().get(0).getLocation();
        if (drivers.size() > 0) {
            //proveriti da li su slobodni celu voznju
            //od njegove lokacije do starta do kraja vreme i moze da otskace 5min
            distances = getBusyDriversDistancesNow(drivers, drive);
        }
        if(distances.size() == 0)return -1L;
        return getMinDistanceDriver(distances);
    }


    public Long getNextDriverForCurrentRide(Drive drive) throws URISyntaxException, IOException, InterruptedException {
        List<DriversAccount> drivers = this.userService.getDriversByStatus(DriverStatus.AVAILABLE);
        Map<Long, Map<String, Double>> distances = new HashMap<>();
        Location startLocation = drive.getStops().get(0).getLocation();
        if (drivers.size() > 0) {
            //proveriti da li su slobodni celu voznju
            //od njegove lokacije do starta do kraja vreme i moze da otskace 5min
            distances =  getFreeDriversDistancesNow(drivers, drive);
        }
        if(distances.size() == 0){
            drivers = this.userService.getDriversByStatus(DriverStatus.BUSY);
            distances = getBusyDriversDistancesNow(drivers, drive);
        }
        if(distances.size() == 0)return -1L;
        return getMinDistanceDriver(distances);
    }

    private Map<Long, Map<String, Double>> getFreeDriversDistancesNow(List<DriversAccount> drivers, Drive drive) throws IOException, URISyntaxException, InterruptedException {
        Location newStart = drive.getStops().get(0).getLocation();
        Map<Long, Map<String, Double>> driversDistances = new HashMap<>();
        for (DriversAccount driver : drivers) {
            Location carCurrentLocation = driver.getCar().getCurrentLocation();
            HashMap<String, Double> result = makeRequestForRide(carCurrentLocation, newStart, null);
            double time = result.get("duration") +  drive.getDuration()*60;//u s
            //da li ima voznju koja pocinje pre vocoga vremena now + time
            long millis = System.currentTimeMillis();
            Date newRideStart = new Date(millis); //od sad
            Date newRideEnd = new Date((long) (millis + time*1000));
            if(isDriverFreeInPeriod(newRideStart, newRideEnd, driver)){
                driversDistances.put(driver.getId(), result);
            }
        }
        return driversDistances;
    }

    private Map<Long, Map<String, Double>> getBusyDriversDistancesNow(List<DriversAccount> drivers, Drive drive) throws URISyntaxException, IOException, InterruptedException {
        Location newStart = drive.getStops().get(0).getLocation();
        Map<Long, Map<String, Double>> driversDistances = new HashMap<>();
        for (DriversAccount driver : drivers) {
            Location carCurrentLocation = driver.getCar().getCurrentLocation();
            Location endLocation = rideSimulationService.getCarEndStop(driver.getCar().getId());
            HashMap<String, Double> result = makeRequestForRide(carCurrentLocation, endLocation, newStart);
            double totalTime = result.get("duration") + drive.getDuration()*60;
            long millis = System.currentTimeMillis();
            Date newRideStart = new Date(millis); //od sad
            Date newRideEnd = new Date((long) (millis + totalTime*1000)); // do kraja nove voznje
            if(isDriverFreeInPeriod(newRideStart, newRideEnd, driver)){
                driversDistances.put(driver.getId(), result);
            }
        }
        return driversDistances;
    }

    private Map<Long, Map<String, Double>> getDriversDistancesFuture(List<DriversAccount> drivers, Drive drive) throws URISyntaxException, IOException, InterruptedException {
        Location newStart = drive.getStops().get(0).getLocation();
        Map<Long, Map<String, Double>> driversDistances = new HashMap<>();
        for (DriversAccount driver : drivers) {
            Location carCurrentLocation = driver.getCar().getCurrentLocation();
            Location endLocation = rideSimulationService.getCarEndStop(driver.getCar().getId());
            HashMap<String, Double> result = makeRequestForRide(carCurrentLocation, endLocation, newStart);
            double totalTime = result.get("duration") + drive.getDuration()*60;
            Date newRideStart = new Date(drive.getDate().getTime()); //od sad
            Date newRideEnd = new Date((long) (newRideStart.getTime() + totalTime*1000)); // do kraja nove voznje
            if(isDriverFreeInPeriod(newRideStart, newRideEnd, driver)){
                driversDistances.put(driver.getId(), result);
            }
        }
        return driversDistances;
    }

    private boolean isDriverFreeInPeriod(Date start, Date end, DriversAccount driver) {
        for(Drive drive: driveRepository.findByDriver(driver)){
            Date oldStartDate = drive.getDate();
            Date oldEndDate = new Date((long) (drive.getDate().getTime() + drive.getDuration()*60*1000));
            if(oldStartDate.before(end) || oldEndDate.after(start))
                return false;
        }
        return true;
    }

    private Long getMinDistanceDriver(Map<Long, Map<String, Double>> distances) {
        Long id = -1L;
        double min = -1;
        for (Long i : distances.keySet()) {
            if (id == -1) {
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

//    private Map<Long, Map<String, Double>> getDistancesAvailableDriver(List<DriversAccount> drivers, Location startLocation) throws URISyntaxException, IOException, InterruptedException {
//        Map<Long, Map<String, Double>> distances = new HashMap<>();
//        for (DriversAccount driver : drivers) {
//            Location carCurrentLocation = driver.getCar().getCurrentLocation();
//            distances.put(driver.getId(), makeRequestForRide(carCurrentLocation, startLocation, null));
//        }
//        return distances;
//    }
}
