package com.cohabit.cohabitbackend.repository;

import com.cohabit.cohabitbackend.model.ContactInterest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactInterestRepository extends JpaRepository<ContactInterest, Long> {
    // Prevents spamming the database with duplicate requests
    boolean existsByRequesterIdAndTargetUserId(Long requesterId, Long targetUserId);
}