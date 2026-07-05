package com.cohabit.cohabitbackend.exception;

public class EmailAlreadyExistsException extends RuntimeException{

    public EmailAlreadyExistsException(String message){
        super(message); //Call the constructor of the parent (RuntimeException) and give it this message
    }
}
