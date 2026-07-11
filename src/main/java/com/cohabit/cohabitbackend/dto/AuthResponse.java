package com.cohabit.cohabitbackend.dto;

/**
 * Response returned after a successful login.
 */
public record AuthResponse(
        String token,
        String tokenType,
        UserResponse user
) {
}
