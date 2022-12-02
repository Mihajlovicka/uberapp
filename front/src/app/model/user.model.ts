import { Address } from "./address.model";




export enum Status {
    ACTIVE = 'ACTIVE',
    NOTACTIVATED = 'NOTACTIVATED',
    BANNED = 'BANNED'
}

export enum Role {
    CLIENT = 'CLIENT',
    DRIVER = 'DRIVER',
    ADMINISTRATOR = 'ADMINISTRATOR'
}


interface UserInterface{
    name: string;
    surname: string;
    email: string;
    address: Address;
    phone: string;
    status: Status;
    role: Role;

}

export class User implements UserInterface{
    public name: string;
    public surname: string;
    public email: string;
    public address: Address;
    public phone: string;
    public status: Status;
    public role: Role;

    constructor(userInt: UserInterface){
        this.name = userInt.name;
        this.surname = userInt.surname;
        this.email = userInt.email;
        this.address = userInt.address;
        this.phone = userInt.phone;
        this.status = userInt.status;
        this.role = userInt.role;
    }
}

