package com.example.securitytest.config;

import com.example.securitytest.config.Interceptor.GetUserDataInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class GetUserDataInterceptorConfig implements WebMvcConfigurer {
	@Autowired
	private GetUserDataInterceptor getUserDataInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry){
		registry.addInterceptor(getUserDataInterceptor)
				.addPathPatterns("/user");
	}
}
