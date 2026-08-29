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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
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

        userRepository.save(user);
        return "User registered successfully!";
    }

    @Override
    public AuthResponseDto login(LoginRequestDto request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        String token = tokenProvider.generateToken(authentication);
        return new AuthResponseDto(token);
    }
}