package com.cohabit.cohabitbackend.mapper;

import com.cohabit.cohabitbackend.dto.QuestionnaireRequest;
import com.cohabit.cohabitbackend.dto.QuestionnaireResponseDto;
import com.cohabit.cohabitbackend.model.QuestionnaireResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

//Maps questionnaire request and response DTOs to questionnaire entities.

@Mapper(componentModel = "spring")
public interface QuestionnaireMapper {

    //Converts a questionnaire request into an entity.
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    //dont add id and user in the Questionnaire response table
    QuestionnaireResponse toEntity(QuestionnaireRequest request);

    //Converts a questionnaire entity into an API response.
    @Mapping(target = "userId", source = "user.id")  //Copy the value of user.id into the userId field.
    QuestionnaireResponseDto toDto(QuestionnaireResponse response);

    //Applies a request payload onto an existing questionnaire entity.
    //this is to update the questionnaire response, if someone changes their answers
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    void updateEntity(QuestionnaireRequest request, @MappingTarget QuestionnaireResponse response);
}
