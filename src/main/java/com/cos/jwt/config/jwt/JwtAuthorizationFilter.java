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

// ��ť��Ƽ�� filter������ �ִµ�, �� �����߿� BasicAuthenticationFilter ��� ���� ����.
// �����̳� ������ �ʿ��� Ư�� �ּҸ� ��û���� �� �� ���͸� ������ Ÿ�� �Ǿ�����.
// ���࿡ �����̳� ������ �ʿ��� �ּҰ� �ƴ϶�� �� ���͸� Ÿ������.
public class JwtAuthorizationFilter extends BasicAuthenticationFilter{
	
	private UserRepository userRepository;

	public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
		super(authenticationManager);
		this.userRepository = userRepository;
	}

	//�����̳� ������ �ʿ��� �ּҿ�û�� �ش� ���͸� Ÿ�Ե�
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		System.out.println("�����̳� ������ �ʿ��� �ּ� ��û��");
		String jwtHeader = request.getHeader("Authorization");
		System.out.println("jwtHeader :" + jwtHeader);
		
		//header�� �ִ��� Ȯ��
		if(jwtHeader == null || !jwtHeader.startsWith("Bearer")) {
			chain.doFilter(request, response);
			return;
		}
		
		//JWT ��ū�� �����ؼ� �������� ��������� Ȯ��
		String jwtToken = request.getHeader("Authorization").replace("Bearer ", "");
		String username
				= JWT.require(Algorithm.HMAC512("cos")).build().verify(jwtToken).getClaim("username").asString();
		
		if(username != null) {
			User userEntity = userRepository.findByUsername(username);
			PrincipalDetails principalDetails = new PrincipalDetails(userEntity);
			
			// JWT ��ū ������ ���ؼ� ������ �����̸� Authentication��ü�� ������ش�.
			// ���� ����ߴ� �α��� �Ϸ�Ǹ� �����ϴ� ����� �ƴ� ������ �����ϴ� ���.
			// JWT��ū�� ���� ����(�α���)�� �Ϸ��� ����� �̱� ������, ������ �Ϸ�Ǿ����� �������ִ� Authentication��ü�� �����ص� �������.
			// �̶� ��й�ȣ�� null�� ���� ����̾���. �� ����ڴ� �̹� ������ �߱� ������ id,pwd�� ��ġ�ϴ����� ���� ������ ����.
			Authentication authentication 
				= new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
			
			// ��ť��Ƽ ������ ȣ���Ͽ�, Authentication��ü�� ���� �־��ش�.
			// ���� ������ ���� ��ť��Ƽ ������ �����Ѵ�.
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
			chain.doFilter(request, response);
		}
	}
}
