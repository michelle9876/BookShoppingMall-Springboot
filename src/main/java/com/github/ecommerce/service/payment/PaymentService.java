package com.github.ecommerce.service.payment;

import com.github.ecommerce.web.dto.payment.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {
    /**
     * Method to validate a credit card number using the Luhn algorithm.
     * @param cardNumber Credit card number as a string
     * @return boolean indicating whether the card number is valid
     */

    // [[ Validate credit card : Luhn Algorithm 적용!]]
    /*
        << Luhn Algorithm 방식 >>
        1. Loops through the digits from right to left.
        2. Doubles every second digit and adjusts values >9.
        3. Computes the sum.
        4. Returns whether the sum is divisible by 10.
     */
    public boolean validateCreditCard(String cardNumber){
        int sum = 0;
        boolean alternate = false;

        for(int i = cardNumber.length() - 1; i >=0; i--){
            int n = Character.getNumericValue(cardNumber.charAt(i));
            if (alternate){
                n *= 2;
                if (n > 9){
                    n -= 9;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }

    /**
     * Method to validate the shipping address.
     * @param address ShippingAddressDto object containing the address details
     * @return boolean indicating whether the address is valid
     */

    public boolean validateAddress(ShippingAddressDTO address){
        return address != null &&
                address.getMainAddress() != null && !address.getMainAddress().isEmpty() &&
                address.getZipCode() != null && !address.getZipCode().isEmpty();
    }


    public void savePaymentInfo(PaymentInfoDTO paymentInfo) {
        // Extracting the necessary information from the DTO
        String cardNumber = paymentInfo.getPaymentCard().getCardNumber();
        ShippingAddressDTO shippingAddress = paymentInfo.getShippingAddress();
        List<BookDTO> books = paymentInfo.getBooks();

        // Create the PaymentInformation entity
        PaymentInformation paymentInformation = new PaymentInformation(cardNumber, shippingAddress, books);

        // Log the information
        System.out.println("Saved Payment Info: " + paymentInformation);
    }

    public float processOrder(PaymentInfoDTO paymentInfo, int userId) {
        float totalPrice = 0.0f;

        for(BookDTO book : paymentInfo.getBooks()){
            // Simulate fetching book info from the database
            BookInfo bookInfo = getBookInfoFromDb(book.getBookId());

            // Calculate total price for this book
            float bookTotal = (float) (bookInfo.getPrice() * book.getQuantity());
            totalPrice += bookTotal;

           // Reduce stock quantity in the database
            reduceStockQuantity(book.getBookId(), book.getQuantity());

        }
        // Log the order details
        System.out.println("Order processed for user ID " + userId + " with total price: " + totalPrice);

        return totalPrice;

    }

    private BookInfo getBookInfoFromDb(int bookId){
        // Simulate a book info retrieval
        return new BookInfo(bookId, "Sample Book", 20.0, 100); // ID, Title, Price, Stock

    }

    private void reduceStockQuantity(int bookId, int quantity){
        // Simulate stock reduction
        System.out.println("Reduced stock for book ID " + bookId + " by " + quantity);
    }

}
