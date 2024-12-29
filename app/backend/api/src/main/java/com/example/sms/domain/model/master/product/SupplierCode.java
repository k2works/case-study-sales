package com.example.sms.domain.model.master.product;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * 仕入先コード
 */
@Value
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class SupplierCode {
    String value;
    Integer branchNumber; // 仕入先枝番

    public static SupplierCode of(String value, Integer supplierBranchNumber) {
        return new SupplierCode(value, supplierBranchNumber);
    }
}
