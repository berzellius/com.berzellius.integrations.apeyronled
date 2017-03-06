package com.berzellius.integrations.apeyronled;

/**
 * Created by berz on 20.09.2015.
 */

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.ServletContextInitializer;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.EnumSet;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableScheduling
@EnableAsync
public class WebInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    @Bean
    public ServletContextInitializer servletContextInitializer() {
        return new ServletContextInitializer() {
            @Override
            public void onStartup(ServletContext servletContext) throws ServletException {
                System.out.println("on startup action 322343");

                final CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
                characterEncodingFilter.setEncoding("UTF-8");
                characterEncodingFilter.setForceEncoding(false);

                servletContext.addFilter("characterEncodingFilter", characterEncodingFilter).addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "/*");
            }
        };

    }


}

