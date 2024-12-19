package com.github.ecommerce.web.controller.mypage;

import com.github.ecommerce.service.mypage.MyPageService;
import com.github.ecommerce.service.security.CustomUserDetails;
import com.github.ecommerce.web.dto.mypage.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
@Slf4j
public class MyPageController {

    private final MyPageService myPageService;


    // 유저정보 가져오기
    @GetMapping("/getUserInfo")
    public ResponseEntity<UserInfoDTO> getUserInfo(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        //토큰에서 user 정보 가져오기
        if(userDetails == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new UserInfoDTO("로그인된 사용자만 이용하실 수 있습니다.", HttpStatus.UNAUTHORIZED.value()));
        }
        Integer userId = userDetails.getUserId();

        //유저 정보 가지고오기
        UserInfoDTO result = myPageService.getUserInfo(userId);
        return ResponseEntity.ok(result);
    }

    // 유저정보 수정
    @PutMapping("/putUserInfo/{id}")
    public ResponseEntity<UserInfoDTO> putUserInfo(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable String id, @ModelAttribute UserInfoDTO userInfo,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) {
        //토큰에서 user 정보 가져오기
        if(userDetails == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new UserInfoDTO("로그인된 사용자만 이용하실 수 있습니다.", HttpStatus.UNAUTHORIZED.value()));
        }
        Integer userId = userDetails.getUserId();

        if(!userId.equals(Integer.valueOf(id))){
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new UserInfoDTO("본인의 정보만 수정할 수 있습니다.", HttpStatus.UNAUTHORIZED.value()));
        }

        UserInfoDTO result = null;
        try{
            //유저 정보 수정하기
            result = myPageService.putUserInfo(id, userInfo, image);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new UserInfoDTO(MyPageStatus.USER_INFO_ERROR.getMessage(), MyPageStatus.USER_INFO_ERROR.getCode()));
        }
        return ResponseEntity.ok(result);
    }


    //  장바구니 목록
    @GetMapping("/getCartItems")
    public ResponseEntity<CartListDTO> getCartItems(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        //토큰에서 user 정보 가져오기
        if(userDetails == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new CartListDTO(MyPageStatus.USER_INFO_ERROR.getMessage(), MyPageStatus.USER_INFO_ERROR.getCode()));
        }
        Integer userId = userDetails.getUserId();

        //장바구니 목록 가지고오기
        CartListDTO result = myPageService.getCartItems(userId);

        return ResponseEntity.ok(result);
    }

    //장바구니 상세
    @GetMapping("/getCartItem/{id}")
    public ResponseEntity<CartListDTO> getCartItem(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable String id
    ) {
        // 토큰에서 user 정보 가져오기
        if(userDetails == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new CartListDTO("로그인된 사용자만 이용하실 수 있습니다.", HttpStatus.UNAUTHORIZED.value()));
        }
        Integer userId = userDetails.getUserId();

        //장바구니 상세 가지고오기
        CartListDTO result = myPageService.getCartItem(userId, id);

        return ResponseEntity.ok(result);
    }

    // 장바구니 옵션 수정
    @PutMapping("/putCartOption")
    public ResponseEntity<DefaultDTO> putCartOption(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody CartDetailDTO cartDetailDTO
    ) {
        // 토큰에서 user 정보 가져오기
        if(userDetails == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new DefaultDTO("로그인된 사용자만 이용하실 수 있습니다.", HttpStatus.UNAUTHORIZED.value()));
        }
        Integer userId = userDetails.getUserId();

        //장바구니 옵션 수정
        DefaultDTO result = myPageService.putCartOption(userId, cartDetailDTO);

        return ResponseEntity.ok(result);
    }

    //  장바구니  삭제
    @DeleteMapping("/deleteCartItems")
    public ResponseEntity<DefaultDTO> deleteCartItems(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            //  @RequestBody CartDetailDTO cartDetailDTO,
            @RequestBody List<CartDetailDTO> cartDetailDTOs
    ) {
        // 토큰에서 user 정보 가져오기
        if(userDetails == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new DefaultDTO("로그인된 사용자만 이용하실 수 있습니다.", HttpStatus.UNAUTHORIZED.value()));
        }
        Integer userId = userDetails.getUserId();

        if(cartDetailDTOs.size() == 0){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new DefaultDTO(MyPageStatus.CART_ID_IS_NULL));
        }
        //장바구니 삭제
        DefaultDTO result = myPageService.deleteCartItems(cartDetailDTOs);

        return ResponseEntity.ok(result);
    }


}
