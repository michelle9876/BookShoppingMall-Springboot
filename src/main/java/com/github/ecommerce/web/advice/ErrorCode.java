package com.github.ecommerce.web.advice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // 로그인
    REGISTER_FAILURE(400,"입력하신 데이터에 문제가 있습니다.",HttpStatus.BAD_REQUEST),
    SECESSION_NOT_FOUND(404,"탈퇴한 회원이 아닙니다.",HttpStatus.NOT_FOUND),
    SECESSION_DETAIL(403," 해당 날짜에 탈퇴를 하셨습니다.",HttpStatus.FORBIDDEN),
    USER_SECESSION_FAILURE(401,"지금 상태에서 탈퇴 진행을 하실 수 없습니다.", HttpStatus.UNAUTHORIZED),
    LOGIN_FAILURE(406,"로그인 할 수 없습니다.", HttpStatus.NOT_ACCEPTABLE),
    LOGIN_FAILURE2(406,"탈퇴를 하셔서 로그인이 불가능합니다.", HttpStatus.NOT_ACCEPTABLE),
    LOGIN_FAILURE3(400,"비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    EXPIRED_TOKEN(401,"발급된 토큰이 만료되었습니다.", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED_TOKEN(401,"발급된 토큰이 만료되었습니다.", HttpStatus.UNAUTHORIZED),
    ENTRY_POINT_FAILURE(401,"인증되지 않은 사용자입니다.", HttpStatus.UNAUTHORIZED),
    ACCESS_DENIED(403,"접근할 수 있는 권한이 없습니다.", HttpStatus.FORBIDDEN),
    EMAIL_NOT_EXIST(404,"해당 이메일의 사용자를 찾을 수 없습니다.",HttpStatus.NOT_FOUND),
    EMAIL_ALREADY_EXIST(400,"이미 존재하는 이메일입니다.",HttpStatus.BAD_REQUEST),
    WEAK_PASSWORD(400,"비밀번호는 최소 8자 이상이어야 합니다.",HttpStatus.BAD_REQUEST),
    UPLOAD_FAILURE(406,"파일 업로드에 실패했습니다.",HttpStatus.NOT_ACCEPTABLE),
    USER_NOT_FOUNDED(404,"해당 유저를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    USER_ERROR_FORBIDDEN(403,"로그인 정보와 접근 정보가 다릅니다.", HttpStatus.FORBIDDEN);
    private final int statusCode;
    private final String errorMessage;
    private final HttpStatus httpStatus;
}
