package com.github.ecommerce.service.auth;

import com.github.ecommerce.config.security.JwtTokenProvider;
import com.github.ecommerce.data.entity.auth.User;
import com.github.ecommerce.data.repository.auth.AuthRepository;
import com.github.ecommerce.service.exception.*;
import com.github.ecommerce.service.s3Image.AwsS3Service;
import com.github.ecommerce.service.security.CustomUserDetails;
import com.github.ecommerce.web.advice.ErrorCode;
import com.github.ecommerce.web.dto.auth.Authority;
import com.github.ecommerce.web.dto.auth.CheckedEmailRequest;
import com.github.ecommerce.web.dto.auth.LoginRequest;
import com.github.ecommerce.web.dto.auth.SignRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final AuthRepository authRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final AwsS3Service awsS3Service;

    private boolean isPasswordStrong(String password) {
        return password.length() >= 8;
    }

    private void checkUserDeleted(User user) {
        if (user.getDeletedAt() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDate = user.getDeletedAt().format(formatter);
            throw new DeletedUserException(formattedDate, ErrorCode.SECESSION_DETAIL);
        }
    }

    public boolean checkedEmail(CheckedEmailRequest checkedEmailRequest){
        String email = checkedEmailRequest.getEmail();
        if (authRepository.existsByEmail(email)) {
            log.warn("이메일이 이미 존재합니다: {}", email);
            throw new BadRequestException(ErrorCode.EMAIL_ALREADY_EXIST);
        }
        return true;
    }

    @Transactional(transactionManager = "tmJpa1")
    public boolean signUp(SignRequest signUpRequest, MultipartFile profileImage) {
        String email = signUpRequest.getEmail();
        String password = signUpRequest.getPassword();
        String username = signUpRequest.getUserName();
        String phone = signUpRequest.getPhone();
        String gender = signUpRequest.getGender();
        String zipCode = signUpRequest.getZipCode();
        String mainAddress = signUpRequest.getMainAddress();
        String detailAddress = signUpRequest.getDetailsAddress();


        if (!isPasswordStrong(password)) {
            log.warn("비밀번호가 너무 약합니다.");
            throw new BadRequestException(ErrorCode.WEAK_PASSWORD);
        }

        String profileImageUrl;
        if (profileImage != null && !profileImage.isEmpty()) {
            try {
                profileImageUrl = awsS3Service.uploadImageToS3(profileImage);
            } catch (IOException e) {
                throw new S3UpLordException(ErrorCode.UPLOAD_FAILURE);
            }
        } else {
            profileImageUrl = "https://project2-profile.s3.ap-northeast-2.amazonaws.com/basic.jpg";
        }

        User userPrincipal = User.builder()
                .email(email)
                .userName(username)
                .password(passwordEncoder.encode(password))
                .phone(phone)
                .gender(gender)
                .profileImage(profileImageUrl)
                .zipCode(zipCode)
                .mainAddress(mainAddress)
                .detailAddress(detailAddress)
                .authorities(new HashSet<>(List.of(Authority.ROLE_USER)))
                .build();

        authRepository.save(userPrincipal);
        return true;
    }

    public String login(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        User user = authRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException2(ErrorCode.EMAIL_NOT_EXIST));

        checkUserDeleted(user);

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();

            Set<Authority> roles = ((CustomUserDetails) userPrincipal).getAuthoritySet();

            return jwtTokenProvider.createToken(email, userPrincipal.getUsername(), roles);

        } catch (BadCredentialsException e) {
            log.warn("로그인 실패: 잘못된 비밀번호");
            throw new BadRequestException(ErrorCode.LOGIN_FAILURE3);
        } catch (Exception e) {
            log.error("로그인 처리 중 오류 발생: {}", e.getMessage());
            throw new NotAcceptException(ErrorCode.LOGIN_FAILURE);
        }
    }

    @Transactional(transactionManager = "tmJpa1")
    public boolean secession() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Integer userId = userDetails.getUserId();

        User user = authRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException2(ErrorCode.USER_NOT_FOUNDED));

        if (user.getDeletedAt() != null) {
            throw new NotAcceptException(ErrorCode.USER_SECESSION_FAILURE);
        }

        user.deleteUser();
        authRepository.save(user);

        return true;
    }
}
