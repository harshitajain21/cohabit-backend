package com.cohabit.cohabitbackend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.Instant;

/**
 * Records a single compatibility check performed via
 * {@code POST /friends/check-compatibility}, independent of whether a
 * friend request was ever sent. This is what answers "who checked
 * compatibility with whom, and when".
 */
@Entity
@Table(
        name = "compatibility_check_logs",
        indexes = {
                @Index(name = "idx_compat_log_requester", columnList = "requester_id"),
                @Index(name = "idx_compat_log_recipient", columnList = "recipient_id")
        }
)
public class CompatibilityCheckLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipient;

    @Column(nullable = false)
    private Integer overallScore;

    @Column(nullable = false, updatable = false)
    private Instant checkedAt = Instant.now();

    //-----------

    public CompatibilityCheckLog() {
    }

    //------------

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

    public Integer getOverallScore() {
        return overallScore;
    }

    public void setOverallScore(Integer overallScore) {
        this.overallScore = overallScore;
    }

    public Instant getCheckedAt() {
        return checkedAt;
    }

    public void setCheckedAt(Instant checkedAt) {
        this.checkedAt = checkedAt;
    }
}