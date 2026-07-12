package com.cohabit.cohabitbackend.service.matching;

import java.util.List;

//Internal compatibility report produced by the matching engine.

public record CompatibilityReport(
        int overallScore,
        String compatibilityBand,
        List<String> matchedTraits,
        List<String> conflicts,
        List<String> dealBreakerConflicts,
        List<String> reasoning
) {
}
