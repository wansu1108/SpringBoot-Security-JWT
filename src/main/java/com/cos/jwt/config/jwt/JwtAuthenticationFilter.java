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

// ������ ��ť��Ƽ���� UsernamePasswordAuthenticationFilter�� ������
// /login ��û�ؼ� username,password �����ϸ� (post)
// UsernamePasswordAuthenticationFilter ������
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
	
	private final AuthenticationManager authenticationManager; // ���� �α����� �����ϴ� ��ü

	// /login ��û�� �ϸ� �α��� �õ��� ���ؼ� ����Ǵ� �Լ�
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		System.out.println("JwtAuthenticationFilter : �α��� �õ���");
		
		try {
			// 1. username, password 
			InputStream inputStream = request.getInputStream();
			String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
			ObjectMapper objectMapper = new ObjectMapper(); // json�� �Ľ��ؼ� ��ü�� �־��ش�. x-www-form-urlencoded�� �Ľ��� �ȵ�
			User user = objectMapper.readValue(messageBody, User.class);
			
			UsernamePasswordAuthenticationToken authenticationToken = 
					new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
			
			// authenticationManager�� �α��� �õ�, PrincipalUserDetailsSerivce�� ȣ��ǰ�, loadByUsername()�Լ��� �����.
			Authentication authentication = authenticationManager.authenticate(authenticationToken);
			System.out.println(authenticationToken);
			
			
			// 3. PrincipalDetails�� ���ǿ� ��´�.(���� ������ ���ؼ�)
			PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
			return authentication;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	// attemptAuthentication���� �� ������ ���������εǾ����� , successfulAuthentication �Լ��� ����ȴ�.
	// JWT ��ū�� ���� rqeust��û�� ����ڿ��� JWT��ū�� response���ָ� �ȴ�.
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		System.out.println("successfulAuthentication ����� , ������ �Ϸ�Ǿ��ٴ� �ǹ�");
		
		PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
		
		// RSA : ����Ű/���Ű ���
		// Hash: ������ �˰��ִ� secret key
		String jwtToken = JWT.create()
				.withSubject("cos��ū")
				.withExpiresAt(new Date(System.currentTimeMillis()+(60000*10)))
				.withClaim("id", principalDetails.getUser().getId())
				.withClaim("username", principalDetails.getUser().getUsername())
				.sign(Algorithm.HMAC512("cos"));
		
		response.addHeader("Authorization", "Bearer " + jwtToken);
	}
}
