package com.cohabit.cohabitbackend.service.matching;

import com.cohabit.cohabitbackend.model.QuestionnaireResponse;
import com.cohabit.cohabitbackend.model.enums.LightPreference;
import com.cohabit.cohabitbackend.model.enums.SleepSchedule;
import org.springframework.stereotype.Component;

@Component
public class SleepClusterCalculator {

    private static final double NO_PENALTY = 1.0;
    private static final double MID_PENALTY = 0.70;
    private static final double BIG_PENALTY = 0.40;

    public double calculate(QuestionnaireResponse source, QuestionnaireResponse candidate) {

        double score = NO_PENALTY;

        // Rule 1: early bird + needs darkness &  night owl - big penalty
        if (isEarlyDarkVsNightOwl(source, candidate)
                || isEarlyDarkVsNightOwl(candidate, source)) {

            score += BIG_PENALTY;
        }

        // Rule 2: medium + needs darkness & night owl - mid penalty
        else if (isMediumDarkVsNightOwl(source, candidate)
                || isMediumDarkVsNightOwl(candidate, source)) {

            score += MID_PENALTY;
        }

        // Rule 3: early bird+ dim light & night owl - mid penalty
        else if (isEarlyDimVsNightOwl(source, candidate)
                || isEarlyDimVsNightOwl(candidate, source)) {

            score += MID_PENALTY;
        }

        else {
            score += NO_PENALTY;
        }

        return score;
    }

    private boolean isEarlyDarkVsNightOwl(QuestionnaireResponse a,
                                          QuestionnaireResponse b) {

        return a.getSleepSchedule() == SleepSchedule.EARLY_SLEEPER
                && a.getSleepLightPreference() == LightPreference.NEEDS_DARKNESS
                && b.getSleepSchedule() == SleepSchedule.NIGHT_OWL;
    }

    private boolean isMediumDarkVsNightOwl(QuestionnaireResponse a,
                                           QuestionnaireResponse b) {

        return a.getSleepSchedule() == SleepSchedule.MODERATE_SLEEPER
                && a.getSleepLightPreference() == LightPreference.NEEDS_DARKNESS
                && b.getSleepSchedule() == SleepSchedule.NIGHT_OWL;
    }

    private boolean isEarlyDimVsNightOwl(QuestionnaireResponse a,
                                         QuestionnaireResponse b) {

        return a.getSleepSchedule() == SleepSchedule.EARLY_SLEEPER
                && a.getSleepLightPreference() == LightPreference.DIM_LIGHT_OK
                && b.getSleepSchedule() == SleepSchedule.NIGHT_OWL;
    }
}