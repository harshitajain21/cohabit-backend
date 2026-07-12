package com.cohabit.cohabitbackend.service;

import com.cohabit.cohabitbackend.dto.FriendRequestResponse;
import com.cohabit.cohabitbackend.dto.FriendSearchResponse;
import com.cohabit.cohabitbackend.exception.FriendRequestAccessDeniedException;
import com.cohabit.cohabitbackend.exception.FriendRequestConflictException;
import com.cohabit.cohabitbackend.exception.FriendRequestNotFoundException;
import com.cohabit.cohabitbackend.exception.UserNotFoundException;
import com.cohabit.cohabitbackend.mapper.FriendRequestMapper;
import com.cohabit.cohabitbackend.model.FriendRequest;
import com.cohabit.cohabitbackend.model.QuestionnaireResponse;
import com.cohabit.cohabitbackend.model.RoommatePreference;
import com.cohabit.cohabitbackend.model.User;
import com.cohabit.cohabitbackend.model.enums.FriendRequestStatus;
import com.cohabit.cohabitbackend.repository.FriendRequestRepository;
import com.cohabit.cohabitbackend.repository.QuestionnaireResponseRepository;
import com.cohabit.cohabitbackend.repository.RoommatePreferenceRepository;
import com.cohabit.cohabitbackend.repository.UserRepository;
import com.cohabit.cohabitbackend.service.matching.CompatibilityEngine;
import com.cohabit.cohabitbackend.service.matching.CompatibilityReport;
import com.cohabit.cohabitbackend.service.matching.CompatibilityReportGenerator;
import com.cohabit.cohabitbackend.util.IitEmailValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

/**
 * Handles friend search, friend request creation, acceptance, and report generation.
 */
@Service
public class FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;
    private final UserRepository userRepository;
    private final QuestionnaireResponseRepository questionnaireResponseRepository;
    private final RoommatePreferenceRepository roommatePreferenceRepository;
    private final FriendRequestMapper friendRequestMapper;
    private final IitEmailValidator iitEmailValidator;
    private final CompatibilityEngine compatibilityEngine;
    private final CompatibilityReportGenerator compatibilityReportGenerator;

    /**
     * Creates a friend request service.
     *
     * @param friendRequestRepository friend request repository
     * @param userRepository user repository
     * @param questionnaireResponseRepository questionnaire repository
     * @param roommatePreferenceRepository roommate preference repository
     * @param friendRequestMapper friend request mapper
     * @param iitEmailValidator IIT email validator
     * @param compatibilityEngine compatibility engine
     * @param compatibilityReportGenerator compatibility report generator
     */
    public FriendRequestService(
            FriendRequestRepository friendRequestRepository,
            UserRepository userRepository,
            QuestionnaireResponseRepository questionnaireResponseRepository,
            RoommatePreferenceRepository roommatePreferenceRepository,
            FriendRequestMapper friendRequestMapper,
            IitEmailValidator iitEmailValidator,
            CompatibilityEngine compatibilityEngine,
            CompatibilityReportGenerator compatibilityReportGenerator
    ) {
        this.friendRequestRepository = friendRequestRepository;
        this.userRepository = userRepository;
        this.questionnaireResponseRepository = questionnaireResponseRepository;
        this.roommatePreferenceRepository = roommatePreferenceRepository;
        this.friendRequestMapper = friendRequestMapper;
        this.iitEmailValidator = iitEmailValidator;
        this.compatibilityEngine = compatibilityEngine;
        this.compatibilityReportGenerator = compatibilityReportGenerator;
    }

    /**
     * Searches for a user by IIT email.
     *
     * @param authenticatedEmail current user's IIT email
     * @param searchedEmail searched IIT email
     * @return matching user profile
     */
    @Transactional(readOnly = true)
    public FriendSearchResponse searchByIitEmail(String authenticatedEmail, String searchedEmail) {
        String normalizedEmail = iitEmailValidator.normalize(searchedEmail);
        if (iitEmailValidator.normalize(authenticatedEmail).equals(normalizedEmail)) {
            throw new FriendRequestConflictException("You cannot search yourself for a friend request.");
        }

        User user = userRepository.findByIitEmail(normalizedEmail)
                .orElseThrow(() -> new UserNotFoundException("No user found with this IIT email."));
        return friendRequestMapper.toSearchResponse(user);
    }

    /**
     * Sends a friend request and emails the recipient.
     *
     * @param requesterEmail authenticated requester's IIT email
     * @param recipientEmail recipient IIT email
     * @return created friend request
     */
    @Transactional
    public FriendRequestResponse sendFriendRequest(String requesterEmail, String recipientEmail) {
        User requester = findUserByEmail(requesterEmail);
        User recipient = findUserByEmail(iitEmailValidator.normalize(recipientEmail));

        if (requester.getId().equals(recipient.getId())) {
            throw new FriendRequestConflictException("You cannot send a friend request to yourself.");
        }
        ensureNoActiveRequestExists(requester, recipient);

        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setRequester(requester);
        friendRequest.setRecipient(recipient);
        friendRequest.setStatus(FriendRequestStatus.PENDING);

        FriendRequest savedRequest = friendRequestRepository.save(friendRequest);
        return friendRequestMapper.toResponse(savedRequest);
    }

    @Transactional(readOnly = true)
    public CompatibilityReport checkCompatibility(
            String requesterEmail,
            String recipientEmail
    ) {

        User requester = findUserByEmail(requesterEmail);
        User recipient = findUserByEmail(recipientEmail);

        QuestionnaireResponse requesterQuestionnaire =
                questionnaireResponseRepository.findByUser(requester)
                        .orElseThrow(() ->
                                new RuntimeException("Complete your questionnaire first."));

        QuestionnaireResponse recipientQuestionnaire =
                questionnaireResponseRepository.findByUser(recipient)
                        .orElseThrow(() ->
                                new RuntimeException("Friend has not completed the questionnaire."));

        return compatibilityEngine.calculateCompatibility(
                requesterQuestionnaire,
                preferenceOrDefault(requester),
                recipientQuestionnaire,
                preferenceOrDefault(recipient)
        ).orElseThrow(() ->
                new RuntimeException("Compatibility could not be calculated."));
    }

    /**
     * Accepts a pending friend request as the recipient.
     *
     * @param recipientEmail authenticated recipient's IIT email
     * @param requestId friend request id
     * @return accepted friend request with a compatibility report when available
     */
    @Transactional
    public FriendRequestResponse acceptFriendRequest(String recipientEmail, Long requestId) {
        User recipient = findUserByEmail(recipientEmail);
        FriendRequest friendRequest = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new FriendRequestNotFoundException("Friend request not found."));

        if (!friendRequest.getRecipient().getId().equals(recipient.getId())) {
            throw new FriendRequestAccessDeniedException("Only the recipient can accept this friend request.");
        }
        if (friendRequest.getStatus() != FriendRequestStatus.PENDING) {
            throw new FriendRequestConflictException("Only pending friend requests can be accepted.");
        }

        friendRequest.setStatus(FriendRequestStatus.ACCEPTED);
        friendRequest.setRespondedAt(Instant.now());
        generateCompatibilityReportIfPossible(friendRequest);

        FriendRequest savedRequest = friendRequestRepository.save(friendRequest);
        return friendRequestMapper.toResponse(savedRequest);
    }

    private User findUserByEmail(String email) {
        String normalizedEmail = iitEmailValidator.normalize(email);
        return userRepository.findByIitEmail(normalizedEmail)
                .orElseThrow(() -> new UserNotFoundException("User account was not found."));
    }

    private void ensureNoActiveRequestExists(User requester, User recipient) {
        boolean pendingExists = friendRequestRepository
                .findByRequesterAndRecipientAndStatusOrRequesterAndRecipientAndStatus(
                        requester,
                        recipient,
                        FriendRequestStatus.PENDING,
                        recipient,
                        requester,
                        FriendRequestStatus.PENDING
                )
                .isPresent();
        boolean acceptedExists = friendRequestRepository
                .findByRequesterAndRecipientAndStatusOrRequesterAndRecipientAndStatus(
                        requester,
                        recipient,
                        FriendRequestStatus.ACCEPTED,
                        recipient,
                        requester,
                        FriendRequestStatus.ACCEPTED
                )
                .isPresent();

        if (pendingExists || acceptedExists) {
            throw new FriendRequestConflictException("A friend request or friendship already exists between these users.");
        }
    }

    private void generateCompatibilityReportIfPossible(FriendRequest friendRequest) {
        questionnaireResponseRepository.findByUser(friendRequest.getRequester())
                .ifPresent(requesterQuestionnaire -> questionnaireResponseRepository.findByUser(friendRequest.getRecipient())
                        .ifPresent(recipientQuestionnaire -> compatibilityEngine.calculateCompatibility(
                                        requesterQuestionnaire,
                                        preferenceOrDefault(friendRequest.getRequester()),
                                        recipientQuestionnaire,
                                        preferenceOrDefault(friendRequest.getRecipient())
                                )
                                .ifPresent(report -> applyReport(friendRequest, report))));
    }

    private RoommatePreference preferenceOrDefault(User user) {
        return roommatePreferenceRepository.findByUser(user).orElseGet(RoommatePreference::new);
    }

    private void applyReport(FriendRequest friendRequest, CompatibilityReport report) {
        friendRequest.setOverallScore(report.overallScore());
        friendRequest.setMatchingTraits(compatibilityReportGenerator.showproperly(report.matchedTraits()));
        friendRequest.setPotentialConflicts(compatibilityReportGenerator.showproperly(report.conflicts()));
        friendRequest.setDealBreakerConflicts(compatibilityReportGenerator.showproperly(report.dealBreakerConflicts()));
        friendRequest.setReasoning(compatibilityReportGenerator.showproperly(report.reasoning()));
    }
}
