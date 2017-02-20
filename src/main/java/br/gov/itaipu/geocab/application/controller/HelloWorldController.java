package br.gov.itaipu.geocab.application.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by lcvmelo on 16/02/2017.
 */
@RestController
public class HelloWorldController {

    @RequestMapping("/")
    public String index() {
        return "bla";
    }
}
