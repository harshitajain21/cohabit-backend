package com.cohabit.cohabitbackend.service.matching;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Centralizes scoring thresholds, weights, and penalties for roommate matching.
 *
 * All component weights are proportional, not absolute -- the engine normalizes by
 * their sum, so they don't strictly need to total 100. They're kept summing to 100
 * here purely so the numbers are easy to reason about at a glance.
 */
@Component
@ConfigurationProperties(prefix = "cohabit.matching.scoring")
public class MatchingScoringProperties {

    // Score bounds
    private int minScore = 0;
    private int maxScore = 100;
    private int maxMatches = 10;

    // Report thresholds
    private double strongMatchThreshold = 0.85;
    private double conflictThreshold = 0.65;

    // Base component weights (proportional; sums to 100 for readability)
    private double sleepClusterWeight = 25.0;
    private double studyClusterWeight = 20.0;
    private double cleanlinessWeight = 20.0;
    private double substanceUseWeight = 15.0;
    private double guestPreferenceWeight = 8.0;
    private double personalityWeight = 7.0;
    private double sharingPreferenceWeight = 5.0;

    // Dealbreaker penalty multiplier (points deducted per severity unit of a triggered dealbreaker)
    private double dealBreakerPenalty = 15.0;

    // How strongly a shared "top priority" criterion boosts a component's weight.
    // boostedWeight = baseWeight * (1 + priorityBoostMultiplier * (matchedSides / 2))
    // where matchedSides is 0, 1, or 2 depending on how many of the two users flagged
    // this criterion as a top priority. Symmetric in both users by construction.
    private double priorityBoostMultiplier = 1.0;

    // Shared penalty magnitudes used by SleepClusterCalculator and StudyClusterCalculator
    private double majorClusterPenalty = 0.35;
    private double mediumClusterPenalty = 0.20;
    private double smallClusterPenalty = 0.10;

    public int getMinScore() { return minScore; }
    public void setMinScore(int minScore) { this.minScore = minScore; }

    public int getMaxScore() { return maxScore; }
    public void setMaxScore(int maxScore) { this.maxScore = maxScore; }

    public int getMaxMatches() { return maxMatches; }
    public void setMaxMatches(int maxMatches) { this.maxMatches = maxMatches; }

    public double getStrongMatchThreshold() { return strongMatchThreshold; }
    public void setStrongMatchThreshold(double strongMatchThreshold) { this.strongMatchThreshold = strongMatchThreshold; }

    public double getConflictThreshold() { return conflictThreshold; }
    public void setConflictThreshold(double conflictThreshold) { this.conflictThreshold = conflictThreshold; }

    public double getSleepClusterWeight() { return sleepClusterWeight; }
    public void setSleepClusterWeight(double sleepClusterWeight) { this.sleepClusterWeight = sleepClusterWeight; }

    public double getStudyClusterWeight() { return studyClusterWeight; }
    public void setStudyClusterWeight(double studyClusterWeight) { this.studyClusterWeight = studyClusterWeight; }

    public double getCleanlinessWeight() { return cleanlinessWeight; }
    public void setCleanlinessWeight(double cleanlinessWeight) { this.cleanlinessWeight = cleanlinessWeight; }

    public double getSubstanceUseWeight() { return substanceUseWeight; }
    public void setSubstanceUseWeight(double substanceUseWeight) { this.substanceUseWeight = substanceUseWeight; }

    public double getGuestPreferenceWeight() { return guestPreferenceWeight; }
    public void setGuestPreferenceWeight(double guestPreferenceWeight) { this.guestPreferenceWeight = guestPreferenceWeight; }

    public double getPersonalityWeight() { return personalityWeight; }
    public void setPersonalityWeight(double personalityWeight) { this.personalityWeight = personalityWeight; }

    public double getSharingPreferenceWeight() { return sharingPreferenceWeight; }
    public void setSharingPreferenceWeight(double sharingPreferenceWeight) { this.sharingPreferenceWeight = sharingPreferenceWeight; }

    public double getDealBreakerPenalty() { return dealBreakerPenalty; }
    public void setDealBreakerPenalty(double dealBreakerPenalty) { this.dealBreakerPenalty = dealBreakerPenalty; }

    public double getPriorityBoostMultiplier() { return priorityBoostMultiplier; }
    public void setPriorityBoostMultiplier(double priorityBoostMultiplier) { this.priorityBoostMultiplier = priorityBoostMultiplier; }

    public double getMajorClusterPenalty() { return majorClusterPenalty; }
    public void setMajorClusterPenalty(double majorClusterPenalty) { this.majorClusterPenalty = majorClusterPenalty; }

    public double getMediumClusterPenalty() { return mediumClusterPenalty; }
    public void setMediumClusterPenalty(double mediumClusterPenalty) { this.mediumClusterPenalty = mediumClusterPenalty; }

    public double getSmallClusterPenalty() { return smallClusterPenalty; }
    public void setSmallClusterPenalty(double smallClusterPenalty) { this.smallClusterPenalty = smallClusterPenalty; }
}