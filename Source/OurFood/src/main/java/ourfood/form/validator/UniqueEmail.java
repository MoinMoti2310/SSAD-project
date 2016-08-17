package ourfood.form.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import ourfood.service.UserService;

public class UniqueEmail implements ConstraintValidator<ourfood.annotation.UniqueEmail, String> {

    @Autowired
    private UserService userService;

    @Override
    public void initialize(ourfood.annotation.UniqueEmail constraintAnnotation) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean isValid = this.userService.isEmailUnique(value);
        return !isValid;
    }

}
