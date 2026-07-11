package com.cohabit.cohabitbackend.util;

import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Validates and normalizes institutional IIT email addresses accepted by Cohabit.
 */
@Component
public class IitEmailValidator {

    private static final Pattern IIT_BHU_EMAIL = Pattern.compile("^[A-Za-z0-9._%+-]+@itbhu\\.ac\\.in$");

    /**
     * Normalizes an email address for storage and comparison.
     *
     * @param email email supplied by a user
     * @return trimmed lower-case email
     */
    public String normalize(String email) {
        return email == null ? null : email.trim().toLowerCase(Locale.ROOT);
    }

    /**
     * Checks whether an email belongs to the accepted IIT domain.
     *
     * @param email email supplied by a user
     * @return true when the email belongs to IIT BHU
     */
    public boolean isValid(String email) {
        return email != null && IIT_BHU_EMAIL.matcher(email).matches();
    }
}
