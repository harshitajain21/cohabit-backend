package com.cohabit.cohabitbackend.service.matching;

import com.cohabit.cohabitbackend.model.QuestionnaireResponse;
import com.cohabit.cohabitbackend.model.RoommatePreference;
import com.cohabit.cohabitbackend.model.enums.*;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Cohabit Matching Algorithm v3: dedicated sleep/study cluster calculators,
 * configurable weights, and a symmetric priority boost.
 *
 * Symmetry guarantee: calculateCompatibility(a, aPrefs, b, bPrefs) and
 * calculateCompatibility(b, bPrefs, a, aPrefs) always produce the same
 * overallScore, band, strengths, concerns, and dealbreakers. This matters
 * because both roommates in a pair see their own copy of the report and
 * they must agree. Every helper below (trait similarity, cluster
 * calculators, dealbreaker union, priority boost) is written to be
 * order-independent in its two user arguments -- keep that property when
 * modifying this class.
 */
@Component
public class CompatibilityEngine {

    private static final List<PreferenceCriterion> SLEEP_CRITERIA = List.of(
            PreferenceCriterion.SLEEP_SCHEDULE, PreferenceCriterion.SLEEP_LIGHT_PREFERENCE,
            PreferenceCriterion.NEED_SILENCE, PreferenceCriterion.PHONE_VOLUME);

    private static final List<PreferenceCriterion> STUDY_CRITERIA = List.of(
            PreferenceCriterion.STUDY_LOCATION, PreferenceCriterion.NEED_SILENCE,
            PreferenceCriterion.PHONE_VOLUME);

    private final TraitSimilarityCalculator similarity;
    private final MatchingScoringProperties properties;
    private final SleepClusterCalculator sleepClusterCalculator;
    private final StudyClusterCalculator studyClusterCalculator;

    public CompatibilityEngine(TraitSimilarityCalculator similarity,
                               MatchingScoringProperties properties,
                               SleepClusterCalculator sleepClusterCalculator,
                               StudyClusterCalculator studyClusterCalculator) {
        this.similarity = similarity;
        this.properties = properties;
        this.sleepClusterCalculator = sleepClusterCalculator;
        this.studyClusterCalculator = studyClusterCalculator;
    }

    public Optional<CompatibilityReport> calculateCompatibility(QuestionnaireResponse a, RoommatePreference aPreferences,
                                                                QuestionnaireResponse b, RoommatePreference bPreferences) {
        List<TraitComparison> traits = comparisons(a, b);

        double sleepScore = sleepClusterCalculator.calculate(a, b);
        double studyScore = studyClusterCalculator.calculate(a, b);

        List<Component> components = List.of(
                new Component("Sleep environment", properties.getSleepClusterWeight(), sleepScore, SLEEP_CRITERIA),
                new Component("Study environment", properties.getStudyClusterWeight(), studyScore, STUDY_CRITERIA),
                new Component("Cleanliness", properties.getCleanlinessWeight(),
                        trait(traits, PreferenceCriterion.CLEANLINESS).similarity(), List.of(PreferenceCriterion.CLEANLINESS)),
                new Component("Substance use", properties.getSubstanceUseWeight(),
                        trait(traits, PreferenceCriterion.SUBSTANCE_USE).similarity(), List.of(PreferenceCriterion.SUBSTANCE_USE)),
                new Component("Guest preference", properties.getGuestPreferenceWeight(),
                        trait(traits, PreferenceCriterion.GUEST_PREFERENCE).similarity(), List.of(PreferenceCriterion.GUEST_PREFERENCE)),
                new Component("Personality", properties.getPersonalityWeight(),
                        trait(traits, PreferenceCriterion.PERSONALITY).similarity(), List.of(PreferenceCriterion.PERSONALITY)),
                new Component("Sharing preference", properties.getSharingPreferenceWeight(),
                        trait(traits, PreferenceCriterion.SHARING_PREFERENCE).similarity(), List.of(PreferenceCriterion.SHARING_PREFERENCE))
        );

        // Weighted quadratic mean: squaring each component's similarity before averaging
        // makes the score more sensitive to weak spots than a plain weighted average would
        // be -- one badly mismatched high-weight trait pulls the score down harder than an
        // equivalent linear penalty, which better reflects how roommate conflicts compound.
        double totalWeight = components.stream().mapToDouble(c -> boostedWeight(c, aPreferences, bPreferences)).sum();
        double base = components.stream()
                .mapToDouble(c -> boostedWeight(c, aPreferences, bPreferences) * c.score() * c.score())
                .sum() * 100.0 / totalWeight;

        List<TraitComparison> triggeredDealBreakers = triggeredDealBreakers(traits, aPreferences, bPreferences);
        List<String> dealBreakers = triggeredDealBreakers.stream().map(t -> t.label() + " is a dealbreaker conflict").toList();
        double penalty = triggeredDealBreakers.stream().mapToDouble(t -> properties.getDealBreakerPenalty() * (1 - t.similarity())).sum();

        int finalScore = (int) Math.round(Math.max(properties.getMinScore(), Math.min(properties.getMaxScore(), base - penalty)));

        List<String> strengths = new ArrayList<>();
        List<String> concerns = new ArrayList<>();
        addNarrative("sleep schedules", sleepScore, strengths, concerns);
        addNarrative("study environments", studyScore, strengths, concerns);
        for (Component component : components.subList(2, components.size())) {
            if (component.score() >= properties.getStrongMatchThreshold()) strengths.add("Similar " + component.label().toLowerCase(Locale.ROOT));
            else if (component.score() < properties.getConflictThreshold()) concerns.add("Different " + component.label().toLowerCase(Locale.ROOT));
        }

        List<String> reasoning = new ArrayList<>();
        reasoning.add("Base score: " + Math.round(base) + ". Dealbreaker penalties: " + Math.round(penalty) + ".");

        return Optional.of(new CompatibilityReport(finalScore, band(finalScore),
                strengths.stream().distinct().toList(), concerns.stream().distinct().toList(),
                dealBreakers, reasoning));
    }

    private List<TraitComparison> comparisons(QuestionnaireResponse a, QuestionnaireResponse b) {
        return QuestionnaireTrait.matchingTraits().stream()
                .map(t -> new TraitComparison(t.criterion(), t.label(), t.valueExtractor().apply(a), t.valueExtractor().apply(b),
                        similarity.similarity(t.criterion(), t.valueExtractor().apply(a), t.valueExtractor().apply(b))))
                .toList();
    }

    private TraitComparison trait(List<TraitComparison> traits, PreferenceCriterion criterion) {
        return traits.stream().filter(t -> t.criterion() == criterion).findFirst().orElseThrow();
    }

    // Boosts a component's weight when either (or both) users flagged one of its
    // criteria as a top priority. Symmetric: swapping (a, b) leaves aPriority + bPriority
    // unchanged, so this never favors whichever user happens to be passed first.
    private double boostedWeight(Component component, RoommatePreference a, RoommatePreference b) {
        double aPriority = component.criteria().stream().anyMatch(c -> priorities(a).contains(c)) ? 1 : 0;
        double bPriority = component.criteria().stream().anyMatch(c -> priorities(b).contains(c)) ? 1 : 0;
        return component.baseWeight() * (1 + properties.getPriorityBoostMultiplier() * ((aPriority + bPriority) / 2));
    }

    private Set<PreferenceCriterion> priorities(RoommatePreference preference) {
        return preference.getTopPriorities() == null ? Set.of() : preference.getTopPriorities();
    }

    private Set<PreferenceCriterion> dealBreakers(RoommatePreference preference) {
        return preference.getDealBreakers() == null ? Set.of() : preference.getDealBreakers();
    }

    // Union of both users' dealbreaker criteria -- symmetric regardless of argument order.
    private List<TraitComparison> triggeredDealBreakers(List<TraitComparison> traits, RoommatePreference a, RoommatePreference b) {
        Set<PreferenceCriterion> selected = new HashSet<>(dealBreakers(a));
        selected.addAll(dealBreakers(b));
        return traits.stream().filter(t -> selected.contains(t.criterion()) && t.similarity() < 1.0).toList();
    }

    private void addNarrative(String label, double score, List<String> strengths, List<String> concerns) {
        if (score >= properties.getStrongMatchThreshold()) strengths.add("Compatible " + label);
        else if (score < properties.getConflictThreshold()) concerns.add("Different " + label);
    }

    private String band(int score) {
        return score >= 85 ? "Excellent Match" : score >= 70 ? "Good Match" : score >= 50 ? "Moderate Match" : "Low Compatibility";
    }

    private record Component(String label, double baseWeight, double score, List<PreferenceCriterion> criteria) { }
}