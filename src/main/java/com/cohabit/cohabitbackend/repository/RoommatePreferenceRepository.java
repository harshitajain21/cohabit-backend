package com.cohabit.cohabitbackend.repository;

import com.cohabit.cohabitbackend.model.RoommatePreference;
import com.cohabit.cohabitbackend.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Provides database access for roommate preferences.
 */
public interface RoommatePreferenceRepository extends JpaRepository<RoommatePreference, Long> {

    /**
     * Checks whether a user already has roommate preferences.
     *
     * @param user preference owner
     * @return true when preferences exist
     */
    boolean existsByUser(User user);

    /**
     * Finds roommate preferences by owner.
     *
     * @param user preference owner
     * @return preferences when present
     */
    @EntityGraph(attributePaths = {"topPriorities", "dealBreakers"})
    Optional<RoommatePreference> findByUser(User user);

    /**
     * Finds roommate preferences for a batch of users.
     *
     * @param users preference owners
     * @return roommate preferences for supplied users
     */
    @EntityGraph(attributePaths = {"topPriorities", "dealBreakers"})
    List<RoommatePreference> findAllByUserIn(Collection<User> users);
}
