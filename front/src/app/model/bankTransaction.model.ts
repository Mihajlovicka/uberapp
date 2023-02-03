export interface BankTransactionInterface{
    amount: number;
    receiver: string;
    sender: string;
    transactionStatus: TransactionStatus;
    transactionType:TransactionType
    //dodati date, kao i na beku logicno..strasno jelena :D
}

export class BankTransaction implements BankTransactionInterface{
    public amount: number;
    public receiver: string;
    public sender: string;
    public transactionStatus: TransactionStatus;
    public transactionType: TransactionType;
    constructor(bnkTrans: BankTransactionInterface){
        this.amount = bnkTrans.amount;
        this.receiver = bnkTrans.receiver;
        this.sender = bnkTrans.sender;
        this.transactionStatus = bnkTrans.transactionStatus;
        this.transactionType = bnkTrans.transactionType;
    }
}


export enum TransactionStatus{
    FINALIZED = 'FINALIZED',
    WAITING_FINALIZATION = 'WAITING_FINALIZATION',
    FAILED = 'FAILED',
    WAITING_VERIFICATION = 'WAITING_VERIFICATION'
}

export enum TransactionType{
    INFLOW = 'INFLOW',
    OUTFLOW = 'OUTFLOW'
}