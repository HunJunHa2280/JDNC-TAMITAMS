package com.example.jdnctamitams.security;


// 로그인 입니다 로그인 필터

import com.example.jdnctamitams.dto.LoginRequestDto;
import com.example.jdnctamitams.entity.UserRoleEnum;
import com.example.jdnctamitams.jwt.JwtUtill;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtill jwtUtil;
    // 이걸 상속 받아서 사용한다

    public JwtAuthenticationFilter(JwtUtill jwtUtil) {
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/api/auth/login"); // 이런 셋필터프로세서 url 이거는 필터를 통해서 지나간다 대신 jwt를 사용해서
    }


    @Override // 오버라이드는 부모가 있어서 상속을 받으니까 익스텐드에 있는 메서드를 입맛대로 바꿔서 사용할때 오버라이드를 사용한다.
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class); // 값을 비교하고 이렇게 해서 로그인을 한다는 이야기다
            //로그인 리퀘스트 디티오가 없으니 빨간줄
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getUsername(),
                            requestDto.getPassword(),
                            null
                    )
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }    //이건 대충 로그인 하는 과정이다 라고 생각하자


    @Override // 이건 토큰에다가 정보를 담아주는거다
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        String username = ((UserDetailsImpl) authResult.getPrincipal()).getUsername(); // 유저 jwt유틸 요기서 유저 네임 유저 임플로 만드는거
        UserRoleEnum role = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getRole(); // 역할

        String token = jwtUtil.createToken(username, role);  // 유저 정보랑 임플을 넣어서 토큰을 만든다
        response.addHeader(JwtUtill.AUTHORIZATION_HEADER, token); // 반환할때 헤더에 더한다 jwtUtill 어썬테이케이션 헤더에 토큰값을 넣겠다.

        // 로그인 성공 메세지 전달
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8"); // 유알에프 8로 인코딩한다
        String massage = "로그인 성공";
        response.getWriter().write("상태코드 : " + response.getStatus() +", 메세지 : " +  massage); // 이렇게 주겠다 상태코드는 200 이런거
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {

        // 오류 메세지 전달
        String errorMessage = failed.getMessage();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain");
        response.setStatus(401); // 403이 유저인건 아는데 요기 권한은 없어고 401은 넌 우리 유저가 아니야 이런 느낌이다
        response.getWriter().write("상태코드 : " + response.getStatus() +", 메세지 : " + errorMessage);


    }

}