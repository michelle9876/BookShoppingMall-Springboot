package com.github.ecommerce.web.dto.mypage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentListDTO {
    private DefaultDTO status;
    private List<PaymentDetailDTO> payments;

    public PaymentListDTO(MyPageStatus myPageStatus) {
        this.status.setMessage(myPageStatus.getMessage());
        this.status.setCode(myPageStatus.getCode());
    }
    public PaymentListDTO(String msg, int code){
        status.setMessage(msg);
        status.setCode(code);
    }
}
