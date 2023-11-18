package com.example.jdnctamitams.config;

import com.example.jdnctamitams.jwt.JwtUtill;
import com.example.jdnctamitams.security.JwtAuthenticationFilter;
import com.example.jdnctamitams.security.JwtAuthorizationFilter;
import com.example.jdnctamitams.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Configuration // 규약
@EnableWebSecurity // Spring Security 지원을 가능하게 함
@RequiredArgsConstructor
//@Order(1)
public class WebSecurityConfig {

    private final JwtUtill jwtUtill;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtAuthorizationFilter jwtAuthorizationFilter;
    // 빈 자체는 규칙을 만드는거다

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        config.addAllowedOriginPattern("*");

        config.addAllowedHeader("*");

        config.addAllowedMethod("*");

        config.addExposedHeader("Authorization");

        config.setAllowCredentials(true);

        config.validateAllowCredentials();

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    @Bean // 패스워드 인코더라는 규칙을 뉴 비크립트 패스워드 인코더를 가져오는 규약이다
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean // 어쏠라제이션 이것도 기능이다 이건 허가 인증 매니저다.
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean // jwt오쏠라제이션 필터
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtUtill);
        filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        return filter;
    }


    @Bean // jwt아우젠티케이션 필터
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(jwtUtill, userDetailsService);
    }

    @Bean // 어떤식으로 필터체인을 쓸건가
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CSRF 설정 사용안한다는거다.
        http.cors();
        http.csrf().disable();
        http.formLogin().disable();


        // 기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하기 위한 설정
        http.sessionManagement((sessionManagement) -> // 화살표는 람다식이다 이런 모습이 있다
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.authorizeHttpRequests((authorizeHttpRequests) ->
                        authorizeHttpRequests

//                        .requestMatchers(HttpMethod.GET).permitAll()
//                        .requestMatchers(HttpMethod.PUT).permitAll()
//                        .requestMatchers(HttpMethod.POST).permitAll()
                                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // resources 접근 허용 설정
                                .antMatchers("/").permitAll() // 메인 페이지 요청 허가
                                .anyRequest().authenticated() // 그 외 모든 요청 인증처리
        );

        http.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

//        // 필터 관리
//        http.addFilter(jwtAuthorizationFilter());
//        // 이클래스에서 로그인 한다는 소리다. 애드 필터를 더한다  UsernamePasswordAuthenticationFilter.class); 이것들
//        return http.build();

        // 필터 관리
        http.addFilterBefore(jwtAuthorizationFilter(), JwtAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        // 이클래스에서 로그인 한다는 소리다. 애드 필터를 더한다  UsernamePasswordAuthenticationFilter.class); 이것들
        return http.build();
    }

}