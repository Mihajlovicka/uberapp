INSERT INTO roles(id,name) VALUES (1,'ROLE_DRIVER');

INSERT INTO address(id, city, number,street)
VALUES (1,'novi sad',5,'ulica');

INSERT INTO user (id,email,enabled,last_password_reset_date,name,password,status,surname,username,role_id)
VALUES (1 ,'test@gmail.com' ,true,null,'mika','nekasifra123',0,'mikic','test@gmail.com',1);

INSERT INTO user (id,email,enabled,last_password_reset_date,name,password,status,surname,username,role_id)
VALUES (2 ,'testtest@gmail.com' ,true,null,'mika','nekasifra123',0,'mikic','test@gmail.com',1);


INSERT INTO car(id,brand,color,latitude,longitude,model,num_of_seats,plate_number, car_on_map)
VALUES (1,'BMW','crvenajebena',45.248861,19.833332,'X6',5,'NS000AB',false);

INSERT INTO drivers_account(id,driver_status,car_id,user_id,drivers_availability)
VALUES (1,0,1,1, true);

INSERT INTO drivers_account(id,driver_status,car_id,user_id,drivers_availability)
VALUES (2,0,1,1, true);

INSERT INTO clients_account(id,bank_status, phone,address_id,clients_bank_account_id,user_id, picture_id)
VALUES(1,2,'1234567890',1,null,2,null)

