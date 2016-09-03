package co.lsnbox.auth.controllers;

import co.lsnbox.auth.services.interfaces.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController

public class AccountController{
    @Autowired
    IAccountService accountService;

    @RequestMapping(value = "/user")
    public ResponseEntity current(Principal principal){
        return new ResponseEntity(accountService.findByEmail(principal.getName()),HttpStatus.OK);
    }
}