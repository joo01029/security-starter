package com.example.securitytest.handler;

import com.example.securitytest.exception.CustomException;
import com.example.securitytest.domain.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(CustomException.class)
	public ResponseEntity<Response> handleCustomException(CustomException e){
		Response response = new Response(e.getStatus().value(), e.getMessage());

		return new ResponseEntity<Response>(response, e.getStatus());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Response> handleCustomException(Exception e){
		e.printStackTrace();
		Response response = new Response(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버에러");

		return new ResponseEntity<Response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
