package com.berzellius.integrations.apeyronled.businesslogic.processes;

import com.berzellius.integrations.amocrmru.dto.api.amocrm.*;
import com.berzellius.integrations.amocrmru.dto.api.amocrm.response.AmoCRMCreatedEntityResponse;
import com.berzellius.integrations.amocrmru.service.AmoCRMService;
import com.berzellius.integrations.apeyronled.businesslogic.rules.transformer.FieldsTransformer;
import com.berzellius.integrations.apeyronled.businesslogic.rules.validator.BusinessRulesValidator;
import com.berzellius.integrations.apeyronled.dmodel.LeadFromSite;
import com.berzellius.integrations.apeyronled.dmodel.Site;
import com.berzellius.integrations.apeyronled.dto.site.Lead;
import com.berzellius.integrations.apeyronled.dto.site.Result;
import com.berzellius.integrations.apeyronled.repository.LeadFromSiteRepository;
import com.berzellius.integrations.apeyronled.repository.SiteRepository;
import com.berzellius.integrations.basic.exception.APIAuthException;
import com.berzellius.integrations.comagicru.service.ComagicAPIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by berz on 09.03.2017.
 */
@Service
@Transactional
public class LeadFromSiteServiceImpl implements LeadsFromSiteService {

    @Autowired
    SiteRepository siteRepository;

    @Autowired
    BusinessRulesValidator businessRulesValidator;

    @Autowired
    FieldsTransformer fieldsTransformer;

    @Autowired
    LeadFromSiteRepository leadFromSiteRepository;

    @Autowired
    AmoCRMService amoCRMService;

    @Autowired
    ComagicAPIService comagicAPIService;

    private Long phoneNumberCustomFieldLeads;
    private Long phoneNumberCustomField;
    private Long commentCustomField;
    private Long defaultUserID;
    private Long marketingChannelContactsCustomField;
    private Long marketingChannelLeadsCustomField;
    private Long emailContactCustomField;
    private String emailContactEnum;
    private Long phoneNumberContactStockField;
    private String phoneNumberStockFieldContactEnumWork;

    private Long leadFromSiteTagId;

    private Long sourceLeadsCustomField;
    private Long sourceContactsCustomField;

    private HashMap<Integer, Long> siteIdToLeadsSource;

    private static final Logger log = LoggerFactory.getLogger(LeadsFromSiteService.class);

    private String transformPhone(String phone){
        String res = fieldsTransformer.transform(phone, FieldsTransformer.Transformation.CALL_NUMBER_COMMON);
        res = fieldsTransformer.transform(res, FieldsTransformer.Transformation.CALL_NUMBER_LEADING_7);
        return res;
    }

    @Override
    public LeadFromSite processLeadFromSite(LeadFromSite leadFromSite) throws APIAuthException {
        if(leadFromSite.getSite() != null && leadFromSite.getLead() != null) {
            log.info("Started processing lead from site " + leadFromSite.getSite().getUrl() + "; contacts: " + leadFromSite.getLead().getPhone() + " / " + leadFromSite.getLead().getEmail());

            // Приводим номер к общему формату
            if (leadFromSite.getLead().getPhone() != null) {
                leadFromSite.getLead().setPhone(this.transformPhone(leadFromSite.getLead().getPhone()));
            }

            if (!businessRulesValidator.validate(leadFromSite)) {
                log.error("LeadFromSite object has not validated!");
                leadFromSite.setState(LeadFromSite.State.DONE);
                leadFromSiteRepository.save(leadFromSite);

                return leadFromSite;
            } else {
                log.info("LeadFromSite was succesfully validated!");
            }


            String marketingChannel = "";
            if(leadFromSite.getLead().getVisitor_id() != null){
                marketingChannel = comagicAPIService.getActiveAcByVisitorId(leadFromSite.getLead().getVisitor_id());
            }

            AmoCRMContact contact = contactForLeadFromSite(leadFromSite, marketingChannel);

            if(contact == null){
                throw new RuntimeException("seems to be contact was not created and not exists for leadFromSite#" + leadFromSite.getId());
            }

            this.workWithContact(leadFromSite, contact, marketingChannel);


            leadFromSite.setState(LeadFromSite.State.DONE);
            leadFromSiteRepository.save(leadFromSite);

            return leadFromSite;
        }
        return null;
    }

    public HashMap<Integer, Long> getSiteIdToLeadsSource() {
        if(siteIdToLeadsSource == null){
            this.setSiteIdToLeadsSource(new HashMap<>());
        }

        if(siteIdToLeadsSource.size() == 0){
            log.debug("updating siteIdToLeadsSource in AmoCRMLeadsFromSiteServiceImpl");
            List<Site> sites = (List<Site>) siteRepository.findAll();
            for(Site site : sites){
                siteIdToLeadsSource.put(site.getCallTrackingSiteId(), Long.decode(site.getCrmLeadSourceId()));
            }
        }
        return siteIdToLeadsSource;
    }

    private AmoCRMContact contactForLeadFromSite(LeadFromSite leadFromSite, String sourceName) throws APIAuthException {
        Lead lead = leadFromSite.getLead();
        if(lead == null){
            return null;
        }

        String contacts = contactStrByLead(lead);

        if(lead.getPhone() != null) {
            List<AmoCRMContact> contactsByPhone = amoCRMService.getContactsByQuery(lead.getPhone());
            if(contactsByPhone.size() > 0){
                return contactsByPhone.get(0);
            }
        }

        if(lead.getEmail() != null){
            List<AmoCRMContact> contactsByEmail = amoCRMService.getContactsByQuery(lead.getEmail());
            if(contactsByEmail.size() > 0){
                return contactsByEmail.get(0);
            }
        }

        AmoCRMContact amoCRMContact = new AmoCRMContact();
        amoCRMContact.setName(lead.getName() + " (с сайта " + lead.getOrigin() + ") :[" + contacts + "]");
        amoCRMContact.setResponsible_user_id(this.getDefaultUserID());

        if(lead.getPhone() != null){
            String[] fieldNumber = {lead.getPhone()};
            amoCRMContact.addStringValuesToCustomField(this.getPhoneNumberCustomField(), fieldNumber);
            amoCRMContact.addStringValuesToCustomField(this.getPhoneNumberContactStockField(), fieldNumber, this.getPhoneNumberStockFieldContactEnumWork());
        }

        if(lead.getEmail() != null){
            String[] fieldEmail = {lead.getEmail()};
            amoCRMContact.addStringValuesToCustomField(this.getEmailContactCustomField(), fieldEmail, this.getEmailContactEnum());
        }

        String[] fieldProject = {leadFromSite.getSite().getCrmContactSourceId()};
        amoCRMContact.addStringValuesToCustomField(this.getSourceContactsCustomField(), fieldProject);

        String[] fieldSource = {sourceName};
        amoCRMContact.addStringValuesToCustomField(this.getMarketingChannelContactsCustomField(), fieldSource);


        AmoCRMCreatedEntityResponse response = amoCRMService.addContact(amoCRMContact);

        if(response.getId() != null){
            log.info("Contact was created: #" + response.getId());
            AmoCRMContact contact = amoCRMService.getContactById(response.getId());
            return contact;
        }

        return null;
    }

    private void workWithContact(LeadFromSite leadFromSite, AmoCRMContact contact, String sourceName) throws APIAuthException {
        log.info("searching leads for contact#" + contact.getId().toString());

        // Проверяем заполненность полей "телефон" и  "email"
        log.info("checking contact fields...");
        String phone2check = leadFromSite.getLead().getPhone();
        log.info("phone: " + phone2check);
        String email2check = leadFromSite.getLead().getEmail();
        log.info("email: " + email2check);
        for(AmoCRMCustomField amoCRMCustomField : contact.getCustom_fields()){
            if(amoCRMCustomField.getCode() != null &&
                    amoCRMCustomField.getCode().equals("PHONE")){
                for(AmoCRMCustomFieldValue amoCRMCustomFieldValue : amoCRMCustomField.getValues()){
                    String transformedPhone = this.transformPhone(amoCRMCustomFieldValue.getValue());
                    if(transformedPhone.equals(phone2check)){
                        // больше можно не проверять
                        phone2check = null;
                    }
                }
            }

            if(amoCRMCustomField.getCode() != null &&
                    amoCRMCustomField.getCode().equals("EMAIL")){
                for(AmoCRMCustomFieldValue amoCRMCustomFieldValue : amoCRMCustomField.getValues()){
                    if(amoCRMCustomFieldValue.getValue().equals(email2check)){
                        // больше можно не проверять
                        email2check = null;
                    }
                }
            }
        }

        if(phone2check != null){
            log.info("adding to phone numbers: ".concat(phone2check));
            String[] phone2checkField = {phone2check};
            contact.addStringValuesToCustomField(this.getPhoneNumberContactStockField(), phone2checkField, this.getPhoneNumberStockFieldContactEnumWork());
        }

        if(email2check != null){
            log.info("adding to emails: ".concat(email2check));
            String[] email2checkField = {email2check};
            contact.addStringValuesToCustomField(this.getEmailContactCustomField(), email2checkField, this.getEmailContactEnum());
        }

        amoCRMService.saveByUpdate(contact);


        // Проверяем, есть ли сделки
        ArrayList<Long> leadIds = contact.getLinked_leads_id();
        if (leadIds != null && leadIds.size() != 0) {
            log.info("Leads found. Checking statuses");

            for (Long leadId : leadIds){
                log.info("work with lead#" + leadId);
                AmoCRMLead lead = amoCRMService.getLeadById(leadId);

                if(lead != null){
                    if(amoCRMService.getLeadClosedStatusesIDs().contains(lead.getStatus_id())){
                        log.info("lead is closed. next..");
                    }
                    else{
                        log.info("lead is open. ok");
                        return;
                    }
                }
            }
        }
        // Если лид не найден, то попадаем сюда и создаем лид
        this.createLead(leadFromSite, contact, sourceName);

    }

    private void createLead(LeadFromSite leadFromSite, AmoCRMContact contact, String sourceName) throws APIAuthException {
        if(leadFromSite.getLead() == null)
            return;

        AmoCRMLead lead = new AmoCRMLead();

        lead.setName("Заявка с сайта -> " + this.contactStrByLead(leadFromSite.getLead()));
        //lead.setResponsible_user_id(this.getDefaultUserID());

        if(leadFromSite.getLead().getPhone() != null){
            String[] numberField = {leadFromSite.getLead().getPhone()};
            lead.addStringValuesToCustomField(this.getPhoneNumberCustomFieldLeads(), numberField);
        }

        if(leadFromSite.getLead().getComment() != null){
            String[] commentField = {leadFromSite.getLead().getComment()};
            lead.addStringValuesToCustomField(this.getCommentCustomField(), commentField);
        }

        String[] fieldProject = {leadFromSite.getSite().getCrmLeadSourceId()};
        lead.addStringValuesToCustomField(this.getSourceLeadsCustomField(), fieldProject);

        String[] fieldSource = {sourceName};
        lead.addStringValuesToCustomField(this.getMarketingChannelLeadsCustomField(), fieldSource);


        //lead.tag(this.getLeadFromSiteTagId(), "Заявка с сайта");
        lead = this.transfromLeadFromSiteForChangePipelineAndTags(lead, leadFromSite);
        // если в ходе трансформации ответственный не определен
        // если у контакта есть ответственный и он не является ответственным по умолчанию, ставим отвественного за контакт
        // иначе ставим пользователя по умолчанию
        if(lead.getResponsible_user_id() == null){
            if(
                    contact.getResponsible_user_id() != null &&
                            !contact.getResponsible_user_id().equals(this.getDefaultUserID())
                    ){
                lead.setResponsible_user_id(contact.getResponsible_user_id());
            }
            else{
                lead.setResponsible_user_id(this.getDefaultUserID());
            }
        }


        log.info("creating lead for leadFromSite..");

        AmoCRMCreatedEntityResponse amoCRMCreatedEntityResponse = amoCRMService.addLead(lead);
        if(amoCRMCreatedEntityResponse.getId() != null){
            log.info("created lead#" + amoCRMCreatedEntityResponse.getId().toString());

            AmoCRMLead lead1 = amoCRMService.getLeadById(amoCRMCreatedEntityResponse.getId());
            amoCRMService.addContactToLead(contact, lead1);
        }
        else{
            log.error("error creating lead!");
            throw new RuntimeException("Lead was not created with unknown reason!");
        }
    }


    private String contactStrByLead(Lead lead){
        if(lead == null)
            return "";

        String contact = "";
        if(lead.getPhone() != null){
            contact = contact.concat(lead.getPhone());
        }

        if(lead.getEmail() != null){
            contact = contact.concat((contact.equals(""))? lead.getEmail() : " / ".concat(lead.getEmail()));
        }

        return contact;
    }

    protected AmoCRMLead transfromLeadFromSiteForChangePipelineAndTags(AmoCRMLead amoCRMLead, LeadFromSite leadFromSite){
        HashMap<String, Object> params = new LinkedHashMap<>();
        if(leadFromSite.getSite() != null){
            params.put("site", leadFromSite.getSite());
        }

        return fieldsTransformer.transform(
                amoCRMLead,
                FieldsTransformer.Transformation.AMOCRM_LEADFROMSITE_PIPELINE_AND_TAGS,
                params
        );
    }


    public Long getPhoneNumberCustomFieldLeads() {
        return phoneNumberCustomFieldLeads;
    }

    @Override
    public void setPhoneNumberCustomFieldLeads(Long phoneNumberCustomFieldLeads) {
        this.phoneNumberCustomFieldLeads = phoneNumberCustomFieldLeads;
    }

    public Long getPhoneNumberCustomField() {
        return phoneNumberCustomField;
    }

    @Override
    public void setPhoneNumberCustomField(Long phoneNumberCustomField) {
        this.phoneNumberCustomField = phoneNumberCustomField;
    }

    public Long getCommentCustomField() {
        return commentCustomField;
    }

    @Override
    public void setCommentCustomField(Long commentCustomField) {
        this.commentCustomField = commentCustomField;
    }

    public Long getDefaultUserID() {
        return defaultUserID;
    }

    @Override
    public void setDefaultUserID(Long defaultUserID) {
        this.defaultUserID = defaultUserID;
    }

    public Long getMarketingChannelContactsCustomField() {
        return marketingChannelContactsCustomField;
    }

    @Override
    public void setMarketingChannelContactsCustomField(Long marketingChannelContactsCustomField) {
        this.marketingChannelContactsCustomField = marketingChannelContactsCustomField;
    }

    public Long getMarketingChannelLeadsCustomField() {
        return marketingChannelLeadsCustomField;
    }

    @Override
    public void setMarketingChannelLeadsCustomField(Long marketingChannelLeadsCustomField) {
        this.marketingChannelLeadsCustomField = marketingChannelLeadsCustomField;
    }

    public Long getEmailContactCustomField() {
        return emailContactCustomField;
    }

    @Override
    public void setEmailContactCustomField(Long emailContactCustomField) {
        this.emailContactCustomField = emailContactCustomField;
    }

    public String getEmailContactEnum() {
        return emailContactEnum;
    }

    @Override
    public void setEmailContactEnum(String emailContactEnum) {
        this.emailContactEnum = emailContactEnum;
    }

    public Long getPhoneNumberContactStockField() {
        return phoneNumberContactStockField;
    }

    @Override
    public void setPhoneNumberContactStockField(Long phoneNumberContactStockField) {
        this.phoneNumberContactStockField = phoneNumberContactStockField;
    }

    public String getPhoneNumberStockFieldContactEnumWork() {
        return phoneNumberStockFieldContactEnumWork;
    }

    @Override
    public void setPhoneNumberStockFieldContactEnumWork(String phoneNumberStockFieldContactEnumWork) {
        this.phoneNumberStockFieldContactEnumWork = phoneNumberStockFieldContactEnumWork;
    }

    public Long getLeadFromSiteTagId() {
        return leadFromSiteTagId;
    }

    @Override
    public void setLeadFromSiteTagId(Long leadFromSiteTagId) {
        this.leadFromSiteTagId = leadFromSiteTagId;
    }

    public Long getSourceLeadsCustomField() {
        return sourceLeadsCustomField;
    }

    @Override
    public void setSourceLeadsCustomField(Long sourceLeadsCustomField) {
        this.sourceLeadsCustomField = sourceLeadsCustomField;
    }

    public Long getSourceContactsCustomField() {
        return sourceContactsCustomField;
    }

    @Override
    public void setSourceContactsCustomField(Long sourceContactsCustomField) {
        this.sourceContactsCustomField = sourceContactsCustomField;
    }

    @Override
    public Result newLeadFromSite(List<Lead> leads, String origin, String password) {
        List<Site> sites = siteRepository.findByUrlAndPassword(origin, password);
        if(sites.size() == 0){
            return new Result("error");
        }

        Site site = sites.get(0);

        List<LeadFromSite> leadFromSiteList = new ArrayList<>();
        for(Lead lead : leads){
            LeadFromSite leadFromSite = new LeadFromSite();
            leadFromSite.setDtmCreate(new Date());
            leadFromSite.setSite(site);
            leadFromSite.setLead(lead);
            leadFromSite.setState(LeadFromSite.State.NEW);

            leadFromSiteList.add(leadFromSite);
        }

        leadFromSiteRepository.save(leadFromSiteList);
        return new Result("success");
    }

    public void setSiteIdToLeadsSource(HashMap<Integer, Long> siteIdToLeadsSource) {
        this.siteIdToLeadsSource = siteIdToLeadsSource;
    }
}
