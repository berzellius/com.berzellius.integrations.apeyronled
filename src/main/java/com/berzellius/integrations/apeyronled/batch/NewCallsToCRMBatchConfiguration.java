package com.berzellius.integrations.apeyronled.batch;

import com.berzellius.integrations.amocrmru.service.AmoCRMService;
import com.berzellius.integrations.apeyronled.businesslogic.processes.IncomingCallBusinessProcess;
import com.berzellius.integrations.apeyronled.dmodel.TrackedCall;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by berz on 14.10.2015.
 */
@Configuration
@EnableBatchProcessing
@EnableAutoConfiguration
@PropertySource("classpath:batch.properties")
public class NewCallsToCRMBatchConfiguration {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    IncomingCallBusinessProcess incomingCallBusinessProcess;

    @Autowired
    JobBuilderFactory jobBuilderFactory;


    @Autowired
    AmoCRMService amoCRMService;

    @Bean
    public ItemReader<TrackedCall> callReader(){
        JpaPagingItemReader<TrackedCall> reader = new JpaPagingItemReader<>();
        reader.setEntityManagerFactory(entityManager.getEntityManagerFactory());
        reader.setQueryString("select c from Call c where state = :st");
        HashMap<String, Object> params = new LinkedHashMap<>();
        params.put("st", TrackedCall.State.NEW);
        reader.setParameterValues(params);

        return reader;
    }

    @Bean
    public ItemProcessor<TrackedCall, TrackedCall> callProcessor(){
        return new ItemProcessor<TrackedCall, TrackedCall>() {
            @Override
            public TrackedCall process(TrackedCall call) throws Exception {
                try{
                    incomingCallBusinessProcess.newIncomingCall(call);
                }
                catch(RuntimeException e){
                    System.out.println("exception while processing LeadFromSite");
                    e.printStackTrace();
                    throw e;
                }

                return call;
            }
        };
    }

    @Bean
    public Step callAddToCRMStep(
            StepBuilderFactory stepBuilderFactory,
            ItemReader<TrackedCall> callItemReader,
            ItemProcessor<TrackedCall, TrackedCall> callProcessor

    ){
        return stepBuilderFactory.get("callAddToCRMStep")
                .<TrackedCall, TrackedCall>chunk(1)
                .reader(callItemReader)
                .processor(callProcessor)
                .faultTolerant()
                .skip(RuntimeException.class)
                .skipLimit(2000)
                .build();
    }

    @Bean
    public Job newCallsToCRMJob(
        Step callAddToCRMStep
    ){
        RunIdIncrementer runIdIncrementer = new RunIdIncrementer();

        return jobBuilderFactory.get("newCallsToCRMJob")
                .incrementer(runIdIncrementer)
                .flow(callAddToCRMStep)
                .end()
                .build();
    }
}
