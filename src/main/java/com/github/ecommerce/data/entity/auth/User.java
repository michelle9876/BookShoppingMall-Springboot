package com.github.ecommerce.data.entity.auth;

import com.github.ecommerce.web.dto.auth.Authority;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
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
}
//    @ElementCollection(targetClass = Authority.class)
//    @Enumerated(EnumType.STRING)
//    private Set<Authority> authorities;
