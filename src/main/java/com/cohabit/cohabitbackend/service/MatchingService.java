package com.cohabit.cohabitbackend.service;

import com.cohabit.cohabitbackend.dto.RoommateMatchResponse;
import com.cohabit.cohabitbackend.exception.QuestionnaireNotFoundException;
import com.cohabit.cohabitbackend.exception.RoommatePreferenceNotFoundException;
import com.cohabit.cohabitbackend.exception.UserNotFoundException;
import com.cohabit.cohabitbackend.model.QuestionnaireResponse;
import com.cohabit.cohabitbackend.model.RoommatePreference;
import com.cohabit.cohabitbackend.model.User;
import com.cohabit.cohabitbackend.repository.QuestionnaireResponseRepository;
import com.cohabit.cohabitbackend.repository.RoommatePreferenceRepository;
import com.cohabit.cohabitbackend.repository.UserRepository;
import com.cohabit.cohabitbackend.service.matching.CompatibilityEngine;
import com.cohabit.cohabitbackend.service.matching.CompatibilityReportGenerator;
import com.cohabit.cohabitbackend.service.matching.MatchingScoringProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

//Finds and ranks roommate matches for authenticated users.

@Service
public class MatchingService {

    private final UserRepository userRepository;
    private final QuestionnaireResponseRepository questionnaireResponseRepository;
    private final RoommatePreferenceRepository roommatePreferenceRepository;
    private final CompatibilityEngine compatibilityEngine;
    private final CompatibilityReportGenerator compatibilityReportGenerator;
    private final MatchingScoringProperties scoringProperties;

    public MatchingService(
            UserRepository userRepository,
            QuestionnaireResponseRepository questionnaireResponseRepository,
            RoommatePreferenceRepository roommatePreferenceRepository,
            CompatibilityEngine compatibilityEngine,
            CompatibilityReportGenerator compatibilityReportGenerator,
            MatchingScoringProperties scoringProperties
    ) {
        this.userRepository = userRepository;
        this.questionnaireResponseRepository = questionnaireResponseRepository;
        this.roommatePreferenceRepository = roommatePreferenceRepository;
        this.compatibilityEngine = compatibilityEngine;
        this.compatibilityReportGenerator = compatibilityReportGenerator;
        this.scoringProperties = scoringProperties;
    }

    //Returns the top roommate matches for the authenticated user.
    @Transactional(readOnly = true)
    public List<RoommateMatchResponse> findTopMatches(String userEmail) {
        User sourceUser = userRepository.findByIitEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("Authenticated user account was not found."));
        QuestionnaireResponse sourceQuestionnaire = questionnaireResponseRepository.findByUser(sourceUser)
                .orElseThrow(() -> new QuestionnaireNotFoundException("Complete your questionnaire before matching."));
        RoommatePreference sourcePreference = roommatePreferenceRepository.findByUser(sourceUser)
                .orElseThrow(() -> new RoommatePreferenceNotFoundException("Complete roommate preferences before matching."));

        if (!sourcePreference.isMatchingEnabled()) {
            return List.of();
        }

        List<User> candidates = userRepository.findAllByGenderAndYear(sourceUser.getGender(), sourceUser.getYear()).stream()
                .filter(candidate -> !candidate.getId().equals(sourceUser.getId()))
                .toList();

        if (candidates.isEmpty()) {
            return List.of();
        }

        Map<Long, QuestionnaireResponse> questionnairesByUserId = questionnaireResponseRepository.findAllByUserIn(candidates).stream()
                .collect(Collectors.toMap(response -> response.getUser().getId(), Function.identity(), (left, right) -> left));
        Map<Long, RoommatePreference> preferencesByUserId = roommatePreferenceRepository.findAllByUserIn(candidates).stream()
                .collect(Collectors.toMap(preference -> preference.getUser().getId(), Function.identity(), (left, right) -> left));

        return candidates.stream()
                .map(candidate -> buildCandidateMatch(
                        sourceQuestionnaire,
                        sourcePreference,
                        candidate,
                        questionnairesByUserId.get(candidate.getId()),
                        preferencesByUserId.get(candidate.getId())
                ))
                .flatMap(List::stream)
                .sorted(Comparator.comparing(
                        (RoommateMatchResponse match) -> match.compatibilityReport().overallScore()
                ).reversed())
                .limit(scoringProperties.getMaxMatches())
                .toList();
    }

    private List<RoommateMatchResponse> buildCandidateMatch(
            QuestionnaireResponse sourceQuestionnaire,
            RoommatePreference sourcePreference,
            User candidate,
            QuestionnaireResponse candidateQuestionnaire,
            RoommatePreference candidatePreference
    ) {
        if (candidateQuestionnaire == null || candidatePreference == null || !candidatePreference.isMatchingEnabled()) {
            return List.of();
        }

        return compatibilityEngine.calculateCompatibility(
                        sourceQuestionnaire,
                        sourcePreference,
                        candidateQuestionnaire,
                        candidatePreference
                )
                .map(report -> new RoommateMatchResponse(
                        candidate.getId(),
                        candidate.getName(),
                        candidate.getIitEmail(),
                        candidate.getBranch(),
                        candidate.getYear(),
                        compatibilityReportGenerator.toResponse(report)
                ))
                .map(List::of)
                .orElseGet(List::of);
    }
}
