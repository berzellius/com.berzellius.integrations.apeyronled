package com.berzellius.integrations.apeyronled.dto.site;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

/**
 * Created by berz on 28.03.2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CallRecordDTO {

    public CallRecordDTO() {
    }

    private String _id;
    private String site;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date notification_time;
    private String called_phone;
    private String calling_phone;
    private Long call_id;
    private String direction;
    private String record_link;
    private Long employee_id;
    private Integer file_duration;

    /**
     * Техническе поля
     */
    private String result;
    private String processed;

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public Date getNotification_time() {
        return notification_time;
    }

    public void setNotification_time(Date notification_time) {
        this.notification_time = notification_time;
    }

    public String getCalled_phone() {
        return called_phone;
    }

    public void setCalled_phone(String called_phone) {
        this.called_phone = called_phone;
    }

    public String getCalling_phone() {
        return calling_phone;
    }

    public void setCalling_phone(String calling_phone) {
        this.calling_phone = calling_phone;
    }

    public Long getCall_id() {
        return call_id;
    }

    public void setCall_id(Long call_id) {
        this.call_id = call_id;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getRecord_link() {
        return record_link;
    }

    public void setRecord_link(String record_link) {
        this.record_link = record_link;
    }

    public Long getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(Long employee_id) {
        this.employee_id = employee_id;
    }

    public Integer getFile_duration() {
        return file_duration;
    }

    public void setFile_duration(Integer file_duration) {
        this.file_duration = file_duration;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getProcessed() {
        return processed;
    }

    public void setProcessed(String processed) {
        this.processed = processed;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
