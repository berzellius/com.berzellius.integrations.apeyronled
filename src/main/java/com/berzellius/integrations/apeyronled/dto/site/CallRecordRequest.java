package com.berzellius.integrations.apeyronled.dto.site;

import java.util.List;

/**
 * Created by berz on 28.03.2017.
 */
public class CallRecordRequest {

    public CallRecordRequest() {
    }

    private List<CallRecordDTO> call_records;

    public List<CallRecordDTO> getCall_records() {
        return call_records;
    }

    public void setCall_records(List<CallRecordDTO> call_records) {
        this.call_records = call_records;
    }
}
