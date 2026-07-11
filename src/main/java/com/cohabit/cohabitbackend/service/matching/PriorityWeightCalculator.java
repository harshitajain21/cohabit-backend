package com.cohabit.cohabitbackend.service.matching;

import com.cohabit.cohabitbackend.model.RoommatePreference;
import com.cohabit.cohabitbackend.model.enums.PreferenceCriterion;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;

/**
 * Calculates trait weights from a user's top priorities.
 */
@Component
public class PriorityWeightCalculator {

    private final MatchingScoringProperties scoringProperties;

    /**
     * Creates a priority weight calculator.
     *
     * @param scoringProperties centralized scoring properties
     */
    public PriorityWeightCalculator(MatchingScoringProperties scoringProperties) {
        this.scoringProperties = scoringProperties;
    }

    /**
     * Returns the score weight for a questionnaire criterion.
     *
     * @param criterion questionnaire criterion
     * @param sourcePreference preferences of the user requesting matches
     * @return weight for this criterion
     */
    public int weightFor(
            PreferenceCriterion criterion,
            RoommatePreference sourcePreference,
            RoommatePreference candidatePreference
    ) {
        boolean sourcePriority = topPriorities(sourcePreference).contains(criterion);
        boolean candidatePriority = topPriorities(candidatePreference).contains(criterion);

        if (sourcePriority && candidatePriority) {
            return scoringProperties.getMutualTopPriorityWeight();
        }
        if (sourcePriority) {
            return scoringProperties.getSourceTopPriorityWeight();
        }
        if (candidatePriority) {
            return scoringProperties.getCandidateTopPriorityWeight();
        }
        return scoringProperties.getDefaultWeight();
    }

    private Set<PreferenceCriterion> topPriorities(RoommatePreference preference) {
        if (preference.getTopPriorities() == null) {
            return Collections.emptySet();
        }
        return preference.getTopPriorities();
    }
}
