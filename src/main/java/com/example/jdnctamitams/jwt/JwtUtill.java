package com.example.jdnctamitams.jwt;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Base64;

@Slf4j(topic = "JwtUtill")
@Component
@RequiredArgsConstructor
public class JwtUtill {
    // Header KEY 값
    public static final String AUTHORIZATION_HEADER = "Authorization"; //이거 이름ㅇ; "Authorization";
    // 사용자 권한 값의 KEY
//    public static final String AUTHORIZATION_KEY = "auth"; // 이거 이름이 auth다
    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer "; // 이거 이름이 "Bearer
    // 토큰 만료시간
    private final long TOKEN_TIME = 60 * 60 * 1000L; // 60분 L은 롱타입이여서 들어간거

    @Value("${jwt.secret.key}") // Base64 Encode 한 SecretKey
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256; // 이거는 토큰을 어떤식으로 만든다는거다. 이 키를 통해서 jwt 형식으로 있는데 알고리즘에 따라서 있는데
    // 키 값에 따라서 뭔가를 한다.

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // header 에서 JWT 가져오기
    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER); // 겟헤더를 통해서 AUTHORIZATION_HEADER 값을 가져온다. 겟헤더로 아이피도 가져올 수 있다.
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // 토큰 검증
    public boolean validateToken(String token) { //요기서 validateToken는 대조한다라는 거다 대조한다라고 생각해라 값을 비교한다 라는 느낌 이건 토큰을 검증할 때 많이 사용한다. 서명을 통해서
        // 내꺼인가 아닌가를 검증 이거를 통해서 트루를 가져오는건데 했다가 안되면 저 아래식으로 에러가 뜬다.
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {            //SignatureException   보안이, 형태가 다르다 그러면 유효하지 않는 JWT 서명 입니다 나오게
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
    // 이게 토큰 깨먹는 과정 토큰에서 가져온다는 말이다. 유틸에서 알 수 있는건 토큰을 만들고, 토큰을 가져오고, 정보를 깨먹는거다 순서대로 토큰을 만들고 주고, 헤더에서 그걸 가져오고 그걸 검증하고 그걸 검증해서 트루면 이제 사용자 정보를 가져온다
}