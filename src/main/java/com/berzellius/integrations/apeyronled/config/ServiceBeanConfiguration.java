package com.berzellius.integrations.apeyronled.config;


import com.berzellius.integrations.amocrmru.dto.ErrorHandlers.AmoCRMAPIRequestErrorHandler;
import com.berzellius.integrations.amocrmru.service.AmoCRMService;
import com.berzellius.integrations.amocrmru.service.AmoCRMServiceImpl;
import com.berzellius.integrations.apeyronled.settings.APISettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

        AmoCRMAPIRequestErrorHandler errorHandler = new AmoCRMAPIRequestErrorHandler();
        amoCRMService.setErrorHandler(errorHandler);

        return amoCRMService;
    }

}
