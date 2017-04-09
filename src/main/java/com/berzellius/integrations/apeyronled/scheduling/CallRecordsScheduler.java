package com.berzellius.integrations.apeyronled.scheduling;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by berz on 03.04.2017.
 */
@Service
public class CallRecordsScheduler {
    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    Job newCallRecordsToCRMJob;

    @Scheduled(fixedDelay = 900000)
    public void runCallsBatch() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
        jobParametersBuilder.addDate("start", new Date());

        System.out.println("START saving call records to CRM!");
        jobLauncher.run(newCallRecordsToCRMJob, jobParametersBuilder.toJobParameters());
    }
}
