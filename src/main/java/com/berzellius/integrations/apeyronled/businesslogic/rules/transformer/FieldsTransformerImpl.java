package com.berzellius.integrations.apeyronled.businesslogic.rules.transformer;

import com.berzellius.integrations.apeyronled.businesslogic.rules.exceptions.TransformationException;
import org.springframework.util.Assert;

/**
 * Created by berz on 09.03.2017.
 */
public class FieldsTransformerImpl implements FieldsTransformer {

    public TransformerProcessor getTransformerProcessorByTransformation(Transformation transformation) throws TransformationException{
        switch (transformation){
            case CALL_NUMBER_COMMON:
                return new CallNumberCommonTransformerProcessor();
            case CALL_NUMBER_LEADING_7:
                return new CallNumberLeading7TransformerProcessor();
        }

        throw new TransformationException("cant get proper TransformerProcessor!");
    }

    @Override
    public String transform(String input, Transformation transformation) throws TransformationException{
        Assert.notNull(input);
        Assert.notNull(transformation);

        TransformerProcessor transformerProcessor = this.getTransformerProcessorByTransformation(transformation);
        return  transformerProcessor.transform(input);
    }
}
