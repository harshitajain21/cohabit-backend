package com.cohabit.cohabitbackend.repository;

import com.cohabit.cohabitbackend.model.User;
import com.cohabit.cohabitbackend.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Provides persistence access for email verification tokens.
 */
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    /**
     * Finds a verification token by its opaque token value.
     *
     * @param token token value received from the verification link
     * @return matching token when present
     */
    Optional<VerificationToken> findByToken(String token);

    /**
     * Removes all verification tokens owned by a user.
     *
     * @param user token owner
     */
    void deleteByUser(User user);
}
