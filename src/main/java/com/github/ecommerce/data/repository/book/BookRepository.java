package com.github.ecommerce.data.repository.book;

import com.github.ecommerce.data.entity.book.Book;
import com.github.ecommerce.web.dto.book.BookStockUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book,Integer> {
    // 재고가 0 이상인 경우만 출력
    Page<Book> findByStockQuantityGreaterThan(int stockQuantity, Pageable pageable);

    Page<Book> findByCategoryContaining(String category, Pageable pageable);

    @Query("SELECT b.bookId, b.stockQuantity FROM Book b WHERE b.bookId IN :bookIds")
    List<Book> findStocksByBookIds(@Param("bookIds") List<Integer> bookIds);

    @Modifying
    @Query("UPDATE Book b SET b.stockQuantity = b.stockQuantity - :amount WHERE b.bookId = :id AND b.stockQuantity >= :amount")
    int reduceStockQuantityById(@Param("id") Integer id, @Param("amount") int amount);

    @Modifying
    @Query("UPDATE Book b SET b.stockQuantity = b.stockQuantity - :stockReduction WHERE b.bookId = :bookId AND b.stockQuantity >= :stockReduction")
    int reduceStockQuantitiesByIds(List<BookStockUpdateDto> bookStockUpdateDtos);
}
