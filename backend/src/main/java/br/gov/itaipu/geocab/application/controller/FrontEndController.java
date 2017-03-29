package br.gov.itaipu.geocab.application.controller;

import br.gov.itaipu.geocab.application.configuration.FrontEndConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by lcvmelo on 29/03/2017.
 */
@RestController
@RequestMapping("/api/.config")
public class FrontEndController {

    @Autowired
    private FrontEndConfiguration configuration;

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody FrontEndConfiguration getConfig() {
        return this.configuration;
    }
}
