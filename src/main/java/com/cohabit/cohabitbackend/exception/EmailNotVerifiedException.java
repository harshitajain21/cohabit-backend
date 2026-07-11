package com.cohabit.cohabitbackend.exception;

/**
 * Raised when an unverified user attempts an action that requires verification.
 */
public class EmailNotVerifiedException extends RuntimeException {

    /**
     * Creates an email-not-verified exception.
     *
     * @param message safe API-facing error message
     */
    public EmailNotVerifiedException(String message) {
        super(message);
    }
}
