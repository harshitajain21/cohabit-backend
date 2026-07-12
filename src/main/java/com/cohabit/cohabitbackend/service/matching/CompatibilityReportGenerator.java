package com.cohabit.cohabitbackend.service.matching;

import com.cohabit.cohabitbackend.dto.CompatibilityReportResponse;
import org.springframework.stereotype.Component;

//Converts compatibility engine reports into API response reports.

@Component
public class CompatibilityReportGenerator {

    //Converts an internal report into an API response
    public CompatibilityReportResponse toResponse(CompatibilityReport report) {
        return new CompatibilityReportResponse(
                report.overallScore(),
                report.compatibilityBand(),
                report.matchedTraits(),
                report.conflicts(),
                report.dealBreakerConflicts(),
                report.reasoning()
        );
    }

    // Converts 1 string into multiple lined strings

    public String showproperly(Iterable<String> values) {
        if (values == null) {
            return "";
        }
        return String.join("\n", values);
    }
}
