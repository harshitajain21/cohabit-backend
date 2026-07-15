package com.cohabit.cohabitbackend.controller;

import com.cohabit.cohabitbackend.dto.RoommateMatchResponse;
import com.cohabit.cohabitbackend.service.MatchingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//REST controller for roommate matching.

@RestController
@RequestMapping("/matches")
public class MatchingController {

    private final MatchingService matchingService;
    public MatchingController(MatchingService matchingService) {
        this.matchingService = matchingService;
    }

    //Returns the authenticated user's top roommate matches.
    @GetMapping
    public ResponseEntity<List<RoommateMatchResponse>> getTopMatches(Authentication authentication) {
        return ResponseEntity.ok(matchingService.findTopMatches(authentication.getName()));
    }
}
