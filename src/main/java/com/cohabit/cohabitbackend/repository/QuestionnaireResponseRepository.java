package com.cohabit.cohabitbackend.repository;

import com.cohabit.cohabitbackend.model.QuestionnaireResponse;
import com.cohabit.cohabitbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Provides database access for questionnaire responses.
 */
public interface QuestionnaireResponseRepository extends JpaRepository<QuestionnaireResponse, Long> {

    /**
     * Checks whether a user already submitted a questionnaire.
     *
     * @param user questionnaire owner
     * @return true when a questionnaire exists for the user
     */
    boolean existsByUser(User user);

    /**
     * Finds a questionnaire by owner.
     *
     * @param user questionnaire owner
     * @return questionnaire response when present
     */
    Optional<QuestionnaireResponse> findByUser(User user);

    /**
     * Finds questionnaires for a batch of users.
     *
     * @param users questionnaire owners
     * @return questionnaire responses for supplied users
     */
    List<QuestionnaireResponse> findAllByUserIn(Collection<User> users);
}
