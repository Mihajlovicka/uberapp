import { Car } from './car.model';
import { Role } from './user.model';

interface DriverCarInfoInterface{
    username: string;
    name: string;
    surname: string;
    email: string;
    phone: string;
    password: string;
    checkPassword: string;
    role: Role;
    car: Car;
}

export class DriverCarInfo implements DriverCarInfoInterface{
    public username: string;
    public name: string;
    public surname: string;
    public email: string;
    public phone: string;
    public password: string;
    public checkPassword: string;
    public role: Role;
    public car: Car;

    constructor(formInt: DriverCarInfoInterface){
        this.username = formInt.username;
        this.name = formInt.name;
        this.surname = formInt.surname;
        this.email = formInt.email;
        this.phone = formInt.phone;
        this.password = formInt.password;
        this.checkPassword = formInt.checkPassword;
        this.role = formInt.role;
        this.car = formInt.car;
    }
}