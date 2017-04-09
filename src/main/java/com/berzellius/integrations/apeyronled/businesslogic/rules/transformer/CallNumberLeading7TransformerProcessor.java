package com.berzellius.integrations.apeyronled.businesslogic.rules.transformer;

import com.berzellius.integrations.apeyronled.businesslogic.rules.exceptions.TransformationException;
import org.springframework.util.Assert;

import java.util.HashMap;

/**
 * Created by berz on 09.03.2017.
 */
public class CallNumberLeading7TransformerProcessor implements TransformerProcessor {
    protected HashMap<String, Object> params;

    @Override
    public String transform(String phone) throws TransformationException {
        Assert.notNull(phone);
        return "7".concat(phone);
    }

    @Override
    public <T> T transform(T input) throws TransformationException {
        throw new TransformationException(" objects not allowed for CallNumberLeading7Transformer");
    }

    public HashMap<String, Object> getParams() {
        return params;
    }

    @Override
    public void setParams(HashMap<String, Object> params) {
        this.params = params;
    }
}
