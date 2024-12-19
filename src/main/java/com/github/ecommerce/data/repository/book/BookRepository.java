package com.github.ecommerce.data.repository.book;

import com.github.ecommerce.data.entity.book.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book,Integer> {
    // 재고가 0 이상인 경우만 출력
    Page<Book> findByStockQuantityGreaterThan(int stockQuantity, Pageable pageable);

    Page<Book> findByCategoryContaining(String category, Pageable pageable);

}
