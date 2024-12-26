package com.github.ecommerce.service.payment;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class IamPortService {
    private final IamportClient client;
    // Verify payment after receiving IMP UID from client
    public boolean verifyPayment(String impUid, BigDecimal expectedAmount)
            throws IamportResponseException, IOException {
        IamportResponse<Payment> payment = client.paymentByImpUid(impUid);

        if (payment.getResponse() == null) {
            return false;
        }

        Payment paymentData = payment.getResponse();
        return paymentData.getAmount().equals(expectedAmount) &&
                "paid".equals(paymentData.getStatus());
    }
}
