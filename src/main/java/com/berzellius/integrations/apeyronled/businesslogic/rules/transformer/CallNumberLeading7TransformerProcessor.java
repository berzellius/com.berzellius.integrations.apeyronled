package com.berzellius.integrations.apeyronled.businesslogic.rules.transformer;

import com.berzellius.integrations.apeyronled.businesslogic.rules.exceptions.TransformationException;
import org.springframework.util.Assert;

/**
 * Created by berz on 09.03.2017.
 */
public class CallNumberLeading7TransformerProcessor implements TransformerProcessor {
    @Override
    public String transform(String phone) throws TransformationException {
        Assert.notNull(phone);
        return "7".concat(phone);
    }
}
