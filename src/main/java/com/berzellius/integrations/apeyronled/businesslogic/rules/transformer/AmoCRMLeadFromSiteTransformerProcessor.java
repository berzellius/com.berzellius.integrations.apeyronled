package com.berzellius.integrations.apeyronled.businesslogic.rules.transformer;

import com.berzellius.integrations.amocrmru.dto.api.amocrm.AmoCRMLead;
import com.berzellius.integrations.apeyronled.businesslogic.rules.exceptions.TransformationException;
import com.berzellius.integrations.apeyronled.dmodel.Site;
import org.junit.Assert;

import java.util.HashMap;

/**
 * Created by berz on 07.06.2017.
 */
public class AmoCRMLeadFromSiteTransformerProcessor implements TransformerProcessor {

    protected HashMap<String, Object> params;

    @Override
    public String transform(String input) throws TransformationException {
        throw new TransformationException("string transformation not allowed for LeadFromSiteTransformer");
    }

    @Override
    public <T> T transform(T input) throws TransformationException {
        Assert.assertTrue("input object must be AmoCRMLead!", input instanceof AmoCRMLead);
        AmoCRMLead lead = (AmoCRMLead) input;

        if(!this.getParams().containsKey("site")) {
            return (T) lead;
        }

        if(this.getParams().get("site") != null){
            Site site = (Site) this.getParams().get("site");

            if(site.getUrl().contains("www.apeyronled.ru")){
                // ставим тег "каталожный"
                lead.tag(1l, "каталожный");
                lead.setPipeline_id(446961l);
            }

            if(site.getUrl().contains("/led-apeyron.ru")){
                lead.setPipeline_id(446961l);
                lead.tag(1l, "LP_main");
            }

            if(site.getUrl().contains("opt.led-apeyron.ru")){

                String marketingChannelName = (String) this.getParams().get("marketing_channel");
                String searchEngine = (String) this.getParams().get("search_engine_name");
                String utmSource = (String) this.getParams().get("utm_source");

                if(
                        marketingChannelName.contains("ндекс") ||
                                searchEngine.contains("yandex") ||
                                utmSource.contains("yandex")
                        ){
                    lead.tag(1l, "Я.Д.");
                }

                if(
                        marketingChannelName.contains("oogle") ||
                                searchEngine.contains("google") ||
                                utmSource.contains("google")
                        ){
                    lead.tag(1l, "GA");
                }

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
