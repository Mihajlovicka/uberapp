
export enum Status {
    ACTIVE = 'ACTIVE',
    NOTACTIVATED = 'NOTACTIVATED',
    BANNED = 'BANNED',
    UNDERREVISION = 'UNDERREVISION',
    DELETED = 'DELETED'
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
    status: Status;
    role: Role;

}

export class User implements UserInterface{
    public name: string;
    public surname: string;
    public email: string;
    public status: Status;
    public role: Role;

    constructor(userInt: UserInterface){
        this.name = userInt.name;
        this.surname = userInt.surname;
        this.email = userInt.email;
        this.status = userInt.status;
        this.role = userInt.role;
    }
}

