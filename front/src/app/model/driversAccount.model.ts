import { Car } from "./car.model";
import { User } from "./user.model";

interface DriversAccountInterface{
    user: User;
    picture: string;
    phone: string;
    car: Car;

}

export class DriversAccount implements DriversAccountInterface{
    public user: User;
    public picture: string;
    public phone: string;
    public car: Car;

    constructor(driversInt: DriversAccountInterface){
        this.user = driversInt.user;
        this.picture = driversInt.picture;
        this.phone = driversInt.phone;
        this.car = driversInt.car;
        
    }
}