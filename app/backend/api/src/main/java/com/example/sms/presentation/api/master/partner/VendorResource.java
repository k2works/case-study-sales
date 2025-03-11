package com.example.sms.presentation.api.master.partner;

import com.example.sms.domain.model.master.partner.invoice.ClosingDate;
import com.example.sms.domain.model.master.partner.invoice.PaymentDay;
import com.example.sms.domain.model.master.partner.invoice.PaymentMethod;
import com.example.sms.domain.model.master.partner.invoice.PaymentMonth;
import com.example.sms.domain.model.master.partner.vendor.Vendor;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Schema(description = "仕入先")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VendorResource {
    @NotNull
    String vendorCode;
    @NotNull
    Integer vendorBranchNumber;
    String vendorName;
    String vendorNameKana;
    String vendorContactName;
    String vendorDepartmentName;
    String vendorPostalCode;
    String vendorPrefecture;
    String vendorAddress1;
    String vendorAddress2;
    String vendorPhoneNumber;
    String vendorFaxNumber;
    String vendorEmailAddress;
    ClosingDate vendorClosingDate;
    PaymentMonth vendorPaymentMonth;
    PaymentDay vendorPaymentDate;
    PaymentMethod vendorPaymentMethod;

    public static VendorResource from(Vendor vendor) {
        return VendorResource.builder()
                .vendorCode(vendor.getVendorCode().getCode().getValue())
                .vendorBranchNumber(vendor.getVendorCode().getBranchNumber())
                .vendorName(vendor.getVendorName().getValue().getName())
                .vendorNameKana(vendor.getVendorName().getValue().getNameKana())
                .vendorContactName(vendor.getVendorContactName())
                .vendorDepartmentName(vendor.getVendorDepartmentName())
                .vendorPostalCode(vendor.getVendorAddress().getPostalCode().getValue())
                .vendorPrefecture(vendor.getVendorAddress().getPrefecture().name())
                .vendorAddress1(vendor.getVendorAddress().getAddress1())
                .vendorAddress2(vendor.getVendorAddress().getAddress2())
                .vendorPhoneNumber(vendor.getVendorPhoneNumber().getValue())
                .vendorFaxNumber(vendor.getVendorFaxNumber().getValue())
                .vendorEmailAddress(vendor.getVendorEmailAddress().getValue())
                .vendorClosingDate(vendor.getVendorClosingInvoice().getClosingDay())
                .vendorPaymentMonth(vendor.getVendorClosingInvoice().getPaymentMonth())
                .vendorPaymentDate(vendor.getVendorClosingInvoice().getPaymentDay())
                .vendorPaymentMethod(vendor.getVendorClosingInvoice().getPaymentMethod())
                .build();
    }

    public static List<VendorResource> from(List<Vendor> vendors) {
        return vendors.stream()
                .map(VendorResource::from)
                .toList();
    }
}
