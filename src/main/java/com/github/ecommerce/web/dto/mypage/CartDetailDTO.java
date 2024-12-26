package com.github.ecommerce.web.dto.mypage;

import com.github.ecommerce.data.entity.cart.Cart;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDetailDTO {
    private Integer userId;
    private Integer cartId;
    private Integer bookId;
    private String title;
    private String bookImage;
    private String publisher;
    private String author;
    private Integer price;
    private Integer quantity;
    private Integer stockQuantity;


    public CartDetailDTO(Cart cart) {
        this.userId = cart.getUser().getUserId();
        this.cartId = cart.getCartId();
        this.bookId = cart.getBook().getBookId();
        this.title = cart.getBook().getTitle();
        this.bookImage = cart.getBook().getBookImageUrl();
        this.publisher = cart.getBook().getPublisher();
        this.author = cart.getBook().getAuthor();
        this.price = (int) cart.getBook().getPrice();
        this.stockQuantity = cart.getBook().getStockQuantity();
        this.quantity = cart.getQuantity();
    }

    public static CartDetailDTO from(Cart cart) {return new CartDetailDTO(cart);}
}
