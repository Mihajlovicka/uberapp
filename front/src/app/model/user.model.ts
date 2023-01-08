
export enum Status {
    ACTIVE = 'ACTIVE',
    NOTACTIVATED = 'NOTACTIVATED',
    BANNED = 'BANNED',
    UNDERREVISION = 'UNDERREVISION',
    DELETED = 'DELETED'
}

export enum Role {
    ROLE_DRIVER = 'ROLE_DRIVER',
    ROLE_ADMINISTRATOR = 'ROLE_ADMINISTRATOR',
    ROLE_CLIENT = 'ROLE_CLIENT',
}


interface UserInterface{
    username: string;
    name: string;
    surname: string;
    email: string;
    status: Status;
    role: Role;

}

export class User implements UserInterface{
    public username: string;
    public name: string;
    public surname: string;
    public email: string;
    public status: Status;
    public role: Role;

    constructor(userInt: UserInterface){
        this.username = userInt.username;
        this.name = userInt.name;
        this.surname = userInt.surname;
        this.email = userInt.email;
        this.status = userInt.status;
        this.role = userInt.role;
    }
}

