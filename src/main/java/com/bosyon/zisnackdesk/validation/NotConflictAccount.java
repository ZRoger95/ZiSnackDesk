package com.bosyon.zisnackdesk.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = NotConflictAccountValidator.class)
public @interface NotConflictAccount {

    String message() default "账号已存在";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
