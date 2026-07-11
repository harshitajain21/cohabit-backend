package com.cohabit.cohabitbackend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Request payload used to send a new account verification email.
 */
public record ResendVerificationRequest(

        @NotBlank(message = "IIT email is required")
        @Email(message = "Invalid email format")
        String iitEmail
) {
}
