package com.cos.jwt.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.jwt.auth.PrincipalDetails;
import com.cos.jwt.model.User;
import com.cos.jwt.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class RestApiController {
	
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@GetMapping("/home")
	public String home() {
		return "<h2>home</h2>";
	}
	
	@PostMapping("/token")
	public String token() {
		return "<h2>token</h2>";
	}
	
	@PostMapping("/join")
	public String join(@RequestBody User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setRoles("ROLE_USER");
		userRepository.save(user);
		return "회원가입 완료";
	}
	
	@GetMapping("/user")
	public String withNoRoles(Authentication authentication) {
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		System.out.println("user: " + principalDetails.getUser().getUsername());
		return "withNoRoles";
	}
	
	// user,manager,admin 접근 가능
	@GetMapping("/api/v1/user")
	public String user(Authentication authentication) {
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		System.out.println("user: " + principalDetails.getUser().getUsername());
		return "user";
	}
	
	// manager,admin 접근 가능
	@GetMapping("/api/v1/manager")
	public String manager() {
		return "manager";
	}
	
	// admin 접근 가능
	@GetMapping("/api/v1/admin")
	public String admin() {
		return "admin";
	}
}
