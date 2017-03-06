package com.berzellius.integrations.apeyronled.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.ParseException;

/**
 * Created by berz on 20.09.2015.
 */
@Controller
@RequestMapping(value = "/")
public class IndexController extends BaseController {


    @RequestMapping
    public String indexPage(
            Model model,
            String code,
            String domain
    ) throws ParseException {
        model.addAttribute("code", code);
        model.addAttribute("domain", domain);

        return "index";
    }

}
