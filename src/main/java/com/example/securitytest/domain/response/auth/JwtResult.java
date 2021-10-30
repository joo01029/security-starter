package com.example.securitytest.domain.response.auth;

import lombok.*;

import java.util.Date;


@Getter
@Setter
@Builder
public class JwtResult {
	private String accessToken;
	private Date accessExpiredAt;
	private String refreshToken;
	private Date refreshExpiredAt;

	public static JwtResultBuilder builder(){
		return new JwtResultBuilder();
	}

}
