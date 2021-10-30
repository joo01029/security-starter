package com.example.securitytest.config.Interceptor;

import com.example.securitytest.domain.entity.User;
import com.example.securitytest.exception.CustomException;
import com.example.securitytest.domain.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class GetUserDataInterceptor implements HandlerInterceptor {
	@Autowired
	private UserRepo userRepo;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		Long idx = Long.valueOf(request.getAttribute("idx").toString());
		User user = userRepo.findByIdx(idx).orElseGet(()-> {
			throw new CustomException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.");
		});

		request.setAttribute("user", user);

		return true;
	}
}
