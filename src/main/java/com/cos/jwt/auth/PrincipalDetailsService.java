package com.cos.jwt.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cos.jwt.model.User;
import com.cos.jwt.repository.UserRepository;

import lombok.RequiredArgsConstructor;

// http://localhost:8080/login ��û�� UserService ���� (�⺻ �α��� ��û�ּ�)
// /login ��û�� ��ť��Ƽ�� ����� ����(UsernamePasswordAuthenticationFilter)�� ���� UserService ����
// SecurityConfig���� FormLogin.disable() �߱� ������, /login ��û�� �ص� UserDetailsService ������ �ȵ�
// ���� ���͸� �����ϰ�, ��ť��Ƽ ���Ϳ� ������ָ�� JwtAuthenticationFilter
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService{
	
	private final UserRepository repository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("PrincipalDetailsService�� loadUserByUsername()");
		User userEntity = repository.findByUsername(username);
		System.out.println("userEntity : " + userEntity.toString());
		return new PrincipalDetails(userEntity);
	}
	
}
