package com.cohabit.cohabitbackend.service;

import com.cohabit.cohabitbackend.dto.QuestionnaireRequest;
import com.cohabit.cohabitbackend.dto.QuestionnaireResponseDto;
import com.cohabit.cohabitbackend.exception.QuestionnaireAlreadyExistsException;
import com.cohabit.cohabitbackend.exception.QuestionnaireNotFoundException;
import com.cohabit.cohabitbackend.exception.UserNotFoundException;
import com.cohabit.cohabitbackend.mapper.QuestionnaireMapper;
import com.cohabit.cohabitbackend.model.QuestionnaireResponse;
import com.cohabit.cohabitbackend.model.User;
import com.cohabit.cohabitbackend.repository.QuestionnaireResponseRepository;
import com.cohabit.cohabitbackend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Handles questionnaire creation, retrieval, and updates for authenticated users.
 */
@Service
public class QuestionnaireService {

    private final QuestionnaireResponseRepository questionnaireResponseRepository;
    private final UserRepository userRepository;
    private final QuestionnaireMapper questionnaireMapper;

    /**
     * Creates a questionnaire service.
     *
     * @param questionnaireResponseRepository questionnaire repository
     * @param userRepository user repository
     * @param questionnaireMapper questionnaire mapper
     */
    public QuestionnaireService(
            QuestionnaireResponseRepository questionnaireResponseRepository,
            UserRepository userRepository,
            QuestionnaireMapper questionnaireMapper
    ) {
        this.questionnaireResponseRepository = questionnaireResponseRepository;
        this.userRepository = userRepository;
        this.questionnaireMapper = questionnaireMapper;
    }

    /**
     * Creates the authenticated user's questionnaire response.
     *
     * @param userEmail authenticated user's IIT email
     * @param request validated questionnaire payload
     * @return saved questionnaire response
     */
    @Transactional
    public QuestionnaireResponseDto createQuestionnaire(String userEmail, QuestionnaireRequest request) {
        User user = findUserByEmail(userEmail);
        if (questionnaireResponseRepository.existsByUser(user)) {
            throw new QuestionnaireAlreadyExistsException("Questionnaire already exists for this user.");
        }

        QuestionnaireResponse questionnaireResponse = questionnaireMapper.toEntity(request);
        questionnaireResponse.setUser(user);

        QuestionnaireResponse savedResponse = questionnaireResponseRepository.save(questionnaireResponse);
        return questionnaireMapper.toDto(savedResponse);
    }

    /**
     * Retrieves the authenticated user's questionnaire response.
     *
     * @param userEmail authenticated user's IIT email
     * @return questionnaire response
     */
    @Transactional(readOnly = true)
    public QuestionnaireResponseDto getMyQuestionnaire(String userEmail) {
        User user = findUserByEmail(userEmail);
        QuestionnaireResponse questionnaireResponse = questionnaireResponseRepository.findByUser(user)
                .orElseThrow(() -> new QuestionnaireNotFoundException("Questionnaire not found for this user."));

        return questionnaireMapper.toDto(questionnaireResponse);
    }

    /**
     * Updates the authenticated user's existing questionnaire response.
     *
     * @param userEmail authenticated user's IIT email
     * @param request validated questionnaire payload
     * @return updated questionnaire response
     */
    @Transactional
    public QuestionnaireResponseDto updateQuestionnaire(String userEmail, QuestionnaireRequest request) {
        User user = findUserByEmail(userEmail);
        QuestionnaireResponse questionnaireResponse = questionnaireResponseRepository.findByUser(user)
                .orElseThrow(() -> new QuestionnaireNotFoundException("Questionnaire not found for this user."));

        questionnaireMapper.updateEntity(request, questionnaireResponse);
        QuestionnaireResponse savedResponse = questionnaireResponseRepository.save(questionnaireResponse);
        return questionnaireMapper.toDto(savedResponse);
    }

    private User findUserByEmail(String userEmail) {
        return userRepository.findByIitEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("Authenticated user account was not found."));
    }
}
