package com.sparta.travel.domain.jwt;


import com.sparta.travel.domain.dto.TokenDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {
    // Header KEY 값
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String AUTHORIZATION_REFRESH_HEADER = "Refresh-Token";
    // 사용자 권한 값의 KEY
    public static final String AUTHORIZATION_KEY = "auth";
    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer "; //토큰임을 알려주는 국룰
    // 토큰 만료시간
    private final long TOKEN_TIME = 60 * 60 * 1000L; // 60분

    private final long REFRESH_TOKEN_TIME = 14 * 24 * 60 * 60 * 1000L; // 14일

    @Value("${jwt.secret.key}") // Base64 Encode 한 SecretKey
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    // 로그 설정
    public static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // 엑세스 토큰 생성
//    public String createToken(String username,UserRoleEnum role) {
//        Date date = new Date();
//
//        return BEARER_PREFIX +
//                Jwts.builder()
//                        .setSubject(username) // 사용자 식별자값(ID)
//                        .claim(AUTHORIZATION_KEY,role) // 사용자 권한
//                        .setExpiration(new Date(date.getTime() + TOKEN_TIME)) // 만료 시간
//                        .setIssuedAt(date) // 발급일
//                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘
//                        .compact();
//    }
    public String createToken(String username) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username) // 사용자 식별자값(ID)
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME)) // 만료 시간
                        .setIssuedAt(date) // 발급일
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                        .compact();
    }

    // 리프레쉬 토큰 생성
    public String createRefreshToken(String username) {
        Date date = new Date();

        return   Jwts.builder()
                .setSubject(username) // 사용자 식별자값(ID)
                .setExpiration(new Date(date.getTime() + REFRESH_TOKEN_TIME)) // 만료 시간
                .setIssuedAt(date) // 발급일
                .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                .compact();
    }

//    // 리프레쉬 토큰 생성
//    public String createRefreshToken(String username,UserRoleEnum role) {
//        Date date = new Date();
//
//        return   Jwts.builder()
//                .setSubject(username) // 사용자 식별자값(ID)
//                .claim(AUTHORIZATION_KEY,role) // 사용자 권한
//                .setExpiration(new Date(date.getTime() + REFRESH_TOKEN_TIME)) // 만료 시간
//                .setIssuedAt(date) // 발급일
//                .signWith(key, signatureAlgorithm) // 암호화 알고리즘
//                .compact();
//    }

//    // 2개 토큰 생성
//    public TokenDto createAllToken(String username, UserRoleEnum role){
//        String accessToken = createToken(username,role);
//        String refreshToken  = createRefreshToken(username, role);
//        TokenDto tokenDto = new TokenDto();
//        tokenDto.setAccessToken(accessToken);
//        tokenDto.setRefreshToken(refreshToken);
//        return tokenDto;
//    }
// 2개 토큰 생성
public TokenDto createAllToken(String username){
    String accessToken = createToken(username);
    String refreshToken  = createRefreshToken(username);
    TokenDto tokenDto = new TokenDto();
    tokenDto.setAccessToken(accessToken);
    tokenDto.setRefreshToken(refreshToken);
    return tokenDto;
}

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token); //토큰 위변조, 만료 검증
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            logger.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }


    // header 에서 JWT 가져오기(순수한 토큰을 뽑아낼 수 있다)
    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // 리프레쉬 토큰 추출 메서드
    public String extractRefreshToken(HttpServletRequest request) {
        return request.getHeader(AUTHORIZATION_REFRESH_HEADER);

    }
    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}

