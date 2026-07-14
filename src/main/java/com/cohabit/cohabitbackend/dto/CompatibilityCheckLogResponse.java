package com.cohabit.cohabitbackend.dto;

import java.time.Instant;

//API response for a single past compatibility check.

public record CompatibilityCheckLogResponse(
        Long id,
        Long recipientId,
        String recipientName,
        String recipientIitEmail,
        Integer overallScore,
        Instant checkedAt
) {
}