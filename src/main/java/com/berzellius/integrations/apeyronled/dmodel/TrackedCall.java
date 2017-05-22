package com.berzellius.integrations.apeyronled.dmodel;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by berz on 21.09.2015.
 */
@Entity(name = "Call")
@Table(
        name = "calls",
        uniqueConstraints = @UniqueConstraint(columnNames = {"number", "dt", "site_id", "duplicate_reason"})
)
@Access(AccessType.FIELD)
public class TrackedCall extends DModelEntity {

    public TrackedCall(){}

    public Long getCampaign_id() {
        return campaign_id;
    }

    public void setCampaign_id(Long campaign_id) {
        this.campaign_id = campaign_id;
    }

    /*
     * Состояние в контексте обработки входящих звонков
     */
    public static enum State {
        NEW,
        DONE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "call_id_generator")
    @SequenceGenerator(name = "call_id_generator", sequenceName = "call_id_seq")
    @NotNull
    @Column(updatable = false, insertable = false, columnDefinition = "bigint")
    private Long id;

    @Column(name = "dtm_create")
    @DateTimeFormat(pattern = "YYYY-mm-dd")
    protected Date dtmCreate;

    @Column(name = "dtm_update")
    @DateTimeFormat(pattern = "YYYY-mm-dd")
    protected Date dtmUpdate;

    @Column(name = "site_id")
    private Integer siteId;

    @DateTimeFormat(pattern = "YYYY-mm-dd")
    private Date dt;

    private String source;

    private String number;

    /**
     * Поле используется для того, чтобы вручную обработать ситуацию, когда появилось 2 звонка с одного номера в одном проекте в одно время
     * Это крайне (!) маловероятная, может возникнуть в случае нескольких одновременных (до секунды) звонков с многоканального номера на многоканальный входящий
     * или в случае некорректных данных (буквенный код вместо номера телефона и одновременные звонки)
     * Поле не должно иметь значение null
     */
    @Column(name = "duplicate_reason")
    @NotNull
    private String duplicateReason = "";

    @Enumerated(EnumType.STRING)
    private State state;

    private String virtual_number;
    private String search_engine;
    private String search_query;
    private Long campaign_id;

    @Override
    public boolean equals(Object obj) {

        return (obj instanceof TrackedCall) && (this.getId().equals(((TrackedCall) obj).getId())) ||
                (
                        this.getNumber().equals(((TrackedCall) obj).getNumber()) &&
                                this.getDt().equals(((TrackedCall) obj).getDt()) &&
                                this.getSiteId().equals(((TrackedCall) obj).getSiteId()) &&
                                this.getDuplicateReason().equals(((TrackedCall) obj).getDuplicateReason())
                        );
    }

    @Override
    public String toString(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return "num: " + this.getNumber() +
                ", date: " + sdf.format(this.getDt()) +
                ", source: " + this.getSource();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDtmCreate() {
        return dtmCreate;
    }

    public void setDtmCreate(Date dtmCreate) {
        this.dtmCreate = dtmCreate;
    }

    public Date getDtmUpdate() {
        return dtmUpdate;
    }

    public void setDtmUpdate(Date dtmUpdate) {
        this.dtmUpdate = dtmUpdate;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Date getDt() {
        return dt;
    }

    public void setDt(Date dt) {
        this.dt = dt;
    }

    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDuplicateReason() {
        return duplicateReason;
    }

    public void setDuplicateReason(String duplicateReason) {
        this.duplicateReason = duplicateReason;
    }

    public String getVirtual_number() {
        return virtual_number;
    }

    public void setVirtual_number(String virtual_number) {
        this.virtual_number = virtual_number;
    }

    public String getSearch_engine() {
        return search_engine;
    }

    public void setSearch_engine(String search_engine) {
        this.search_engine = search_engine;
    }

    public String getSearch_query() {
        return search_query;
    }

    public void setSearch_query(String search_query) {
        this.search_query = search_query;
    }
}

