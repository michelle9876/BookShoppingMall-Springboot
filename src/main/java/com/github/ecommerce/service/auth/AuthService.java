package com.github.ecommerce.service.auth;

import com.github.ecommerce.config.security.JwtTokenProvider;
import com.github.ecommerce.data.entity.auth.User;
import com.github.ecommerce.data.repository.auth.AuthRepository;
import com.github.ecommerce.service.exception.EmailAlreadyExistsException;
import com.github.ecommerce.service.exception.NotAcceptException;
import com.github.ecommerce.service.exception.NotFoundException;
import com.github.ecommerce.service.exception.WeakPasswordException;
import com.github.ecommerce.service.security.CustomUserDetails;
import com.github.ecommerce.web.advice.ErrorCode;
import com.github.ecommerce.web.dto.auth.Authority;
import com.github.ecommerce.web.dto.auth.LoginRequest;
import com.github.ecommerce.web.dto.auth.SignRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import org.springframework.security.core.GrantedAuthority;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final AuthRepository authRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final S3Client s3Client;
    @Value("${cloud.aws.s3.buckets.bucket1.name}")
    private String bucket1;

    public String saveFile(MultipartFile multipartFile) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();

        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucket1)
                .key(originalFilename)
                .contentType(multipartFile.getContentType())
                .build();

        s3Client.putObject(putRequest, RequestBody.fromInputStream(multipartFile.getInputStream(), multipartFile.getSize()));

        return s3Client.utilities()
                .getUrl(GetUrlRequest.builder().bucket(bucket1).key(originalFilename).build())
                .toString();
    }

    private boolean isPasswordStrong(String password) {
        return password.length() >= 8;
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

        if (authRepository.existsByEmail(email)) {
            log.warn("이메일이 이미 존재합니다: {}", email);
            throw new EmailAlreadyExistsException("이미 존재하는 이메일입니다.");
        }

        if (!isPasswordStrong(password)) {
            log.warn("비밀번호가 너무 약합니다.");
            throw new WeakPasswordException("비밀번호는 최소 8자 이상이어야 합니다."); // 사용자 정의 예외
        }

        String profileImageUrl;
        if (profileImage != null && !profileImage.isEmpty()) {
            try {
                profileImageUrl = saveFile(profileImage);
            } catch (IOException e) {
                log.error("파일 업로드 실패: {}", e.getMessage());
                return false;
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
            throw new NotAcceptException(ErrorCode.LOGIN_FAILURE);
        } catch (Exception e) {
            log.error("로그인 처리 중 오류 발생: {}", e.getMessage());
            throw new NotAcceptException(ErrorCode.LOGIN_FAILURE);
        }
    }

    @Transactional(transactionManager = "tmJpa1")
    public String secession() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Integer userId = userDetails.getUserId();

        User user = authRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("해당 사용자를 찾을 수 없습니다."));

        if (user.getDeletedAt() != null) {
            throw new NotAcceptException(user.getDeletedAt() + " 날짜에 탈퇴한 사용자입니다.");
        }

        user.deleteUser();
        authRepository.save(user);

        return "회원 탈퇴가 완료되었습니다.";
    }
}
