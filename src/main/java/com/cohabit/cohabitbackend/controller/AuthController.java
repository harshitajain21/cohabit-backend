package com.cohabit.cohabitbackend.controller;

import com.cohabit.cohabitbackend.dto.AuthResponse;
import com.cohabit.cohabitbackend.dto.LoginRequest;
import com.cohabit.cohabitbackend.dto.MessageResponse;
import com.cohabit.cohabitbackend.dto.RegisterUserRequest;
import com.cohabit.cohabitbackend.dto.ResendVerificationRequest;
import com.cohabit.cohabitbackend.dto.UserResponse;
import com.cohabit.cohabitbackend.service.AuthService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// REST controller for registration, login, email verification, and verification resend flows.
@Validated
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    // Creates an authentication controller.
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    //Registers a new user and sends an email verification link.
    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(@Valid @RequestBody RegisterUserRequest request) {
        UserResponse user = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new MessageResponse("Registration successful. Verification email sent to " + user.iitEmail() + "."));
    }

    // Authenticates a verified user.
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }}

    /*
    //Verifies a registered user's IIT email address.
    @GetMapping("/verify")
    public ResponseEntity<MessageResponse> verify(@RequestParam @NotBlank(message = "Token is required") String token) {
        authService.verifyEmail(token);
        return ResponseEntity.ok(new MessageResponse("Email verified successfully."));
    }

    // Sends a fresh verification link to an unverified account.
    @PostMapping("/resend-verification")
    public ResponseEntity<MessageResponse> resendVerification(@Valid @RequestBody ResendVerificationRequest request) {
        authService.resendVerification(request.iitEmail());
        return ResponseEntity.ok(new MessageResponse("Verification email sent."));
    }
}*/
