package com.cohabit.cohabitbackend.service.matching;

import com.cohabit.cohabitbackend.dto.CompatibilityReportResponse;
import org.springframework.stereotype.Component;

/**
 * Converts compatibility engine reports into API response reports.
 */
@Component
public class CompatibilityReportGenerator {

    /**
     * Converts an internal report into an API response.
     *
     * @param report internal compatibility report
     * @return API compatibility report
     */
    public CompatibilityReportResponse toResponse(CompatibilityReport report) {
        return new CompatibilityReportResponse(
                report.overallScore(),
                report.matchedTraits(),
                report.conflicts(),
                report.dealBreakerConflicts(),
                report.reasoning()
        );
    }

    /**
     * Joins report items for persistence in an accepted friend request.
     *
     * @param values report values
     * @return newline-delimited value
     */
    public String joinForStorage(Iterable<String> values) {
        if (values == null) {
            return "";
        }
        return String.join("\n", values);
    }
}
