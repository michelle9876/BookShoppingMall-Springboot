package com.github.ecommerce.web.dto.mypage;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MyPageStatus {
    USER_NOT_FOUNDED("해당 유저를 찾을 수 없습니다.", 404),
    USER_ERROR_FORBIDDEN("로그인 정보와 접근 정보가 다릅니다.",403),
    USER_INFO_RETURN("유저 정보 요청이 완료되었습니다.", 200),
    //유저정보 수정
    USER_INFO_PUT_RETURN("사용자 정보를 수정했습니다.", 200),
    USER_INFO_PUT("사용자 정보를 수정했습니다.", 204),
    USER_INFO_ERROR("사용자 정보를 수정 시 에러가 발생했습니다.", 500),


    //장바구니 목록
    CART_ITEMS_RETURN("장바구니 목록 요청이 완료되었습니다.", 200),
    //장바구니 상세
    CART_RETURN("장바구니 상세요청이 완료되었습니다.", 200),
    //장바구니 옵션 수정
    CART_PUT("상품의 옵션을 수정했습니다.", 204),
    //장바구니 삭제
    CART_DELETE("상품을 삭제했습니다.", 204),
    CART_ERROR("장바구니 작업 중 에러가 발생했습니다.", 500),
    CART_NOT_FOUNDED("장바구니에 해당 상품이 없습니다.", 404),
    CART_QUANTITY_ERROR("준비된 상품보다 많은 수량을 선택하실 수 없습니다.", 400),
    CART_ID_IS_NULL("삭제할 상품을 선택해주세요.", 400),
    CART_ID_ACCESS_ERROR("상품삭제 권한이 없습니다.", 401),

    // 결제내역
    PAYMENT_LIST_RETURN("결제내역 목록 요청이 완료되었습니다.", 200),
    //결제 상세정보
    PAYMENT_RETURN("결제내역 상세 요청이 완료되었습니다.", 200),
    PAYMENT_NOT_FOUNDED("결제정보를 찾을 수 없습니다..", 404);

    private final String message;
    private final int code;

}
