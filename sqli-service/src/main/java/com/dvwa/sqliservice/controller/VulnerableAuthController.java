package com.dvwa.sqliservice.controller;

import com.dvwa.sqliservice.entity.AppUser;
import com.dvwa.sqliservice.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class VulnerableAuthController {

    private final AppUserService appUserService;

    @GetMapping("/vulnerable/login")
    public AppUser vulnerableLogin(@RequestParam String username, @RequestParam String password) {
        // VULNERABLE ENDPOINT:
        // Bu endpoint eğitim/lab amacıyla SQL Injection'a açık login metodunu çağırır.
        return appUserService.vulnerableLogin(username, password);
    }
}
