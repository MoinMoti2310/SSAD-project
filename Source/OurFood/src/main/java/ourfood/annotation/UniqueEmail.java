package ourfood.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Custom Annotation to validate if the value is a unique email or not
 * 
 \* @author raghu.mulukoju
 *
 */
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ourfood.form.validator.UniqueEmail.class)
public @interface UniqueEmail {
    String message() default "Email not unique";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
