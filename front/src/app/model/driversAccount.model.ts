import { Car } from "./car.model";
import { User } from "./user.model";

export enum DriverStatus{
    AVAILABLE = 'AVAILABLE',
    BUSY = 'BUSY'
}

interface DriversAccountInterface{
    user: User;
    picture: string;
    phone: string;
    car: Car;
    driverStatus: DriverStatus.AVAILABLE;

}

export class DriversAccount implements DriversAccountInterface{
    public user: User;
    public picture: string;
    public phone: string;
    public car: Car;
    public driverStatus: DriverStatus.AVAILABLE;

    constructor(driversInt: DriversAccountInterface){
        this.user = driversInt.user;
        this.picture = driversInt.picture;
        this.phone = driversInt.phone;
        this.car = driversInt.car;
        this.driverStatus = driversInt.driverStatus;
        
    }
}