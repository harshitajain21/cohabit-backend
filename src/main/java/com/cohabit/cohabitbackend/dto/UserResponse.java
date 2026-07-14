package com.cohabit.cohabitbackend.dto;

import com.cohabit.cohabitbackend.model.enums.Gender;

public record UserResponse(

        Long id,
        String name,
        String iitEmail,
        String branch,
        Integer year,
        Gender gender

) {}