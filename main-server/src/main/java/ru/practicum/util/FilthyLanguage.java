package ru.practicum.util;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = FilthyLanguageValidator.class)
@Documented
public @interface FilthyLanguage {

    String message() default "{CapitalLetter.invalid}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}
