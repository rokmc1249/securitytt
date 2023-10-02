package com.sparta.travel.domain.dto;

import lombok.Getter;

@Getter
public class PlanMsgResponseDto {
    private int statusCode;
    private String msg;
    private Long planId;

    public PlanMsgResponseDto(int statusCode, String msg, Long planId){
        this.statusCode = statusCode;
        this.msg = msg;
        this.planId = planId;
    }
}
