package com.github.ecommerce.service.exception;

public class S3Exception extends RuntimeException{
    private final int statusCode;

    public S3Exception(String message, int statusCode) {
        super(message); // 부모 클래스의 생성자 호출
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
