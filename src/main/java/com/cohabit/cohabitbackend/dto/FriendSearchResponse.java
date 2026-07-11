package com.cohabit.cohabitbackend.dto;

/**
 * User search result returned before sending a friend request.
 */
public record FriendSearchResponse(
        Long id,
        String name,
        String iitEmail,
        String branch,
        Integer year
) {
}
