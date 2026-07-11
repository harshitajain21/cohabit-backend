package com.cohabit.cohabitbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice         //This class handles exceptions thrown by all controllers
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)  //If you see this exception, call this method
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExists(EmailAlreadyExistsException e) {

        ErrorResponse error = new ErrorResponse("EMAIL_ALREADY_EXISTS", e.getMessage(),null);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationFailed(AuthenticationFailedException e) {

        ErrorResponse error = new ErrorResponse("AUTHENTICATION_FAILED", e.getMessage(), null);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(EmailNotVerifiedException.class)
    public ResponseEntity<ErrorResponse> handleEmailNotVerified(EmailNotVerifiedException e) {

        ErrorResponse error = new ErrorResponse("EMAIL_NOT_VERIFIED", e.getMessage(), null);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(InvalidVerificationTokenException.class)
    public ResponseEntity<ErrorResponse> handleInvalidVerificationToken(InvalidVerificationTokenException e) {

        ErrorResponse error = new ErrorResponse("INVALID_VERIFICATION_TOKEN", e.getMessage(), null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException e) {

        ErrorResponse error = new ErrorResponse("USER_NOT_FOUND", e.getMessage(), null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(QuestionnaireAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleQuestionnaireAlreadyExists(QuestionnaireAlreadyExistsException e) {

        ErrorResponse error = new ErrorResponse("QUESTIONNAIRE_ALREADY_EXISTS", e.getMessage(), null);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(QuestionnaireNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleQuestionnaireNotFound(QuestionnaireNotFoundException e) {

        ErrorResponse error = new ErrorResponse("QUESTIONNAIRE_NOT_FOUND", e.getMessage(), null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(RoommatePreferenceAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleRoommatePreferenceAlreadyExists(RoommatePreferenceAlreadyExistsException e) {

        ErrorResponse error = new ErrorResponse("ROOMMATE_PREFERENCE_ALREADY_EXISTS", e.getMessage(), null);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(RoommatePreferenceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRoommatePreferenceNotFound(RoommatePreferenceNotFoundException e) {

        ErrorResponse error = new ErrorResponse("ROOMMATE_PREFERENCE_NOT_FOUND", e.getMessage(), null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(FriendRequestNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleFriendRequestNotFound(FriendRequestNotFoundException e) {

        ErrorResponse error = new ErrorResponse("FRIEND_REQUEST_NOT_FOUND", e.getMessage(), null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(FriendRequestConflictException.class)
    public ResponseEntity<ErrorResponse> handleFriendRequestConflict(FriendRequestConflictException e) {

        ErrorResponse error = new ErrorResponse("FRIEND_REQUEST_CONFLICT", e.getMessage(), null);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(FriendRequestAccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleFriendRequestAccessDenied(FriendRequestAccessDeniedException e) {

        ErrorResponse error = new ErrorResponse("FRIEND_REQUEST_ACCESS_DENIED", e.getMessage(), null);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )

                );

        return ResponseEntity.badRequest()
                .body(

                        new ErrorResponse(

                                "VALIDATION_ERROR",

                                "Validation failed",

                                errors

                        )

                );
    }
}
