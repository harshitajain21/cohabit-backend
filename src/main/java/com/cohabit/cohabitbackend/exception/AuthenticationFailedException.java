package com.cohabit.cohabitbackend.exception;

//Raised when supplied login credentials cannot authenticate a user.

public class AuthenticationFailedException extends RuntimeException {

    public AuthenticationFailedException(String message) {
        super(message);
    }
}
