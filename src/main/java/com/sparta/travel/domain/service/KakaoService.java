package com.sparta.travel.domain.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.travel.domain.dto.KakaoInfoDto;
import com.sparta.travel.domain.dto.KakaoResponseDto;
import com.sparta.travel.domain.dto.MsgResponseDto;
import com.sparta.travel.domain.entity.User;
import com.sparta.travel.domain.jwt.JwtUtil;
import com.sparta.travel.domain.jwt.UserRoleEnum;
import com.sparta.travel.domain.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@Slf4j(topic = "KAKAO Login")
@Service
@RequiredArgsConstructor
public class KakaoService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RestTemplate rt;
    private final JwtUtil jwtUtil;

    @Value("${kakao.client-id}")
    private String client_id;

    @Value("${kakao.redirect-uri}")
    private String redirect_uri;
    public MsgResponseDto kakaoLogin(String code,HttpServletResponse response) throws JsonProcessingException {
        String accessToken = getToken(code); // 카카오 인가 토큰 발급

        KakaoInfoDto kakaoInfoDto = getKakaoInfo(accessToken); // 유저정보 가져오기

        User kakaoUser = registerKakaoUserIfNeeded(kakaoInfoDto); // 로그인 하기 (이메일 없음 DB에 저장하고 로그인 없으면 그냥 로그인)
        String token = jwtUtil.createToken(kakaoUser.getUserId());  // 직접 만든 토큰을 헤더에 넣기
//        String token = jwtUtil.createToken(kakaoUser.getUserId(),kakaoUser.getRole());  // 직접 만든 토큰을 헤더에 넣기
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER,token);

        return new MsgResponseDto(HttpServletResponse.SC_OK,"로그인 성공했습니다.");
    }

    private String getToken(String code) throws JsonProcessingException{
        URI uri = UriComponentsBuilder
                .fromUriString("https://kauth.kakao.com")
                .path("/oauth/token")
                .encode()
                .build()
                .toUri();

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", client_id);
        body.add("redirect_uri", redirect_uri);
        body.add("code", code);

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
                .post(uri)
                .headers(headers)
                .body(body);

        // HTTP 요청 보내기
        ResponseEntity<String> res = rt.exchange(
                requestEntity,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(res.getBody());
        return jsonNode.get("access_token").asText();
    }

    private KakaoInfoDto getKakaoInfo(String accessToken) throws JsonProcessingException {
        URI uri = UriComponentsBuilder
                .fromUriString("https://kapi.kakao.com")
                .path("/v2/user/me")
                .encode()
                .build()
                .toUri();

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer "+accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
                .post(uri)
                .headers(headers)
                .body(new LinkedMultiValueMap<>());


        // HTTP 요청 보내기
        ResponseEntity<String> res = rt.exchange(
                requestEntity,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        JsonNode jsonNode = new ObjectMapper().readTree(res.getBody());
        String nickname = jsonNode.get("properties").get("nickname").asText();
        String email = jsonNode.get("kakao_account").get("email").asText();
        Long kakaoId = jsonNode.get("id").asLong();
        return new KakaoInfoDto(nickname,email,kakaoId);
    }

    private User registerKakaoUserIfNeeded(KakaoInfoDto kakaoInfoDto) {
        Optional<User> kakaoEmail = userRepository.findByEmail(kakaoInfoDto.getEmail());
        if(!kakaoEmail.isPresent()){ //  존재 안하면 save
            String id = Long.toString(kakaoInfoDto.getKakaoId());
            String encodedPassword = passwordEncoder.encode(UUID.randomUUID().toString());
//            User kakaoUser = new User(id, encodedPassword, UserRoleEnum.USER,kakaoInfoDto.getEmail(),kakaoInfoDto.getNickname());
            User kakaoUser = new User(id, encodedPassword,kakaoInfoDto.getEmail(),kakaoInfoDto.getNickname());
            userRepository.save(kakaoUser);
            return kakaoUser;
        } // 존재하면 그냥 로그인 처리
        User existUser = kakaoEmail.get();

        return existUser;
    }


}