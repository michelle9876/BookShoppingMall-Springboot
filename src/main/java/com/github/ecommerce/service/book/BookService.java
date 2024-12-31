package com.github.ecommerce.service.book;

import com.github.ecommerce.data.entity.book.Book;
import com.github.ecommerce.data.entity.cart.Cart;
import com.github.ecommerce.data.repository.book.BookRepository;
import com.github.ecommerce.service.exception.InsufficientStockException;
import com.github.ecommerce.web.dto.book.BookDetailResponse;
import com.github.ecommerce.web.dto.book.BookResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Service
@RequiredArgsConstructor
public class BookService {
    private final ReentrantLock lock = new ReentrantLock();
    private final BookRepository bookRepository;

    public Page<BookResponse> getAllBooks(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        // 재고가 있는 도서들만 반환
        return bookRepository.findByStockQuantityGreaterThan(0, pageable)
                .map(book -> new BookResponse(
                        book.getBookId(),
                        book.getBookImageUrl(),
                        book.getTitle(),
                        (int) book.getPrice()));
    }

    public BookDetailResponse getBookDetail(Integer bookId, Integer amount) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));


        return new BookDetailResponse(
                book.getBookId(),
                book.getTitle(),
                book.getBookImageUrl(),
                (int) book.getPrice(),
                book.getSummary(),
                book.getAuthor(),
                book.getPublisher(),
                book.getStockQuantity(),
                amount
        );
    }

    public Page<BookResponse> getBooksByCategory(String category, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return bookRepository.findByCategoryContaining(category, pageable)
                .map(book -> new BookResponse(
                        book.getBookId(),
                        book.getBookImageUrl(),
                        book.getTitle(),
                        (int) book.getPrice()));
    }


    @Transactional
    public void reduceBookStocks(List<Cart> cartItems) {
        for (Cart cartItem : cartItems) {
            final int updatedRows = bookRepository.reduceStockQuantityById(cartItem.getBook().getBookId(), cartItem.getQuantity());
            if (updatedRows == 0) {
                final Book book = cartItem.getBook();
                throw new InsufficientStockException(book.getBookId(), book.getTitle());
            }
        }
    }
}
