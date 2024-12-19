package com.github.ecommerce.data.entity.book;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "book")
public class Book {

    @Id
    @Column(name = "book_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bookId;

    @Column(name = "title")
    private String title;

    @Column(name = "price")
    private float price;

    @Column(name = "stock_quantity")
    private Integer stockQuantity;

    @Column(name = "available_until")
    private Timestamp availableUntil;

    @Column(name = "book_image")
    private String bookImageUrl;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "summary")
    private String summary;

    @Column(name = "category")
    private String category;

    @Column(name = "publisher")
    private String publisher;

    @Column(name = "author")
    private String author;

}
