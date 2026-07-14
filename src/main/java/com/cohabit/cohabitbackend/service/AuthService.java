package com.cohabit.cohabitbackend.service;

import com.cohabit.cohabitbackend.dto.AuthResponse;
import com.cohabit.cohabitbackend.dto.LoginRequest;
import com.cohabit.cohabitbackend.dto.RegisterUserRequest;
import com.cohabit.cohabitbackend.dto.UserResponse;
import com.cohabit.cohabitbackend.exception.AuthenticationFailedException;
import com.cohabit.cohabitbackend.exception.EmailAlreadyExistsException;
import com.cohabit.cohabitbackend.exception.EmailNotVerifiedException;
import com.cohabit.cohabitbackend.exception.InvalidVerificationTokenException;
import com.cohabit.cohabitbackend.exception.UserNotFoundException;
import com.cohabit.cohabitbackend.mapper.UserMapper;
import com.cohabit.cohabitbackend.model.User;
import com.cohabit.cohabitbackend.model.VerificationToken;
import com.cohabit.cohabitbackend.repository.UserRepository;
import com.cohabit.cohabitbackend.repository.VerificationTokenRepository;
import com.cohabit.cohabitbackend.util.IitEmailValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Clock;
import java.time.Instant;
import java.util.Base64;

//Coordinates registration, login
// note: email verification, and verification resends is commented.. not used yet

@Service
public class AuthService {

    private static final int TOKEN_BYTES = 48;

    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final IitEmailValidator iitEmailValidator;
    private final SecureRandom secureRandom;
    private final Clock clock;
    private final long verificationTokenExpirationMillis;
    public AuthService(
            UserRepository userRepository,
            VerificationTokenRepository verificationTokenRepository,
            UserMapper userMapper,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            UserDetailsService userDetailsService,
            IitEmailValidator iitEmailValidator,
            @Value("${cohabit.auth.verification-token-expiration-millis:3600000}") long verificationTokenExpirationMillis
    ) {
        this.userRepository = userRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.iitEmailValidator = iitEmailValidator;
        this.secureRandom = new SecureRandom();
        this.clock = Clock.systemUTC();
        this.verificationTokenExpirationMillis = verificationTokenExpirationMillis;
    }

    // Registers a user and sends the first verification email.
    @Transactional
    public UserResponse register(RegisterUserRequest request) {

        String normalizedEmail = iitEmailValidator.normalize(request.iitEmail());
        if (!iitEmailValidator.isValid(normalizedEmail)) {
            throw new AuthenticationFailedException("Only IIT BHU email addresses are allowed.");
        }
        if (userRepository.existsByIitEmail(normalizedEmail)) {
            throw new EmailAlreadyExistsException("An account with this IIT BHU email already exists.");
        }

        User user = userMapper.toEntity(request);
        user.setIitEmail(normalizedEmail);
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setEmailVerified(true);

        User savedUser = userRepository.save(user);
        //VerificationToken token = createVerificationToken(savedUser);

        return userMapper.toResponse(savedUser);
    }

    //Authenticates a verified user and returns a JWT.
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        String normalizedEmail = iitEmailValidator.normalize(request.iitEmail());
        User user = userRepository.findByIitEmail(normalizedEmail)
                .orElseThrow(() -> new AuthenticationFailedException("Invalid email or password."));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new AuthenticationFailedException("Invalid email or password.");
        }
        /*
        if (!user.isEmailVerified()) {
            throw new EmailNotVerifiedException("Please verify your IIT email before logging in.");
        }*/

        UserDetails userDetails = userDetailsService.loadUserByUsername(normalizedEmail);
        String jwt = jwtService.generateToken(userDetails);

        return new AuthResponse(jwt, "Bearer", userMapper.toResponse(user));
    }

    /*
    //Verifies a user account using a single-use token.
    @Transactional
    public void verifyEmail(String tokenValue) {
        VerificationToken token = verificationTokenRepository.findByToken(tokenValue)
                .orElseThrow(() -> new InvalidVerificationTokenException("Invalid verification token."));

        if (token.isUsed()) {
            throw new InvalidVerificationTokenException("Verification token has already been used.");
        }
        if (token.getExpiresAt().isBefore(Instant.now(clock))) {
            throw new InvalidVerificationTokenException("Verification token has expired.");
        }

        User user = token.getUser();
        user.setEmailVerified(true);
        token.setUsed(true);
        userRepository.save(user);
        verificationTokenRepository.save(token);
    }

    //Sends a new verification email for an unverified account.
    @Transactional
    public void resendVerification(String email) {
        String normalizedEmail = iitEmailValidator.normalize(email);
        User user = userRepository.findByIitEmail(normalizedEmail)
                .orElseThrow(() -> new UserNotFoundException("No account exists for this IIT email."));

        if (user.isEmailVerified()) {
            throw new EmailNotVerifiedException("This account is already verified.");
        }

        verificationTokenRepository.deleteByUser(user);
        VerificationToken token = createVerificationToken(user);
    }

    private VerificationToken createVerificationToken(User user) {
        byte[] randomBytes = new byte[TOKEN_BYTES];
        secureRandom.nextBytes(randomBytes);
        String tokenValue = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
        Instant expiresAt = Instant.now(clock).plusMillis(verificationTokenExpirationMillis);
        return verificationTokenRepository.save(new VerificationToken(tokenValue, user, expiresAt));
    }*/
}
