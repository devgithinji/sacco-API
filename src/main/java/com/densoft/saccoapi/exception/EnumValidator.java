package com.densoft.saccoapi.exception;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EnumValidatorConstraint.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@NotNull
public @interface EnumValidator {

    Class<? extends Enum<?>> type();
    String message() default "must be any of enum {values}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
