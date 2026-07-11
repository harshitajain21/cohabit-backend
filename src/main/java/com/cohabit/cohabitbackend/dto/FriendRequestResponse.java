package com.cohabit.cohabitbackend.dto;

import com.cohabit.cohabitbackend.model.enums.FriendRequestStatus;

import java.time.Instant;

/**
 * API response for friend request workflow actions.
 */
public record FriendRequestResponse(
        Long id,
        Long requesterId,
        String requesterName,
        String requesterIitEmail,
        Long recipientId,
        String recipientName,
        String recipientIitEmail,
        FriendRequestStatus status,
        Instant requestedAt,
        Instant respondedAt,
        CompatibilityReportResponse compatibilityReport
) {
}
