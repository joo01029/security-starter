package com.example.securitytest.Service.auth;

import com.example.securitytest.constant.TimeConstant;
import com.example.securitytest.domain.dto.auth.LoginDto;
import com.example.securitytest.domain.dto.auth.RegisterDto;
import com.example.securitytest.domain.entity.User;
import com.example.securitytest.enums.JwtAuth;
import com.example.securitytest.enums.Role;
import com.example.securitytest.exception.CustomException;
import com.example.securitytest.lib.JwtProvider;
import com.example.securitytest.domain.repo.UserRepo;
import com.example.securitytest.domain.response.auth.JwtResult;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@AllArgsConstructor
@Service
public class AuthServiceImpl implements AuthService{
	private UserRepo userRepo;
	private JwtProvider jwtProvider;

	@Transactional
	@Override
	public void register(RegisterDto registerDto) {
		if(getUserById(registerDto.getId()) != null)
			throw new CustomException(HttpStatus.FORBIDDEN, "이미 존재하는 사람");

		User user = User.builder()
				.id(registerDto.getId())
				.password(registerDto.getPassword())
				.name(registerDto.getName())
				.role(Role.USER)
				.build();

		userRepo.save(user);
	}

	@Override
	@Transactional(readOnly = true)
	public JwtResult login(LoginDto loginDto) {
		User user = getUserById(loginDto.getId());

		if(user == null)
			throw new CustomException(HttpStatus.NOT_FOUND, "존재하지 않는 유저");
		if(!user.getPassword().equals(loginDto.getPassword()))
			throw new CustomException(HttpStatus.FORBIDDEN, "비밀번호가 다릅니다");


		return getJwts(user, true);
	}

	private JwtResult getJwts(User user,  boolean canRefresh_refreshToken){
		int accessExpired_at = TimeConstant.MILLISECOND_HOUR;
		String accessToken = jwtProvider.createToken(user, accessExpired_at, JwtAuth.ACCESS);
		int refreshExpired_at = 4 * TimeConstant.MILLISECOND_DAY;
		String refreshToken = canRefresh_refreshToken ?jwtProvider.createToken(user, refreshExpired_at, JwtAuth.REFRESH) : null;

		return JwtResult.builder()
				.accessToken(accessToken)
				.accessExpiredAt(new Date(System.currentTimeMillis() + accessExpired_at))
				.refreshToken(refreshToken)
				.refreshExpiredAt(canRefresh_refreshToken ? new Date(System.currentTimeMillis() + refreshExpired_at) : null)
				.build();
	}

	@Override
	public JwtResult refreshToken(String refreshToken) {
		Claims claims = jwtProvider.validToken(refreshToken, JwtAuth.REFRESH);
		Long idx = Long.valueOf(claims.get("idx").toString());
		User user = getUserByIdx(idx);

		if(user == null)
			throw new CustomException(HttpStatus.NOT_FOUND, "존재하지 않는 유저");

		boolean canRefresh_refreshToken = false;
		if(claims.getExpiration().getTime() - System.currentTimeMillis() <= TimeConstant.MILLISECOND_DAY)
			canRefresh_refreshToken = true;

		return getJwts(user, canRefresh_refreshToken);
	}

	public User getUserById(String id){
		return userRepo.findById(id).orElse(null);
	}

	public User getUserByIdx(Long idx){
		return userRepo.findByIdx(idx).orElse(null);
	}
}
