package com.berzellius.integrations.apeyronled.businesslogic.rules.transformer;

import com.berzellius.integrations.amocrmru.dto.api.amocrm.AmoCRMLead;
import com.berzellius.integrations.apeyronled.businesslogic.rules.exceptions.TransformationException;
import org.apache.tomcat.websocket.Transformation;
import org.junit.Assert;

import java.util.HashMap;

/**
 * Created by berz on 09.04.2017.
 */
public class AmoCRMLeadPipelineTransformerProcessor implements TransformerProcessor {
    protected HashMap<String, Object> params;

    @Override
    public String transform(String input) throws TransformationException {
        throw new TransformationException("string transformation not allowed for PipelineTransformer");
    }

    @Override
    public <T> T transform(T input) throws TransformationException {
        Assert.assertTrue("input object must be AmoCRMLead!", input instanceof AmoCRMLead);
        AmoCRMLead lead = (AmoCRMLead) input;

        return null;
    }

    @Override
    public void setParams(HashMap<String, Object> params) {
        this.params = params;
    }

    public HashMap<String, Object> getParams() {
        return params;
    }
}
