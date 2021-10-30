package com.example.securitytest.lib;

import com.example.securitytest.domain.entity.User;
import com.example.securitytest.enums.JwtAuth;
import com.example.securitytest.exception.CustomException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtProvider {
	@Value("${jwt.auth.access}")
	String ACCESSSECRET_KEY;
	@Value("${jwt.auth.refresh}")
	String REFRESHSECRET_KEY;

	public String createToken(User user, long ttlMillis, JwtAuth authType) {
		if (ttlMillis <= 0)
			throw new RuntimeException("Expiry time must be greater than Zero : [" + ttlMillis + "] ");

		try {
			Key signingKey = makeSigningKey(authType);
			Map<String, Object> claims = new HashMap<>();
			claims.put("idx", user.getIdx());
			claims.put("role", user.getRole());
			return encodingToken(claims, signingKey, ttlMillis, authType);
		} catch (CustomException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

	private Key makeSigningKey(JwtAuth authType) {
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		byte[] secretKey;
		try {
			if (authType == JwtAuth.REFRESH)
				secretKey = DatatypeConverter.parseBase64Binary(REFRESHSECRET_KEY);
			else
				secretKey = DatatypeConverter.parseBase64Binary(ACCESSSECRET_KEY);

			return new SecretKeySpec(secretKey, signatureAlgorithm.getJcaName());
		} catch (Exception e) {
			e.printStackTrace();
			throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "key생성 에러");
		}
	}

	private String encodingToken(Map<String, Object> body, Key secretKey, long ttlMillis, JwtAuth authType) {
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		try {
			return Jwts.builder()
					.setClaims(body)
					.setIssuedAt(new Date(System.currentTimeMillis()))
					.setExpiration(new Date(System.currentTimeMillis() + ttlMillis))
					.signWith(secretKey, signatureAlgorithm)
					.compact();


		} catch (Exception e) {
			e.printStackTrace();
			throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "토큰 생성 에러");
		}
	}

	public Claims validToken(String token, JwtAuth jwtAuth) {
		try{
			Key key = makeSigningKey(jwtAuth);
			Claims claims = Jwts.parserBuilder().setSigningKey(key)
					.build()
					.parseClaimsJws(token)
					.getBody();
			return claims;
		}catch (ExpiredJwtException e) {
			throw new CustomException(HttpStatus.GONE, "토큰 만료");
		}catch (SignatureException | MalformedJwtException e){
			throw new CustomException(HttpStatus.UNAUTHORIZED, "토큰 위조");
		}catch (Exception e){
			throw e;
		}
	}
}
