package com.cohabit.cohabitbackend.service;

import com.cohabit.cohabitbackend.dto.RoommatePreferenceRequest;
import com.cohabit.cohabitbackend.dto.RoommatePreferenceResponse;
import com.cohabit.cohabitbackend.exception.RoommatePreferenceAlreadyExistsException;
import com.cohabit.cohabitbackend.exception.RoommatePreferenceNotFoundException;
import com.cohabit.cohabitbackend.exception.UserNotFoundException;
import com.cohabit.cohabitbackend.mapper.RoommatePreferenceMapper;
import com.cohabit.cohabitbackend.model.RoommatePreference;
import com.cohabit.cohabitbackend.model.User;
import com.cohabit.cohabitbackend.repository.RoommatePreferenceRepository;
import com.cohabit.cohabitbackend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Handles roommate preference creation, retrieval, and updates for authenticated users.
 */
@Service
public class RoommatePreferenceService {

    private final RoommatePreferenceRepository roommatePreferenceRepository;
    private final UserRepository userRepository;
    private final RoommatePreferenceMapper roommatePreferenceMapper;

    /**
     * Creates a roommate preference service.
     *
     * @param roommatePreferenceRepository roommate preference repository
     * @param userRepository user repository
     * @param roommatePreferenceMapper roommate preference mapper
     */
    public RoommatePreferenceService(
            RoommatePreferenceRepository roommatePreferenceRepository,
            UserRepository userRepository,
            RoommatePreferenceMapper roommatePreferenceMapper
    ) {
        this.roommatePreferenceRepository = roommatePreferenceRepository;
        this.userRepository = userRepository;
        this.roommatePreferenceMapper = roommatePreferenceMapper;
    }

    /**
     * Creates roommate preferences for the authenticated user.
     *
     * @param userEmail authenticated user's IIT email
     * @param request validated preference payload
     * @return saved roommate preferences
     */
    @Transactional
    public RoommatePreferenceResponse createPreferences(String userEmail, RoommatePreferenceRequest request) {
        User user = findUserByEmail(userEmail);
        if (roommatePreferenceRepository.existsByUser(user)) {
            throw new RoommatePreferenceAlreadyExistsException("Roommate preferences already exist for this user.");
        }

        RoommatePreference preference = roommatePreferenceMapper.toEntity(request);
        preference.setUser(user);

        RoommatePreference savedPreference = roommatePreferenceRepository.save(preference);
        return roommatePreferenceMapper.toResponse(savedPreference);
    }

    /**
     * Retrieves roommate preferences for the authenticated user.
     *
     * @param userEmail authenticated user's IIT email
     * @return roommate preferences
     */
    @Transactional(readOnly = true)
    public RoommatePreferenceResponse getMyPreferences(String userEmail) {
        User user = findUserByEmail(userEmail);
        RoommatePreference preference = roommatePreferenceRepository.findByUser(user)
                .orElseThrow(() -> new RoommatePreferenceNotFoundException("Roommate preferences not found for this user."));

        return roommatePreferenceMapper.toResponse(preference);
    }

    /**
     * Updates roommate preferences for the authenticated user.
     *
     * @param userEmail authenticated user's IIT email
     * @param request validated preference payload
     * @return updated roommate preferences
     */
    @Transactional
    public RoommatePreferenceResponse updatePreferences(String userEmail, RoommatePreferenceRequest request) {
        User user = findUserByEmail(userEmail);
        RoommatePreference preference = roommatePreferenceRepository.findByUser(user)
                .orElseThrow(() -> new RoommatePreferenceNotFoundException("Roommate preferences not found for this user."));

        roommatePreferenceMapper.updateEntity(request, preference);
        RoommatePreference savedPreference = roommatePreferenceRepository.save(preference);
        return roommatePreferenceMapper.toResponse(savedPreference);
    }

    private User findUserByEmail(String userEmail) {
        return userRepository.findByIitEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("Authenticated user account was not found."));
    }
}
