package com.example.sms.presentation.api.sales.payment.incoming;

import com.example.sms.domain.model.sales.payment.incoming.Payment;
import com.example.sms.service.sales.payment.incoming.PaymentCriteria;

public class PaymentResourceDTOMapper {

    public static Payment convertToEntity(PaymentResource resource) {
        return Payment.of(
                resource.getPaymentNumber(),
                resource.getPaymentDate(),
                resource.getDepartmentCode(),
                resource.getDepartmentStartDate(),
                resource.getCustomerCode(),
                resource.getCustomerBranchNumber(),
                resource.getPaymentMethodType().getCode(),
                resource.getPaymentAccountCode(),
                resource.getPaymentAmount(),
                resource.getOffsetAmount()
        );
    }

    /**
     * PaymentCriteriaResource を PaymentCriteria に変換
     *
     * @param resource 入金検索条件リソース
     * @return 入金検索条件
     */
    public static PaymentCriteria convertToCriteria(PaymentCriteriaResource resource) {
        if (resource == null) {
            return null;
        }

        return PaymentCriteria.builder()
                .paymentNumber(resource.getPaymentNumber())
                .paymentDate(resource.getPaymentDate())
                .departmentCode(resource.getDepartmentCode())
                .customerCode(resource.getCustomerCode())
                .paymentMethodType(resource.getPaymentMethodType() != null ? resource.getPaymentMethodType().getCode() : null)
                .paymentAccountCode(resource.getPaymentAccountCode())
                .build();
    }
}
