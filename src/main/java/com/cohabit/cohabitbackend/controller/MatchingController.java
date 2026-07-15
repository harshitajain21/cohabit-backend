package com.cohabit.cohabitbackend.controller;

import com.cohabit.cohabitbackend.dto.RoommateMatchResponse;
import com.cohabit.cohabitbackend.service.MatchingService;
import com.cohabit.cohabitbackend.model.User;
import com.cohabit.cohabitbackend.model.QuestionnaireResponse;
import com.cohabit.cohabitbackend.model.RoommatePreference;
import com.cohabit.cohabitbackend.service.matching.CompatibilityReport;
import com.cohabit.cohabitbackend.service.matching.CompatibilityEngine;
import com.cohabit.cohabitbackend.repository.UserRepository;
import com.cohabit.cohabitbackend.repository.QuestionnaireResponseRepository;
import com.cohabit.cohabitbackend.repository.RoommatePreferenceRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RestController
@RequestMapping("/matches")
public class MatchingController {

    // 1. These declarations must exist at the top of the class
    private final MatchingService matchingService;
    private final UserRepository userRepository;
    private final QuestionnaireResponseRepository questionnaireRepo;
    private final RoommatePreferenceRepository preferenceRepo;
    private final CompatibilityEngine compatibilityEngine;

    // 2. The constructor must inject all of them
    public MatchingController(
            MatchingService matchingService,
            UserRepository userRepository,
            QuestionnaireResponseRepository questionnaireRepo,
            RoommatePreferenceRepository preferenceRepo,
            CompatibilityEngine compatibilityEngine) {
        this.matchingService = matchingService;
        this.userRepository = userRepository;
        this.questionnaireRepo = questionnaireRepo;
        this.preferenceRepo = preferenceRepo;
        this.compatibilityEngine = compatibilityEngine;
    }

    // Returns the authenticated user's top roommate matches.
    @GetMapping
    public ResponseEntity<List<RoommateMatchResponse>> getTopMatches(Authentication authentication) {
        return ResponseEntity.ok(matchingService.findTopMatches(authentication.getName()));
    }

    // Admin endpoint
    @GetMapping("/admin/score")
    public ResponseEntity<?> getAdminCompatibilityReport(
            @RequestParam Long user1Id,
            @RequestParam Long user2Id) {

        User user1 = userRepository.findById(user1Id)
                .orElseThrow(() -> new RuntimeException("User 1 not found"));
        User user2 = userRepository.findById(user2Id)
                .orElseThrow(() -> new RuntimeException("User 2 not found"));

        QuestionnaireResponse q1 = questionnaireRepo.findByUser(user1)
                .orElseThrow(() -> new RuntimeException("User 1 Questionnaire not found"));
        QuestionnaireResponse q2 = questionnaireRepo.findByUser(user2)
                .orElseThrow(() -> new RuntimeException("User 2 Questionnaire not found"));

        RoommatePreference p1 = preferenceRepo.findByUser(user1)
                .orElseThrow(() -> new RuntimeException("User 1 Preferences not found"));
        RoommatePreference p2 = preferenceRepo.findByUser(user2)
                .orElseThrow(() -> new RuntimeException("User 2 Preferences not found"));

        // 3. Now the engine and variables are recognized!
        CompatibilityReport report = compatibilityEngine.calculateCompatibility(q1, p1, q2, p2)
                .orElseThrow(() -> new RuntimeException("Match failed (e.g., dealbreaker triggered)"));

        return ResponseEntity.ok(report);
    }
}