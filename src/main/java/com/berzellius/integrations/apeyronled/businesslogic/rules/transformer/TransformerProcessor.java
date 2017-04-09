package com.berzellius.integrations.apeyronled.businesslogic.rules.transformer;

import com.berzellius.integrations.apeyronled.businesslogic.rules.exceptions.TransformationException;

import java.util.HashMap;

/**
 * Created by berz on 09.03.2017.
 */
public interface TransformerProcessor {
    String transform(String input) throws TransformationException;
    <T> T transform(T input) throws TransformationException;
    void setParams(HashMap<String, Object> params);
}
