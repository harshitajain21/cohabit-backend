package com.cohabit.cohabitbackend.util;

import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.regex.Pattern;

//Validates and normalizes institutional IIT email addresses accepted by Cohabit.

@Component   // it registers this class as a Spring Bean. Spring creates and manages its object, so it can be injected wherever needed.
public class IitEmailValidator {

    //Normalizes an email address for storage and comparison: return trimmed lowercase email
    public String normalize(String email) {
        return email == null ? null : email.trim().toLowerCase(Locale.ROOT);
    }
    //for ex: if "    abc@itbhU.ac.in " ...it will return "abc@itbhu.ac.in"

    private static final Pattern IIT_BHU_EMAIL = Pattern.compile("^[A-Za-z0-9._%+-]+@itbhu\\.ac\\.in$");

    //Checks whether an email belongs to the accepted IIT domain : return true or false
    public boolean isValid(String email) {
        return email != null && IIT_BHU_EMAIL.matcher(email).matches();
    }
}
