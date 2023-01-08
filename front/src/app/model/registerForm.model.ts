import { Address } from "./address.model";
import { Role } from "./user.model";

interface RegisterFormInterface{
    username: string;
    name: string;
    surname: string;
    email: string;
    phone: string;
    address: Address;
    password: string;
    checkPassword: string;
    role: Role;
    bankAccountNumber: string;

}

export class RegisterForm implements RegisterFormInterface{
    public username: string;
    public name: string;
    public surname: string;
    public email: string;
    public phone: string;
    public address: Address;
    public password: string;
    public checkPassword: string;
    public role: Role;
    public bankAccountNumber: string;

    constructor(registerFormInt: RegisterFormInterface){
        this.username = registerFormInt.username;
        this.name = registerFormInt.name;
        this.surname = registerFormInt.surname;
        this.email = registerFormInt.email;
        this.phone = registerFormInt.phone;
        this.address = registerFormInt.address;
        this.password = registerFormInt.password;
        this.checkPassword = registerFormInt.checkPassword;
        this.role = registerFormInt.role;
        this.bankAccountNumber = registerFormInt.bankAccountNumber;
    }
}