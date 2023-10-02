package com.sparta.travel.domain.dto;

import com.sparta.travel.domain.entity.User;
import lombok.Getter;

@Getter
public class ProfileResponseDto {
    private String email;
    private String nickname;

    public ProfileResponseDto(User getUser) {
        this.email = getUser.getEmail();
        this.nickname = getUser.getNickname();
    }
}
