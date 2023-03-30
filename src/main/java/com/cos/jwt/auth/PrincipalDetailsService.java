package com.cos.jwt.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cos.jwt.model.User;
import com.cos.jwt.repository.UserRepository;

import lombok.RequiredArgsConstructor;

// http://localhost:8080/login 요청시 UserService 실행 (기본 로그인 요청주소)
// /login 요청시 시큐리티에 내장된 필터(UsernamePasswordAuthenticationFilter)에 의해 UserService 실행
// SecurityConfig에서 FormLogin.disable() 했기 때문에, /login 요청을 해도 UserDetailsService 실행이 안됨
// 직접 필터를 생성하고, 시큐리티 필터에 등록해주면됨 JwtAuthenticationFilter
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService{
	
	private final UserRepository repository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("PrincipalDetailsService의 loadUserByUsername()");
		User userEntity = repository.findByUsername(username);
		System.out.println("userEntity : " + userEntity.toString());
		return new PrincipalDetails(userEntity);
	}
	
}
