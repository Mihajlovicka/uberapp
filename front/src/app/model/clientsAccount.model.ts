import { Address } from "./address.model";
import { User } from "./user.model";

interface ClientsAccountInterface{
    user: User;
    address: Address;
    picture: string;
    phone: string;
}

export class ClientsAccount implements ClientsAccountInterface{
    public user: User;
    public address: Address;
    public picture: string;
    public phone: string;

    constructor(clientsInt: ClientsAccountInterface){
        this.user = clientsInt.user;
        this.address = clientsInt.address;
        this.picture = clientsInt.picture;
        this.phone = clientsInt.phone;
    }
}