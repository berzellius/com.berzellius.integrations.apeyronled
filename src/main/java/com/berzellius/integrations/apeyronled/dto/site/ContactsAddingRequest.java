package com.berzellius.integrations.apeyronled.dto.site;

import java.util.List;

/**
 * Created by berz on 25.06.2017.
 */
public class ContactsAddingRequest {

    protected List<ContactAddDTO> addedContacts;

    public ContactsAddingRequest() {
    }

    public List<ContactAddDTO> getAddedContacts() {
        return addedContacts;
    }

    public void setAddedContacts(List<ContactAddDTO> addedContacts) {
        this.addedContacts = addedContacts;
    }
}
