package com.sparta.travel.domain.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.travel.domain.dto.LoginRequestDto;
import com.sparta.travel.domain.dto.MsgResponseDto;
import com.sparta.travel.domain.dto.TokenDto;
import com.sparta.travel.domain.entity.RefreshToken;
import com.sparta.travel.domain.jwt.JwtUtil;
import com.sparta.travel.domain.jwt.UserRoleEnum;
import com.sparta.travel.domain.repository.RefreshTokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j(topic = "로그인 및 JWT 생성")

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/api/user/login");
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getUserId(),
                            requestDto.getPassword(),
                            null
                    )
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
    @SneakyThrows
    @Override //추가 메시지 작업
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        String userId = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();
        //UserRoleEnum role = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getRole();

        //TokenDto token = jwtUtil.createAllToken(userId, role);
        TokenDto token = jwtUtil.createAllToken(userId);

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token.getAccessToken()); // 엑세스 토큰
        response.addHeader(JwtUtil.AUTHORIZATION_REFRESH_HEADER,token.getRefreshToken()); // 리프레쉬 토큰

        response.setStatus(HttpServletResponse.SC_OK); // 상태코드 반환
        response.setContentType("application/json;charset=UTF-8"); // JSON 형식으로 반환

        // 로그인 성공 시 리프레쉬 토큰 저장
        RefreshToken refreshToken = new RefreshToken(token.getRefreshToken(), ((UserDetailsImpl)authResult.getPrincipal()).getUser());
        refreshTokenRepository.save(refreshToken);

        MsgResponseDto responseBody = new MsgResponseDto(HttpServletResponse.SC_OK,"로그인 성공2");

        ObjectMapper objectMapper = new ObjectMapper(); // JSON 문자열로 바꿈
        String responseBodyToJson = objectMapper.writeValueAsString(responseBody);
        response.getWriter().write(responseBodyToJson);
    }

    @SneakyThrows
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8"); // JSON 형식으로 반환

        MsgResponseDto responseBody = new MsgResponseDto(HttpServletResponse.SC_UNAUTHORIZED,"로그인 실패하였습니다.");

        ObjectMapper objectMapper = new ObjectMapper(); // JSON 문자열로 바꿈
        String responseBodyToJson = objectMapper.writeValueAsString(responseBody);
        response.getWriter().write(responseBodyToJson);

    }

}
