interface BankAccountInterface{
    balance: number;
    bankAccountNumber: string;
    verificationEmail: string;
    ownerName: string;
    ownerSurname: string;
}

export class BankAccount implements BankAccountInterface{
    public balance: number;
    public bankAccountNumber: string;
    public verificationEmail: string;
    public ownerName: string;
    public ownerSurname: string;

    constructor(bankAccountInt: BankAccountInterface){
        this.balance = bankAccountInt.balance;
        this.bankAccountNumber = bankAccountInt.bankAccountNumber;
        this.verificationEmail = bankAccountInt.verificationEmail;
        this.ownerName = bankAccountInt.ownerName;
        this.ownerSurname = bankAccountInt.ownerSurname;
    }
}