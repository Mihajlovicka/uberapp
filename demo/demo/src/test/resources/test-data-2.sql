
INSERT INTO user (id, email, enabled, last_password_reset_date, name, password, status, surname, username, role_id)
VALUES (5, 'jelenamanojlovic27062000@gmail.com', true, null, 'mika', '$2a$10$s9x6V5BoqKj8dHDv.oJjCeu6R0Qimjsw3vgtN5qFnLcYj3Gy.PXNe', 0, 'mikic', 'jelenamanojlovic27062000@gmail.com', 2);

INSERT INTO clients_account(id, bank_status, phone, address_id, clients_bank_account_id, user_id, picture_id, in_drive)
VALUES (4, 0, '1234567890', 1, 3, 5, null, false);

INSERT INTO drive(id,drive_type,driver_id,owner_id,baby,baby_seats,pets,seats,split_bill,owner_transaction_id)
VALUES (4,0,1,4,0,0,0,0,false,1);


-- INSERT INTO drive(id,drive_type,driver_id,owner_id,baby,baby_seats,pets,seats,split_bill)
-- VALUES (2,1,1,1,0,0,0,0,false);

