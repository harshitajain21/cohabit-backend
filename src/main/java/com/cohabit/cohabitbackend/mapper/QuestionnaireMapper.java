package com.cohabit.cohabitbackend.mapper;

import com.cohabit.cohabitbackend.dto.QuestionnaireRequest;
import com.cohabit.cohabitbackend.dto.QuestionnaireResponseDto;
import com.cohabit.cohabitbackend.model.QuestionnaireResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Maps questionnaire request and response DTOs to questionnaire entities.
 */
@Mapper(componentModel = "spring")
public interface QuestionnaireMapper {

    /**
     * Converts a questionnaire request into an entity.
     *
     * @param request validated questionnaire payload
     * @return questionnaire entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    QuestionnaireResponse toEntity(QuestionnaireRequest request);

    /**
     * Converts a questionnaire entity into an API response.
     *
     * @param response questionnaire entity
     * @return questionnaire response DTO
     */
    @Mapping(target = "userId", source = "user.id")
    QuestionnaireResponseDto toDto(QuestionnaireResponse response);

    /**
     * Applies a request payload onto an existing questionnaire entity.
     *
     * @param request validated questionnaire payload
     * @param response existing questionnaire entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    void updateEntity(QuestionnaireRequest request, @MappingTarget QuestionnaireResponse response);
}
