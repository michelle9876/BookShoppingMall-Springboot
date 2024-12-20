package com.github.ecommerce.web.dto.mypage;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoDTO {

    private DefaultDTO status;

    private Integer userId;
    private String email;
    private String userName;
    private String profileImage;
    private String gender;
    private String phone;
    private String zipCode;
    private String mainAddress;
    private String detailsAddress;

     public UserInfoDTO(String msg, int code){
         this.status.setMessage(msg);
         this.status.setCode(code);
     }

    public UserInfoDTO(MyPageStatus myPageStatus) {
        this.status.setMessage(myPageStatus.getMessage());
        this.status.setCode(myPageStatus.getCode());
    }
}
