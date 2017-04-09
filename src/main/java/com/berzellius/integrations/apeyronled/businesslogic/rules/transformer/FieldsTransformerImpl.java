package com.berzellius.integrations.apeyronled.businesslogic.rules.transformer;

import com.berzellius.integrations.apeyronled.businesslogic.rules.exceptions.TransformationException;
import org.springframework.util.Assert;

import java.util.HashMap;

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

    /**
     * Метод для трансформации строковых значений
     * @param input - входная строка
     * @param transformation - тип трансфораации
     * @return String
     * @throws TransformationException - вызывается при ошибке обработки
     */
    @Override
    public String transform(String input, Transformation transformation) throws TransformationException{
        Assert.notNull(input);
        Assert.notNull(transformation);

        TransformerProcessor transformerProcessor = this.getTransformerProcessorByTransformation(transformation);
        return  transformerProcessor.transform(input);
    }


    /**
     * @param input - объект трансформации
     * @param transformation - тип трансформации
     * @param <T> - тип объекта трансформации
     * @return T - результат, преобразованный объект
     */
    @Override
    public <T> T transform(T input, Transformation transformation){
        return transform(input, transformation, null);
    }

    /**
     * @param input - объект трансформации
     * @param transformation - тип трансформации
     * @param params - дополнительные параметры
     * @param <T> - тип объекта трансформации
     * @return T - результат, преобразованный объект
     */
    @Override
    public <T> T transform(T input, Transformation transformation, HashMap<String, Object> params){
        Assert.notNull(input);
        Assert.notNull(transformation);

        TransformerProcessor transformerProcessor = this.getTransformerProcessorByTransformation(transformation);
        transformerProcessor.setParams(params);
        return  transformerProcessor.transform(input);
    }
}
