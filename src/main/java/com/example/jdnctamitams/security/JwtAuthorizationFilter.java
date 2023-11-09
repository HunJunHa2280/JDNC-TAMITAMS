package com.example.jdnctamitams.security;


import com.example.jdnctamitams.jwt.JwtUtill;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Component("AuthFilter") // 컴포너트 안썼고 스프링에서 사용중인 언어가 있어서 어쓰 필터로 따로 이름을 지정해줌 컴포넌트에다가
@Slf4j(topic = "JWT 검증 및 인가")
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtill jwtUtill;
    private final UserDetailsServiceImpl userDetailsService; // 받아온다는 내용 유틸에 있는 내용을 사용한다 유저서비스 임플


//    public JwtAuthorizationFilter(JwtUtill jwtUtill, UserDetailsServiceImpl userDetailsService) { //유저디테일을 임플로 만들어주는
//        this.jwtUtill = jwtUtill;
//        this.userDetailsService = userDetailsService;
//    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException { // ServletException, IOException 예외 인터넷 예외를 던져준다

        String tokenValue = jwtUtill.getJwtFromHeader(req); // 토큰벨류 겟프롬 토큰 헤더에서 가져온다 jwt유틸 안에 있는 .getjwtFromHeader 헤더 http서블렉 리퀘스트에서 가져온다
        // jwt헤더에 있는 값을 가져온다 헤더에 있는 정해져있는 값을 가져온다 옆동네에서 정의한걸 가져온다 그리고 이걸 가져온걸 스트링 토큰벨류라 한다/
        if (StringUtils.hasText(tokenValue)) { // 요기서 토큰을 깨먹는다는 것

            if (!jwtUtill.validateToken(tokenValue)) {  // 느낌표는 반대다 그러니까 펄스가 나오는게 에러로 판단되라
                log.error("Token Error");
                return;
            }

            Claims info = jwtUtill.getUserInfoFromToken(tokenValue); // 토큰벨류를 사용해서 겟유저인포프롬 토큰을 사용해서 클라임스를 가져오고 인포로 이름을 바꾼다. 이거는 타입이다
            // 이런 타입을 인포라는 이름을 바꿨따 근데 이름을 바꾼다는 것 보다 지정해준다는게 좋다

            try { // 시도해라
                setAuthentication(info.getSubject()); // 역활 주는걸 시도해라
            } catch (Exception e) { // 했는데 에러면
                log.error(e.getMessage()); // 오류를 주고 끝냈다
                return;
            }
        }

        filterChain.doFilter(req, res); // 계속 하라고
    }

    // 인증 처리 어떤식으로 할거냐
    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }  // 이 유저는 인증된 유저다

    // 인증 객체 생성 요기서 객체를 생성
    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()); // 토큰 통과했다는 말 인가한 객체를 만들었다는거다
    }
    //인증된 유저를 가지고 이런걸 만든다

}