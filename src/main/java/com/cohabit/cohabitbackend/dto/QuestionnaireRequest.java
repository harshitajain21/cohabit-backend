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
import jakarta.validation.constraints.NotNull;

/**
 * Request payload for creating or updating a questionnaire response.
 */
public record QuestionnaireRequest(

        @NotNull(message = "Sleep schedule is required")
        SleepSchedule sleepSchedule,

        @NotNull(message = "Sleep light preference is required")
        LightPreference sleepLightPreference,

        @NotNull(message = "Phone volume is required")
        PhoneVolume phoneVolume,

        @NotNull(message = "Cleanliness is required")
        Cleanliness cleanliness,

        @NotNull(message = "Study location is required")
        StudyLocation studyLocation,

        @NotNull(message = "Need silence preference is required")
        Boolean needSilence,

        @NotNull(message = "Guest preference is required")
        GuestPreference guestPreference,

        @NotNull(message = "Personality is required")
        Personality personality,

        @NotNull(message = "Sharing preference is required")
        SharingPreference sharingPreference,

        @NotNull(message = "Language importance is required")
        LanguageImportance languageImportance,

        @NotNull(message = "Substance use is required")
        SubstanceUse substanceUse
) {
}
