package com.github.ecommerce.web.controller.auth;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ecommerce.service.auth.AuthService;
import com.github.ecommerce.service.exception.*;
import com.github.ecommerce.web.advice.ErrorCode;
import com.github.ecommerce.web.dto.auth.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;

    @PostMapping(value = "/email")
    public ResponseEntity<CheckedEmailResponse> checkEmail(@Valid@RequestBody CheckedEmailRequest emailCheckRequest, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(ErrorCode.REGISTER_FAILURE);
        }

        boolean validation = authService.checkedEmail(emailCheckRequest);
        return ResponseEntity.ok(validation ? new CheckedEmailResponse(true,"중복되지 않은 이메일입니다."):new CheckedEmailResponse(false,"이미 사용중인 이메일입니다."));
    }

    @PostMapping(value = "/signup")
    public ResponseEntity<SignResponse> register(@RequestParam(value = "profileImage", required = false ) MultipartFile profileImage, @Valid@RequestParam("signUpRequest") String signUpRequestJson) {
        SignRequest signUpRequest;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            signUpRequest = objectMapper.readValue(signUpRequestJson, SignRequest.class);
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body(new SignResponse(false, "잘못된 요청 형식입니다."));
        }
        boolean isSuccess = authService.signUp(signUpRequest,profileImage);
        return ResponseEntity.ok(isSuccess ? new SignResponse(true,"회원가입 성공하였습니다.") : new SignResponse(false,"회원가입 실패하였습니다."));
    }

    @PostMapping(value = "/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse httpServletResponse, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(error ->
                    log.error("Field: {}, Message: {}", error.getField(), error.getDefaultMessage())
            );
            throw new BadRequestException(ErrorCode.REGISTER_FAILURE);
        }

        String token = authService.login(loginRequest);
        httpServletResponse.setHeader("Bearer_Token", token);
        return ResponseEntity.ok("로그인이 성공하였습니다.");
    }

    @GetMapping(value = "/delete")
    public ResponseEntity<DeletedUserResponse> secession() {
        try {
            boolean successSecession = authService.secession();
            return ResponseEntity.ok(new DeletedUserResponse(successSecession,"회원 탈퇴 과정이 완료되었습니다."));
        } catch (NotAcceptException e) {
            throw new NotAcceptException(ErrorCode.USER_SECESSION_FAILURE);
        } catch (NotFoundException e) {
            throw new NotFoundException2(ErrorCode.SECESSION_NOT_FOUND);
        }
    }
}