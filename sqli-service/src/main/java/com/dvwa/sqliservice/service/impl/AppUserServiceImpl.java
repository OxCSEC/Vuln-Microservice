package com.dvwa.sqliservice.service.impl;

import com.dvwa.sqliservice.entity.AppUser;
import com.dvwa.sqliservice.repository.AppUserRepository;
import com.dvwa.sqliservice.service.AppUserService;
import jakarta.persistence.EntityManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import javax.sql.DataSource;

@Service
@RequiredArgsConstructor
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository appUserRepository;
    private final EntityManager entityManager;
    private final DataSource dataSource;

    @Override
    public List<AppUser> findAll() {
        return appUserRepository.findAll();
    }

    @Override
    public AppUser findById(Long id) {
        return appUserRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @Override
    public List<AppUser> searchByUsername(String username) {
        return appUserRepository.findByUsernameContainingIgnoreCase(username);
    }

    @Override
    public List<AppUser> vulnerableSearchByUsername(String username) {
        // VULNERABLE CODE START:
        // Kullanıcı girdisi SQL sorgusuna doğrudan string birleştirme ile ekleniyor.
        // Bu kullanım kasıtlı olarak SQL Injection zafiyeti üretir.
        String sql = "SELECT id, username, email, password, full_name FROM users WHERE username = '" + username + "'";
        // VULNERABLE CODE END
        return entityManager.createNativeQuery(sql, AppUser.class).getResultList();
    }

    @Override
    public List<AppUser> vulnerablePreparedStatementSearchByUsername(String username) {
        // VULNERABLE CODE START:
        // Burada PreparedStatement kullanılıyor gibi görünse de kullanıcı girdisi
        // SQL string'ine prepareStatement çağrılmadan önce doğrudan ekleniyor.
        // Bu nedenle kullanım kasıtlı olarak SQL Injection zafiyeti üretir.
        String sql = "SELECT id, username, email, password, full_name FROM users WHERE username = '" + username + "'";
        // VULNERABLE CODE END

        List<AppUser> users = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                users.add(AppUser.builder()
                        .id(resultSet.getLong("id"))
                        .username(resultSet.getString("username"))
                        .email(resultSet.getString("email"))
                        .password(resultSet.getString("password"))
                        .fullName(resultSet.getString("full_name"))
                        .build());
            }

            return users;
        } catch (SQLException exception) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "PreparedStatement based vulnerable query failed",
                    exception
            );
        }
    }

    @Override
    public AppUser vulnerableLogin(String username, String password) {
        // VULNERABLE CODE START:
        // Username ve password değerleri SQL sorgusuna doğrudan ekleniyor.
        // Bu kullanım kasıtlı olarak login benzeri SQL Injection zafiyeti üretir.
        String sql = "SELECT id, username, email, password, full_name FROM users " +
                "WHERE username = '" + username + "' AND password = '" + password + "'";
        // VULNERABLE CODE END
        List<AppUser> results = entityManager.createNativeQuery(sql, AppUser.class).getResultList();
        return results.stream()
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));
    }

    @Override
    public AppUser create(AppUser appUser) {
        return appUserRepository.save(appUser);
    }

    @Override
    public void deleteById(Long id) {
        if (!appUserRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        appUserRepository.deleteById(id);
    }
}
