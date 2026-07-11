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

/**
 * Maps friend workflow entities into API-safe DTOs.
 */
@Mapper(componentModel = "spring")
public interface FriendRequestMapper {

    /**
     * Converts a user into a friend search result.
     *
     * @param user searched user
     * @return search response DTO
     */
    FriendSearchResponse toSearchResponse(User user);

    /**
     * Converts a friend request into an API response.
     *
     * @param friendRequest friend request entity
     * @return friend request response DTO
     */
    @Mapping(target = "requesterId", source = "requester.id")
    @Mapping(target = "requesterName", source = "requester.name")
    @Mapping(target = "requesterIitEmail", source = "requester.iitEmail")
    @Mapping(target = "recipientId", source = "recipient.id")
    @Mapping(target = "recipientName", source = "recipient.name")
    @Mapping(target = "recipientIitEmail", source = "recipient.iitEmail")
    @Mapping(target = "compatibilityReport", expression = "java(toCompatibilityReport(friendRequest))")
    FriendRequestResponse toResponse(FriendRequest friendRequest);

    /**
     * Converts persisted report columns into a compatibility report DTO.
     *
     * @param friendRequest accepted friend request
     * @return compatibility report when available
     */
    default CompatibilityReportResponse toCompatibilityReport(FriendRequest friendRequest) {
        if (friendRequest.getOverallScore() == null) {
            return null;
        }
        return new CompatibilityReportResponse(
                friendRequest.getOverallScore(),
                splitReportItems(friendRequest.getMatchingTraits()),
                splitReportItems(friendRequest.getPotentialConflicts()),
                splitReportItems(friendRequest.getDealBreakerConflicts()),
                splitReportItems(friendRequest.getReasoning())
        );
    }

    /**
     * Splits newline-delimited report text into API list items.
     *
     * @param value persisted report text
     * @return report items
     */
    default List<String> splitReportItems(String value) {
        if (value == null || value.isBlank()) {
            return List.of();
        }
        return Arrays.stream(value.split("\\R"))
                .filter(item -> !item.isBlank())
                .toList();
    }
}
