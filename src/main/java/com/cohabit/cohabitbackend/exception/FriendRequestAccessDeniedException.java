package com.cohabit.cohabitbackend.exception;

/**
 * Raised when a user attempts to act on a friend request they do not own.
 */
public class FriendRequestAccessDeniedException extends RuntimeException {

    /**
     * Creates a friend-request-access-denied exception.
     *
     * @param message safe API-facing error message
     */
    public FriendRequestAccessDeniedException(String message) {
        super(message);
    }
}
