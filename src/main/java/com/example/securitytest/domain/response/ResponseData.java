package com.example.securitytest.domain.response;

import lombok.Getter;

@Getter
public class ResponseData<T> extends Response {
	private T data;

	public ResponseData(T data){
		super();
		this.data = data;
	}
}
