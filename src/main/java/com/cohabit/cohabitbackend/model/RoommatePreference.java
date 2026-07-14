package com.cohabit.cohabitbackend.model;

import com.cohabit.cohabitbackend.model.enums.PreferenceCriterion;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.util.LinkedHashSet;
import java.util.Set;

//Stores a user's roommate matching preferences.

//stores id, user, top priorites, dealbreakers, matching enabled or not

@Entity
@Table(name = "roommate_preferences")
public class RoommatePreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @ElementCollection(targetClass = PreferenceCriterion.class)
    @CollectionTable(name = "roommate_preference_top_priorities", joinColumns = @JoinColumn(name = "preference_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "criterion", nullable = false)
    private Set<PreferenceCriterion> topPriorities = new LinkedHashSet<>();

    @ElementCollection(targetClass = PreferenceCriterion.class)
    @CollectionTable(name = "roommate_preference_deal_breakers", joinColumns = @JoinColumn(name = "preference_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "criterion", nullable = false)
    private Set<PreferenceCriterion> dealBreakers = new LinkedHashSet<>();

    @Column(nullable = false)
    private boolean matchingEnabled = true;

    /**
     * Required by JPA.
     */
    public RoommatePreference() {
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<PreferenceCriterion> getTopPriorities() {
        return topPriorities;
    }

    public void setTopPriorities(Set<PreferenceCriterion> topPriorities) {
        this.topPriorities = topPriorities;
    }

    public Set<PreferenceCriterion> getDealBreakers() {
        return dealBreakers;
    }

    public void setDealBreakers(Set<PreferenceCriterion> dealBreakers) {
        this.dealBreakers = dealBreakers;
    }

    public boolean isMatchingEnabled() {
        return matchingEnabled;
    }

    public void setMatchingEnabled(boolean matchingEnabled) {
        this.matchingEnabled = matchingEnabled;
    }
}
