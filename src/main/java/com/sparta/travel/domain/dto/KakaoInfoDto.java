package com.sparta.travel.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoInfoDto {
    private String nickname;
    private String email;
    private Long kakaoId;

    public KakaoInfoDto(String nickname, String email,Long kakaoId) {
        this.nickname = nickname;
        this.email = email;
        this.kakaoId = kakaoId;
    }
}
