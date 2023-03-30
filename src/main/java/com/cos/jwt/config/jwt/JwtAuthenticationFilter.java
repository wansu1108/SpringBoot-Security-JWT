package com.cos.jwt.config.jwt;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.cos.jwt.auth.PrincipalDetails;
import com.cos.jwt.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

// 스프링 시큐리티에는 UsernamePasswordAuthenticationFilter가 존재함
// /login 요청해서 username,password 전송하면 (post)
// UsernamePasswordAuthenticationFilter 동작함
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
	
	private final AuthenticationManager authenticationManager; // 실제 로그인을 실행하는 객체

	// /login 요청을 하면 로그인 시도를 위해서 실행되는 함수
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		System.out.println("JwtAuthenticationFilter : 로그인 시도중");
		
		try {
			// 1. username, password 
			InputStream inputStream = request.getInputStream();
			String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
			ObjectMapper objectMapper = new ObjectMapper(); // json을 파싱해서 객체에 넣어준다. x-www-form-urlencoded는 파싱이 안됨
			User user = objectMapper.readValue(messageBody, User.class);
			
			UsernamePasswordAuthenticationToken authenticationToken = 
					new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
			
			// authenticationManager로 로그인 시도, PrincipalUserDetailsSerivce가 호출되고, loadByUsername()함수가 실행됨.
			Authentication authentication = authenticationManager.authenticate(authenticationToken);
			System.out.println(authenticationToken);
			
			
			// 3. PrincipalDetails를 세션에 담는다.(권한 관리를 위해서)
			PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
			return authentication;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	// attemptAuthentication실행 후 인증이 정상적으로되었으면 , successfulAuthentication 함수가 실행된다.
	// JWT 토큰을 만들어서 rqeust요청한 사용자에게 JWT토큰을 response해주면 된다.
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		System.out.println("successfulAuthentication 실행됨 , 인증이 완료되었다는 의미");
		
		PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
		
		// RSA : 공개키/비밀키 방식
		// Hash: 서버만 알고있는 secret key
		String jwtToken = JWT.create()
				.withSubject("cos토큰")
				.withExpiresAt(new Date(System.currentTimeMillis()+(60000*10)))
				.withClaim("id", principalDetails.getUser().getId())
				.withClaim("username", principalDetails.getUser().getUsername())
				.sign(Algorithm.HMAC512("cos"));
		
		response.addHeader("Authorization", "Bearer " + jwtToken);
	}
}
