package com.berzellius.integrations.apeyronled;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * Created by berz on 03.01.2017.
 */
@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableScheduling
@EnableAsync
@PropertySource("classpath:application.properties")
public class Application {
    public static void main(String[] args) throws ClassNotFoundException {
        SpringApplication.run(Application.class, args);

    }
}
