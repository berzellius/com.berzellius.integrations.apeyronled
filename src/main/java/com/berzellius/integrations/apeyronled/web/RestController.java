package com.berzellius.integrations.apeyronled.web;

import com.berzellius.integrations.apeyronled.businesslogic.processes.CallsService;
import com.berzellius.integrations.apeyronled.businesslogic.processes.LeadsFromSiteService;
import com.berzellius.integrations.apeyronled.dto.site.*;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by berz on 15.06.2016.
 */
@org.springframework.web.bind.annotation.RestController
@RequestMapping("/rest/")
public class RestController extends BaseController {

    @Autowired
    LeadsFromSiteService leadsFromSiteService;

    @Autowired
    CallsService callsService;

    @RequestMapping(
            value = "lead_from_site",
            method = RequestMethod.POST,
            consumes="application/json",
            produces="application/json"
    )
    @ResponseBody
    public Result newLeadFromSite(
            @RequestBody
            LeadRequest leadRequest
    ){
        return leadsFromSiteService.newLeadFromSite(leadRequest.getLeads(), leadRequest.getOrigin(), leadRequest.getPassword());
    }

    @RequestMapping(
            value = "call_webhook",
            method = RequestMethod.POST,
            consumes="application/json",
            produces="application/json"
    )
    @ResponseBody
    public Result newCallWebhook(
            @RequestBody
            CallRequest callRequest
    ) throws NotFoundException {
        //System.out.println("webhook! " + callRequest.toString());
        //throw new NotFoundException("out of service!");
        return callsService.newCallFromWebhook(callRequest);
    }

    @RequestMapping(
            value = "call_record",
            method = RequestMethod.POST,
            consumes="application/json",
            produces="application/json"
    )
    @ResponseBody
    public Result newCallRecords(
            @RequestBody
            CallRecordRequest callRecordRequest
    ){
        return callsService.newCallRecords(callRecordRequest);
    }

    @RequestMapping(
            value = "contacts_add",
            method = RequestMethod.POST,
            consumes="application/json",
            produces="application/json"
    )
    @ResponseBody
    public Result newContactAdd(
            @RequestBody
            ContactsAddingRequest contactsAddingRequest
    )
    {
        return  callsService.newContactsAddedInCRM(contactsAddingRequest);
    }
}

