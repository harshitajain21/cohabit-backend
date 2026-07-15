package com.cohabit.cohabitbackend.model.enums;

//This is for me to keep track of status of users
public enum ContactInterestStatus {
    PENDING,            // The user clicked "Interested", waiting for your manual action
    PAYMENT_REQUESTED,  // You have reached out to ask for Rs 25
    UNLOCKED            // Payment received, contact details shared
}
