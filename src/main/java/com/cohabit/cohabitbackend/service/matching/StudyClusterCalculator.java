package com.cohabit.cohabitbackend.service.matching;

import com.cohabit.cohabitbackend.model.QuestionnaireResponse;
import com.cohabit.cohabitbackend.model.enums.PhoneVolume;
import com.cohabit.cohabitbackend.model.enums.StudyLocation;
import org.springframework.stereotype.Component;

@Component
public class StudyClusterCalculator {

    private static final double BIG_PENALTY = 0.60;
    private static final double MID_PENALTY = 0.30;

    public double calculate(QuestionnaireResponse source,QuestionnaireResponse candidate) {

        double score = 1.0;

        // Source studies in room, candidate plays speaker
        if (source.getStudyLocation() == StudyLocation.ROOM
                && source.isNeedSilence()
                && candidate.getPhoneVolume() == PhoneVolume.OFTEN_USE_SPEAKER) {

            score -= BIG_PENALTY;
        }

        else if (source.getStudyLocation() == StudyLocation.ROOM
                && source.isNeedSilence()
                && candidate.getPhoneVolume() == PhoneVolume.SOMETIMES_USE_SPEAKER) {

            score -= MID_PENALTY;
        }

        // Candidate studies in room, source plays speaker
        if (candidate.getStudyLocation() == StudyLocation.ROOM
                && candidate.isNeedSilence()
                && source.getPhoneVolume() == PhoneVolume.OFTEN_USE_SPEAKER) {

            score -= BIG_PENALTY;
        }

        else if (candidate.getStudyLocation() == StudyLocation.ROOM
                && candidate.isNeedSilence()
                && source.getPhoneVolume() == PhoneVolume.SOMETIMES_USE_SPEAKER) {

            score -= MID_PENALTY;
        }

        return Math.max(0.0, score);
    }
}