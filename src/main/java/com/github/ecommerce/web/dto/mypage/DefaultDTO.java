package com.github.ecommerce.web.dto.mypage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DefaultDTO {
    private String message;
    private Integer code;

    public DefaultDTO(MyPageStatus myPageStatus) {
        this.message = myPageStatus.getMessage();
        this.code = myPageStatus.getCode();
    }
}
