package com.cohabit.cohabitbackend.mapper;

import com.cohabit.cohabitbackend.dto.RegisterUserRequest;
import com.cohabit.cohabitbackend.dto.UserResponse;
import com.cohabit.cohabitbackend.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Maps user DTOs and entities without exposing persistence objects through the API layer.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Converts a registration request into a user entity.
     *
     * @param request validated registration payload
     * @return user entity populated with profile data
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "emailVerified", ignore = true)
    @Mapping(target = "questionnaireResponse", ignore = true)
    User toEntity(RegisterUserRequest request);

    /**
     * Converts a user entity into an API-safe response.
     *
     * @param user persisted user
     * @return user response DTO
     */
    UserResponse toResponse(User user);
}
