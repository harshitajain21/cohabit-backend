package com.cohabit.cohabitbackend.exception;

/**
 * Raised when an email verification token is missing, expired, already used, or invalid.
 */
public class InvalidVerificationTokenException extends RuntimeException {

    /**
     * Creates an invalid-token exception.
     *
     * @param message safe API-facing error message
     */
    public InvalidVerificationTokenException(String message) {
        super(message);
    }
}
