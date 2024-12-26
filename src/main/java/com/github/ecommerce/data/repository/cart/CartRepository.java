package com.github.ecommerce.data.repository.cart;

import com.github.ecommerce.data.entity.auth.User;
import com.github.ecommerce.data.entity.book.Book;
import com.github.ecommerce.data.entity.cart.Cart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

    Cart findByBookAndUser(Book book, User user);
    //processPayment 호출
    @Query("SELECT c From Cart c JOIN FETCH c.book b WHERE c.user.userId = :userId ORDER BY c.cartId DESC")
    List<Cart> findAllByUserId(@Param("userId")Integer userId);


    //마이페이지 사용
    @Query("SELECT c From Cart c JOIN FETCH c.book b WHERE c.user.userId = :userId ORDER BY c.cartId DESC")
    Page<Cart> findAllByUserIdPageable(@Param("userId") Integer userId, Pageable pageable);

    //마이페이지 사용
    @Query("SELECT c From Cart c JOIN FETCH c.book b WHERE c.cartId = :cartId AND c.user.userId = :userId")
    Optional<Cart> findByIdFetchJoin(@Param("cartId") Integer cartId, @Param("userId") Integer userId);

}
