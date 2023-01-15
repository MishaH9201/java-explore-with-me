package ru.practicum.util;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FilthyLanguageValidator implements ConstraintValidator<FilthyLanguage,String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value.toLowerCase().contains("хуй") || value.toLowerCase().contains("бляд")
                || value.toLowerCase().contains("муда") || value.toLowerCase().contains("пизд")) {
            return false;
        } else {
            return true;
        }
    }
}
