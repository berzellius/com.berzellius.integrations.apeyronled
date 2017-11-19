import com.berzellius.integrations.amocrmru.dto.api.amocrm.AmoCRMContact;
import com.berzellius.integrations.amocrmru.dto.api.amocrm.AmoCRMCustomField;
import com.berzellius.integrations.amocrmru.dto.api.amocrm.AmoCRMCustomFieldValue;
import com.berzellius.integrations.amocrmru.dto.api.amocrm.AmoCRMLead;
import com.berzellius.integrations.amocrmru.service.AmoCRMService;
import com.berzellius.integrations.apeyronled.TestApplication;
import com.berzellius.integrations.apeyronled.businesslogic.processes.LeadsFromSiteService;
import com.berzellius.integrations.apeyronled.dmodel.LeadFromSite;
import com.berzellius.integrations.apeyronled.dmodel.Site;
import com.berzellius.integrations.apeyronled.dto.site.Lead;
import com.berzellius.integrations.basic.exception.APIAuthException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created by berz on 06.03.2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {TestApplication.class, TestBeans.class})
public class TestAmocrm {
    @Autowired
    private AmoCRMService amoCRMService;

    @Autowired
    private LeadsFromSiteService leadsFromSiteService;

    @Test
    public void simpleTest() throws APIAuthException {
        List<AmoCRMContact> crmContacts = amoCRMService.getContactsByQuery("");

        System.out.println(crmContacts);

        List<AmoCRMLead> crmLeads = amoCRMService.getLeadsByQuery("");

        System.out.println(crmLeads);
    }

    //@Test
    public void testLeadFromSite() throws APIAuthException {
        LeadFromSite leadFromSite = new LeadFromSite();

        Lead lead = new Lead();
        lead.setName("Заказать товар");
        lead.setOrigin("www.apeyronled.ru");
        lead.setReferer("www.apeyronled.ru");
        lead.setName("Покупатель Василий");
        lead.setPhone("89111234567");
        lead.setEmail("vasily@notexistsmailer.hh");
        lead.setComment("комментирую я");
        leadFromSite.setLead(lead);

        Site site = new Site();
        site.setCrmContactSourceId("4493303");
        site.setCrmLeadSourceId("4493271");
        leadFromSite.setSite(site);

        leadFromSite.setState(LeadFromSite.State.NEW);

        leadsFromSiteService.processLeadFromSite(leadFromSite);
    }

    /**
     * Перенос данных из кастомного поля "мобильный телефон" в "системное" поле Amo
     */
    //@Test
    public void testMoveCustomMobilePhoneToStock() throws APIAuthException {
        Long[] ids = {40601885l};
        Long customMobileField = 1897384l;
        Long stockMobileField = 1543576l;
        String mobPhoneEnum = "3663596";

        for(Long id : ids){
            this.processMoveCustomMobilePhoneToStock(id, customMobileField, stockMobileField, mobPhoneEnum);
        }
    }

    protected void processMoveCustomMobilePhoneToStock(Long id, Long customMobileField, Long stockMobileField, String mobPhoneEnum) throws APIAuthException {
        System.out.println("Contact#" + id.toString());
        AmoCRMContact contact = amoCRMService.getContactById(id);
        AmoCRMCustomField mob = contact.customFieldByFieldId(customMobileField);
        if(mob != null && mob.getValues().size() > 0){
            for(AmoCRMCustomFieldValue value : mob.getValues()) {
                System.out.println("found custom mob phone " + value.getValue());
                contact.addStringValuesToCustomField(stockMobileField, new String[]{value.getValue()}, mobPhoneEnum);
            }
            amoCRMService.saveByUpdate(contact);
        }
    }

    //@Test
    public void loopContacts() throws APIAuthException {
        Long customMobileField = 1897384l;
        Long stockMobileField = 1543576l;
        String mobPhoneEnum = "3663596";

        long limit = 200l;
        long offset = 0l;
        List<AmoCRMContact> crmContacts;
        long count = 0l;

        do{
            System.out.println("offset = " + offset);
            crmContacts = amoCRMService.getContactsEntities(limit, offset);
            if(crmContacts != null){
                System.out.println(crmContacts.size() + " contacts..");

                for(AmoCRMContact crmContact : crmContacts){
                    try {
                        this.processMoveCustomMobilePhoneToStock(crmContact.getId(), customMobileField, stockMobileField, mobPhoneEnum);
                    }
                    catch (RuntimeException e){
                        System.out.println("Processing stopped on id#" + crmContact.getId().toString());
                    }
                }

                count += crmContacts.size();
                offset += limit;
                System.out.println(count + " contacts processed..");
            }
        }
        while(crmContacts != null);

        System.out.println(count + " contacts processed..");
    }
}
