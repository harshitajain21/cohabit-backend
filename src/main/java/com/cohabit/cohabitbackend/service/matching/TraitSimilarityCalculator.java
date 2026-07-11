package com.cohabit.cohabitbackend.service.matching;

import com.cohabit.cohabitbackend.model.enums.Cleanliness;
import com.cohabit.cohabitbackend.model.enums.GuestPreference;
import com.cohabit.cohabitbackend.model.enums.LanguageImportance;
import com.cohabit.cohabitbackend.model.enums.LightPreference;
import com.cohabit.cohabitbackend.model.enums.Personality;
import com.cohabit.cohabitbackend.model.enums.PhoneVolume;
import com.cohabit.cohabitbackend.model.enums.PreferenceCriterion;
import com.cohabit.cohabitbackend.model.enums.SharingPreference;
import com.cohabit.cohabitbackend.model.enums.SleepSchedule;
import com.cohabit.cohabitbackend.model.enums.StudyLocation;
import com.cohabit.cohabitbackend.model.enums.SubstanceUse;
import org.springframework.stereotype.Component;

/**
 * Calculates normalized trait-level compatibility before weighting and penalties.
 */
@Component
public class TraitSimilarityCalculator {

    private final MatchingScoringProperties scoringProperties;

    /**
     * Creates a trait similarity calculator.
     *
     * @param scoringProperties centralized scoring properties
     */
    public TraitSimilarityCalculator(MatchingScoringProperties scoringProperties) {
        this.scoringProperties = scoringProperties;
    }

    /**
     * Calculates a similarity value from 0.0 to 1.0 for a questionnaire criterion.
     *
     * @param criterion questionnaire criterion
     * @param sourceValue source user's answer
     * @param candidateValue candidate user's answer
     * @return normalized similarity
     */
    public double similarity(PreferenceCriterion criterion, Object sourceValue, Object candidateValue) {
        if (sourceValue == null || candidateValue == null) {
            return 0.0;
        }
        if (sourceValue.equals(candidateValue)) {
            return 1.0;
        }

        return switch (criterion) {
            case SLEEP_SCHEDULE -> ordinalSimilarity((SleepSchedule) sourceValue, (SleepSchedule) candidateValue, 2);
            case SLEEP_LIGHT_PREFERENCE -> lightSimilarity((LightPreference) sourceValue, (LightPreference) candidateValue);
            case PHONE_VOLUME -> ordinalSimilarity((PhoneVolume) sourceValue, (PhoneVolume) candidateValue, 2);
            case CLEANLINESS -> ordinalSimilarity((Cleanliness) sourceValue, (Cleanliness) candidateValue, 3);
            case STUDY_LOCATION -> studyLocationSimilarity((StudyLocation) sourceValue, (StudyLocation) candidateValue);
            case NEED_SILENCE -> scoringProperties.getBooleanMismatchSimilarity();
            case GUEST_PREFERENCE -> ordinalSimilarity((GuestPreference) sourceValue, (GuestPreference) candidateValue, 2);
            case PERSONALITY -> ordinalSimilarity((Personality) sourceValue, (Personality) candidateValue, 2);
            case SHARING_PREFERENCE -> ordinalSimilarity((SharingPreference) sourceValue, (SharingPreference) candidateValue, 2);
            case LANGUAGE_IMPORTANCE -> ordinalSimilarity((LanguageImportance) sourceValue, (LanguageImportance) candidateValue, 2);
            case SUBSTANCE_USE -> ordinalSimilarity((SubstanceUse) sourceValue, (SubstanceUse) candidateValue, 3);
        };
    }

    private double ordinalSimilarity(Enum<?> sourceValue, Enum<?> candidateValue, int maxDistance) {
        int distance = Math.abs(sourceValue.ordinal() - candidateValue.ordinal());
        return Math.max(0.0, 1.0 - (distance / (double) maxDistance));
    }

    private double lightSimilarity(LightPreference sourceValue, LightPreference candidateValue) {
        if (sourceValue == LightPreference.DIM_LIGHT_OK || candidateValue == LightPreference.DIM_LIGHT_OK) {
            return scoringProperties.getDimLightBridgeSimilarity();
        }
        return scoringProperties.getOppositeLightSimilarity();
    }

    private double studyLocationSimilarity(StudyLocation sourceValue, StudyLocation candidateValue) {
        if (sourceValue == StudyLocation.READING_ROOM || candidateValue == StudyLocation.READING_ROOM) {
            return scoringProperties.getReadingRoomBridgeSimilarity();
        }
        return scoringProperties.getDifferentStudyLocationSimilarity();
    }
}
