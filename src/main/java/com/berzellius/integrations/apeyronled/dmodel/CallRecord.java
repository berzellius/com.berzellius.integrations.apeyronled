package com.berzellius.integrations.apeyronled.dmodel;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by berz on 28.03.2017.
 */
@Entity(name = "CallRecord")
@Table(
        name = "call_records",
        uniqueConstraints = @UniqueConstraint(columnNames = {"uuid"})
)
@Access(AccessType.FIELD)
public class CallRecord extends DModelEntity {
    public CallRecord() {
    }

    public static enum Direction {
        IN, OUT
    }

    public static enum State{
        NEW, DONE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "call_rec_id_generator")
    @SequenceGenerator(name = "call_rec_id_generator", sequenceName = "call_rec_id_seq")
    @NotNull
    @Column(updatable = false, insertable = false, columnDefinition = "bigint")
    protected Long id;

    @Enumerated(EnumType.STRING)
    protected State state;

    protected String uuid;
    protected String link;
    protected String duration;
    @Enumerated(EnumType.STRING)
    protected Direction direction;
    protected String calling_phone;
    protected String called_phone;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    protected Date dt;

    public Date getDt() {
        return dt;
    }

    public void setDt(Date dt) {
        this.dt = dt;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public String getCalling_phone() {
        return calling_phone;
    }

    public void setCalling_phone(String calling_phone) {
        this.calling_phone = calling_phone;
    }

    public String getCalled_phone() {
        return called_phone;
    }

    public void setCalled_phone(String called_phone) {
        this.called_phone = called_phone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CallRecord &&
                this.getId().equals(((CallRecord) obj).getId());
    }

    @Override
    public String toString() {
        return "callRecord#".concat(this.getId().toString());
    }
}
