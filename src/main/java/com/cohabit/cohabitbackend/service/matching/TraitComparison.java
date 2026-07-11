package com.cohabit.cohabitbackend.service.matching;

import com.cohabit.cohabitbackend.model.enums.PreferenceCriterion;

/**
 * Represents one questionnaire trait comparison between two users.
 */
public record TraitComparison(
        PreferenceCriterion criterion,
        String label,
        Object sourceValue,
        Object candidateValue,
        double similarity
) {
}
