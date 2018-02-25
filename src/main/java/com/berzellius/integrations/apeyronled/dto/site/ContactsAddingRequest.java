package com.berzellius.integrations.apeyronled.dto.site;

import java.util.List;

/**
 * Created by berz on 25.06.2017.
 */
public class ContactsAddingRequest {

    protected List<ContactAddDTO> addedContacts;

    protected List<ContactAddDTO> editedContacts;

    public ContactsAddingRequest() {
    }

    public List<ContactAddDTO> getAddedContacts() {
        return addedContacts;
    }

    public void setAddedContacts(List<ContactAddDTO> addedContacts) {
        this.addedContacts = addedContacts;
    }

    public List<ContactAddDTO> getEditedContacts() {
        return editedContacts;
    }

    public void setEditedContacts(List<ContactAddDTO> editedContacts) {
        this.editedContacts = editedContacts;
    }
}
