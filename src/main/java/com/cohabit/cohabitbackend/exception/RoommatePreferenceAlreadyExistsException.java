package com.cohabit.cohabitbackend.exception;

/**
 * Raised when a user tries to create roommate preferences more than once.
 */
public class RoommatePreferenceAlreadyExistsException extends RuntimeException {

    /**
     * Creates a roommate-preference-already-exists exception.
     *
     * @param message safe API-facing error message
     */
    public RoommatePreferenceAlreadyExistsException(String message) {
        super(message);
    }
}
