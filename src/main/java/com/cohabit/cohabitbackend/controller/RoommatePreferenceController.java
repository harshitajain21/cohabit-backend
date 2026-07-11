package com.cohabit.cohabitbackend.controller;

import com.cohabit.cohabitbackend.dto.RoommatePreferenceRequest;
import com.cohabit.cohabitbackend.dto.RoommatePreferenceResponse;
import com.cohabit.cohabitbackend.service.RoommatePreferenceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for authenticated users' roommate preferences.
 */
@RestController
@RequestMapping("/preferences")
public class RoommatePreferenceController {

    private final RoommatePreferenceService roommatePreferenceService;

    /**
     * Creates a roommate preference controller.
     *
     * @param roommatePreferenceService roommate preference service
     */
    public RoommatePreferenceController(RoommatePreferenceService roommatePreferenceService) {
        this.roommatePreferenceService = roommatePreferenceService;
    }

    /**
     * Creates roommate preferences for the authenticated user.
     *
     * @param request preference payload
     * @param authentication current Spring Security authentication
     * @return created roommate preferences
     */
    @PostMapping
    public ResponseEntity<RoommatePreferenceResponse> createPreferences(
            @Valid @RequestBody RoommatePreferenceRequest request,
            Authentication authentication
    ) {
        RoommatePreferenceResponse response = roommatePreferenceService.createPreferences(authentication.getName(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Retrieves roommate preferences for the authenticated user.
     *
     * @param authentication current Spring Security authentication
     * @return roommate preferences
     */
    @GetMapping("/me")
    public ResponseEntity<RoommatePreferenceResponse> getMyPreferences(Authentication authentication) {
        RoommatePreferenceResponse response = roommatePreferenceService.getMyPreferences(authentication.getName());
        return ResponseEntity.ok(response);
    }

    /**
     * Updates roommate preferences for the authenticated user.
     *
     * @param request preference payload
     * @param authentication current Spring Security authentication
     * @return updated roommate preferences
     */
    @PutMapping
    public ResponseEntity<RoommatePreferenceResponse> updatePreferences(
            @Valid @RequestBody RoommatePreferenceRequest request,
            Authentication authentication
    ) {
        RoommatePreferenceResponse response = roommatePreferenceService.updatePreferences(authentication.getName(), request);
        return ResponseEntity.ok(response);
    }
}
