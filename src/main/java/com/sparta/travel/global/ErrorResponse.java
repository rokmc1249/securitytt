package com.sparta.travel.global;

import lombok.Getter;

@Getter

public class ErrorResponse {
    private final int statusCode;
    private final String msg;


    public ErrorResponse(ErrorCode errorCode) {
        this.statusCode =errorCode.getStatusCode().value();
        this.msg = errorCode.getMsg();
    }
}
