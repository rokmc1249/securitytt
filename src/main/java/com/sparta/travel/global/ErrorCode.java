package com.sparta.travel.global;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    ID_NOT_MATCH(HttpStatus.BAD_REQUEST, "작성자가 일치하지 않습니다"),
    ID_NOT_FOUND(HttpStatus.NOT_FOUND, "아이디를 찾을 수 없습니다."),
    IMG_NULL(HttpStatus.BAD_REQUEST,"이미지를 찾을 수 없습니다."),
    BOOKMARK_NOT_FOUND(HttpStatus.NOT_FOUND, "선택한 북마크는 존재하지 않습니다."),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "토큰이 유효하지 않습니다."),
    DUPLICATED_ID(HttpStatus.BAD_REQUEST, "중복된 아이디입니다."),
    DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, "중복된 이메일입니다."),
    DUPLICATED_NICKNAME(HttpStatus.BAD_REQUEST, "중복된 닉네임입니다."),
    PLAN_NOT_FOUND(HttpStatus.BAD_REQUEST, "없는 여행일정입니다."),
    S3_NOT_UPLOAD(HttpStatus.BAD_REQUEST,"S3에 업로드가 되지 않았습니다."),
    PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "잘못된 패스워드입니다.");

    private final HttpStatus statusCode;;
    private final String msg;
    

    ErrorCode(HttpStatus statusCode, String msg) {
        this.statusCode = statusCode;
        this.msg = msg;
    }
}
