package com.example.securitytest.config;

import com.example.securitytest.config.filter.JwtFilter;
import com.example.securitytest.lib.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private final JwtProvider jwtProvider;

	private final HandlerExceptionResolver handlerExceptionResolver;
	@Override
	public void configure(WebSecurity web) {
		web.ignoring()
			.antMatchers("/swagger-ui.html");
	}

	@Override
	public void configure(HttpSecurity http){
		try {
			http
	            .httpBasic().disable()
					.cors().configurationSource(corsConfiguration())
					.and()
					.csrf().disable()
					.authorizeRequests()
					.antMatchers("/auth","auth/**").permitAll();
			JwtFilter jwtFilter = new JwtFilter(handlerExceptionResolver, jwtProvider);
			http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
		} catch (Exception exception) {
			exception.printStackTrace();
		}

	}
	@Bean
	public CorsConfigurationSource corsConfiguration(){
		CorsConfiguration configuration = new CorsConfiguration();

		configuration.setAllowedOriginPatterns(Collections.singletonList("*"));
		configuration.addAllowedHeader("*");
		configuration.addAllowedMethod("*");
		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
