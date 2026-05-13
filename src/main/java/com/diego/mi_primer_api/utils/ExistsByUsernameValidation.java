package com.diego.mi_primer_api.utils;

import com.diego.mi_primer_api.services.UserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExistsByUsernameValidation implements ConstraintValidator<ExistsByUsername, String> {

    @Autowired
    private UserService userService;

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {

        if(username==null || username.isEmpty()){
            return true;
        }
        return !userService.existsByUsername(username);
    }
}
