interface AddressInterface {
    city: string;
    street: string;
    number: string;
}

export class Address implements AddressInterface {
    public city: string;
    public street: string;
    public number: string;

    constructor(addressInt: AddressInterface) {
        this.city = addressInt.city;
        this.street = addressInt.street;
        this.number = addressInt.number;
        }
}