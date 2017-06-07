package com.berzellius.integrations.apeyronled.businesslogic.rules.transformer;

import com.berzellius.integrations.amocrmru.dto.api.amocrm.AmoCRMLead;
import com.berzellius.integrations.apeyronled.businesslogic.rules.exceptions.TransformationException;
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

        if(!this.getParams().containsKey("virtual_number"))
            return (T) lead;

        if(this.getParams().get("virtual_number").equals("74959892629")){
            // Сетевой отдел
            lead.setPipeline_id(535984l);
            // ответственный - Антон Филатов
            lead.setResponsible_user_id(1283781l);
        }

        if(this.getParams().get("virtual_number").equals("74951628555")){
            //Входящие заявки
            lead.setPipeline_id(446961l);
            lead.tag(1l, "каталожный");
        }

        if(this.getParams().get("virtual_number").equals("78007078776")){
            // Входящие заявки
            lead.setPipeline_id(446961l);
            lead.tag(1l, "LP_main");
        }

        if(
                (
                        this.getParams().containsKey("search_engine") &&
                                this.getParams().get("search_engine").equals("google")
                    ) ||
                        (
                                this.getParams().containsKey("campaign_id") &&
                                        this.getParams().get("campaign_id").equals(78283l)
                                ) ||
                        (
                                this.getParams().containsKey("source") &&
                                        ((String) this.getParams().get("source")).contains("oogle")
                                )
                ){
            // google
            lead.setPipeline_id(446961l);
            lead.tag(1l, "GA");
        }

        if(
                (
                        this.getParams().containsKey("search_engine") &&
                                this.getParams().get("search_engine").equals("yandex")
                ) ||
                        (
                                this.getParams().containsKey("campaign_id") &&
                                        this.getParams().get("campaign_id").equals(78282l)
                        ) ||
                        (
                                this.getParams().containsKey("source") &&
                                        ((String) this.getParams().get("source")).contains("ндекс")
                        )
                ){
            // google
            lead.setPipeline_id(446961l);
            lead.tag(1l, "Я.Д.");
        }

        if(this.getParams().containsKey("search_engine")){
            if(this.getParams().get("search_engine").equals("google")){
                // google
                lead.setPipeline_id(446961l);
            }

            if(this.getParams().get("search_engine").equals("yandex")){
                // google
                lead.setPipeline_id(446961l);
            }
        }

        return (T) lead;
    }

    @Override
    public void setParams(HashMap<String, Object> params) {
        this.params = params;
    }

    public HashMap<String, Object> getParams() {
        return params;
    }
}
