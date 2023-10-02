package com.sparta.travel.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;


@Getter

public class MsgResponseDto {
    private int statusCode;
    private String msg;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String  profile_img_url;

    public MsgResponseDto(int statusCode, String msg) {
        this.statusCode = statusCode;
        this.msg = msg;
    }

    public MsgResponseDto(int statusCode, String msg, String profile_img_url){
        this.statusCode = statusCode;
        this.msg = msg;
        this.profile_img_url = profile_img_url;
    }
}
