export enum Fuel{
    GASOLINE = 'GASOLINE',
    DIESEL = 'DIESEL',
    AUTOGAS = 'AUTOGAS'
}

export enum CarBodyType{
    COUPE = 'COUPE',
    HATCHBACK = 'HATCHBACK',
    SEDAN = 'SEDAN',
    SUV = 'SUV',
    JEEP = 'JEEP'
}

interface CarInterface{
    brand: string;
    model: string;
    color: string;
    plateNumber: string;
    bodyType: CarBodyType;
    fuelType: Fuel;
    numOfSeats:number;
}

export class Car implements CarInterface{
    public brand: string;
    public model: string;
    public color: string;
    public plateNumber: string;
    public bodyType: CarBodyType;
    public fuelType: Fuel;
    public numOfSeats: number;

    constructor(carInt: CarInterface){
        this.brand = carInt.brand;
        this.model = carInt.model;
        this.color = carInt.color;
        this.plateNumber = carInt.plateNumber;
        this.bodyType = carInt.bodyType;
        this.fuelType = carInt.fuelType;
        this.numOfSeats = carInt.numOfSeats;
    }
}