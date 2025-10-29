package com.example.sms.presentation.api.sales.payment.incoming;

import com.example.sms.domain.model.sales.payment.incoming.PaymentReceived;
import com.example.sms.service.sales.payment.incoming.PaymentReceivedCriteria;

public class PaymentReceivedResourceDTOMapper {

    public static PaymentReceived convertToEntity(PaymentReceivedResource resource) {
        return PaymentReceived.of(
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
     * PaymentReceivedCriteriaResource を PaymentReceivedCriteria に変換
     *
     * @param resource 入金検索条件リソース
     * @return 入金検索条件
     */
    public static PaymentReceivedCriteria convertToCriteria(PaymentReceivedCriteriaResource resource) {
        if (resource == null) {
            return null;
        }

        return PaymentReceivedCriteria.builder()
                .paymentNumber(resource.getPaymentNumber())
                .paymentDate(resource.getPaymentDate())
                .departmentCode(resource.getDepartmentCode())
                .customerCode(resource.getCustomerCode())
                .paymentMethodType(resource.getPaymentMethodType() != null ? resource.getPaymentMethodType().getCode() : null)
                .paymentAccountCode(resource.getPaymentAccountCode())
                .build();
    }
}
