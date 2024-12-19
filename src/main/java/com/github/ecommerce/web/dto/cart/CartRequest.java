package com.github.ecommerce.web.dto.cart;

import com.github.ecommerce.data.entity.cart.Cart;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartRequest {

    @NotNull(message = "User ID는 필수입니다.")
    private Integer userId;

    @NotNull(message = "수량은 필수입니다.")
    @Min(value = 1, message = "수량은 최소 1 이상이어야 합니다.")
    private Integer quantity;

    @NotNull(message = "Book ID 필수입니다.")
    private Integer bookId;

    public CartRequest(Cart cart) {
        this (
                cart.getUser().getUserId(),
                cart.getQuantity(),
                cart.getBook().getBookId()
        );
    }

}
