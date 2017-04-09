package com.berzellius.integrations.apeyronled.businesslogic.rules.transformer;

import com.berzellius.integrations.apeyronled.businesslogic.rules.exceptions.TransformationException;

import java.util.HashMap;

/**
 * Created by berz on 09.03.2017.
 */
public class CallNumberCommonTransformerProcessor implements TransformerProcessor {
    protected HashMap<String, Object> params;

    @Override
    public String transform(String phone) throws TransformationException {
        if(phone.matches("^[\\d]+$")){
            if(phone.length() >= 11 &&
                    (
                            phone.charAt(0) == '7' ||
                                    phone.charAt(0) == '8'
                    )
                    ){
                return phone.substring(1);
            }
            return phone;
        }
        else{
            String parsed = "";
            for (Integer i = 0; i < phone.length(); i++){
                String ch = String.valueOf(phone.charAt(i));
                if(ch.matches("\\d")){
                    parsed = parsed.concat(ch);
                }
            }

            if(parsed.length() == 0){
                return "";
            }

            return transform(parsed);
        }
    }

    @Override
    public <T> T transform(T input) throws TransformationException {
        throw new TransformationException(" objects not allowed for CallNumberCommonTransformer");
    }

    public HashMap<String, Object> getParams() {
        return params;
    }

    @Override
    public void setParams(HashMap<String, Object> params) {
        this.params = params;
    }
}
