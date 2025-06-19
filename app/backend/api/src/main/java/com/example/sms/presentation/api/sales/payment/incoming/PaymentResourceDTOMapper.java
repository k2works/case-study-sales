package com.example.sms.presentation.api.sales.payment.incoming;

import com.example.sms.domain.model.sales.payment.incoming.Payment;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PaymentResourceDTOMapper {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_ZONED_DATE_TIME;

    public static Payment convertToEntity(PaymentResource resource) {
        return Payment.of(
                resource.getPaymentNumber(),
                LocalDateTime.parse(resource.getPaymentDate(), FORMATTER),
                resource.getDepartmentCode(),
                LocalDateTime.parse(resource.getDepartmentStartDate(), FORMATTER),
                resource.getCustomerCode(),
                resource.getCustomerBranchNumber(),
                Integer.parseInt(resource.getPaymentMethodType()),
                resource.getPaymentAccountCode(),
                resource.getPaymentAmount(),
                resource.getOffsetAmount()
        );
    }
}