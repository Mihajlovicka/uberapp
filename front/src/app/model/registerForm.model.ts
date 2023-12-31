import { Address } from "./address.model";
import { Role } from "./user.model";

interface RegisterFormInterface{
    name: string;
    surname: string;
    email: string;
    phone: string;
    address: Address;
    password: string;
    checkPassword: string;
    role: Role;

}

export class RegisterForm implements RegisterFormInterface{
    public name: string;
    public surname: string;
    public email: string;
    public phone: string;
    public address: Address;
    public password: string;
    public checkPassword: string;
    public role: Role;

    constructor(registerFormInt: RegisterFormInterface){
        this.name = registerFormInt.name;
        this.surname = registerFormInt.surname;
        this.email = registerFormInt.email;
        this.phone = registerFormInt.phone;
        this.address = registerFormInt.address;
        this.password = registerFormInt.password;
        this.checkPassword = registerFormInt.checkPassword;
        this.role = registerFormInt.role;
    }
}