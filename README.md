# 스프링부트 시큐리티 + JWT
https://github.com/codingspecialist/Springboot-Security-JWT-Easy
강의를 참고하여 내용을 정리하였습니다.

# 인증
## 로그인
+ URL: POST /login
	+ application/json
	+ username , password
	
## UsernamePasswordAuthenticationFilter 등록
+ 로그인 요청시 UsernamePasswordAuthenticationFilter 동작한다
+ 오버라이딩된 attemptAuthentication()이 동작한다
+ requestBody의 username, password를 ObjectMapper로 받는다
+ UsernamePasswordAuthenticationToken으로 Authentication 객체를 만든다.
+ Authentication객체를 만들때 자동으로 UserDetailsService가 호출된다.
	+ authenticationManager.authenticate 실행 -> UserDetailsService의 loadByUsername()함수가 실행된다.
	+ 그렇기 때문에 UserDetailsService를 상속하여 직접 서비스를 구현한다.
	+ UserDetailsService를 통해서 리턴될 UserDetails을 커스텀해서 구현한다.
	
### successfulAuthentication
+ attemptAuthentication실행 후 인증이 정상적으로되었으면 , successfulAuthentication 함수가 실행된다
+ JWT 토큰을 생성한다.
+ 생성된 JWT토큰을 response에 넣어서 클라이언트에게 반환한다
	
# 인가
+ Tip : JWT를 사용하면 UserDetailsService를 호출하지 않기 때문에 @AuthenticationPrincipal 사용 불가능
+ 왜냐하면 @AuthenticationPrincipal은 UserDetailsService에서 리턴될 때 만들어지기 때문이다.
+ Tip : 토큰 검증 (이게 인증이기 때문에 AuthenticationManager도 필요 없음)
+ Tip : 스프링 시큐리티가 수행해주는 권한 처리를 위해 아래와 같이 토큰을 만들어서 Authentication 객체를 강제로 만들고 그걸 세션에 저장!

## BasicAuthenticationFilter 등록
```java

// 토큰을 검증해서 정상적인 사용자인지 확인
String jwtToken = request.getHeader("Authorization").replace("Bearer ", "");
String username
	= JWT.require(Algorithm.HMAC512("cos")).build().verify(jwtToken).getClaim("username").asString();
	
// DB에서 유저정보 조회
User user = userRepository.findByUsername(username);
	
// Authentication 객체 강제로 생성
PrincipalDetails principalDetails = new PrincipalDetails(user);
Authentication authentication =
	new UsernamePasswordAuthenticationToken(
        principalDetails, //나중에 컨트롤러에서 DI해서 쓸 때 사용하기 편함.
        null, // 패스워드는 모르니까 null 처리, 어차피 지금 인증하는게 아니니까!!
        principalDetails.getAuthorities());

// 강제로 시큐리티의 세션에 접근하여 값 저장
SecurityContextHolder.getContext().setAuthentication(authentication);
```



