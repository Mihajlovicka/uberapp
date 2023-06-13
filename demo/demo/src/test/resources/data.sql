INSERT INTO roles(id,name) VALUES (1,'ROLE_DRIVER');

INSERT INTO address(id, city, number,street)
VALUES (1,'novi sad',5,'ulica');

INSERT INTO user (id,email,enabled,last_password_reset_date,name,password,status,surname,username,role_id)
VALUES (1 ,'test@gmail.com' ,true,null,'mika','nekasifra123',0,'mikic','test@gmail.com',1);

INSERT INTO user (id,email,enabled,last_password_reset_date,name,password,status,surname,username,role_id)
VALUES (2 ,'testtest@gmail.com' ,true,null,'mika','nekasifra123',0,'mikic','test@gmail.com',1);

INSERT INTO roles(id, name)
VALUES (1, 'ROLE_DRIVER');
INSERT INTO roles(id, name)
VALUES (2, 'ROLE_CLIENT');

INSERT INTO address(id, city, number, street)
VALUES (1, 'novi sad', 5, 'ulica');

-- nekasifra123

INSERT INTO user (id, email, enabled, last_password_reset_date, name, password, status, surname, username, role_id)
VALUES (1, 'driver@com', true, null, 'mika', '$2a$10$s9x6V5BoqKj8dHDv.oJjCeu6R0Qimjsw3vgtN5qFnLcYj3Gy.PXNe', 0, 'mikic', 'driver@com', 1);

INSERT INTO user (id, email, enabled, last_password_reset_date, name, password, status, surname, username, role_id)
VALUES (2, 'klijent1@com', true, null, 'mika', '$2a$10$s9x6V5BoqKj8dHDv.oJjCeu6R0Qimjsw3vgtN5qFnLcYj3Gy.PXNe', 0, 'mikic', 'klijent1@com', 2);

INSERT INTO user (id, email, enabled, last_password_reset_date, name, password, status, surname, username, role_id)
VALUES (3, 'klijent2@com', true, null, 'mika', '$2a$10$s9x6V5BoqKj8dHDv.oJjCeu6R0Qimjsw3vgtN5qFnLcYj3Gy.PXNe', 0, 'mikic', 'klijent2@com', 2);

INSERT INTO user (id, email, enabled, last_password_reset_date, name, password, status, surname, username, role_id)
VALUES (4, 'klijent3@com', true, null, 'mika', '$2a$10$s9x6V5BoqKj8dHDv.oJjCeu6R0Qimjsw3vgtN5qFnLcYj3Gy.PXNe', 0, 'mikic', 'klijent3@com', 2);

-- driver

INSERT INTO car(id, brand, color, model, num_of_seats, plate_number, car_on_map, latitude, longitude)
VALUES (1, 'BMW', 'crvenajebena', 'X6', 5, 'NS000AB', false, 45.254125, 19.796972);

INSERT INTO drivers_account(id, driver_status, car_id, user_id, drivers_availability)
VALUES (1, 0, 1, 1, true);

-- clients

INSERT INTO clients_bank_account(id, balance, account_number, verification_email)
VALUES (1, 1000.0, '111','saramihajlovic94@gmail.com');

INSERT INTO clients_bank_account(id, balance, account_number, verification_email)
VALUES (2, 1000.0, '222','saramihajlovic94@gmail.com');

INSERT INTO clients_bank_account(id, balance, account_number, verification_email)
VALUES (3, 1000.0, '333','saramihajlovic94@gmail.com');

INSERT INTO clients_account(id, bank_status, phone, address_id, clients_bank_account_id, user_id, picture_id, in_drive)
VALUES (1, 0, '1234567890', 1, 1, 2, null, false);

INSERT INTO clients_account(id, bank_status, phone, address_id, clients_bank_account_id, user_id, picture_id, in_drive)
VALUES (2, 0, '1234567890', 1, 2, 3, null, false);

INSERT INTO clients_account(id, bank_status, phone, address_id, clients_bank_account_id, user_id, picture_id, in_drive)
VALUES (3, 0, '1234567890', 1, 3, 4, null, false);

-- transaction


-- drive

INSERT INTO drive(id, drive_type, driver_id, owner_id, baby, baby_seats, pets, seats, split_bill, date, distance,
                  duration, price, owner_debit, owner_transaction_id)
VALUES (1, 0, 1, 1, 0, 0, 0, 0, false, '2022-02-02 15:44:00', 5.0, 5.5, 500.0, 500.0, 1);

INSERT INTO drive(id, drive_type, driver_id, owner_id, baby, baby_seats, pets, seats, split_bill, date, distance,
                  duration, price, owner_debit, owner_transaction_id)
VALUES (2, 1, 1, 1, 0, 0, 0, 0, false, '2022-02-02 15:44:00', 5.0, 5.5, 500.0, 500.0, 1);


INSERT INTO drive(id, drive_type, driver_id, owner_id, baby, baby_seats, pets, seats, split_bill, date, distance,
                  duration, price, owner_debit, owner_transaction_id, drive_status)
VALUES (1, 0, 1, 1, 0, 0, 0, 0, false, '2022-02-02 15:44:00', 5.0, 5.5, 500.0, 500.0, 1, 1);

-- stops, passengers

INSERT INTO passenger(id, passenger_email, contribution, payment, debit,paying_enabled, transaction_id)
VALUES (1, 'klijent2@com', 0, 0, 200.0, true,1);

INSERT INTO passenger(id, passenger_email, contribution, payment, debit,paying_enabled, transaction_id)
VALUES (2, 'klijent3@com', 0, 0, 200.0, true,1);

INSERT INTO passenger(id, passenger_email, contribution, payment, debit,paying_enabled, transaction_id)
VALUES (3, 'klijent2@com', 0, 0, 200.0, true,1);

INSERT INTO passenger(id, passenger_email, contribution, payment, debit,paying_enabled, transaction_id)
VALUES (4, 'klijent3@com', 0, 0, 200.0, true,1);

INSERT INTO passenger(id, passenger_email, contribution, payment, debit,paying_enabled, transaction_id)
VALUES (5, 'klijent2@com', 2, 0, 200.0, true,1);

-- INSERT INTO real_address(id, latitude, longitude)
-- VALUES (1, 45.254125, 19.796972);
--
-- INSERT INTO real_address(id, latitude, longitude)
-- VALUES (2, 45.257101, 19.812107);

INSERT INTO bank_transaction(id,transaction_type, amount, transaction_status)
VALUES (1,0,500.0,3);


INSERT INTO drive_passengers(drive_id, passengers_id)
VALUES (1,1);

INSERT INTO drive_passengers(drive_id, passengers_id)
VALUES (1,2);

INSERT INTO drive_passengers(drive_id, passengers_id)
VALUES (2,3);

INSERT INTO drive_passengers(drive_id, passengers_id)
VALUES (2,4);

INSERT INTO drive_passengers(drive_id, passengers_id)
VALUES (3,5);


INSERT INTO car(id,brand,color,latitude,longitude,model,num_of_seats,plate_number, car_on_map)
VALUES (1,'BMW','crvenajebena',45.248861,19.833332,'X6',5,'NS000AB',false);

INSERT INTO drivers_account(id,driver_status,car_id,user_id,drivers_availability)
VALUES (1,0,1,1, true);

INSERT INTO drivers_account(id,driver_status,car_id,user_id,drivers_availability)
VALUES (2,0,1,1, true);

INSERT INTO clients_account(id,bank_status, phone,address_id,clients_bank_account_id,user_id, picture_id)
VALUES(1,2,'1234567890',1,null,2,null)

