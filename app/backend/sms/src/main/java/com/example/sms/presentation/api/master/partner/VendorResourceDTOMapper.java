package com.example.sms.presentation.api.master.partner;

import com.example.sms.domain.model.master.partner.vendor.Vendor;
import com.example.sms.service.master.partner.VendorCriteria;

public class VendorResourceDTOMapper {

    public static Vendor convertToEntity(VendorResource resource) {
        return Vendor.of(
                resource.getVendorCode(),
                resource.getVendorBranchNumber(),
                resource.getVendorName(),
                resource.getVendorNameKana(),
                resource.getVendorContactName(),
                resource.getVendorDepartmentName(),
                resource.getVendorPostalCode(),
                resource.getVendorPrefecture(),
                resource.getVendorAddress1(),
                resource.getVendorAddress2(),
                resource.getVendorPhoneNumber(),
                resource.getVendorFaxNumber(),
                resource.getVendorEmailAddress(),
                resource.getVendorClosingDate().getValue(),
                resource.getVendorPaymentMonth().getValue(),
                resource.getVendorPaymentDate().getValue(),
                resource.getVendorPaymentMethod().getValue()
        );
    }

    public static VendorCriteria convertToCriteria(VendorCriteriaResource resource) {
        return VendorCriteria.builder()
                .vendorCode(resource.getVendorCode())
                .vendorName(resource.getVendorName())
                .vendorContactName(resource.getVendorContactName())
                .vendorDepartmentName(resource.getVendorDepartmentName())
                .postalCode(resource.getPostalCode())
                .prefecture(resource.getPrefecture())
                .address1(resource.getAddress1())
                .address2(resource.getAddress2())
                .vendorPhoneNumber(resource.getVendorPhoneNumber())
                .vendorFaxNumber(resource.getVendorFaxNumber())
                .vendorEmailAddress(resource.getVendorEmailAddress())
                .build();
    }
}