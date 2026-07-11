package com.cohabit.cohabitbackend.service.matching;

import com.cohabit.cohabitbackend.model.QuestionnaireResponse;
import com.cohabit.cohabitbackend.model.RoommatePreference;
import com.cohabit.cohabitbackend.model.enums.PreferenceCriterion;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

/**
 * Calculates compatibility between two users' questionnaire and preference data.
 */
@Component
public class CompatibilityEngine {

    private final PriorityWeightCalculator priorityWeightCalculator;
    private final PenaltyCalculator penaltyCalculator;
    private final TraitSimilarityCalculator traitSimilarityCalculator;
    private final MatchingScoringProperties scoringProperties;

    /**
     * Creates a compatibility engine.
     *
     * @param priorityWeightCalculator priority weight calculator
     * @param penaltyCalculator cross-feature penalty calculator
     * @param traitSimilarityCalculator trait similarity calculator
     * @param scoringProperties centralized scoring properties
     */
    public CompatibilityEngine(
            PriorityWeightCalculator priorityWeightCalculator,
            PenaltyCalculator penaltyCalculator,
            TraitSimilarityCalculator traitSimilarityCalculator,
            MatchingScoringProperties scoringProperties
    ) {
        this.priorityWeightCalculator = priorityWeightCalculator;
        this.penaltyCalculator = penaltyCalculator;
        this.traitSimilarityCalculator = traitSimilarityCalculator;
        this.scoringProperties = scoringProperties;
    }

    /**
     * Calculates a compatibility report when deal breakers allow the candidate.
     *
     * @param sourceQuestionnaire questionnaire of the user requesting matches
     * @param sourcePreference preferences of the user requesting matches
     * @param candidateQuestionnaire candidate questionnaire
     * @param candidatePreference candidate preferences
     * @return compatibility report, or empty when hard-filtered by deal breakers
     */
    public Optional<CompatibilityReport> calculateCompatibility(
            QuestionnaireResponse sourceQuestionnaire,
            RoommatePreference sourcePreference,
            QuestionnaireResponse candidateQuestionnaire,
            RoommatePreference candidatePreference
    ) {
        List<TraitComparison> comparisons = buildComparisons(sourceQuestionnaire, candidateQuestionnaire);
        List<String> dealBreakerConflicts = findDealBreakerConflicts(
                comparisons,
                sourcePreference,
                candidatePreference
        );
        if (!dealBreakerConflicts.isEmpty()) {
            return Optional.empty();
        }

        double earnedWeight = 0;
        int possibleWeight = 0;
        List<String> matchedTraits = new ArrayList<>();
        List<String> conflicts = new ArrayList<>();
        List<String> reasoning = new ArrayList<>();

        for (TraitComparison comparison : comparisons) {
            int weight = priorityWeightCalculator.weightFor(comparison.criterion(), sourcePreference, candidatePreference);
            possibleWeight += weight;

            double weightedContribution = weight * comparison.similarity();
            earnedWeight += weightedContribution;

            if (isStrongMatch(comparison)) {
                matchedTraits.add(comparison.label());
                reasoning.add(comparison.label() + " strongly matched and contributed "
                        + formatContribution(weightedContribution, weight) + " weighted points.");
            } else if (isConflict(comparison)) {
                conflicts.add(comparison.label());
                reasoning.add(comparison.label() + " is a conflict and contributed "
                        + formatContribution(weightedContribution, weight) + " weighted points.");
            } else {
                reasoning.add(comparison.label() + " is a partial match and contributed "
                        + formatContribution(weightedContribution, weight) + " weighted points.");
            }
        }

        int baseScore = possibleWeight == 0
                ? scoringProperties.getMinScore()
                : Math.round((float) ((earnedWeight * 100.0) / possibleWeight));
        PenaltyCalculator.PenaltyResult penaltyResult = penaltyCalculator.calculatePenalties(
                sourceQuestionnaire,
                candidateQuestionnaire
        );
        conflicts.addAll(penaltyResult.conflicts());
        reasoning.addAll(penaltyResult.reasoning());

        int finalScore = clamp(baseScore - penaltyResult.penalty());
        reasoning.add("Base weighted score was " + baseScore + ". Cross-feature penalties reduced it by "
                + penaltyResult.penalty() + " points.");

        return Optional.of(new CompatibilityReport(
                finalScore,
                matchedTraits,
                conflicts.stream().distinct().toList(),
                dealBreakerConflicts,
                reasoning.stream().distinct().toList()
        ));
    }

    private List<TraitComparison> buildComparisons(
            QuestionnaireResponse sourceQuestionnaire,
            QuestionnaireResponse candidateQuestionnaire
    ) {
        return QuestionnaireTrait.matchingTraits().stream()
                .map(trait -> compare(
                        trait.criterion(),
                        trait.label(),
                        trait.valueExtractor().apply(sourceQuestionnaire),
                        trait.valueExtractor().apply(candidateQuestionnaire)
                ))
                .toList();
    }

    private TraitComparison compare(PreferenceCriterion criterion, String label, Object sourceValue, Object candidateValue) {
        return new TraitComparison(
                criterion,
                label,
                sourceValue,
                candidateValue,
                traitSimilarityCalculator.similarity(criterion, sourceValue, candidateValue)
        );
    }

    private List<String> findDealBreakerConflicts(
            List<TraitComparison> comparisons,
            RoommatePreference sourcePreference,
            RoommatePreference candidatePreference
    ) {
        return comparisons.stream()
                .filter(this::isConflict)
                .filter(comparison -> dealBreakers(sourcePreference).contains(comparison.criterion())
                        || dealBreakers(candidatePreference).contains(comparison.criterion()))
                .map(comparison -> comparison.label() + " conflicts with a deal breaker.")
                .toList();
    }

    private boolean isStrongMatch(TraitComparison comparison) {
        return comparison.similarity() >= scoringProperties.getStrongMatchThreshold();
    }

    private boolean isConflict(TraitComparison comparison) {
        return comparison.similarity() < scoringProperties.getConflictThreshold();
    }

    private Set<PreferenceCriterion> dealBreakers(RoommatePreference preference) {
        if (preference.getDealBreakers() == null) {
            return Collections.emptySet();
        }
        return preference.getDealBreakers();
    }

    private String formatContribution(double earnedWeight, int possibleWeight) {
        return String.format(Locale.ROOT, "%.1f/%d", earnedWeight, possibleWeight);
    }

    private int clamp(int score) {
        return Math.max(scoringProperties.getMinScore(), Math.min(scoringProperties.getMaxScore(), score));
    }
}
