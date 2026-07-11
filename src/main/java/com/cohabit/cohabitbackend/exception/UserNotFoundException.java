package com.cohabit.cohabitbackend.exception;

/**
 * Raised when an expected user account cannot be found.
 */
public class UserNotFoundException extends RuntimeException {

    /**
     * Creates a user-not-found exception.
     *
     * @param message safe API-facing error message
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}
