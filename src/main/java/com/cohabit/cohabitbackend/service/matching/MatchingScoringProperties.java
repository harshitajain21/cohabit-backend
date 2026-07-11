package com.cohabit.cohabitbackend.service.matching;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Centralizes scoring thresholds, weights, and penalties for roommate matching.
 */
@Component
@ConfigurationProperties(prefix = "cohabit.matching.scoring")
public class MatchingScoringProperties {

    private int minScore = 0;
    private int maxScore = 100;
    private int maxMatches = 10;
    private double strongMatchThreshold = 0.85;
    private double conflictThreshold = 0.65;
    private int sourceTopPriorityWeight = 6;
    private int mutualTopPriorityWeight = 8;
    private int candidateTopPriorityWeight = 4;
    private int defaultWeight = 2;
    private double booleanMismatchSimilarity = 0.25;
    private double dimLightBridgeSimilarity = 0.65;
    private double oppositeLightSimilarity = 0.35;
    private double readingRoomBridgeSimilarity = 0.75;
    private double differentStudyLocationSimilarity = 0.45;
    private int nightOwlEarlySleeperPenalty = 14;
    private int darknessFlexibleLightPenalty = 8;
    private int frequentSpeakerSilencePenalty = 12;
    private int occasionalSpeakerSilencePenalty = 5;
    private int oppositeGuestPreferencePenalty = 10;
    private int extremeCleanlinessPenalty = 12;
    private int moderateCleanlinessPenalty = 6;
    private int noSubstanceUsePenalty = 15;
    private int substanceUseGapPenalty = 8;
    private int darkEarlySleeperNightOwlPenalty = 12;
    private int roomStudySilenceSpeakerPenalty = 14;
    private int introvertGuestPenalty = 8;
    private int extrovertNoGuestPenalty = 7;
    private int roomStudyGuestPenalty = 6;
    private int cleanRelaxedSharingPenalty = 4;
    private int introvertLanguagePenalty = 5;
    private int socialSubstanceUsePenalty = 8;

    public int getMinScore() {
        return minScore;
    }

    public void setMinScore(int minScore) {
        this.minScore = minScore;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

    public int getMaxMatches() {
        return maxMatches;
    }

    public void setMaxMatches(int maxMatches) {
        this.maxMatches = maxMatches;
    }

    public double getStrongMatchThreshold() {
        return strongMatchThreshold;
    }

    public void setStrongMatchThreshold(double strongMatchThreshold) {
        this.strongMatchThreshold = strongMatchThreshold;
    }

    public double getConflictThreshold() {
        return conflictThreshold;
    }

    public void setConflictThreshold(double conflictThreshold) {
        this.conflictThreshold = conflictThreshold;
    }

    public int getSourceTopPriorityWeight() {
        return sourceTopPriorityWeight;
    }

    public void setSourceTopPriorityWeight(int sourceTopPriorityWeight) {
        this.sourceTopPriorityWeight = sourceTopPriorityWeight;
    }

    public int getMutualTopPriorityWeight() {
        return mutualTopPriorityWeight;
    }

    public void setMutualTopPriorityWeight(int mutualTopPriorityWeight) {
        this.mutualTopPriorityWeight = mutualTopPriorityWeight;
    }

    public int getCandidateTopPriorityWeight() {
        return candidateTopPriorityWeight;
    }

    public void setCandidateTopPriorityWeight(int candidateTopPriorityWeight) {
        this.candidateTopPriorityWeight = candidateTopPriorityWeight;
    }

    public int getDefaultWeight() {
        return defaultWeight;
    }

    public void setDefaultWeight(int defaultWeight) {
        this.defaultWeight = defaultWeight;
    }

    public double getBooleanMismatchSimilarity() {
        return booleanMismatchSimilarity;
    }

    public void setBooleanMismatchSimilarity(double booleanMismatchSimilarity) {
        this.booleanMismatchSimilarity = booleanMismatchSimilarity;
    }

    public double getDimLightBridgeSimilarity() {
        return dimLightBridgeSimilarity;
    }

    public void setDimLightBridgeSimilarity(double dimLightBridgeSimilarity) {
        this.dimLightBridgeSimilarity = dimLightBridgeSimilarity;
    }

    public double getOppositeLightSimilarity() {
        return oppositeLightSimilarity;
    }

    public void setOppositeLightSimilarity(double oppositeLightSimilarity) {
        this.oppositeLightSimilarity = oppositeLightSimilarity;
    }

    public double getReadingRoomBridgeSimilarity() {
        return readingRoomBridgeSimilarity;
    }

    public void setReadingRoomBridgeSimilarity(double readingRoomBridgeSimilarity) {
        this.readingRoomBridgeSimilarity = readingRoomBridgeSimilarity;
    }

    public double getDifferentStudyLocationSimilarity() {
        return differentStudyLocationSimilarity;
    }

    public void setDifferentStudyLocationSimilarity(double differentStudyLocationSimilarity) {
        this.differentStudyLocationSimilarity = differentStudyLocationSimilarity;
    }

    public int getNightOwlEarlySleeperPenalty() {
        return nightOwlEarlySleeperPenalty;
    }

    public void setNightOwlEarlySleeperPenalty(int nightOwlEarlySleeperPenalty) {
        this.nightOwlEarlySleeperPenalty = nightOwlEarlySleeperPenalty;
    }

    public int getDarknessFlexibleLightPenalty() {
        return darknessFlexibleLightPenalty;
    }

    public void setDarknessFlexibleLightPenalty(int darknessFlexibleLightPenalty) {
        this.darknessFlexibleLightPenalty = darknessFlexibleLightPenalty;
    }

    public int getFrequentSpeakerSilencePenalty() {
        return frequentSpeakerSilencePenalty;
    }

    public void setFrequentSpeakerSilencePenalty(int frequentSpeakerSilencePenalty) {
        this.frequentSpeakerSilencePenalty = frequentSpeakerSilencePenalty;
    }

    public int getOccasionalSpeakerSilencePenalty() {
        return occasionalSpeakerSilencePenalty;
    }

    public void setOccasionalSpeakerSilencePenalty(int occasionalSpeakerSilencePenalty) {
        this.occasionalSpeakerSilencePenalty = occasionalSpeakerSilencePenalty;
    }

    public int getOppositeGuestPreferencePenalty() {
        return oppositeGuestPreferencePenalty;
    }

    public void setOppositeGuestPreferencePenalty(int oppositeGuestPreferencePenalty) {
        this.oppositeGuestPreferencePenalty = oppositeGuestPreferencePenalty;
    }

    public int getExtremeCleanlinessPenalty() {
        return extremeCleanlinessPenalty;
    }

    public void setExtremeCleanlinessPenalty(int extremeCleanlinessPenalty) {
        this.extremeCleanlinessPenalty = extremeCleanlinessPenalty;
    }

    public int getModerateCleanlinessPenalty() {
        return moderateCleanlinessPenalty;
    }

    public void setModerateCleanlinessPenalty(int moderateCleanlinessPenalty) {
        this.moderateCleanlinessPenalty = moderateCleanlinessPenalty;
    }

    public int getNoSubstanceUsePenalty() {
        return noSubstanceUsePenalty;
    }

    public void setNoSubstanceUsePenalty(int noSubstanceUsePenalty) {
        this.noSubstanceUsePenalty = noSubstanceUsePenalty;
    }

    public int getSubstanceUseGapPenalty() {
        return substanceUseGapPenalty;
    }

    public void setSubstanceUseGapPenalty(int substanceUseGapPenalty) {
        this.substanceUseGapPenalty = substanceUseGapPenalty;
    }

    public int getDarkEarlySleeperNightOwlPenalty() {
        return darkEarlySleeperNightOwlPenalty;
    }

    public void setDarkEarlySleeperNightOwlPenalty(int darkEarlySleeperNightOwlPenalty) {
        this.darkEarlySleeperNightOwlPenalty = darkEarlySleeperNightOwlPenalty;
    }

    public int getRoomStudySilenceSpeakerPenalty() {
        return roomStudySilenceSpeakerPenalty;
    }

    public void setRoomStudySilenceSpeakerPenalty(int roomStudySilenceSpeakerPenalty) {
        this.roomStudySilenceSpeakerPenalty = roomStudySilenceSpeakerPenalty;
    }

    public int getIntrovertGuestPenalty() {
        return introvertGuestPenalty;
    }

    public void setIntrovertGuestPenalty(int introvertGuestPenalty) {
        this.introvertGuestPenalty = introvertGuestPenalty;
    }

    public int getExtrovertNoGuestPenalty() {
        return extrovertNoGuestPenalty;
    }

    public void setExtrovertNoGuestPenalty(int extrovertNoGuestPenalty) {
        this.extrovertNoGuestPenalty = extrovertNoGuestPenalty;
    }

    public int getRoomStudyGuestPenalty() {
        return roomStudyGuestPenalty;
    }

    public void setRoomStudyGuestPenalty(int roomStudyGuestPenalty) {
        this.roomStudyGuestPenalty = roomStudyGuestPenalty;
    }

    public int getCleanRelaxedSharingPenalty() {
        return cleanRelaxedSharingPenalty;
    }

    public void setCleanRelaxedSharingPenalty(int cleanRelaxedSharingPenalty) {
        this.cleanRelaxedSharingPenalty = cleanRelaxedSharingPenalty;
    }

    public int getIntrovertLanguagePenalty() {
        return introvertLanguagePenalty;
    }

    public void setIntrovertLanguagePenalty(int introvertLanguagePenalty) {
        this.introvertLanguagePenalty = introvertLanguagePenalty;
    }

    public int getSocialSubstanceUsePenalty() {
        return socialSubstanceUsePenalty;
    }

    public void setSocialSubstanceUsePenalty(int socialSubstanceUsePenalty) {
        this.socialSubstanceUsePenalty = socialSubstanceUsePenalty;
    }
}
