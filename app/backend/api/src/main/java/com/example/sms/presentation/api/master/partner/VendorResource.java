package com.example.sms.presentation.api.master.partner;

import com.example.sms.domain.model.master.partner.billing.ClosingDate;
import com.example.sms.domain.model.master.partner.billing.PaymentDay;
import com.example.sms.domain.model.master.partner.billing.PaymentMethod;
import com.example.sms.domain.model.master.partner.billing.PaymentMonth;
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
    @Schema(description = "仕入先コード")
    String vendorCode;
    @NotNull
    @Schema(description = "仕入先枝番")
    Integer vendorBranchNumber;
    @Schema(description = "仕入先名")
    String vendorName;
    @Schema(description = "仕入先名カナ")
    String vendorNameKana;
    @Schema(description = "仕入先担当者名")
    String vendorContactName;
    @Schema(description = "仕入先部署名")
    String vendorDepartmentName;
    @Schema(description = "仕入先郵便番号")
    String vendorPostalCode;
    @Schema(description = "仕入先都道府県")
    String vendorPrefecture;
    @Schema(description = "仕入先住所1")
    String vendorAddress1;
    @Schema(description = "仕入先住所2")
    String vendorAddress2;
    @Schema(description = "仕入先電話番号")
    String vendorPhoneNumber;
    @Schema(description = "仕入先FAX番号")
    String vendorFaxNumber;
    @Schema(description = "仕入先メールアドレス")
    String vendorEmailAddress;
    @Schema(description = "仕入先締め日")
    ClosingDate vendorClosingDate;
    @Schema(description = "仕入先支払月")
    PaymentMonth vendorPaymentMonth;
    @Schema(description = "仕入先支払日")
    PaymentDay vendorPaymentDate;
    @Schema(description = "仕入先支払方法")
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
                .vendorClosingDate(vendor.getVendorClosingBilling().getClosingDay())
                .vendorPaymentMonth(vendor.getVendorClosingBilling().getPaymentMonth())
                .vendorPaymentDate(vendor.getVendorClosingBilling().getPaymentDay())
                .vendorPaymentMethod(vendor.getVendorClosingBilling().getPaymentMethod())
                .build();
    }

    public static List<VendorResource> from(List<Vendor> vendors) {
        return vendors.stream()
                .map(VendorResource::from)
                .toList();
    }
}
