package com.cohabit.cohabitbackend.dto;

import com.cohabit.cohabitbackend.model.enums.PreferenceCriterion;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

/**
 * Request payload for creating or updating roommate preferences.
 */
public record RoommatePreferenceRequest(

        @NotEmpty(message = "At least one top priority is required")
        Set<@NotNull(message = "Top priority cannot be null") PreferenceCriterion> topPriorities,

        @NotEmpty(message = "At least one deal breaker is required")
        Set<@NotNull(message = "Deal breaker cannot be null") PreferenceCriterion> dealBreakers,

        @NotNull(message = "Matching enabled value is required")
        Boolean matchingEnabled
) {
}
