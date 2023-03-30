package com.cos.jwt.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyFilter3 implements Filter{

	@Override
	// Http-Method = POST
	// Http-Header Authorization에 토큰("cos") 로직 실행
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		
//		if("POST".equals(req.getMethod())) {
//			System.out.println("POST 요청됨");
//			String headerAuth = req.getHeader("Authorization");
//			System.out.println(headerAuth);
//			System.out.println("필터3");
//			
//			// 1. 로그인 로직에서, 로그인 완료시 토큰 생성 및 응답을 통해 클라이언트에게 전달
//			// 2. 클라이언트는 모든 요청에 토큰을 넣어서 요청 (Http-header(Authorization))
//			// 3. 토큰 검증
//			if ("cos".equals(headerAuth)) {
//			} else {
//				PrintWriter out = resp.getWriter();
//				out.println("인증 안됨");
//			}
//			
//		}
		chain.doFilter(req, resp);
	}

}
