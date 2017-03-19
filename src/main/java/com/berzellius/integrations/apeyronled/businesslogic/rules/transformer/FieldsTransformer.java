package com.berzellius.integrations.apeyronled.businesslogic.rules.transformer;

import org.springframework.stereotype.Service;

/**
 * Created by berz on 09.03.2017.
 */
@Service
public interface FieldsTransformer {
    public static enum Transformation{
        CALL_NUMBER_LEADING_7, CALL_NUMBER_COMMON
    }

    public String transform(String input, Transformation transformation);
}
