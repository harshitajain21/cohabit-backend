package com.cohabit.cohabitbackend.exception;

/**
 * Raised when roommate preferences are required but do not exist.
 */
public class RoommatePreferenceNotFoundException extends RuntimeException {

    /**
     * Creates a roommate-preference-not-found exception.
     *
     * @param message safe API-facing error message
     */
    public RoommatePreferenceNotFoundException(String message) {
        super(message);
    }
}
