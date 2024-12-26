package com.github.ecommerce.service.exception;

public class InsufficientStockException extends IllegalArgumentException {
  public static final String STOCK_REDUCTION_FAILURE = "Failed to reduce stock for book %d (%s): Stock is insufficient or book does not exist.";
  public InsufficientStockException(int bookId, String title) {
    super(String.format(STOCK_REDUCTION_FAILURE, bookId, title));
  }
}