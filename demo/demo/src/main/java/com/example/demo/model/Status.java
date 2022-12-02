package com.example.demo.model;
public enum Status {
    ACTIVE, // account is activated and can be used; account is visible to others
    NOTACTIVATED, // account is created, but not activated - verification email is not confirmed
    BANNED,  // account exist but its owner can't access; account is visible to administrators only
    UNDERREVISION,  // changes are applied to account by owner (driver), but administrators did not accept them yet
    DELETED // account do not exist
}
