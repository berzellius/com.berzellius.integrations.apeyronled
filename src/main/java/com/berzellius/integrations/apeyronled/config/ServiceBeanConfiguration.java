package com.berzellius.integrations.apeyronled.config;


import com.berzellius.integrations.amocrmru.dto.ErrorHandlers.AmoCRMAPIRequestErrorHandler;
import com.berzellius.integrations.amocrmru.service.AmoCRMService;
import com.berzellius.integrations.amocrmru.service.AmoCRMServiceImpl;
import com.berzellius.integrations.apeyronled.businesslogic.processes.*;
import com.berzellius.integrations.apeyronled.businesslogic.rules.transformer.FieldsTransformer;
import com.berzellius.integrations.apeyronled.businesslogic.rules.transformer.FieldsTransformerImpl;
import com.berzellius.integrations.apeyronled.businesslogic.rules.validator.BusinessRulesValidator;
import com.berzellius.integrations.apeyronled.businesslogic.rules.validator.BusinessRulesValidatorImpl;
import com.berzellius.integrations.apeyronled.scheduling.SchedulingService;
import com.berzellius.integrations.apeyronled.scheduling.SchedulingServiceImpl;
import com.berzellius.integrations.apeyronled.settings.APISettings;
import com.berzellius.integrations.comagicru.dto.ErrorHandlers.ComagicAPIRequestErrorHandler;
import com.berzellius.integrations.comagicru.service.ComagicAPIService;
import com.berzellius.integrations.comagicru.service.ComagicAPIServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by berz on 20.10.14.
 */
@Configuration
public class ServiceBeanConfiguration {


    @Bean
    AmoCRMService amoCRMService(){
        AmoCRMService amoCRMService = new AmoCRMServiceImpl();
        amoCRMService.setApiBaseUrl(APISettings.AmoCRMApiBaseUrl);
        amoCRMService.setLoginUrl(APISettings.AmoCRMLoginUrl);
        amoCRMService.setUserHash(APISettings.AmoCRMHash);
        amoCRMService.setUserLogin(APISettings.AmoCRMUser);


        ArrayList<Long> leadClosedStatusesIds = new ArrayList<>(Arrays.asList(APISettings.AmoCRMLeadClosedStatuses));
        amoCRMService.setLeadClosedStatusesIDs(leadClosedStatusesIds);

        amoCRMService.setMaxRelogins(APISettings.AmoCRMMaxRelogins);


        AmoCRMAPIRequestErrorHandler errorHandler = new AmoCRMAPIRequestErrorHandler();
        amoCRMService.setErrorHandler(errorHandler);

        return amoCRMService;
    }

    @Bean
    BusinessRulesValidator businessRulesValidator(){
        return new BusinessRulesValidatorImpl();
    }

    @Bean
    FieldsTransformer fieldsTransformer(){
        return new FieldsTransformerImpl();
    }

    @Bean
    LeadsFromSiteService leadsFromSiteService(){
        LeadsFromSiteService leadsFromSiteService = new LeadFromSiteServiceImpl();
        leadsFromSiteService.setDefaultUserID(APISettings.AmoCRMDefaultUserID);
        leadsFromSiteService.setMarketingChannelContactsCustomField(APISettings.AmoCRMMarketingChannelContactsCustomField);
        leadsFromSiteService.setMarketingChannelLeadsCustomField(APISettings.AmoCRMMarketingChannelLeadsCustomField);
        leadsFromSiteService.setPhoneNumberCustomField(APISettings.AmoCRMPhoneNumberCustomField);
        leadsFromSiteService.setPhoneNumberCustomFieldLeads(APISettings.AmoCRMPhoneNumberCustomFieldLeads);
        leadsFromSiteService.setEmailContactCustomField(APISettings.AmoCRMEmailContactCustomField);
        leadsFromSiteService.setSourceContactsCustomField(APISettings.AmoCRMSourceContactsCustomField);
        leadsFromSiteService.setSourceLeadsCustomField(APISettings.AmoCRMSourceLeadsCustomField);
        leadsFromSiteService.setEmailContactEnum(APISettings.AmoCRMEmailContactEnum);
        leadsFromSiteService.setPhoneNumberContactStockField(APISettings.AmoCRMPhoneNumberStockFieldContact);
        leadsFromSiteService.setPhoneNumberStockFieldContactEnumWork(APISettings.AmoCRMPhoneNumberStockFieldContactEnumWork);
        leadsFromSiteService.setCommentCustomField(APISettings.AMOCRMLeadCommentField);
        leadsFromSiteService.setLeadFromSiteTagId(APISettings.AMOCRMLeadFromSiteTagId);
        return leadsFromSiteService;
    }

    @Bean
    CallsService callsService(){
        CallsService callsService = new CallsServiceImpl();
        return callsService;
    }

    @Bean
    SchedulingService schedulingService(){
        SchedulingService schedulingService = new SchedulingServiceImpl();
        return  schedulingService;
    }

    @Bean
    IncomingCallBusinessProcess incomingCallBusinessProcess(){
        IncomingCallBusinessProcessImpl incomingCallBusinessProcess = new IncomingCallBusinessProcessImpl();
        incomingCallBusinessProcess.setDefaultUserId(APISettings.AmoCRMDefaultUserID);
        incomingCallBusinessProcess.setPhoneNumberCustomField(APISettings.AmoCRMPhoneNumberCustomField);
        incomingCallBusinessProcess.setPhoneNumberCustomFieldLeads(APISettings.AmoCRMPhoneNumberCustomFieldLeads);
        incomingCallBusinessProcess.setMarketingChannelContactsCustomField(APISettings.AmoCRMMarketingChannelContactsCustomField);
        incomingCallBusinessProcess.setMarketingChannelLeadsCustomField(APISettings.AmoCRMMarketingChannelLeadsCustomField);
        incomingCallBusinessProcess.setSourceContactsCustomField(APISettings.AmoCRMSourceContactsCustomField);
        incomingCallBusinessProcess.setEmailContactCustomField(APISettings.AmoCRMEmailContactCustomField);
        incomingCallBusinessProcess.setEmailContactEnum(APISettings.AmoCRMEmailContactEnum);
        incomingCallBusinessProcess.setPhoneNumberContactStockField(APISettings.AmoCRMPhoneNumberStockFieldContact);
        incomingCallBusinessProcess.setPhoneNumberStockFieldContactEnumWork(APISettings.AmoCRMPhoneNumberStockFieldContactEnumWork);
        incomingCallBusinessProcess.setSourceLeadsCustomField(APISettings.AmoCRMSourceLeadsCustomField);

        return incomingCallBusinessProcess;
    }

    @Bean
    ComagicAPIService comagicAPIService(){
        ComagicAPIService comagicApiService = new ComagicAPIServiceImpl();
        comagicApiService.setErrorHandler(new ComagicAPIRequestErrorHandler());
        comagicApiService.setLogin(APISettings.comagicLogin);
        comagicApiService.setPassword(APISettings.comagicPass);

        return comagicApiService;
    }
}
