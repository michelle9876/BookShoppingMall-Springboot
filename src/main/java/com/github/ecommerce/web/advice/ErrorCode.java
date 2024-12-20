package com.github.ecommerce.web.advice;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    INDEX_NOT_FOUND(404, "인덱스가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    BOARD_NOT_FOUND(403,"게시글을 찾을 수 없습니다.", HttpStatus.FORBIDDEN),
    LOGIN_FAILURE(406,"로그인 할 수 없습니다.", HttpStatus.NOT_ACCEPTABLE),
    ENTRY_POINT_FAILURE(401,"인증 과정에서 에러", HttpStatus.UNAUTHORIZED);

    private int statusCode;
    private String errorMessage;
    private HttpStatus httpStatus;

    ErrorCode(int statusCode, String errorMessage,HttpStatus httpStatus) {
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
        this.httpStatus = httpStatus;
    }
}
