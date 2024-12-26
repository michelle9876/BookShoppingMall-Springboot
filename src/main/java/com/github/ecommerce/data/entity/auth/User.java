package com.github.ecommerce.data.entity.auth;

import com.github.ecommerce.service.exception.InvalidValueException;
import com.github.ecommerce.web.dto.auth.Authority;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
@DynamicInsert
@Builder
@ToString
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(name = "email", unique = true)
    @NotEmpty(message = "이메일은 필수입니다.")
    @Email(message = "유효한 이메일 형식이어야 합니다.")
    private String email;

    @Column(name = "password", nullable = false)
    @NotEmpty(message = "비밀번호는 필수입니다.")
    private String password;

    @Column(name = "user_name", nullable = false)
    @NotEmpty(message = "사용자 이름은 필수입니다.")
    private String userName;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "phone", nullable = false)
    @NotEmpty(message = "전화번호는 필수입니다.")
    private String phone;

    @Column(name = "gender", nullable = false)
    @NotEmpty(message = "성별은 필수입니다.")
    private String gender;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "zip_code", nullable = false)
    @NotEmpty(message = "우편번호는 필수입니다.")
    private String zipCode;

    @Column(name = "main_address", nullable = false)
    @NotEmpty(message = "주소는 필수입니다.")
    private String mainAddress;

    @Column(name = "details_address")
    private String detailAddress;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Builder.Default
    @Transient
    private Set<Authority> authorities = Set.of(Authority.ROLE_USER);

    public void deleteUser() {
        this.deletedAt = LocalDateTime.now();
    }

    //유저 정보 수정 책임분리 추가 ==============
    public void updateProfileImage(String newImageUrl) {
        if (newImageUrl != null) {
            this.profileImage = newImageUrl;
        }
    }

    public void updateUserName(String userName) {
        if (userName != null && !userName.isEmpty()) {
            if(userName.length() <= 50){
                this.userName = userName;
            }else{
                throw new InvalidValueException("updateUserName : 50자 이내의 이름을 설정해주세요.");
            }
        }
    }

    public void updatePhone(String phone) {
        if (phone != null  && !phone.isEmpty()) {
            if(phone.length() == 11){
                this.phone = phone;
            }else{
                throw new InvalidValueException("updatePhone : 전화번호는 11자로 설정되어야 합니다.");
            }

        }
    }

    public void updateGender(String gender) {
        if (gender != null  && !gender.isEmpty()) {
            if(gender.length() == 1){
                this.gender = gender;
            }else{
                throw new InvalidValueException("updateGender : 성별은 [M,F,U]로 설정되어야 합니다.");
            }
        }
    }

    public void updateZipCode(String zipCode) {
        if (zipCode != null && !zipCode.isEmpty()) {
            if(zipCode.length()  <= 7){
                this.zipCode = zipCode;
            }else{
                throw new InvalidValueException("updateZipCode : 우편번호는 7자 이내로 설정되어야 합니다.");
            }

        }
    }

    public void updateMainAddress(String mainAddress) {
        if (mainAddress != null && !mainAddress.isEmpty()) {
            this.mainAddress = mainAddress;
        }
    }

    public void updateDetailAddress(String detailAddress) {
        if (detailAddress != null && !detailAddress.isEmpty()) {
            this.detailAddress = detailAddress;
        }
    }
    //유저 정보 수정 책임분리 추가 ==============

}
//    @ElementCollection(targetClass = Authority.class)
//    @Enumerated(EnumType.STRING)
//    private Set<Authority> authorities;
