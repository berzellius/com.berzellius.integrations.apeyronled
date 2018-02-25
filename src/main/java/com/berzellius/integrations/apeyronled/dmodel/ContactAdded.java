package com.berzellius.integrations.apeyronled.dmodel;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by berz on 25.06.2017.
 */
@Entity(name = "ContactAdded")
@Table(
        name = "contact_added"
)
@Access(AccessType.FIELD)
public class ContactAdded extends DModelEntity {
    /*
     * Состояние в контексте обработки
     */
    public static enum State {
        NEW,
        DONE
    }

    public ContactAdded() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "call_id_generator")
    @SequenceGenerator(name = "call_id_generator", sequenceName = "call_id_seq")
    @NotNull
    @Column(updatable = false, insertable = false, columnDefinition = "bigint")
    private Long id;

    @Column(name = "contact_id")
    protected Long contactId;

    @Enumerated(EnumType.STRING)
    protected State state;

    @Column(name = "is_edited_only")
    protected Boolean isEditedOnly;

    @Override
    public Long getId() {
        return id;
    }

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof ContactAdded) &&
                this.getId().equals(((ContactAdded) obj).getId());
    }

    @Override
    public String toString() {
        return "ContactAdded#".concat(this.getId().toString());
    }

    public Boolean getIsEditedOnly() {
        return isEditedOnly;
    }

    public void setIsEditedOnly(Boolean isEditedOnly) {
        this.isEditedOnly = isEditedOnly;
    }
}
