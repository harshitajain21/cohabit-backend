package com.cohabit.cohabitbackend.dto;

import com.cohabit.cohabitbackend.model.Gender;
import jakarta.validation.constraints.*;

public record RegisterUserRequest(

        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "IIT email is required")
        @Email(message = "Invalid email format")
        @Pattern(
                regexp = "^[A-Za-z0-9._%+-]+@itbhu\\.ac\\.in$",
                message = "Only IIT BHU email addresses are allowed"
        )
        String iitEmail,

        @NotBlank(message = "Branch is required")
        String branch,

        @NotNull(message = "Year is required")
        @Min(value = 1, message = "Year must be between 1 and 5")
        @Max(value = 5, message = "Year must be between 1 and 5")
        Integer year,  //Integer instead of int because @notnull only works on objects not on int

        @NotNull(message = "Gender is required")
        Gender gender,

        @NotBlank(message = "Phone number is required")
        @Pattern(
                regexp = "^[6-9]\\d{9}$", //first digit must be 6-9, exact 9 digits
                message = "Invalid phone number"
        )
        String phoneNumber

) {}