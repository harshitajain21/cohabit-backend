package com.cohabit.cohabitbackend.dto;

import java.util.List;

/**
 * Compatibility summary generated after a friend request is accepted.
 */
public record CompatibilityReportResponse(
        Integer overallScore,
        List<String> matchingTraits,
        List<String> potentialConflicts,
        List<String> dealBreakerConflicts,
        List<String> reasoning
) {
}
