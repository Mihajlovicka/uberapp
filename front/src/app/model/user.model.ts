import { Address } from "./address.model";




export enum Status {
    ACTIVE = 'ACTIVE',
    NOTACTIVATED = 'NOTACTIVATED',
    BANNED = 'BANNED'
}


interface UserInterface{
    name: string;
    surname: string;
    email: string;
    address: Address;
    phone: string;
    status: Status
  

}

export class User implements UserInterface{
    public name: string;
    public surname: string;
    public email: string;
    public address: Address;
    public phone: string;
    public status: Status;

    constructor(userInt: UserInterface){
        this.name = userInt.name;
        this.surname = userInt.surname;
        this.email = userInt.email;
        this.address = userInt.address;
        this.phone = userInt.phone;
        this.status = userInt.status;
    }
}

