package com.cos.jwt.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cos.jwt.filter.MyFilter1;
import com.cos.jwt.filter.MyFilter2;

@Configuration
public class FilterConfig {

	@Bean
	public FilterRegistrationBean<MyFilter1> filter1(){
		FilterRegistrationBean<MyFilter1> registry = new FilterRegistrationBean<>(new MyFilter1());
		registry.addUrlPatterns("/*");
		registry.setOrder(0);
		return registry;
	}
	
	@Bean
	public FilterRegistrationBean<MyFilter2> filter2(){
		FilterRegistrationBean<MyFilter2> registry = new FilterRegistrationBean<>(new MyFilter2());
		registry.addUrlPatterns("/*");
		registry.setOrder(1);
		return registry;
	}
	
}
