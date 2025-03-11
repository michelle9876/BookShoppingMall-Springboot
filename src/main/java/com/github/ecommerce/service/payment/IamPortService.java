package com.github.ecommerce.service.payment;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.AllArgsConstructor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class IamPortService {
    private final IamportClient client;
    private final int MAX_TRIES = 3;
    private final int DELAY_MILLIS = 1000;
    // Verify payment after receiving IMP UID from client
    @Retryable(
            value = {IamportResponseException.class, IOException.class},
            maxAttempts = MAX_TRIES,
            backoff = @Backoff(delay = DELAY_MILLIS)
    )
    public boolean verifyPayment(String impUid, BigDecimal expectedAmount) throws IamportResponseException, IOException {
        IamportResponse<Payment> payment;

        // for - true, break once it succeeds, check the condition(if (tries == MAX_TRIES)) inside of catch

//        for(int tries = 1; true; tries++) {
//            try {
                payment = client.paymentByImpUid(impUid);
//                break;
//            } catch (IamportResponseException | IOException exc) {
//                System.out.println("Error reaching out to IamPort: " + exc.getMessage());
//                if (tries == MAX_TRIES) {
//                    throw exc;
//                }
//            }
//        }
//


        if (payment.getResponse() == null) {
            return false;
        }

        Payment paymentData = payment.getResponse();
        return paymentData.getAmount().equals(expectedAmount) &&
                "paid".equals(paymentData.getStatus());
    }
}
