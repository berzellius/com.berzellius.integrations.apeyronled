import com.berzellius.integrations.amocrmru.dto.api.amocrm.AmoCRMContact;
import com.berzellius.integrations.amocrmru.dto.api.amocrm.AmoCRMLead;
import com.berzellius.integrations.amocrmru.service.AmoCRMService;
import com.berzellius.integrations.basic.exception.APIAuthException;
import com.berzellius.integrations.apeyronled.TestApplication;
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

    @Test
    public void simpleTest() throws APIAuthException {
        List<AmoCRMContact> crmContacts = amoCRMService.getContactsByQuery("");

        System.out.println(crmContacts);

        List<AmoCRMLead> crmLeads = amoCRMService.getLeadsByQuery("");

        System.out.println(crmLeads);
    }
}
