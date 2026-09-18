package com.eu.matrimonybackend.serviceImpl;

import com.eu.matrimonybackend.dto.AuthResponseDto;
import com.eu.matrimonybackend.dto.LoginRequestDto;
import com.eu.matrimonybackend.dto.RegisterRequestDto;
import com.eu.matrimonybackend.models.Profile;
import com.eu.matrimonybackend.enums.Role;
import com.eu.matrimonybackend.models.User;
import com.eu.matrimonybackend.repositories.UserRepository;
import com.eu.matrimonybackend.security.JwtTokenProvider;
import com.eu.matrimonybackend.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           JwtTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    /**
     * Registers a new user account with a default {@code ROLE_USER}, creating and linking a
     * blank {@link Profile} the user can fill in later.
     *
     * @param request the registration payload (email, raw password, and display name); the
     *                password is BCrypt-hashed before persistence and never stored in plain text
     * @return a human-readable confirmation message
     * @throws IllegalArgumentException if the email is already registered
     */
    @Override
    public String register(RegisterRequestDto request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email is already registered.");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(Set.of(Role.ROLE_USER));

        Profile profile = new Profile();
        profile.setName(request.getName());
        profile.setEmail(request.getEmail());
        user.setProfile(profile);

        User saved = userRepository.save(user);
        log.info("User account created with ID: {}", saved.getId());
        return "User registered successfully!";
    }

    /**
     * Authenticates a user's credentials and, on success, issues a signed JWT for subsequent
     * requests.
     *
     * @param request the login payload containing email and raw password
     * @return an {@link AuthResponseDto} wrapping the issued JWT
     * @throws org.springframework.security.core.AuthenticationException if the email/password
     *         combination does not match any account
     */
    @Override
    public AuthResponseDto login(LoginRequestDto request) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (AuthenticationException ex) {
            log.warn("Failed login attempt for username: {}", request.getEmail());
            throw ex;
        }

        String token = tokenProvider.generateToken(authentication);
        return new AuthResponseDto(token);
    }
}