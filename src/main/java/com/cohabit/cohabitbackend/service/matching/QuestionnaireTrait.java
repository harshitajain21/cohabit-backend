package com.cohabit.cohabitbackend.service.matching;

import com.cohabit.cohabitbackend.model.QuestionnaireResponse;
import com.cohabit.cohabitbackend.model.enums.PreferenceCriterion;

import java.util.List;
import java.util.function.Function;

/**
 * Defines the questionnaire traits consumed by the matching engine.
 */
public record QuestionnaireTrait(
        PreferenceCriterion criterion,
        String label,
        Function<QuestionnaireResponse, Object> valueExtractor
) {

    /**
     * Returns the ordered trait definitions used for scoring.
     *
     * @return questionnaire trait definitions
     */
    public static List<QuestionnaireTrait> matchingTraits() {
        return List.of(
                new QuestionnaireTrait(PreferenceCriterion.SLEEP_SCHEDULE, "Sleep schedule", QuestionnaireResponse::getSleepSchedule),
                new QuestionnaireTrait(PreferenceCriterion.SLEEP_LIGHT_PREFERENCE, "Sleep light preference", QuestionnaireResponse::getSleepLightPreference),
                new QuestionnaireTrait(PreferenceCriterion.PHONE_VOLUME, "Phone volume", QuestionnaireResponse::getPhoneVolume),
                new QuestionnaireTrait(PreferenceCriterion.CLEANLINESS, "Cleanliness", QuestionnaireResponse::getCleanliness),
                new QuestionnaireTrait(PreferenceCriterion.STUDY_LOCATION, "Study location", QuestionnaireResponse::getStudyLocation),
                new QuestionnaireTrait(PreferenceCriterion.NEED_SILENCE, "Need silence", QuestionnaireResponse::isNeedSilence),
                new QuestionnaireTrait(PreferenceCriterion.GUEST_PREFERENCE, "Guest preference", QuestionnaireResponse::getGuestPreference),
                new QuestionnaireTrait(PreferenceCriterion.PERSONALITY, "Personality", QuestionnaireResponse::getPersonality),
                new QuestionnaireTrait(PreferenceCriterion.SHARING_PREFERENCE, "Sharing preference", QuestionnaireResponse::getSharingPreference),
                new QuestionnaireTrait(PreferenceCriterion.LANGUAGE_IMPORTANCE, "Language importance", QuestionnaireResponse::getLanguageImportance),
                new QuestionnaireTrait(PreferenceCriterion.SUBSTANCE_USE, "Substance use", QuestionnaireResponse::getSubstanceUse)
        );
    }
}
