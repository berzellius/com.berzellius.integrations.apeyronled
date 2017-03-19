package com.berzellius.integrations.apeyronled.businesslogic.rules.validator;

import com.berzellius.integrations.apeyronled.businesslogic.rules.exceptions.ValidationException;
import org.springframework.stereotype.Service;

/**
 * Created by berz on 11.01.2017.
 */
@Service
public interface BusinessRulesValidator {
    public ValidationUtil getValidationUtil(Object object) throws ValidationException;

    void setIncomingCallValidationUtil(IncomingCallValidationUtil incomingCallValidationUtil);

    void setLeadFromSiteValidationUtil(LeadFromSiteValidationUtil leadFromSiteValidationUtil);

    boolean validate(Object object) throws ValidationException;
}
