package com.berzellius.integrations.apeyronled.batch;

import com.berzellius.integrations.amocrmru.service.AmoCRMService;
import com.berzellius.integrations.apeyronled.businesslogic.processes.IncomingCallBusinessProcess;
import com.berzellius.integrations.apeyronled.dmodel.CallRecord;
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
public class CallRecordsToCRMBatchConfiguration {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    IncomingCallBusinessProcess incomingCallBusinessProcess;

    @Autowired
    JobBuilderFactory jobBuilderFactory;


    @Autowired
    AmoCRMService amoCRMService;

    @Bean
    public ItemReader<CallRecord> callRecordItemReader(){
        JpaPagingItemReader<CallRecord> reader = new JpaPagingItemReader<>();
        reader.setEntityManagerFactory(entityManager.getEntityManagerFactory());
        reader.setQueryString("select c from CallRecord c where state = :st");
        HashMap<String, Object> params = new LinkedHashMap<>();
        params.put("st", CallRecord.State.NEW);
        reader.setParameterValues(params);

        return reader;
    }

    @Bean
    public ItemProcessor<CallRecord, CallRecord> callRecordProcessor(){
        return new ItemProcessor<CallRecord, CallRecord>() {
            @Override
            public CallRecord process(CallRecord callRecord) throws Exception {
                try{
                    incomingCallBusinessProcess.addCallRecordToCRM(callRecord);
                }
                catch(RuntimeException e){
                    System.out.println("exception while processing CallRecord");
                    e.printStackTrace();
                    throw e;
                }

                return callRecord;
            }
        };
    }

    @Bean
    public Step callRecordAddToCRMStep(
            StepBuilderFactory stepBuilderFactory,
            ItemReader<CallRecord> callRecordItemReader,
            ItemProcessor<CallRecord, CallRecord> callRecordProcessor

    ){
        return stepBuilderFactory.get("callRecordAddToCRMStep")
                .<CallRecord, CallRecord>chunk(1)
                .reader(callRecordItemReader)
                .processor(callRecordProcessor)
                .faultTolerant()
                .skip(RuntimeException.class)
                .skipLimit(2000)
                .build();
    }

    @Bean
    public Job newCallRecordsToCRMJob(
        Step callRecordAddToCRMStep
    ){
        RunIdIncrementer runIdIncrementer = new RunIdIncrementer();

        return jobBuilderFactory.get("newCallRecordsToCRMJob")
                .incrementer(runIdIncrementer)
                .flow(callRecordAddToCRMStep)
                .end()
                .build();
    }
}
