package com.berzellius.integrations.apeyronled.dto.site;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

/**
 * Created by berz on 15.06.2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CallDTO {

    public CallDTO() {
    }

    /*
    * TODO Описание звонка
    */
    private String contact_phone_number; // Номер, с которого звонили
    private Date notification_time; // Время
    private Integer site_id; // ID проекта - отличительный признак сайта (id проекта или др в зависимости от типа коллтрекинга)
    /*
    *
    * Технические поля
     */
    private String result;
    private String processed;

    public String getContact_phone_number() {
        return contact_phone_number;
    }

    public void setContact_phone_number(String contact_phone_number) {
        this.contact_phone_number = contact_phone_number;
    }

    public Date getNotification_time() {
        return notification_time;
    }

    public void setNotification_time(Date notification_time) {
        this.notification_time = notification_time;
    }

    public Integer getSite_id() {
        return site_id;
    }

    public void setSite_id(Integer site_id) {
        this.site_id = site_id;
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
}
