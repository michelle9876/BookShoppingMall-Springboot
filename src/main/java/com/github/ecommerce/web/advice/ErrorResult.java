package com.github.ecommerce.web.advice;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;


@Data
@NoArgsConstructor
public class ErrorResult {
    private int statusCode;

    private HttpStatus status;

    private String message;

}
