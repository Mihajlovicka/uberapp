import { Address } from "./address.model";
import { BankAccount } from "./bankAccount.model";
import { User } from "./user.model";
import {Image} from "./image.model";

export enum BankStatus {
    ACTIVE = 'ACTIVE',
    NOTCONFIRMED = 'NOTCONFIRMED',
    EMPTY = 'EMPTY'
}

interface ClientsAccountInterface{
    user: User;
    address: Address;
    picture: Image;
    phone: string;
    clientsBankAccount: BankAccount;
    bankStatus: BankStatus;
    inDrive: boolean;
}

export class ClientsAccount implements ClientsAccountInterface{
    public user: User;
    public address: Address;
    public picture: Image;
    public phone: string;
    public clientsBankAccount: BankAccount;
    public bankStatus: BankStatus;
    public inDrive: boolean;

    constructor(clientsInt: ClientsAccountInterface){
        this.user = clientsInt.user;
        this.address = clientsInt.address;
        this.picture = clientsInt.picture;
        this.phone = clientsInt.phone;
        this.clientsBankAccount = clientsInt.clientsBankAccount;
        this.bankStatus = clientsInt.bankStatus;
        this.inDrive = clientsInt.inDrive;
    }
}
