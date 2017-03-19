package com.berzellius.integrations.apeyronled.businesslogic.rules.validator;

import com.berzellius.integrations.apeyronled.businesslogic.rules.exceptions.ValidationException;
import org.springframework.stereotype.Service;

/**
 * Created by berz on 11.01.2017.
 */
@Service
public interface SimpleFieldsValidationUtil{
    public enum ValidationType{
        CALL_NUMBER,
        EMAIL
    }

    public boolean validate(String value, ValidationType validationType) throws ValidationException;
}
