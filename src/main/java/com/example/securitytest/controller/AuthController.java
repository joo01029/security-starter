package com.example.securitytest.controller;

import com.example.securitytest.Service.auth.AuthService;
import com.example.securitytest.domain.dto.auth.LoginDto;
import com.example.securitytest.domain.dto.auth.RegisterDto;
import com.example.securitytest.domain.response.Response;
import com.example.securitytest.domain.response.ResponseData;
import com.example.securitytest.domain.response.auth.JwtResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;

	@PostMapping("/register")
	public Response register(@RequestBody RegisterDto registerDto){
		authService.register(registerDto);
		return new Response();
	}

	@PostMapping("/login")
	public ResponseData<JwtResult> login(@RequestBody LoginDto loginDto){
		JwtResult data = authService.login(loginDto);
		return new ResponseData<>(data);
	}

	@PostMapping("/refresh")
	public ResponseData<JwtResult> refresh(@RequestParam String refreshToken){
		JwtResult data = authService.refreshToken(refreshToken);
		return new ResponseData<>(data);
	}
}
