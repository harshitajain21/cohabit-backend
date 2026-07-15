package com.cohabit.cohabitbackend.model;

import com.cohabit.cohabitbackend.model.enums.ContactInterestStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;

//contains id, requester id, target user id, status, created at

@Entity
@Table(name = "contact_interests")
public class ContactInterest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "requester_id", nullable = false)
    private Long requesterId;

    @Column(name = "target_user_id", nullable = false)
    private Long targetUserId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContactInterestStatus status = ContactInterestStatus.PENDING;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public ContactInterest() {}

    public ContactInterest(Long requesterId, Long targetUserId) {
        this.requesterId = requesterId;
        this.targetUserId = targetUserId;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public Long getRequesterId() { return requesterId; }
    public void setRequesterId(Long requesterId) { this.requesterId = requesterId; }
    public Long getTargetUserId() { return targetUserId; }
    public void setTargetUserId(Long targetUserId) { this.targetUserId = targetUserId; }
    public ContactInterestStatus getStatus() { return status; }
    public void setStatus(ContactInterestStatus status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}