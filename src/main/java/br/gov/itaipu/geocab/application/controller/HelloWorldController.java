package br.gov.itaipu.geocab.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * Created by lcvmelo on 16/02/2017.
 */
@RestController
public class HelloWorldController {

    @RequestMapping("/api/hello")
    @PreAuthorize("hasRole('admin')")
    public String index() {
        return "bla";
    }

    @RequestMapping("/api/user")
    public @ResponseBody Principal user(Principal principal) {
        return principal;
    }
}
