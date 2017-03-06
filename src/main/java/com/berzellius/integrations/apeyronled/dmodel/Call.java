package com.berzellius.integrations.apeyronled.dmodel;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Created by berz on 21.09.2015.
 */
@Entity(name = "Call")
@Table(
        name = "calls",
        uniqueConstraints = @UniqueConstraint(columnNames = {"number", "dt", "project_id", "duplicate_reason"})
)
@Access(AccessType.FIELD)
public class Call{

    public Call(){}

    /*
     * Состояние в контексте обработки входящих звонков
     */
    public static enum State {
        NEW,
        DONE
    }

    public static enum Status {

        ANSWERED("ANSWERED"),
        NO_ANSWER("NO ANSWER"),
        BUSY("BUSY"),
        FAILED("FAILED");

        private String str;

        Status(String str) {
            this.str = str;
        }


        public static Status valueByString(String str) {
            for (Status s : Status.values()) {
                if (s.getStr().equals(str)) {
                    return s;
                }
            }
            return null;
        }

        public String getStr() {
            return str;
        }
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

    @Column(name = "project_id")
    private Integer projectId;

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
    private Status status;

    @Enumerated(EnumType.STRING)
    private State state;

    @ElementCollection
    @CollectionTable(name = "calls_params", joinColumns = @JoinColumn(name = "id"))
    private Map<String, String> params = new LinkedHashMap<>();

    @Override
    public boolean equals(Object obj) {

        return (obj instanceof Call) && (this.getId().equals(((Call) obj).getId())) ||
                (
                        this.getNumber().equals(((Call) obj).getNumber()) &&
                                this.getDt().equals(((Call) obj).getDt()) &&
                                this.getProjectId().equals(((Call) obj).getProjectId()) &&
                                this.getDuplicateReason().equals(((Call) obj).getDuplicateReason())
                        );
    }

    @Override
    public String toString(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return "num: " + this.getNumber() +
                ", date: " + sdf.format(this.getDt()) +
                ", source: " + this.getSource() +
                ", params: " + this.getParams().toString();
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

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
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
}

