package com.cohabit.cohabitbackend.mapper;

import com.cohabit.cohabitbackend.dto.RegisterUserRequest;
import com.cohabit.cohabitbackend.dto.UserResponse;
import com.cohabit.cohabitbackend.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

//Maps user DTOs and entities without exposing persistence objects through the API layer.

@Mapper(componentModel = "spring")
public interface UserMapper {

    //Converts a registration request into a user entity.
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "emailVerified", ignore = true)
    @Mapping(target = "questionnaireResponse", ignore = true)
    //all these tell don't copy id, password, emailverified, questionnairersponse in user entity
    User toEntity(RegisterUserRequest request);

    //Converts a user entity into an API-safe response.
    UserResponse toResponse(User user);
}
