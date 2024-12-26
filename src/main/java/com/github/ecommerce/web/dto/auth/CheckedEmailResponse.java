package com.github.ecommerce.web.dto.auth;

import com.github.ecommerce.web.advice.ErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
public class CheckedEmailResponse {
    private boolean available;
    private String message;

    public CheckedEmailResponse(boolean available,String message) {
        this.message = message;
        this.available = available;
    }

}
