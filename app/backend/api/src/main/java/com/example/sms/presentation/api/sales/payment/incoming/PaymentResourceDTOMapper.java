package com.example.sms.presentation.api.sales.payment.incoming;

import com.example.sms.domain.model.sales.payment.incoming.Payment;

public class PaymentResourceDTOMapper {

    public static Payment convertToEntity(PaymentResource resource) {
        return Payment.of(
                resource.getPaymentNumber(),
                resource.getPaymentDate(),
                resource.getDepartmentCode(),
                resource.getDepartmentStartDate(),
                resource.getCustomerCode(),
                resource.getCustomerBranchNumber(),
                Integer.parseInt(resource.getPaymentMethodType()),
                resource.getPaymentAccountCode(),
                resource.getPaymentAmount(),
                resource.getOffsetAmount()
        );
    }
}