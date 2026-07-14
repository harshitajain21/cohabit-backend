package com.cohabit.cohabitbackend.controller;

import com.cohabit.cohabitbackend.dto.FriendRequestResponse;
import com.cohabit.cohabitbackend.dto.FriendSearchResponse;
import com.cohabit.cohabitbackend.dto.SendFriendRequestRequest;
import com.cohabit.cohabitbackend.service.FriendRequestService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.cohabit.cohabitbackend.dto.SendFriendRequestRequest;
import com.cohabit.cohabitbackend.service.matching.CompatibilityReport;
import com.cohabit.cohabitbackend.dto.CompatibilityCheckLogResponse;
import java.util.List;


//REST controller for friend search and friend request workflows.

@Validated
@RestController
@RequestMapping("/friends")
public class FriendRequestController {

    private final FriendRequestService friendRequestService;
    public FriendRequestController(FriendRequestService friendRequestService) {
        this.friendRequestService = friendRequestService;
    }

    //Searches for a Cohabit user by IIT email.
    @GetMapping("/search")
    public ResponseEntity<FriendSearchResponse> searchByIitEmail(
            @RequestParam
            @NotBlank(message = "IIT email is required")
            @Email(message = "Invalid email format")
            String iitEmail,
            Authentication authentication
    ) {
        FriendSearchResponse response = friendRequestService.searchByIitEmail(authentication.getName(), iitEmail);
        return ResponseEntity.ok(response);
    }

    //Checks compatibility with another user before sending a friend request.
    @PostMapping("/check-compatibility")
    public ResponseEntity<CompatibilityReport> checkCompatibility(
            @Valid @RequestBody SendFriendRequestRequest request,
            Authentication authentication
    ) {
        CompatibilityReport report = friendRequestService.checkCompatibility(
                authentication.getName(),
                request.recipientIitEmail()
        );

        return ResponseEntity.ok(report);
    }

    @GetMapping("/check-compatibility/history")
    public ResponseEntity<List<CompatibilityCheckLogResponse>> getCompatibilityCheckHistory(
            Authentication authentication
    ) {
        return ResponseEntity.ok(friendRequestService.getCompatibilityCheckHistory(authentication.getName()));
    }

    //Sends a friend request to another Cohabit user.
    @PostMapping("/requests")
    public ResponseEntity<FriendRequestResponse> sendFriendRequest(
            @Valid @RequestBody SendFriendRequestRequest request,
            Authentication authentication
    ) {
        FriendRequestResponse response = friendRequestService.sendFriendRequest(
                authentication.getName(),
                request.recipientIitEmail()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //Accepts a pending friend request.
    @PutMapping("/requests/{requestId}/accept")
    public ResponseEntity<FriendRequestResponse> acceptFriendRequest(
            @PathVariable Long requestId,
            Authentication authentication
    ) {
        FriendRequestResponse response = friendRequestService.acceptFriendRequest(authentication.getName(), requestId);
        return ResponseEntity.ok(response);
    }
}
