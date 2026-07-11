package com.cohabit.cohabitbackend.exception;

/**
 * Raised when a friend request cannot be found.
 */
public class FriendRequestNotFoundException extends RuntimeException {

    /**
     * Creates a friend-request-not-found exception.
     *
     * @param message safe API-facing error message
     */
    public FriendRequestNotFoundException(String message) {
        super(message);
    }
}
