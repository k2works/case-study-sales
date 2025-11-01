package com.example.sms.presentation.api.procurement.payment;

import com.example.sms.domain.model.procurement.payment.PurchasePayment;
import com.example.sms.service.procurement.payment.PurchasePaymentCriteria;

public class PurchasePaymentResourceDTOMapper {

    public static PurchasePayment convertToEntity(PurchasePaymentResource resource) {
        return PurchasePayment.of(
                resource.getPaymentNumber(),
                resource.getPaymentDate(),
                resource.getDepartmentCode(),
                resource.getDepartmentStartDate(),
                resource.getSupplierCode(),
                resource.getSupplierBranchNumber(),
                resource.getPaymentMethodType(),
                resource.getPaymentAmount(),
                resource.getTotalConsumptionTax(),
                resource.getPaymentCompletedFlag()
        );
    }

    /**
     * PurchasePaymentCriteriaResource を PurchasePaymentCriteria に変換
     *
     * @param resource 支払検索条件リソース
     * @return 支払検索条件
     */
    public static PurchasePaymentCriteria convertToCriteria(PurchasePaymentCriteriaResource resource) {
        if (resource == null) {
            return null;
        }

        return PurchasePaymentCriteria.builder()
                .paymentNumber(resource.getPaymentNumber())
                .paymentDate(resource.getPaymentDate())
                .departmentCode(resource.getDepartmentCode())
                .supplierCode(resource.getSupplierCode())
                .paymentMethodType(resource.getPaymentMethodType())
                .paymentCompletedFlag(resource.getPaymentCompletedFlag() != null ? (resource.getPaymentCompletedFlag() ? 1 : 0) : null)
                .build();
    }
}
