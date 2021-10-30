package com.example.securitytest.lib;

import com.example.securitytest.exception.CustomException;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;

public class CheckJwtType {
	private static final String authorization = "Authorization";

	public static String extract(HttpServletRequest request, String type){
		String header = request.getHeader(authorization);
		if(header != null){
			if(header.toLowerCase().startsWith(type.toLowerCase())){
				return header.substring(type.length()).trim();
			}else{
				throw new CustomException(HttpStatus.BAD_REQUEST,"토큰 검증 오류");
			}

		}
		return null;
	}
}
