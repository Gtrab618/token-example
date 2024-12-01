package com.test.token.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestRolesControler {
    @GetMapping("accesAdmin")
    @PreAuthorize("hasRole('ADMIN')")
    public String accesAdmin(){
        return "rol admin";
    }

    @GetMapping("accesUser")
    @PreAuthorize("hasRole('USER')")
    public String accesUser(){
        return "rol User";
    }

    @GetMapping("accesInvited")
    @PreAuthorize("hasRole('INVITED')")
    public String accesInvited(){
        return "rol Invited";
    }


}
