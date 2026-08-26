package com.eu.demomatrimony.service;

import com.eu.demomatrimony.dto.AuthResponseDto;
import com.eu.demomatrimony.dto.LoginRequestDto;
import com.eu.demomatrimony.dto.RegisterRequestDto;

public interface AuthService {
    String register(RegisterRequestDto request);
    AuthResponseDto login(LoginRequestDto request);
}