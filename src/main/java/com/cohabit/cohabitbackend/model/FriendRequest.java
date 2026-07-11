package com.cohabit.cohabitbackend.model;

import com.cohabit.cohabitbackend.model.enums.FriendRequestStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.Instant;

/**
 * Represents a friend request between two Cohabit users.
 */
@Entity
@Table(name = "friend_requests")
public class FriendRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipient;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FriendRequestStatus status = FriendRequestStatus.PENDING;

    @Column(nullable = false, updatable = false)
    private Instant requestedAt = Instant.now();

    private Instant respondedAt;

    private Integer overallScore;

    @Column(columnDefinition = "TEXT")
    private String matchingTraits;

    @Column(columnDefinition = "TEXT")
    private String potentialConflicts;

    @Column(columnDefinition = "TEXT")
    private String dealBreakerConflicts;

    @Column(columnDefinition = "TEXT")
    private String reasoning;

    /**
     * Required by JPA.
     */
    public FriendRequest() {
    }

    public Long getId() {
        return id;
    }

    public User getRequester() {
        return requester;
    }

    public void setRequester(User requester) {
        this.requester = requester;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public FriendRequestStatus getStatus() {
        return status;
    }

    public void setStatus(FriendRequestStatus status) {
        this.status = status;
    }

    public Instant getRequestedAt() {
        return requestedAt;
    }

    public Instant getRespondedAt() {
        return respondedAt;
    }

    public void setRespondedAt(Instant respondedAt) {
        this.respondedAt = respondedAt;
    }

    public Integer getOverallScore() {
        return overallScore;
    }

    public void setOverallScore(Integer overallScore) {
        this.overallScore = overallScore;
    }

    public String getMatchingTraits() {
        return matchingTraits;
    }

    public void setMatchingTraits(String matchingTraits) {
        this.matchingTraits = matchingTraits;
    }

    public String getPotentialConflicts() {
        return potentialConflicts;
    }

    public void setPotentialConflicts(String potentialConflicts) {
        this.potentialConflicts = potentialConflicts;
    }

    public String getDealBreakerConflicts() {
        return dealBreakerConflicts;
    }

    public void setDealBreakerConflicts(String dealBreakerConflicts) {
        this.dealBreakerConflicts = dealBreakerConflicts;
    }

    public String getReasoning() {
        return reasoning;
    }

    public void setReasoning(String reasoning) {
        this.reasoning = reasoning;
    }
}
