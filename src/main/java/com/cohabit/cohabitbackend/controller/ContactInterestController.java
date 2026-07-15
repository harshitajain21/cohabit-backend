package com.cohabit.cohabitbackend.controller;

import com.cohabit.cohabitbackend.dto.ContactInterestRequest;
import com.cohabit.cohabitbackend.dto.MessageResponse;
import com.cohabit.cohabitbackend.model.ContactInterest;
import com.cohabit.cohabitbackend.model.User;
import com.cohabit.cohabitbackend.repository.ContactInterestRepository;
import com.cohabit.cohabitbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/matches")
@CrossOrigin(origins = "*")
public class ContactInterestController {

    @Autowired
    private ContactInterestRepository contactInterestRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/interest")
    public ResponseEntity<?> registerInterest(
            @RequestBody ContactInterestRequest request,
            Authentication authentication) {

        try {
            // 1. Safely get the user's email from the token
            String email = authentication.getName();

            // 2. Fetch the actual User entity from the database
            User currentUser = userRepository.findByIitEmail(email)
                    .orElseThrow(() -> new RuntimeException("Logged in user not found in DB"));

            Long requesterId = currentUser.getId();
            Long targetId = request.getTargetUserId();

            // 3. Catch null values from the frontend
            if (targetId == null) {
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("Target User ID is missing from the request payload."));
            }

            // 4. Validate that they aren't requesting themselves
            if (requesterId.equals(targetId)) {
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("You cannot express interest in yourself."));
            }

            // 5. Check for duplicates
            if (contactInterestRepository.existsByRequesterIdAndTargetUserId(requesterId, targetId)) {
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("You have already expressed interest in this user."));
            }

            // 6. Save
            ContactInterest interest = new ContactInterest(requesterId, targetId);
            contactInterestRepository.save(interest);

            return ResponseEntity.ok(new MessageResponse("Interest registered successfully."));

        } catch (Exception e) {
            // This will print the EXACT reason for the crash in your backend console
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(new MessageResponse("Server error: " + e.getMessage()));
        }
    }
}