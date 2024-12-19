package com.github.ecommerce.web.controller.book;

import com.github.ecommerce.service.book.BookService;
import com.github.ecommerce.web.dto.book.BookDetailResponse;
import com.github.ecommerce.web.dto.book.BookResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    // 전체 도서 조회
    @GetMapping
    public ResponseEntity<Page<BookResponse>> getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size
    ) {
        Page<BookResponse> books = bookService.getAllBooks(page, size);
        return ResponseEntity.ok(books);
    }

    // 도서 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<BookDetailResponse> getBookDetail(@PathVariable Integer id,
                                                            @RequestParam(defaultValue = "1") Integer amount) {
        BookDetailResponse bookDetailResponse = bookService.getBookDetail(id, amount);
        return ResponseEntity.ok(bookDetailResponse);
    }

    // 카테고리별 필터 조회
    @GetMapping("/category/{category}")
    public ResponseEntity<Page<BookResponse>> getBooksByCategory(@PathVariable String category,
                                                                 @RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "15") int size
    ){
        Page<BookResponse> categoryBooks = bookService.getBooksByCategory(category,page,size);
        return ResponseEntity.ok(categoryBooks);
    }
}
