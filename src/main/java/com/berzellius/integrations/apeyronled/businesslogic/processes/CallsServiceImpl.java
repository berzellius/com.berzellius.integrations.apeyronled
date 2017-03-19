package com.berzellius.integrations.apeyronled.businesslogic.processes;

import com.berzellius.integrations.apeyronled.businesslogic.rules.transformer.FieldsTransformer;
import com.berzellius.integrations.apeyronled.dmodel.TrackedCall;
import com.berzellius.integrations.apeyronled.dto.site.CallDTO;
import com.berzellius.integrations.apeyronled.dto.site.CallRequest;
import com.berzellius.integrations.apeyronled.dto.site.Result;
import com.berzellius.integrations.apeyronled.repository.TrackedCallRepository;
import com.berzellius.integrations.apeyronled.scheduling.SchedulingService;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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
    FieldsTransformer fieldsTransformer;

    private String transformPhone(String phone){
        String res = fieldsTransformer.transform(phone, FieldsTransformer.Transformation.CALL_NUMBER_COMMON);
        //res = fieldsTransformer.transform(res, FieldsTransformer.Transformation.CALL_NUMBER_LEADING_7);
        return res;
    }

    @Override
    public Result newCallFromWebhook(CallRequest callRequest) {
        List<CallDTO> calls = callRequest.getCalls();

        if(calls != null){
            processCalls(calls);

            try {
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

    private void processCalls(List<CallDTO> calls) {
        for(CallDTO callDTO : calls){
            if(callDTO.getContact_phone_number() != null) {
                TrackedCall trackedCall = new TrackedCall();
                trackedCall.setState(TrackedCall.State.NEW);
                trackedCall.setSiteId(callDTO.getSite_id());
                trackedCall.setDt(callDTO.getNotification_time());
                trackedCall.setDtmCreate(new Date());
                String phone = transformPhone(callDTO.getContact_phone_number());
                trackedCall.setNumber(phone);

                trackedCallRepository.save(trackedCall);
            }
        }
    }
}
