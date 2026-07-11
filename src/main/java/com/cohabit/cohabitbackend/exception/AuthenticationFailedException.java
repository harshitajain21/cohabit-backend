package com.cohabit.cohabitbackend.exception;

/**
 * Raised when supplied login credentials cannot authenticate a user.
 */
public class AuthenticationFailedException extends RuntimeException {

    /**
     * Creates an authentication failure exception.
     *
     * @param message safe API-facing error message
     */
    public AuthenticationFailedException(String message) {
        super(message);
    }
}
