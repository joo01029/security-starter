package com.example.securitytest.Service.auth;

import com.example.securitytest.domain.dto.auth.LoginDto;
import com.example.securitytest.domain.dto.auth.RegisterDto;
import com.example.securitytest.domain.response.auth.JwtResult;

public interface AuthService {
	void register(RegisterDto registerDto);
	JwtResult login(LoginDto loginDto);

	JwtResult refreshToken(String refreshToken);
}
