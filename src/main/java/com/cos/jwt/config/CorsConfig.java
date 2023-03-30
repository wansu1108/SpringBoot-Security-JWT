package com.cos.jwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
	
	@Bean
	// ������ �����ӿ�ũ�� ����ִ� Cors ����
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true); //�������� ������ �� �� json�� �ڹٽ�ũ��Ʈ���� ó���� �� �ְ� ������ ����
		config.addAllowedOrigin("*");// ��� ip ������ ���
		config.addAllowedHeader("*");// ��� header ������ ���
		config.addAllowedMethod("*");// ��� method(post, get , put ,patch) ������ ���
		source.registerCorsConfiguration("/api/**", config);
		return new CorsFilter(source);
	}
	
}
