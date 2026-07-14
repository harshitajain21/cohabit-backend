package com.cohabit.cohabitbackend.mapper;

import com.cohabit.cohabitbackend.dto.CompatibilityReportResponse;
import com.cohabit.cohabitbackend.dto.FriendRequestResponse;
import com.cohabit.cohabitbackend.dto.FriendSearchResponse;
import com.cohabit.cohabitbackend.model.FriendRequest;
import com.cohabit.cohabitbackend.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Arrays;
import java.util.List;
import com.cohabit.cohabitbackend.dto.CompatibilityCheckLogResponse;
import com.cohabit.cohabitbackend.model.CompatibilityCheckLog;

// Maps friend workflow entities into API-safe DTOs.

@Mapper(componentModel = "spring")
public interface FriendRequestMapper {

    //Converts a user into a friend search result.
    FriendSearchResponse toSearchResponse(User user);

    //Converts a friend request into an API response.
    @Mapping(target = "requesterId", source = "requester.id")
    @Mapping(target = "requesterName", source = "requester.name")
    @Mapping(target = "requesterIitEmail", source = "requester.iitEmail")
    @Mapping(target = "recipientId", source = "recipient.id")
    @Mapping(target = "recipientName", source = "recipient.name")
    @Mapping(target = "recipientIitEmail", source = "recipient.iitEmail")
    @Mapping(target = "compatibilityReport", expression = "java(toCompatibilityReport(friendRequest))")
    FriendRequestResponse toResponse(FriendRequest friendRequest);

    //Converts persisted report columns into a compatibility report DTO.
    default CompatibilityReportResponse toCompatibilityReport(FriendRequest friendRequest) {
        if (friendRequest.getOverallScore() == null) {
            return null;
        }
        return new CompatibilityReportResponse(
                friendRequest.getOverallScore(),
                compatibilityBand(friendRequest.getOverallScore()),
                splitReportItems(friendRequest.getMatchingTraits()),
                splitReportItems(friendRequest.getPotentialConflicts()),
                splitReportItems(friendRequest.getDealBreakerConflicts()),
                splitReportItems(friendRequest.getReasoning())
        );
    }

    default String compatibilityBand(int score) {
        if (score >= 85) return "Excellent Match";
        if (score >= 70) return "Good Match";
        if (score >= 50) return "Moderate Match";
        return "Low Compatibility";
    }

    @Mapping(target = "recipientId", source = "recipient.id")
    @Mapping(target = "recipientName", source = "recipient.name")
    @Mapping(target = "recipientIitEmail", source = "recipient.iitEmail")
    CompatibilityCheckLogResponse toCheckLogResponse(CompatibilityCheckLog log);

    List<CompatibilityCheckLogResponse> toCheckLogResponses(List<CompatibilityCheckLog> logs);

    //splits newline-delimited report text into API list items.
    default List<String> splitReportItems(String value) {
        if (value == null || value.isBlank()) {
            return List.of();
        }
        return Arrays.stream(value.split("\\R"))
                .filter(item -> !item.isBlank())
                .toList();
    }
}
