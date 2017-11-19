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
        EMAIL,
        /**
         * Эта валидация предназначена для отсева мобильных номеров менеджеров, при звонках
         * на которые не должно делаться ничего - не должен быть создан контакт, не должны обрабаться события и т.д.
         */
        NONRESTRICTED_MANAGER_MOBILE_PHONE
    }

    public boolean validate(String value, ValidationType validationType) throws ValidationException;
}
