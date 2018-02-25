package com.berzellius.integrations.apeyronled.businesslogic.processes;

import com.berzellius.integrations.apeyronled.businesslogic.rules.transformer.FieldsTransformer;
import com.berzellius.integrations.apeyronled.dmodel.CallRecord;
import com.berzellius.integrations.apeyronled.dmodel.ContactAdded;
import com.berzellius.integrations.apeyronled.dmodel.TrackedCall;
import com.berzellius.integrations.apeyronled.dto.site.*;
import com.berzellius.integrations.apeyronled.repository.CallRecordRepository;
import com.berzellius.integrations.apeyronled.repository.ContactAddedRepository;
import com.berzellius.integrations.apeyronled.repository.TrackedCallRepository;
import com.berzellius.integrations.apeyronled.scheduling.SchedulingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by berz on 14.03.2017.
 */
@Service
public class  CallsServiceImpl implements CallsService {
    @Autowired
    SchedulingService schedulingService;

    @Autowired
    TrackedCallRepository trackedCallRepository;

    @Autowired
    ContactAddedRepository contactAddedRepository;

    @Autowired
    FieldsTransformer fieldsTransformer;

    @Autowired
    CallRecordRepository callRecordRepository;

    private static final Logger log = LoggerFactory.getLogger(CallsServiceImpl.class);

    private String transformPhone(String phone){
        String res = fieldsTransformer.transform(phone, FieldsTransformer.Transformation.CALL_NUMBER_COMMON);
        //res = fieldsTransformer.transform(res, FieldsTransformer.Transformation.CALL_NUMBER_LEADING_7);
        return res;
    }

    @Override
    public Result newCallFromWebhook(CallRequest callRequest) {
        List<CallDTO> calls = callRequest.getCalls();

        if(calls != null && calls.size() > 0){
            processCalls(calls);

            try {
                // todo подвесить nowait блокировку!!
                schedulingService.runImportCallsToCRM();
            } catch (JobParametersInvalidException e) {
                e.printStackTrace();
            } catch (JobExecutionAlreadyRunningException e) {
                e.printStackTrace();
            } catch (JobRestartException e) {
                e.printStackTrace();
            } catch (JobInstanceAlreadyCompleteException e) {
                e.printStackTrace();
            }
        }

        return new Result("success");
    }

    @Override
    public Result newCallRecords(CallRecordRequest callRecordRequest) {
        List<CallRecord> callRecords = new ArrayList<>();

        for (CallRecordDTO callRecordDTO : callRecordRequest.getCall_records()) {
            Matcher m = Pattern.compile("\\&\\#34;(\\S)+\\&\\#34;").matcher(callRecordDTO.getRecord_link());
            int i = 0;
            while (m.find()) {
                String link = m.group().replace("&#34;", "");
                String uuid = callRecordDTO.get_id().concat("_").concat(String.valueOf(++i));
                CallRecord.Direction direction = callRecordDTO.getDirection().equals("in")?
                        CallRecord.Direction.IN : callRecordDTO.getDirection().equals("out")?
                        CallRecord.Direction.OUT : null;
                String duration = callRecordDTO.getFile_duration().toString();
                String calling_phone = callRecordDTO.getCalling_phone();
                String called_phone = callRecordDTO.getCalled_phone();
                Date dt = callRecordDTO.getNotification_time();

                if(direction != null) {
                    CallRecord callRecord = new CallRecord();
                    callRecord.setState(CallRecord.State.NEW);
                    callRecord.setCalled_phone(called_phone);
                    callRecord.setCalling_phone(calling_phone);
                    callRecord.setDirection(direction);
                    callRecord.setDuration(duration);
                    callRecord.setDt(dt);
                    callRecord.setLink(link);
                    callRecord.setUuid(uuid);

                    callRecords.add(callRecord);
                }
            }
        }

        if(callRecords.size() > 0){
            callRecordRepository.save(callRecords);
        }

        return new Result("success");
    }

    @Override
    public Result newContactsAddedInCRM(ContactsAddingRequest contactsAddingRequest) throws InterruptedException {

        if(contactsAddingRequest.getAddedContacts().size() > 0 || contactsAddingRequest.getEditedContacts().size() > 0){
            log.info("Adding new ContactAdded entities..");
            for(ContactAddDTO contactAddDTO : contactsAddingRequest.getAddedContacts()){
                ContactAdded contactAdded = new ContactAdded();
                contactAdded.setState(ContactAdded.State.NEW);
                contactAdded.setContactId(contactAddDTO.getContactId());

                contactAddedRepository.save(contactAdded);
            }

            for(ContactAddDTO contactAddDTO : contactsAddingRequest.getEditedContacts()){
                ContactAdded contactAdded = new ContactAdded();
                contactAdded.setState(ContactAdded.State.NEW);
                contactAdded.setIsEditedOnly(true);
                contactAdded.setContactId(contactAddDTO.getContactId());

                contactAddedRepository.save(contactAdded);
            }

            try {
                schedulingService.runProcessingAddedContacts();
            } catch (JobParametersInvalidException e) {
                e.printStackTrace();
            } catch (JobExecutionAlreadyRunningException e) {
                e.printStackTrace();
            } catch (JobRestartException e) {
                e.printStackTrace();
            } catch (JobInstanceAlreadyCompleteException e) {
                e.printStackTrace();
            }
        }
        return new Result("success");
    }

    private void processCalls(List<CallDTO> calls) {
        for(CallDTO callDTO : calls){
            if(callDTO.getContact_phone_number() != null) {
                TrackedCall trackedCall = new TrackedCall();
                trackedCall.setState(TrackedCall.State.NEW);
                trackedCall.setSiteId(callDTO.getSite_id() != null? callDTO.getSite_id() : 0);
                trackedCall.setDt(callDTO.getNotification_time());
                trackedCall.setDtmCreate(new Date());
                String phone = transformPhone(callDTO.getContact_phone_number());
                trackedCall.setNumber(phone);
                trackedCall.setSource(callDTO.getCampaign());
                trackedCall.setVirtual_number(callDTO.getVirtual_number());
                trackedCall.setSearch_engine(callDTO.getSearch_engine());
                trackedCall.setSearch_query(callDTO.getSearch_query());
                trackedCall.setCampaign_id(trackedCall.getCampaign_id());

                trackedCallRepository.save(trackedCall);
            }
        }
    }
}
