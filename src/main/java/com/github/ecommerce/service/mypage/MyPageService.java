package com.github.ecommerce.service.mypage;

import com.github.ecommerce.data.entity.auth.User;
import com.github.ecommerce.data.entity.cart.Cart;
import com.github.ecommerce.data.entity.payment.Payment;
import com.github.ecommerce.data.repository.cart.CartRepository;
import com.github.ecommerce.data.repository.mypage.UserRepository;
import com.github.ecommerce.data.repository.payment.PaymentRepository;
import com.github.ecommerce.service.exception.NotFoundException;
import com.github.ecommerce.service.s3Image.AwsS3Service;
import com.github.ecommerce.web.dto.mypage.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MyPageService {
    //user repository
    private final UserRepository userRepository;
    // s3 service
    private final AwsS3Service awsS3Service;


    //유저 정보 가지고오기
    public UserInfoDTO getUserInfo(Integer userId) {
        User user = userRepository.findById(userId).orElse(null);
        if(user == null) {
            return new UserInfoDTO(MyPageStatus.USER_NOT_FOUNDED);
        }

        UserInfoDTO result = userInfoMapping(user);
        result.setStatus(new DefaultDTO(MyPageStatus.USER_INFO_RETURN));

        return result;
    }

    //user 를 userInfo로 매핑
    public UserInfoDTO userInfoMapping(User user) {
        UserInfoDTO userInfo = UserInfoDTO.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .userName(user.getUserName())
                .profileImage(user.getProfileImage())
                .gender(user.getGender())
                .phone(user.getPhone())
                .zipCode(user.getZipCode())
                .mainAddress(user.getMainAddress())
                .detailsAddress(user.getDetailAddress())
                .build();

        return userInfo;
    }


    //유저 정보 수정하기
    @Transactional(transactionManager = "tmJpa1")
    public UserInfoDTO putUserInfo(String id, UserInfoDTO userInfo, MultipartFile image) {

        Integer userId = Integer.valueOf(id);
        User user = userRepository.findById(userId).orElse(null);
        if(user == null) {
            return new UserInfoDTO(MyPageStatus.USER_NOT_FOUNDED);
        }

        //file이 들어온다면?
        if(image != null) {
            //s3 이미지 등록
            String newImageUrl = awsS3Service.upload(image);
            String basicImageUrl = "https://project2-profile.s3.ap-northeast-2.amazonaws.com/basic.jpg";
            if(!basicImageUrl.equals(user.getProfileImage())){
                //이미 기존의 이미지가 있으므로 기존 이미지를 삭제한다.
                awsS3Service.deleteImageFromS3(user.getProfileImage());
            }
            user.setProfileImage(newImageUrl); //프로필사진 업데이트
        }

        if(userInfo.getUserName() != null && !userInfo.getUserName().isEmpty()){
            user.setUserName(userInfo.getUserName()); //이름 업데이트
        }
        if(userInfo.getPhone() != null && !userInfo.getPhone().isEmpty()){
            user.setPhone(userInfo.getPhone()); //전화번호 업데이트
        }
        if(userInfo.getGender() != null && !userInfo.getGender().isEmpty()){
            user.setGender(userInfo.getGender()); //성별 업데이트
        }
        if(userInfo.getZipCode() != null && !userInfo.getZipCode().isEmpty()){
            user.setZipCode(userInfo.getZipCode()); //우편번호 업데이트
        }
        if(userInfo.getMainAddress() != null && !userInfo.getMainAddress().isEmpty()){
            user.setMainAddress(userInfo.getMainAddress()); //기본주소 업데이트
        }
        if(userInfo.getDetailsAddress() != null && !userInfo.getDetailsAddress().isEmpty()){
            user.setDetailAddress(userInfo.getDetailsAddress()); //상세주소 업데이트
        }

        UserInfoDTO result = userInfoMapping(user);
        result.setStatus(new DefaultDTO(MyPageStatus.USER_INFO_PUT_RETURN));

        return result;
    }

}
