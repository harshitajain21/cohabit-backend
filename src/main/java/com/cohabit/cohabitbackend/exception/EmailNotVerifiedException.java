package com.cohabit.cohabitbackend.exception;

//Raised when an unverified user attempts an action that requires verification.

public class EmailNotVerifiedException extends RuntimeException {

    public EmailNotVerifiedException(String message) {
        super(message);
    }
}
