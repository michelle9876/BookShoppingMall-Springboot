package com.github.ecommerce.service.cart;

import com.github.ecommerce.data.entity.auth.User;
import com.github.ecommerce.data.entity.book.Book;
import com.github.ecommerce.data.entity.cart.Cart;
import com.github.ecommerce.data.repository.auth.AuthRepository;
import com.github.ecommerce.data.repository.book.BookRepository;
import com.github.ecommerce.data.repository.cart.CartRepository;
import com.github.ecommerce.web.dto.cart.CartRequest;
import com.github.ecommerce.web.dto.cart.CartResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final BookRepository bookRepository;
    private final AuthRepository authRepository;

    @Transactional
    public CartResponse addToCart(CartRequest request, Integer userId) {

        User user =authRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found"));

        Cart cart = cartRepository.findByBookAndUser(book,user);

//        if (request.getQuantity() == null || request.getQuantity() <= 0) {
//            throw new IllegalArgumentException("Quantity must be greater than 0");
//        }

        if (cart != null) {
            cart.setQuantity(cart.getQuantity() + request.getQuantity());
        }else {
            cart = new Cart();
            cart.setUser(user);
            cart.setBook(book);
            cart.setQuantity(request.getQuantity());
        }

        Cart savedCart =  cartRepository.save(cart);

        return new CartResponse(
                savedCart.getCartId(),
                savedCart.getUser().getUserId(),
                savedCart.getBook().getBookId(),
                savedCart.getBook().getTitle(),
                savedCart.getQuantity(),
                (int)savedCart.getBook().getPrice()
        );
    }

    public List<CartResponse> getCartDetailsByUserId(Integer userId) {
        return null;
    }
}
