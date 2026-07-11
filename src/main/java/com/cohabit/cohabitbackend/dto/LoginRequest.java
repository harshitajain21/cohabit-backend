package com.cohabit.cohabitbackend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Request payload used to authenticate an existing user.
 */
public record LoginRequest(

        @NotBlank(message = "IIT email is required")
        @Email(message = "Invalid email format")
        String iitEmail,

        @NotBlank(message = "Password is required")
        String password
) {
}
