import com.berzellius.integrations.amocrmru.dto.api.amocrm.AmoCRMContact;
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

    @Test
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
}
