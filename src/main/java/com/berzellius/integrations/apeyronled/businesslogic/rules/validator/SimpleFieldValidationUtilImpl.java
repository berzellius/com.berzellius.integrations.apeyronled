package com.berzellius.integrations.apeyronled.businesslogic.rules.validator;


import com.berzellius.integrations.apeyronled.businesslogic.rules.exceptions.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Arrays;

/**
 * Created by berz on 11.01.2017.
 */
@Service
public class SimpleFieldValidationUtilImpl implements SimpleFieldsValidationUtil {
    private static final Logger log = LoggerFactory.getLogger(SimpleFieldsValidationUtil.class);

    public final static int callNumberMinLength = 3;

    @Override
    public boolean validate(String value, ValidationType validationType) throws ValidationException {
        Assert.notNull(value);
        Assert.notNull(validationType);

        switch (validationType){
            case CALL_NUMBER:
                if(value.length() < callNumberMinLength){
                    log.error("value of type CALL_NUMBER:" + value + " has not pass validation because of its length is less than " + callNumberMinLength);
                    return false;
                }

                if(!value.matches("^[\\d]+$")){
                    log.error("value of type CALL_NUMBER:" + value + " has not pass validation because it contain not only digits");
                    return false;
                }

                return true;

            case EMAIL:
                return true;
            case NONRESTRICTED_MANAGER_MOBILE_PHONE:
                String[] restricted = {"9265206172", "9265206181"};

                return (Arrays.binarySearch(restricted, value) >= 0);
        }

        throw new ValidationException("parameters set to this function is wrong or it is unexpected error, you better call Saul..");
    }
}
