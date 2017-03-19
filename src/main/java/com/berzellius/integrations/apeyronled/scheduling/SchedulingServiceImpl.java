package com.berzellius.integrations.apeyronled.scheduling;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by berz on 14.03.2017.
 */
@Service
public class SchedulingServiceImpl implements SchedulingService {
    @Autowired
    Job newLeadsFromSiteToCRMJob;

    @Autowired
    Job newCallsToCRMJob;

    @Autowired
    JobLauncher jobLauncher;

    @Override
    public void runLeadsFromSiteBatch() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
        jobParametersBuilder.addDate("start", new Date());

        System.out.println("START leads from sites job!");
        jobLauncher.run(newLeadsFromSiteToCRMJob, jobParametersBuilder.toJobParameters());
    }

    @Override
    public void runImportCallsToCRM() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
        jobParametersBuilder.addDate("start", new Date());

        System.out.println("START import calls to CRM!");
        jobLauncher.run(newCallsToCRMJob, jobParametersBuilder.toJobParameters());
    }
}
