package com.cohabit.cohabitbackend.exception;

/**
 * Raised when a questionnaire response is required but none exists.
 */
public class QuestionnaireNotFoundException extends RuntimeException {

    /**
     * Creates a questionnaire-not-found exception.
     *
     * @param message safe API-facing error message
     */
    public QuestionnaireNotFoundException(String message) {
        super(message);
    }
}
