package com.cohabit.cohabitbackend.exception;

/**
 * Raised when a friend request cannot be created or accepted because of workflow state.
 */
public class FriendRequestConflictException extends RuntimeException {

    /**
     * Creates a friend-request-conflict exception.
     *
     * @param message safe API-facing error message
     */
    public FriendRequestConflictException(String message) {
        super(message);
    }
}
