package com.cohabit.cohabitbackend.exception;

/**
 * Raised when a user tries to create more than one questionnaire response.
 */
public class QuestionnaireAlreadyExistsException extends RuntimeException {

    /**
     * Creates a questionnaire-already-exists exception.
     *
     * @param message safe API-facing error message
     */
    public QuestionnaireAlreadyExistsException(String message) {
        super(message);
    }
}
