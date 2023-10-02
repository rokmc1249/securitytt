package com.sparta.travel.domain.dto;

import lombok.Getter;

@Getter
public class KakaoResponseDto {
    private String accessToken;
    private KakaoInfoDto kakakoInfoDto;

    public KakaoResponseDto(String accessToken, KakaoInfoDto kakakoInfoDto) {
        this.accessToken = accessToken;
        this.kakakoInfoDto = kakakoInfoDto;
    }
}
