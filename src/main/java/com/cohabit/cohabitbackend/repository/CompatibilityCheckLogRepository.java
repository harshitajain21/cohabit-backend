package com.cohabit.cohabitbackend.repository;

import com.cohabit.cohabitbackend.model.CompatibilityCheckLog;
import com.cohabit.cohabitbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Provides database access for compatibility check logs.

public interface CompatibilityCheckLogRepository extends JpaRepository<CompatibilityCheckLog, Long> {

    //Finds every compatibility check a user has initiated, most recent first.
    List<CompatibilityCheckLog> findByRequesterOrderByCheckedAtDesc(User requester);
}