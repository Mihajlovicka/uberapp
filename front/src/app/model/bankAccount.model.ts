interface BankAccountInterface{
    balance: number;
    bankAccountNumber: string;
}

export class BankAccount implements BankAccountInterface{
    public balance: number;
    public bankAccountNumber: string;

    constructor(bankAccountInt: BankAccountInterface){
        this.balance = bankAccountInt.balance;
        this.bankAccountNumber = bankAccountInt.bankAccountNumber;
    }
}