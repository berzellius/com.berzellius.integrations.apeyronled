package com.berzellius.integrations.apeyronled.batch;


import com.berzellius.integrations.apeyronled.businesslogic.processes.LeadsFromSiteService;
import com.berzellius.integrations.apeyronled.dmodel.LeadFromSite;
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
 * Created by berz on 19.06.2016.
 */
@Configuration
@EnableBatchProcessing
@EnableAutoConfiguration
@PropertySource("classpath:batch.properties")
public class LeadsFromSiteImportToCRM {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    JobBuilderFactory jobBuilderFactory;

    @Autowired
    private LeadsFromSiteService leadsFromSiteService;

    @Bean
    ItemReader<LeadFromSite> leadFromSiteItemReader(){
        JpaPagingItemReader<LeadFromSite> reader = new JpaPagingItemReader<>();
        reader.setEntityManagerFactory(entityManager.getEntityManagerFactory());
        reader.setQueryString("select lfs from LeadFromSite lfs where state = :st");
        HashMap<String, Object> params = new LinkedHashMap<>();
        params.put("st", LeadFromSite.State.NEW);
        reader.setParameterValues(params);

        return reader;
    }

    @Bean
    ItemProcessor<LeadFromSite, LeadFromSite> processor(){
        return new ItemProcessor<LeadFromSite, LeadFromSite>() {
            @Override
            public LeadFromSite process(LeadFromSite leadFromSite) throws Exception {
                try {
                    // lets go
                    return leadsFromSiteService.processLeadFromSite(leadFromSite);
                }
                catch(RuntimeException e){
                    System.out.println("exception while processing LeadFromSite");
                    e.printStackTrace();
                    throw e;
                }
            }
        };
    }

    @Bean
    public Step leadFromSiteAddToCRMStep(
            StepBuilderFactory stepBuilderFactory,
            ItemReader<LeadFromSite> reader,
            ItemProcessor<LeadFromSite, LeadFromSite> processor

    ){
        return stepBuilderFactory.get("leadFromSiteAddToCRMStep")
                .<LeadFromSite, LeadFromSite>chunk(1)
                .reader(reader)
                .processor(processor)
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
    public Job newLeadsFromSiteToCRMJob(
            Step leadFromSiteAddToCRMStep
    ){
        RunIdIncrementer runIdIncrementer = new RunIdIncrementer();

        return jobBuilderFactory.get("newLeadsFromSiteToCRMJob")
                .incrementer(runIdIncrementer)
                .flow(leadFromSiteAddToCRMStep)
                .end()
                .build();
    }
}
