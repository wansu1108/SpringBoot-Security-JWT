package com.cos.jwt.config.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.cos.jwt.auth.PrincipalDetails;
import com.cos.jwt.model.User;
import com.cos.jwt.repository.UserRepository;

// 시큐리티가 filter가지고 있는데, 그 필터중에 BasicAuthenticationFilter 라는 것이 있음.
// 권한이나 인증이 필요한 특정 주소를 요청했을 때 위 필터를 무조건 타게 되어있음.
// 만약에 권한이나 인증이 필요한 주소가 아니라면 이 필터를 타지않음.
public class JwtAuthorizationFilter extends BasicAuthenticationFilter{
	
	private UserRepository userRepository;

	public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
		super(authenticationManager);
		this.userRepository = userRepository;
	}

	//인증이나 권한이 필요한 주소요청시 해당 필터를 타게됨
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		System.out.println("인증이나 권한이 필요한 주소 요청됨");
		String jwtHeader = request.getHeader("Authorization");
		System.out.println("jwtHeader :" + jwtHeader);
		
		//header가 있는지 확인
		if(jwtHeader == null || !jwtHeader.startsWith("Bearer")) {
			chain.doFilter(request, response);
			return;
		}
		
		//JWT 토큰을 검증해서 정상적인 사용자인지 확인
		String jwtToken = request.getHeader("Authorization").replace("Bearer ", "");
		String username
				= JWT.require(Algorithm.HMAC512("cos")).build().verify(jwtToken).getClaim("username").asString();
		
		if(username != null) {
			User userEntity = userRepository.findByUsername(username);
			PrincipalDetails principalDetails = new PrincipalDetails(userEntity);
			
			// JWT 토큰 서명을 통해서 서명이 정상이면 Authentication객체를 만들어준다.
			// 기존 사용했던 로그인 완료되면 생성하는 방식이 아닌 강제로 생성하는 방식.
			// JWT토큰을 통해 인증(로그인)을 완료한 사용자 이기 때문에, 인증이 완료되었을때 생성해주는 Authentication객체를 생성해도 상관없다.
			// 이때 비밀번호는 null로 만들어도 상관이없다. 이 사용자는 이미 인증을 했기 때문에 id,pwd가 일치하는지에 대한 관심이 없다.
			Authentication authentication 
				= new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
			
			// 시큐리티 세션을 호출하여, Authentication객체를 직접 넣어준다.
			// 권한 인증을 위해 시큐리티 세션을 생성한다.
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
			chain.doFilter(request, response);
		}
	}
}
