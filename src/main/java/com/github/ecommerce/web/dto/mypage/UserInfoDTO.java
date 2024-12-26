package com.github.ecommerce.web.dto.mypage;

import com.github.ecommerce.data.entity.auth.User;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoDTO {

    private Integer userId;
    private String email;
    private String userName;
    private String profileImage;
    private String gender;
    private String phone;
    private String zipCode;
    private String mainAddress;
    private String detailsAddress;

    public UserInfoDTO(User user) {
        this.userId = user.getUserId();
        this.email = user.getEmail();
        this.userName = user.getUserName();
        this.profileImage = user.getProfileImage();
        this.gender = user.getGender();
        this.phone = user.getPhone();
        this.zipCode = user.getZipCode();
        this.mainAddress = user.getMainAddress();
        this.detailsAddress = user.getDetailAddress();
    }

    public static UserInfoDTO from(User user) {return new UserInfoDTO(user);}
}
