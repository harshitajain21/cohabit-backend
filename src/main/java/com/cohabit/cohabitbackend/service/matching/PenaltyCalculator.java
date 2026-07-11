package com.cohabit.cohabitbackend.service.matching;

import com.cohabit.cohabitbackend.model.QuestionnaireResponse;
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
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Calculates cross-feature compatibility penalties.
 */
@Component
public class PenaltyCalculator {

    private final MatchingScoringProperties scoringProperties;

    /**
     * Creates a penalty calculator.
     *
     * @param scoringProperties centralized scoring properties
     */
    public PenaltyCalculator(MatchingScoringProperties scoringProperties) {
        this.scoringProperties = scoringProperties;
    }

    /**
     * Calculates cross-feature penalties and reasoning.
     *
     * @param source questionnaire for the user requesting matches
     * @param candidate questionnaire for the candidate user
     * @return penalty result
     */
    public PenaltyResult calculatePenalties(QuestionnaireResponse source, QuestionnaireResponse candidate) {
        int penalty = 0;
        List<String> conflicts = new ArrayList<>();
        List<String> reasoning = new ArrayList<>();

        if ((source.getSleepSchedule() == SleepSchedule.NIGHT_OWL
                && candidate.getSleepSchedule() == SleepSchedule.EARLY_SLEEPER)
                || (source.getSleepSchedule() == SleepSchedule.EARLY_SLEEPER
                && candidate.getSleepSchedule() == SleepSchedule.NIGHT_OWL)) {
            penalty += scoringProperties.getNightOwlEarlySleeperPenalty();
            conflicts.add("Night owl vs early sleeper");
            reasoning.add("Very different sleep schedules can create noise and routine conflicts.");
        }

        if ((source.getSleepLightPreference() == LightPreference.NEEDS_DARKNESS
                && candidate.getSleepLightPreference() == LightPreference.DOESNT_MATTER)
                || (source.getSleepLightPreference() == LightPreference.DOESNT_MATTER
                && candidate.getSleepLightPreference() == LightPreference.NEEDS_DARKNESS)) {
            penalty += scoringProperties.getDarknessFlexibleLightPenalty();
            conflicts.add("Needs darkness vs flexible light preference");
            reasoning.add("A strict darkness preference may clash with a roommate who is not sensitive to lights.");
        }

        if ((candidate.getPhoneVolume() == PhoneVolume.OFTEN_USE_SPEAKER && source.isNeedSilence())
                || (source.getPhoneVolume() == PhoneVolume.OFTEN_USE_SPEAKER && candidate.isNeedSilence())) {
            penalty += scoringProperties.getFrequentSpeakerSilencePenalty();
            conflicts.add("Speaker phone use vs need for silence");
            reasoning.add("Frequent speaker use is risky when the other user needs silence.");
        } else if ((candidate.getPhoneVolume() == PhoneVolume.SOMETIMES_USE_SPEAKER && source.isNeedSilence())
                || (source.getPhoneVolume() == PhoneVolume.SOMETIMES_USE_SPEAKER && candidate.isNeedSilence())) {
            penalty += scoringProperties.getOccasionalSpeakerSilencePenalty();
            conflicts.add("Occasional speaker use vs need for silence");
            reasoning.add("Occasional speaker use may still bother a user who needs silence.");
        }

        if ((source.getGuestPreference() == GuestPreference.DONT_LIKE_GUESTS
                && candidate.getGuestPreference() == GuestPreference.ENJOY_HAVING_GUESTS)
                || (source.getGuestPreference() == GuestPreference.ENJOY_HAVING_GUESTS
                && candidate.getGuestPreference() == GuestPreference.DONT_LIKE_GUESTS)) {
            penalty += scoringProperties.getOppositeGuestPreferencePenalty();
            conflicts.add("Guests vs does not like guests");
            reasoning.add("Opposite guest preferences can create recurring social-boundary conflicts.");
        }

        if ((source.getCleanliness() == Cleanliness.MESSY && candidate.getCleanliness() == Cleanliness.VERY_CLEAN)
                || (source.getCleanliness() == Cleanliness.VERY_CLEAN && candidate.getCleanliness() == Cleanliness.MESSY)) {
            penalty += scoringProperties.getExtremeCleanlinessPenalty();
            conflicts.add("Messy vs very clean");
            reasoning.add("A large cleanliness gap is one of the strongest day-to-day roommate friction points.");
        } else if (Math.abs(source.getCleanliness().ordinal() - candidate.getCleanliness().ordinal()) == 2) {
            penalty += scoringProperties.getModerateCleanlinessPenalty();
            conflicts.add("Different cleanliness standards");
            reasoning.add("A moderate cleanliness gap may require clear room rules.");
        }

        if ((source.getSubstanceUse() == SubstanceUse.NEVER && candidate.getSubstanceUse() != SubstanceUse.NEVER)
                || (candidate.getSubstanceUse() == SubstanceUse.NEVER && source.getSubstanceUse() != SubstanceUse.NEVER)) {
            penalty += scoringProperties.getNoSubstanceUsePenalty();
            conflicts.add("Substance use vs no substance use");
            reasoning.add("A no-substance-use preference may not tolerate smoking or other substance use.");
        } else if (Math.abs(source.getSubstanceUse().ordinal() - candidate.getSubstanceUse().ordinal()) >= 2) {
            penalty += scoringProperties.getSubstanceUseGapPenalty();
            conflicts.add("Different substance-use comfort levels");
            reasoning.add("Different substance-use habits may need explicit boundaries.");
        }

        PenaltyResult compoundPenalty = calculateCompoundPenalties(source, candidate);
        penalty += compoundPenalty.penalty();
        conflicts.addAll(compoundPenalty.conflicts());
        reasoning.addAll(compoundPenalty.reasoning());

        return new PenaltyResult(penalty, conflicts, reasoning);
    }

    private PenaltyResult calculateCompoundPenalties(QuestionnaireResponse source, QuestionnaireResponse candidate) {
        int penalty = 0;
        List<String> conflicts = new ArrayList<>();
        List<String> reasoning = new ArrayList<>();

        if (isDarkEarlySleeper(source) && candidate.getSleepSchedule() == SleepSchedule.NIGHT_OWL) {
            penalty += scoringProperties.getDarkEarlySleeperNightOwlPenalty();
            conflicts.add("Needs darkness and sleeps early vs night owl");
            reasoning.add("A user who sleeps early and needs darkness is especially sensitive to a night owl's late movement or light.");
        }
        if (isDarkEarlySleeper(candidate) && source.getSleepSchedule() == SleepSchedule.NIGHT_OWL) {
            penalty += scoringProperties.getDarkEarlySleeperNightOwlPenalty();
            conflicts.add("Night owl vs needs darkness and sleeps early");
            reasoning.add("A night owl may disturb a roommate who sleeps early and needs a dark room.");
        }

        if (studiesInRoomAndNeedsSilence(source) && usesSpeaker(candidate)) {
            penalty += scoringProperties.getRoomStudySilenceSpeakerPenalty();
            conflicts.add("Studies in room and needs silence vs speaker phone use");
            reasoning.add("Room study plus silence needs clashes strongly with a roommate who uses phone speakers.");
        }
        if (studiesInRoomAndNeedsSilence(candidate) && usesSpeaker(source)) {
            penalty += scoringProperties.getRoomStudySilenceSpeakerPenalty();
            conflicts.add("Speaker phone use vs studies in room and needs silence");
            reasoning.add("Speaker phone use can interrupt a roommate who relies on the room as a quiet study space.");
        }

        if (source.getPersonality() == Personality.INTROVERT
                && candidate.getGuestPreference() == GuestPreference.ENJOY_HAVING_GUESTS) {
            penalty += scoringProperties.getIntrovertGuestPenalty();
            conflicts.add("Introvert vs enjoys having guests");
            reasoning.add("An introvert may need more private downtime than a roommate who frequently likes guests.");
        }
        if (candidate.getPersonality() == Personality.INTROVERT
                && source.getGuestPreference() == GuestPreference.ENJOY_HAVING_GUESTS) {
            penalty += scoringProperties.getIntrovertGuestPenalty();
            conflicts.add("Enjoys having guests vs introvert");
            reasoning.add("Frequent guests may feel draining for an introverted roommate.");
        }

        if (source.getPersonality() == Personality.EXTROVERT
                && candidate.getGuestPreference() == GuestPreference.DONT_LIKE_GUESTS) {
            penalty += scoringProperties.getExtrovertNoGuestPenalty();
            conflicts.add("Extrovert vs does not like guests");
            reasoning.add("An extrovert's social habits may feel restricted by a roommate who dislikes guests.");
        }
        if (candidate.getPersonality() == Personality.EXTROVERT
                && source.getGuestPreference() == GuestPreference.DONT_LIKE_GUESTS) {
            penalty += scoringProperties.getExtrovertNoGuestPenalty();
            conflicts.add("Does not like guests vs extrovert");
            reasoning.add("A roommate who dislikes guests may conflict with an extrovert's social routine.");
        }

        if (source.getStudyLocation() == StudyLocation.ROOM
                && candidate.getGuestPreference() == GuestPreference.ENJOY_HAVING_GUESTS) {
            penalty += scoringProperties.getRoomStudyGuestPenalty();
            conflicts.add("Studies in room vs frequent guests");
            reasoning.add("Guests in the room can disrupt someone who depends on the room for studying.");
        }
        if (candidate.getStudyLocation() == StudyLocation.ROOM
                && source.getGuestPreference() == GuestPreference.ENJOY_HAVING_GUESTS) {
            penalty += scoringProperties.getRoomStudyGuestPenalty();
            conflicts.add("Frequent guests vs studies in room");
            reasoning.add("A social room environment can make room-based study difficult.");
        }

        if (source.getCleanliness() == Cleanliness.VERY_CLEAN
                && candidate.getSharingPreference() == SharingPreference.HAPPY_TO_SHARE) {
            penalty += scoringProperties.getCleanRelaxedSharingPenalty();
            conflicts.add("Very clean vs relaxed sharing");
            reasoning.add("A very clean user may prefer clearer boundaries around shared items and space.");
        }
        if (candidate.getCleanliness() == Cleanliness.VERY_CLEAN
                && source.getSharingPreference() == SharingPreference.HAPPY_TO_SHARE) {
            penalty += scoringProperties.getCleanRelaxedSharingPenalty();
            conflicts.add("Relaxed sharing vs very clean");
            reasoning.add("Casual sharing can bother a roommate who keeps strict order.");
        }

        if (source.getLanguageImportance() == LanguageImportance.VERY_IMPORTANT
                && source.getPersonality() == Personality.INTROVERT
                && candidate.getLanguageImportance() == LanguageImportance.NOT_IMPORTANT) {
            penalty += scoringProperties.getIntrovertLanguagePenalty();
            conflicts.add("Introvert with high language comfort need vs low language importance");
            reasoning.add("An introvert who values shared language may feel less comfortable with a roommate who does not prioritize it.");
        }
        if (candidate.getLanguageImportance() == LanguageImportance.VERY_IMPORTANT
                && candidate.getPersonality() == Personality.INTROVERT
                && source.getLanguageImportance() == LanguageImportance.NOT_IMPORTANT) {
            penalty += scoringProperties.getIntrovertLanguagePenalty();
            conflicts.add("Low language importance vs introvert with high language comfort need");
            reasoning.add("Language comfort can matter more when an introverted roommate relies on low-friction communication.");
        }

        if (source.getSubstanceUse() == SubstanceUse.NEVER
                && candidate.getGuestPreference() == GuestPreference.ENJOY_HAVING_GUESTS
                && candidate.getSubstanceUse() != SubstanceUse.NEVER) {
            penalty += scoringProperties.getSocialSubstanceUsePenalty();
            conflicts.add("No substance use vs social substance-use environment");
            reasoning.add("Guests combined with substance use can be a stronger concern for someone who avoids substance use.");
        }
        if (candidate.getSubstanceUse() == SubstanceUse.NEVER
                && source.getGuestPreference() == GuestPreference.ENJOY_HAVING_GUESTS
                && source.getSubstanceUse() != SubstanceUse.NEVER) {
            penalty += scoringProperties.getSocialSubstanceUsePenalty();
            conflicts.add("Social substance-use environment vs no substance use");
            reasoning.add("A substance-free roommate may be uncomfortable with guests around substance use.");
        }

        return new PenaltyResult(penalty, conflicts, reasoning);
    }

    private boolean isDarkEarlySleeper(QuestionnaireResponse response) {
        return response.getSleepSchedule() == SleepSchedule.EARLY_SLEEPER
                && response.getSleepLightPreference() == LightPreference.NEEDS_DARKNESS;
    }

    private boolean studiesInRoomAndNeedsSilence(QuestionnaireResponse response) {
        return response.getStudyLocation() == StudyLocation.ROOM && response.isNeedSilence();
    }

    private boolean usesSpeaker(QuestionnaireResponse response) {
        return response.getPhoneVolume() == PhoneVolume.SOMETIMES_USE_SPEAKER
                || response.getPhoneVolume() == PhoneVolume.OFTEN_USE_SPEAKER;
    }

    /**
     * Cross-feature penalty result.
     */
    public record PenaltyResult(int penalty, List<String> conflicts, List<String> reasoning) {
    }
}
