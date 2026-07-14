package com.cohabit.cohabitbackend.repository;

import com.cohabit.cohabitbackend.model.RoommatePreference;
import com.cohabit.cohabitbackend.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

//Provides database access for roommate preferences.

public interface RoommatePreferenceRepository extends JpaRepository<RoommatePreference, Long> {

    //Checks whether a user already has roommate preferences.
    boolean existsByUser(User user);

    //Finds roommate preferences by owner : return preferences when present
    @EntityGraph(attributePaths = {"topPriorities", "dealBreakers"}) //optimization: While fetching RoommatePreference, also fetch topPriorities and dealBreakers immediately."
    Optional<RoommatePreference> findByUser(User user);

    //Finds roommate preferences for a batch of users.: return roommate preference for the users
    @EntityGraph(attributePaths = {"topPriorities", "dealBreakers"})
    List<RoommatePreference> findAllByUserIn(Collection<User> users);
}
