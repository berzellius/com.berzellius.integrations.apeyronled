package com.berzellius.integrations.apeyronled.scheduling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
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
    Job newContactAddedProcessJob;

    @Autowired
    JobLauncher jobLauncher;

    protected Long addedContactsMinDelay = 2000l;

    protected Date lastAddedContactsProcessingStart = null;

    private static final Logger log = LoggerFactory.getLogger(SchedulingService.class);

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

    @Override
    public void runProcessingAddedContacts() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        this.runContactAddedProcessing();
    }

    private synchronized void runContactAddedProcessing() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        // время за addedContactsMinDelay милисекунд до текущего момента
        calendar.add(Calendar.MILLISECOND, (-1) * this.addedContactsMinDelay.intValue());


        if(
                this.lastAddedContactsProcessingStart == null ||
                        // если время после последнего запуска раньше времени calendar
                        this.lastAddedContactsProcessingStart.before(calendar.getTime())
                ){
            log.info("Start batch processing for new added/edited contact");
            JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
            jobParametersBuilder.addDate("start", new Date());
            jobLauncher.run(newContactAddedProcessJob, jobParametersBuilder.toJobParameters());

            this.lastAddedContactsProcessingStart = new Date();
        }
        else{
            // нужно подождать..
            log.info("Wait for delay before starting processing for new added/edited contact again (last was in " + this.lastAddedContactsProcessingStart + " )");
            try {
                Thread.sleep(this.addedContactsMinDelay + 500l);
                this.runProcessingAddedContacts();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
