package com.berzellius.integrations.apeyronled.dto.site;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by berz on 25.06.2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContactAddDTO {
    protected Long contactId;

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }

    public ContactAddDTO() {
    }
}
