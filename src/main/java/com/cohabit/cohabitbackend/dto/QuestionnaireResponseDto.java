package com.cohabit.cohabitbackend.dto;

import com.cohabit.cohabitbackend.model.enums.Cleanliness;
import com.cohabit.cohabitbackend.model.enums.GuestPreference;
import com.cohabit.cohabitbackend.model.enums.LanguageImportance;
import com.cohabit.cohabitbackend.model.enums.LightPreference;
import com.cohabit.cohabitbackend.model.enums.Personality;
import com.cohabit.cohabitbackend.model.enums.PhoneVolume;
import com.cohabit.cohabitbackend.model.enums.SharingPreference;
import com.cohabit.cohabitbackend.model.enums.SleepSchedule;
import com.cohabit.cohabitbackend.model.enums.StudyLocation;
import com.cohabit.cohabitbackend.model.enums.SubstanceUse;

/**
 * API response for a user's questionnaire response.
 */
public record QuestionnaireResponseDto(
        Long id,
        Long userId,
        SleepSchedule sleepSchedule,
        LightPreference sleepLightPreference,
        PhoneVolume phoneVolume,
        Cleanliness cleanliness,
        StudyLocation studyLocation,
        boolean needSilence,
        GuestPreference guestPreference,
        Personality personality,
        SharingPreference sharingPreference,
        LanguageImportance languageImportance,
        SubstanceUse substanceUse
) {
}
