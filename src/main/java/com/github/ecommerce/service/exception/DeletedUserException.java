package com.github.ecommerce.service.exception;


import com.github.ecommerce.web.advice.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DeletedUserException extends RuntimeException{
    private final HttpStatus httpStatus;
    private final String formattedDate;
    public DeletedUserException(String formattedDate, ErrorCode errorCode) {
        super(formattedDate + " 날짜에 탈퇴한 사용자입니다.");
        this.formattedDate = formattedDate;
        this.httpStatus = errorCode.getHttpStatus();
    }
}
