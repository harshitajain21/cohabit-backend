package com.cohabit.cohabitbackend.exception;

import java.util.Map;

public record ErrorResponse(

        String errorCode,
        String message,
        Map<String, String> errors

) {}