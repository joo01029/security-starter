package com.example.securitytest.domain.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterDto {
	private String id;
	private String password;
	private String name;
}
