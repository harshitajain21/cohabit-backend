package com.cohabit.cohabitbackend.mapper;

import com.cohabit.cohabitbackend.dto.RoommatePreferenceRequest;
import com.cohabit.cohabitbackend.dto.RoommatePreferenceResponse;
import com.cohabit.cohabitbackend.model.RoommatePreference;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Maps roommate preference DTOs and entities.
 */
@Mapper(componentModel = "spring")
public interface RoommatePreferenceMapper {

    /**
     * Converts a preference request into an entity.
     *
     * @param request validated preference payload
     * @return roommate preference entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    RoommatePreference toEntity(RoommatePreferenceRequest request);

    /**
     * Converts a roommate preference entity into an API response.
     *
     * @param preference roommate preference entity
     * @return roommate preference response
     */
    @Mapping(target = "userId", source = "user.id")
    RoommatePreferenceResponse toResponse(RoommatePreference preference);

    /**
     * Applies a request payload to an existing preference entity.
     *
     * @param request validated preference payload
     * @param preference existing preference entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    void updateEntity(RoommatePreferenceRequest request, @MappingTarget RoommatePreference preference);
}
