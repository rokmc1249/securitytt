package com.sparta.travel.domain.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.travel.domain.dto.*;
import com.sparta.travel.domain.jwt.JwtUtil;
import com.sparta.travel.domain.security.UserDetailsImpl;
import com.sparta.travel.domain.service.KakaoService;
import com.sparta.travel.domain.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final KakaoService kakaoService;

    @PostMapping("/user/signup")

    public ResponseEntity<MsgResponseDto> signup(@Valid @RequestBody SignupRequestDto requestDto) {
        return ResponseEntity.ok(userService.signup(requestDto));
    }

    @DeleteMapping("/user/userdel")

    public ResponseEntity<MsgResponseDto> deleteUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(userService.deleteUser(userDetails.getUser()));
    }

    @GetMapping("/user/updateprofile")

    public ProfileResponseDto getProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.getProfile(userDetails.getUser());
    }
    @PostMapping("/user/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        return userService.refreshToken(request);
    }

    @PostMapping("/user/updateImg")
    public ResponseEntity<MsgResponseDto> updateProfileImg(@RequestParam(value = "image") MultipartFile image, @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return ResponseEntity.ok(userService.updateProfileImg(image, userDetails.getUser()));
    }


    @PutMapping("/user/updateprofile")

    public ResponseEntity<MsgResponseDto> updateProfile(@Valid @RequestBody ProfileRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(userService.updateProfile(requestDto, userDetails.getUser()));
    }

    @GetMapping("/user/kakao/callback")

    public ResponseEntity<MsgResponseDto> kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {


        return ResponseEntity.ok(kakaoService.kakaoLogin(code, response));
    }
}
//    @GetMapping("/user/kakao/callback")
//    public String kakaoLogin(@RequestParam String code, ) throws JsonProcessingException {
//
//        String token = kakaoService.kakaoLogin(code);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add(JwtUtil.AUTHORIZATION_HEADER, token);
//
//        return "redirect:http://localhost:3000/";
//    }
//}
        //Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, token);
        //cookie.setPath("/"); // 프로토콜을 제외하고 경로 설정
        //response.addCookie(cookie);


