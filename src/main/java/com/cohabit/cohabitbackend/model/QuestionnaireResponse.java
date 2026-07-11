package com.cohabit.cohabitbackend.model;

import com.cohabit.cohabitbackend.model.enums.Cleanliness;
import com.cohabit.cohabitbackend.model.enums.GuestPreference;
import com.cohabit.cohabitbackend.model.enums.LanguageImportance;
import com.cohabit.cohabitbackend.model.enums.LightPreference;
import com.cohabit.cohabitbackend.model.enums.Personality;
import com.cohabit.cohabitbackend.model.enums.PhoneVolume;
import com.cohabit.cohabitbackend.model.enums.SharingPreference;
import com.cohabit.cohabitbackend.model.enums.SleepSchedule;
import com.cohabit.cohabitbackend.model.enums.StudyLocation;
import com.cohabit.cohabitbackend.model.enums.SubstanceUse;
import jakarta.persistence.Column;
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

/**
 * Stores a user's roommate preference questionnaire response.
 */
@Entity
@Table(name = "questionnaire_responses")
public class QuestionnaireResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SleepSchedule sleepSchedule;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LightPreference sleepLightPreference;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PhoneVolume phoneVolume;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Cleanliness cleanliness;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StudyLocation studyLocation;

    @Column(nullable = false)
    private boolean needSilence;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GuestPreference guestPreference;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Personality personality;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SharingPreference sharingPreference;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LanguageImportance languageImportance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubstanceUse substanceUse;

    /**
     * Required by JPA.
     */
    public QuestionnaireResponse() {
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

    public SleepSchedule getSleepSchedule() {
        return sleepSchedule;
    }

    public void setSleepSchedule(SleepSchedule sleepSchedule) {
        this.sleepSchedule = sleepSchedule;
    }

    public LightPreference getSleepLightPreference() {
        return sleepLightPreference;
    }

    public void setSleepLightPreference(LightPreference sleepLightPreference) {
        this.sleepLightPreference = sleepLightPreference;
    }

    public PhoneVolume getPhoneVolume() {
        return phoneVolume;
    }

    public void setPhoneVolume(PhoneVolume phoneVolume) {
        this.phoneVolume = phoneVolume;
    }

    public Cleanliness getCleanliness() {
        return cleanliness;
    }

    public void setCleanliness(Cleanliness cleanliness) {
        this.cleanliness = cleanliness;
    }

    public StudyLocation getStudyLocation() {
        return studyLocation;
    }

    public void setStudyLocation(StudyLocation studyLocation) {
        this.studyLocation = studyLocation;
    }

    public boolean isNeedSilence() {
        return needSilence;
    }

    public void setNeedSilence(boolean needSilence) {
        this.needSilence = needSilence;
    }

    public GuestPreference getGuestPreference() {
        return guestPreference;
    }

    public void setGuestPreference(GuestPreference guestPreference) {
        this.guestPreference = guestPreference;
    }

    public Personality getPersonality() {
        return personality;
    }

    public void setPersonality(Personality personality) {
        this.personality = personality;
    }

    public SharingPreference getSharingPreference() {
        return sharingPreference;
    }

    public void setSharingPreference(SharingPreference sharingPreference) {
        this.sharingPreference = sharingPreference;
    }

    public LanguageImportance getLanguageImportance() {
        return languageImportance;
    }

    public void setLanguageImportance(LanguageImportance languageImportance) {
        this.languageImportance = languageImportance;
    }

    public SubstanceUse getSubstanceUse() {
        return substanceUse;
    }

    public void setSubstanceUse(SubstanceUse substanceUse) {
        this.substanceUse = substanceUse;
    }
}
