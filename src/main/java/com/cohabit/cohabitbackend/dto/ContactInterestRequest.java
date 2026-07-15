package com.cohabit.cohabitbackend.dto;

//contains userid, their interest request
public class ContactInterestRequest {
    private Long targetUserId;

    public ContactInterestRequest() {}

    public Long getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(Long targetUserId) {
        this.targetUserId = targetUserId;
    }
}