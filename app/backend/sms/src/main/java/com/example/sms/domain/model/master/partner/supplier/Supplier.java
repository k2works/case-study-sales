package com.example.sms.domain.model.master.partner.supplier;

import com.example.sms.domain.model.master.partner.Partner;
import com.example.sms.domain.model.master.partner.PartnerName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * 仕入先
 */
@Value
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder(toBuilder = true)
public class Supplier {
    SupplierCode supplierCode; // 仕入先コード
    PartnerName supplierName; // 仕入先名
    Partner partner; // 取引先

    public static Supplier of(String supplierCode, Integer branchNumber, String supplierName) {
        return Supplier.builder()
                .supplierCode(SupplierCode.of(supplierCode, branchNumber))
                .supplierName(PartnerName.of(supplierName, supplierName))
                .build();
    }
}