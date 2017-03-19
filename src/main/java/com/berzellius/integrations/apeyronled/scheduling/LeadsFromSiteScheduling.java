package com.berzellius.integrations.apeyronled.scheduling;

import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Created by berz on 10.03.2017.
 */
@Service
public class LeadsFromSiteScheduling {
    @Autowired
    SchedulingService schedulingService;

    @Scheduled(fixedDelay = 60000)
    public void runLeadsFromSiteBatch() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        schedulingService.runLeadsFromSiteBatch();
    }

}
