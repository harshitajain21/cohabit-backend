package com.cohabit.cohabitbackend.controller;

import com.cohabit.cohabitbackend.dto.ContactInterestRequest;
import com.cohabit.cohabitbackend.dto.MessageResponse;
import com.cohabit.cohabitbackend.model.ContactInterest;
import com.cohabit.cohabitbackend.model.User;
import com.cohabit.cohabitbackend.repository.ContactInterestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/matches")
public class ContactInterestController {

    @Autowired
    private ContactInterestRepository contactInterestRepository;

    @PostMapping("/interest")
    public ResponseEntity<?> registerInterest(
            @RequestBody ContactInterestRequest request,
            Authentication authentication) {

        // 1. Get the logged-in user's ID
        User currentUser = (User) authentication.getPrincipal();
        Long requesterId = currentUser.getId();
        Long targetId = request.getTargetUserId();

        // 2. Validate that they aren't requesting themselves
        if (requesterId.equals(targetId)) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("You cannot express interest in yourself."));
        }

        // 3. Check if they already clicked "Interested" before
        if (contactInterestRepository.existsByRequesterIdAndTargetUserId(requesterId, targetId)) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("You have already expressed interest in this user."));
        }

        // 4. Save the interest to the database
        ContactInterest interest = new ContactInterest(requesterId, targetId);
        contactInterestRepository.save(interest);

        return ResponseEntity.ok(new MessageResponse("Interest registered successfully."));
    }
}