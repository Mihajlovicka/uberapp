import { Car } from "./car.model";
import { User } from "./user.model";
import {Image} from "./image.model";

export enum DriverStatus{
    AVAILABLE = 'AVAILABLE',
    BUSY = 'BUSY',
    GOING_TO_LOCATION = 'GOING_TO_LOCATION'
}

interface DriversAccountInterface{
    user: User;
    picture: Image;
    phone: string;
    car: Car;
    driverStatus: DriverStatus.AVAILABLE;
    driversAvailability:boolean
}

export class DriversAccount implements DriversAccountInterface{
    public user: User;
    public picture: Image;
    public phone: string;
    public car: Car;
    public driverStatus: DriverStatus.AVAILABLE;
    public driversAvailability:boolean;

    constructor(driversInt: DriversAccountInterface){
        this.user = driversInt.user;
        this.picture = driversInt.picture;
        this.phone = driversInt.phone;
        this.car = driversInt.car;
        this.driverStatus = driversInt.driverStatus;
        this.driversAvailability = driversInt.driversAvailability
    }
}
