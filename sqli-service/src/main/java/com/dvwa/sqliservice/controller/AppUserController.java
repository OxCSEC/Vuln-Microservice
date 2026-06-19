package com.dvwa.sqliservice.controller;

import com.dvwa.sqliservice.entity.AppUser;
import com.dvwa.sqliservice.service.AppUserService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class AppUserController {

    private final AppUserService appUserService;

    @GetMapping
    public List<AppUser> getAllUsers() {
        return appUserService.findAll();
    }

    @GetMapping("/{id}")
    public AppUser getUserById(@PathVariable Long id) {
        return appUserService.findById(id);
    }

    @GetMapping("/search")
    public List<AppUser> searchUsers(@RequestParam String username) {
        return appUserService.searchByUsername(username);
    }

    @GetMapping("/vulnerable/search")
    public List<AppUser> vulnerableSearchUsers(@RequestParam String username) {
        // VULNERABLE ENDPOINT:
        // Bu endpoint eğitim/lab amacıyla SQL Injection'a açık servis metodunu çağırır.
        return appUserService.vulnerableSearchByUsername(username);
    }

    @GetMapping("/vulnerable/prepared-search")
    public List<AppUser> vulnerablePreparedStatementSearchUsers(@RequestParam String username) {
        // VULNERABLE ENDPOINT:
        // Bu endpoint eğitim/lab amacıyla PreparedStatement'ın yanlış kullanımından
        // kaynaklanan SQL Injection zafiyetli servis metodunu çağırır.
        return appUserService.vulnerablePreparedStatementSearchByUsername(username);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AppUser createUser(@Valid @RequestBody AppUser appUser) {
        return appUserService.create(appUser);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        appUserService.deleteById(id);
    }
}
