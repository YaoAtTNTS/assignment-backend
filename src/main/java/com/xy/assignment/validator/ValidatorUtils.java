package com.xy.assignment.validator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * @ Author: Xiong Yao
 * @ Date: Created at 11:29 PM 10/16/2022
 * @ Description: entity validator
 * @ Version: 1.0
 * @ Email: gongchen711@gmail.com
 */


public class ValidatorUtils {
    private static Validator validator;

    static {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    public static boolean validateEntity(Object object, Class<?>... groups)
            {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);
        if (!constraintViolations.isEmpty()) {
            ConstraintViolation<Object> constraint = (ConstraintViolation<Object>)constraintViolations.iterator().next();
            return false;
        }
        return true;
    }
}
