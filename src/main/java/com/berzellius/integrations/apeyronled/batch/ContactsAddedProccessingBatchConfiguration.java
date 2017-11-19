package com.berzellius.integrations.apeyronled.batch;

import com.berzellius.integrations.apeyronled.businesslogic.processes.IncomingCallBusinessProcess;
import com.berzellius.integrations.apeyronled.dmodel.ContactAdded;
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
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by berz on 25.06.2017.
 */
@Configuration
@EnableBatchProcessing
@EnableAutoConfiguration
@PropertySource("classpath:batch.properties")
public class ContactsAddedProccessingBatchConfiguration {
    @Autowired
    JobBuilderFactory jobBuilderFactory;

    @Autowired
    IncomingCallBusinessProcess incomingCallBusinessProcess;

    @PersistenceContext
    EntityManager entityManager;

    //@StepScope
    @Bean
    public ItemReader<ContactAdded> contactAddedItemReader(){
        JpaPagingItemReader<ContactAdded> reader = new JpaPagingItemReader<>();
        reader.setEntityManagerFactory(entityManager.getEntityManagerFactory());
        reader.setQueryString("select c from ContactAdded c where state = :st");
        HashMap<String, Object> params = new LinkedHashMap<>();
        params.put("st", ContactAdded.State.NEW);
        reader.setParameterValues(params);

        return reader;
    }

    @Bean
    public ItemProcessor<ContactAdded, ContactAdded> contactAddedItemProcessor(){
        return new ItemProcessor<ContactAdded, ContactAdded>() {
            @Override
            public ContactAdded process(ContactAdded contactAdded) throws Exception {
                try{
                    incomingCallBusinessProcess.processAddedContact(contactAdded);
                }
                catch(RuntimeException e){
                    System.out.println("exception while processing ContactAdded");
                    e.printStackTrace();
                    throw e;
                }

                return contactAdded;
            }
        };
    }

    @Bean
    public Step contactAddedProcessorStep(
            StepBuilderFactory stepBuilderFactory,
            ItemReader<ContactAdded> contactAddedItemReader,
            ItemProcessor<ContactAdded, ContactAdded> contactAddedItemProcessor

    ){


        return stepBuilderFactory.get("contactAddedProcessorStep")
                .<ContactAdded, ContactAdded>chunk(1)
                .reader(contactAddedItemReader)
                .processor(contactAddedItemProcessor)
                .faultTolerant()
                .skip(RuntimeException.class)
                .skipLimit(20)
                .taskExecutor(taskExecutor())
                .throttleLimit(1)
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();
        taskExecutor.setConcurrencyLimit(1);
        return taskExecutor;
    }

    @Bean
    public Job newContactAddedProcessJob(
            Step contactAddedProcessorStep
    ){
        RunIdIncrementer runIdIncrementer = new RunIdIncrementer();

        return jobBuilderFactory.get("newContactAddedProcessJob")
                .incrementer(runIdIncrementer)
                .flow(contactAddedProcessorStep)
                .end()
                .build();
    }

}
