package com.cohabit.cohabitbackend.dto;

/**
 * API response for a ranked roommate match.
 */
public record RoommateMatchResponse(
        Long userId,
        String name,
        String iitEmail,
        String branch,
        Integer year,
        CompatibilityReportResponse compatibilityReport
) {
}
