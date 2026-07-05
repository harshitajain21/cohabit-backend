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
