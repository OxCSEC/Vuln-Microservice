package com.dvwa.sqliservice.service;

import com.dvwa.sqliservice.entity.AppUser;
import java.util.List;

public interface AppUserService {

    List<AppUser> findAll();

    AppUser findById(Long id);

    List<AppUser> searchByUsername(String username);

    List<AppUser> vulnerableSearchByUsername(String username);

    List<AppUser> vulnerablePreparedStatementSearchByUsername(String username);

    AppUser vulnerableLogin(String username, String password);

    AppUser create(AppUser appUser);

    void deleteById(Long id);
}
