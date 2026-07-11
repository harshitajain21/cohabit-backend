package com.cohabit.cohabitbackend.controller;

import com.cohabit.cohabitbackend.dto.QuestionnaireRequest;
import com.cohabit.cohabitbackend.dto.QuestionnaireResponseDto;
import com.cohabit.cohabitbackend.service.QuestionnaireService;
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
 * REST controller for authenticated users' questionnaire responses.
 */
@RestController
@RequestMapping("/questionnaire")
public class QuestionnaireController {

    private final QuestionnaireService questionnaireService;

    /**
     * Creates a questionnaire controller.
     *
     * @param questionnaireService questionnaire service
     */
    public QuestionnaireController(QuestionnaireService questionnaireService) {
        this.questionnaireService = questionnaireService;
    }

    /**
     * Creates the authenticated user's questionnaire response.
     *
     * @param request questionnaire payload
     * @param authentication current Spring Security authentication
     * @return created questionnaire response
     */
    @PostMapping
    public ResponseEntity<QuestionnaireResponseDto> createQuestionnaire(
            @Valid @RequestBody QuestionnaireRequest request,
            Authentication authentication
    ) {
        QuestionnaireResponseDto response = questionnaireService.createQuestionnaire(authentication.getName(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Retrieves the authenticated user's questionnaire response.
     *
     * @param authentication current Spring Security authentication
     * @return questionnaire response
     */
    @GetMapping("/me")
    public ResponseEntity<QuestionnaireResponseDto> getMyQuestionnaire(Authentication authentication) {
        QuestionnaireResponseDto response = questionnaireService.getMyQuestionnaire(authentication.getName());
        return ResponseEntity.ok(response);
    }

    /**
     * Updates the authenticated user's questionnaire response.
     *
     * @param request questionnaire payload
     * @param authentication current Spring Security authentication
     * @return updated questionnaire response
     */
    @PutMapping
    public ResponseEntity<QuestionnaireResponseDto> updateQuestionnaire(
            @Valid @RequestBody QuestionnaireRequest request,
            Authentication authentication
    ) {
        QuestionnaireResponseDto response = questionnaireService.updateQuestionnaire(authentication.getName(), request);
        return ResponseEntity.ok(response);
    }
}
