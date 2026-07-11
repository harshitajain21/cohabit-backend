package com.cohabit.cohabitbackend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Request payload for sending a friend request.
 */
public record SendFriendRequestRequest(

        @NotBlank(message = "Recipient IIT email is required")
        @Email(message = "Invalid email format")
        String recipientIitEmail
) {
}
