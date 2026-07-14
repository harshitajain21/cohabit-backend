package com.cohabit.cohabitbackend.repository;

import com.cohabit.cohabitbackend.model.QuestionnaireResponse;
import com.cohabit.cohabitbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

//Provides database access for questionnaire responses.

public interface QuestionnaireResponseRepository extends JpaRepository<QuestionnaireResponse, Long> {

    //Checks whether a user already submitted a questionnaire: returns true if so
    boolean existsByUser(User user);

    //Finds a questionnaire by user - returns the response
    Optional<QuestionnaireResponse> findByUser(User user);  //optional means it can return QuestionnaireResponse or it can return nothing

    //Finds questionnaires for a batch of users.
    List<QuestionnaireResponse> findAllByUserIn(Collection<User> users);
}
