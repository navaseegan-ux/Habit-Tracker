package com.example.project1.Habit.Tracker.service;

import com.example.project1.Habit.Tracker.model.AppUser;
import com.example.project1.Habit.Tracker.repository.AppUserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.regex.Pattern;

@Service
public class AppUserService implements UserDetailsService {

    private static final Pattern EMAIL_PATTERN =
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AppUserService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public RegistrationResult registerUser(String username, String email, String rawPassword) {
        String normalizedUsername = normalizeUsername(username);
        String normalizedEmail = normalizeEmail(email);

        if (normalizedUsername.isBlank()) {
            return RegistrationResult.failure("Username is required.");
        }

        if (normalizedUsername.length() < 3) {
            return RegistrationResult.failure("Username must be at least 3 characters.");
        }

        if (normalizedEmail.isBlank()) {
            return RegistrationResult.failure("Email is required.");
        }

        if (!EMAIL_PATTERN.matcher(normalizedEmail).matches()) {
            return RegistrationResult.failure("Enter a valid email address.");
        }

        if (rawPassword == null || rawPassword.length() < 6) {
            return RegistrationResult.failure("Password must be at least 6 characters.");
        }

        if (appUserRepository.existsByUsernameIgnoreCase(normalizedUsername)) {
            return RegistrationResult.failure("That username is already taken.");
        }

        if (appUserRepository.existsByEmailIgnoreCase(normalizedEmail)) {
            return RegistrationResult.failure("That email is already registered.");
        }

        AppUser appUser = new AppUser(
            normalizedUsername,
            normalizedEmail,
            passwordEncoder.encode(rawPassword),
            "USER"
        );
        appUserRepository.save(appUser);

        return RegistrationResult.success("Account created.");
    }

    public void ensureDefaultUser(String username, String email, String rawPassword) {
        String normalizedUsername = normalizeUsername(username);
        String normalizedEmail = normalizeEmail(email);

        if (normalizedUsername.isBlank() || rawPassword == null || rawPassword.isBlank()) {
            return;
        }

        if (appUserRepository.existsByUsernameIgnoreCase(normalizedUsername)) {
            return;
        }

        String defaultEmail = normalizedEmail.isBlank()
            ? normalizedUsername.toLowerCase(Locale.ROOT) + "@habittracker.local"
            : normalizedEmail;

        AppUser defaultUser = new AppUser(
            normalizedUsername,
            defaultEmail,
            passwordEncoder.encode(rawPassword),
            "USER"
        );
        appUserRepository.save(defaultUser);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = appUserRepository.findByUsernameIgnoreCase(normalizeUsername(username))
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return User.withUsername(appUser.getUsername())
            .password(appUser.getPasswordHash())
            .roles(appUser.getRole())
            .build();
    }

    private String normalizeUsername(String username) {
        return username == null ? "" : username.trim();
    }

    private String normalizeEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase(Locale.ROOT);
    }
}
