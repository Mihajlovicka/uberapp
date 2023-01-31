package com.example.demo.converter;

import com.example.demo.dto.*;
import com.example.demo.fakeBank.BankConverter;
import com.example.demo.model.*;
import com.example.demo.service.ImageService;
import com.example.demo.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class UserConverter {

    @Autowired
    AddressConverter addressConverter;

    @Autowired
    CarConverter carConverter;

    @Autowired
    BankConverter bankConverter;

    @Autowired
    RoleService roleService;

    public UserDTO toDTO(User user){

        return new UserDTO(user.getUsername(), user.getName(), user.getSurname(), user.getEmail(), user.getStatus(), user.getRole().getName());

    }

    public ClientAccountDTO toDTO(ClientsAccount clientsAccount){
        if(clientsAccount.getClientsBankAccount() == null){
            return new ClientAccountDTO(this.toDTO(clientsAccount.getUser()), addressConverter.toDTO(clientsAccount.getAddress()), this.toDTO(clientsAccount.getPicture()), clientsAccount.getPhone(), null, clientsAccount.getBankStatus());

        }
        return new ClientAccountDTO(this.toDTO(clientsAccount.getUser()), addressConverter.toDTO(clientsAccount.getAddress()), this.toDTO(clientsAccount.getPicture()), clientsAccount.getPhone(), bankConverter.toDto(clientsAccount.getClientsBankAccount()), clientsAccount.getBankStatus());
    }

    public DriverAccountDTO toDTO(DriversAccount driversAccount){
        return new DriverAccountDTO(this.toDTO(driversAccount.getUser()), this.toDTO(driversAccount.getPicture()), driversAccount.getPhone(), carConverter.todto(driversAccount.getCar()), driversAccount.getDriverStatus());
    }

    public List<UsersChatDisplayDTO> getUsersChats(List<ClientsAccount> clientsAccounts, List<DriversAccount> driversAccounts) {
        List<UsersChatDisplayDTO> users = new ArrayList<UsersChatDisplayDTO>();
        for(ClientsAccount clientsAccount : clientsAccounts){
            UsersChatDisplayDTO usersChatDisplayDTO = new UsersChatDisplayDTO();
            usersChatDisplayDTO.setUser(this.toDTO(clientsAccount.getUser()));
            usersChatDisplayDTO.setImage(this.toDTO(clientsAccount.getPicture()));
            usersChatDisplayDTO.setClient(true);
            usersChatDisplayDTO.setHasMessages(false);
            users.add(usersChatDisplayDTO);
        }
        for(DriversAccount driversAccount : driversAccounts){
            UsersChatDisplayDTO usersChatDisplayDTO = new UsersChatDisplayDTO();
            usersChatDisplayDTO.setUser(this.toDTO(driversAccount.getUser()));
            usersChatDisplayDTO.setImage(this.toDTO(driversAccount.getPicture()));
            users.add(usersChatDisplayDTO);
            usersChatDisplayDTO.setClient(false);
            usersChatDisplayDTO.setHasMessages(false);
        }

        return users;

    }

    public ImageDTO toDTO(Image image){
        if(image==null) return new ImageDTO();
        ImageDTO imageDTO = new ImageDTO();

        imageDTO.setName(image.getName());
        if(image.getPicByte()!=null)imageDTO.setPicByte(ImageService.decompressBytes(image.getPicByte()));
        imageDTO.setType(imageDTO.getType());

        return imageDTO;
    }

    public ClientsAccount fromDTO(ClientAccountDTO clientAccountDTO){
        ClientsAccount clientsAccount = new ClientsAccount();
        clientsAccount.setUser(new User());
        clientsAccount.setAddress(new Address());
        clientsAccount.getUser().setName(clientAccountDTO.getUser().getName());
        clientsAccount.getUser().setSurname(clientAccountDTO.getUser().getSurname());
        clientsAccount.getUser().setEmail(clientAccountDTO.getUser().getEmail());
        clientsAccount.getUser().setRole(roleService.findByName(clientAccountDTO.getUser().getRole()));
        clientsAccount.getUser().setStatus(clientAccountDTO.getUser().getStatus());


        clientsAccount.getAddress().setCity(clientAccountDTO.getAddress().getCity());
        clientsAccount.getAddress().setStreet(clientAccountDTO.getAddress().getStreet());
        clientsAccount.getAddress().setNumber(clientAccountDTO.getAddress().getNumber());

        clientsAccount.setPhone(clientAccountDTO.getPhone());
        //clientsAccount.setPicture(clientAccountDTO.getPicture());
        clientsAccount.setBankStatus(clientAccountDTO.getBankStatus());

        return clientsAccount;
    }

    public DriversAccount fromDTO(DriverAccountDTO driverAccountDTO){
        DriversAccount driversAccount = new DriversAccount();
        driversAccount.setUser(new User());
        driversAccount.setCar(new Car());
        driversAccount.getUser().setName(driverAccountDTO.getUser().getName());
        driversAccount.getUser().setSurname(driverAccountDTO.getUser().getSurname());
        driversAccount.getUser().setEmail(driverAccountDTO.getUser().getEmail());
        driversAccount.getUser().setRole(roleService.findByName(driverAccountDTO.getUser().getRole()));
        driversAccount.getUser().setStatus(driverAccountDTO.getUser().getStatus());

        driversAccount.getCar().setBodyType(driverAccountDTO.getCar().getBodyType());
        driversAccount.getCar().setBrand(driverAccountDTO.getCar().getBrand());
        driversAccount.getCar().setColor(driverAccountDTO.getCar().getColor());
        driversAccount.getCar().setModel(driverAccountDTO.getCar().getModel());
        driversAccount.getCar().setFuelType(driverAccountDTO.getCar().getFuelType());
        driversAccount.getCar().setPlateNumber(driverAccountDTO.getCar().getPlateNumber());

        driversAccount.setPhone(driverAccountDTO.getPhone());
        //driversAccount.setPicture(driverAccountDTO.getPicture());
        driversAccount.setDriverStatus(driverAccountDTO.getDriverStatus());

        return driversAccount;
    }

    public List<ClientAccountDTO> toDTOs(List<ClientsAccount> clients){
        List<ClientAccountDTO> dtos = new ArrayList<ClientAccountDTO>();

        for (ClientsAccount client:
             clients) {
            dtos.add(this.toDTO(client));
        }

        return dtos;
    }

}
