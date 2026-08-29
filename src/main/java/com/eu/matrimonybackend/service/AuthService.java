package com.eu.matrimonybackend.service;

import com.eu.matrimonybackend.dto.AuthResponseDto;
import com.eu.matrimonybackend.dto.LoginRequestDto;
import com.eu.matrimonybackend.dto.RegisterRequestDto;

public interface AuthService {
    String register(RegisterRequestDto request);
    AuthResponseDto login(LoginRequestDto request);
}