package com.cohabit.cohabitbackend.dto;

import com.cohabit.cohabitbackend.model.enums.PreferenceCriterion;

import java.util.Set;

/**
 * API response for a user's roommate preferences.
 */
public record RoommatePreferenceResponse(
        Long id,
        Long userId,
        Set<PreferenceCriterion> topPriorities,
        Set<PreferenceCriterion> dealBreakers,
        boolean matchingEnabled
) {
}
