package com.berzellius.integrations.apeyronled.businesslogic.rules.validator;


import com.berzellius.integrations.apeyronled.businesslogic.rules.exceptions.ValidationException;
import com.berzellius.integrations.apeyronled.dmodel.LeadFromSite;
import com.berzellius.integrations.apeyronled.dmodel.TrackedCall;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Created by berz on 11.01.2017.
 */
@Service
public class BusinessRulesValidatorImpl implements BusinessRulesValidator {

    @Autowired
    private IncomingCallValidationUtil incomingCallValidationUtil;

    @Autowired
    private LeadFromSiteValidationUtil leadFromSiteValidationUtil;

    @Override
    public ValidationUtil getValidationUtil(Object object) throws ValidationException {

        Assert.notNull(object);

        if(object instanceof TrackedCall){
            return this.getIncomingCallValidationUtil();
        }

        if(object instanceof LeadFromSite){
            return this.getLeadFromSiteValidationUtil();
        }

        throw new ValidationException("cannot get implementation of the ValidationUtil for object of class " + object.getClass().getName());
    }

    public IncomingCallValidationUtil getIncomingCallValidationUtil() {
        return incomingCallValidationUtil;
    }

    @Override
    public void setIncomingCallValidationUtil(IncomingCallValidationUtil incomingCallValidationUtil) {
        this.incomingCallValidationUtil = incomingCallValidationUtil;
    }

    public LeadFromSiteValidationUtil getLeadFromSiteValidationUtil() {
        return leadFromSiteValidationUtil;
    }

    @Override
    public void setLeadFromSiteValidationUtil(LeadFromSiteValidationUtil leadFromSiteValidationUtil) {
        this.leadFromSiteValidationUtil = leadFromSiteValidationUtil;
    }

    @Override
    public boolean validate(Object object) throws ValidationException {
        return this.getValidationUtil(object).validate(object);
    }
}
