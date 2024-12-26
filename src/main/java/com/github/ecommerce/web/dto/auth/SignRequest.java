package com.github.ecommerce.web.dto.auth;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@Getter
public class SignRequest {
    @NotEmpty(message = "이메일은 필수입니다.")
    @Email(message = "유효한 이메일 형식이어야 합니다.")
    private String email;
    @NotEmpty(message = "비밀번호는 필수입니다.")
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하이어야 합니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,20}$",
            message = "비밀번호는 영문자, 숫자 및 특수 기호를 모두 포함해야 합니다.")
    private String password;
    @NotEmpty(message = "사용자 이름은 필수입니다.")
    private String userName;
    private MultipartFile profileImage;
    @NotEmpty(message = "전화번호는 필수입니다.")
    private String phone;
    @NotEmpty(message = "성별은 필수입니다.")
    private String gender;
    @NotEmpty(message = "우편번호는 필수입니다.")
    private String zipCode;
    @NotEmpty(message = "주소는 필수입니다.")
    private String mainAddress;
    private String detailsAddress;
}
