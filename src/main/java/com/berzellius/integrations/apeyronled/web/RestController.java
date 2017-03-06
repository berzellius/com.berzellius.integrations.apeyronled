package com.berzellius.integrations.apeyronled.web;

import com.berzellius.integrations.apeyronled.dto.TestRequest;
import com.berzellius.integrations.apeyronled.dto.TestResult;
import javassist.NotFoundException;
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

    @RequestMapping(
            value = "call_webhook",
            method = RequestMethod.POST,
            consumes="application/json",
            produces="application/json"
    )
    @ResponseBody
    public TestResult newCallWebhook(
            @RequestBody
            TestRequest testRequest
    ) throws NotFoundException {
        System.out.println("test! " + testRequest.getRequest());
        //throw new NotFoundException("out of service!");
        TestResult testResult = new TestResult();
        testResult.setSuccess("пока!");

        return testResult;
    }
}

