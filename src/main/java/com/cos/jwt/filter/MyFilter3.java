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
	// Http-Header Authorization�� ��ū("cos") ���� ����
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		
//		if("POST".equals(req.getMethod())) {
//			System.out.println("POST ��û��");
//			String headerAuth = req.getHeader("Authorization");
//			System.out.println(headerAuth);
//			System.out.println("����3");
//			
//			// 1. �α��� ��������, �α��� �Ϸ�� ��ū ���� �� ������ ���� Ŭ���̾�Ʈ���� ����
//			// 2. Ŭ���̾�Ʈ�� ��� ��û�� ��ū�� �־ ��û (Http-header(Authorization))
//			// 3. ��ū ����
//			if ("cos".equals(headerAuth)) {
//			} else {
//				PrintWriter out = resp.getWriter();
//				out.println("���� �ȵ�");
//			}
//			
//		}
		chain.doFilter(req, resp);
	}

}
