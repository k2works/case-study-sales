package com.example.sms.domain.model.master.partner;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("仕入先")
class VendorTest {
    private Vendor getVendor() {
        return Vendor.of(
                "001",
                1,
                "仕入先名A",
                "シリヒキサキメイエー",
                "担当者名A",
                "部門名A",
                "123-4567",
                "東京都",
                "新宿区1-1-1",
                "マンション101号室",
                "03-1234-5678",
                "03-1234-5679",
                "test@example.comw",
                10,
                1,
                20,
                2
        );
    }

    @Test
    @DisplayName("仕入先を作成できる")
    void createVendor() {
        Vendor vendor = getVendor();

        assertAll(
                () -> assertEquals("001", vendor.getVendorCode().getCode().getValue()),
                () -> assertEquals(1, vendor.getVendorCode().getBranchNumber()),
                () -> assertEquals("仕入先名A", vendor.getVendorName().getValue().getName()),
                () -> assertEquals("シリヒキサキメイエー", vendor.getVendorName().getValue().getNameKana()),
                () -> assertEquals("担当者名A", vendor.getVendorContactName()),
                () -> assertEquals("部門名A", vendor.getVendorDepartmentName()),
                () -> assertEquals("1234567", vendor.getVendorAddress().getPostalCode().getValue()),
                () -> assertEquals("東京都", vendor.getVendorAddress().getPrefecture().toString()),
                () -> assertEquals("新宿区1-1-1", vendor.getVendorAddress().getAddress1()),
                () -> assertEquals("マンション101号室", vendor.getVendorAddress().getAddress2()),
                () -> assertEquals("03-1234-5678", vendor.getVendorPhoneNumber().getValue()),
                () -> assertEquals("03-1234-5679", vendor.getVendorFaxNumber().getValue()),
                () -> assertEquals("test@example.comw", vendor.getVendorEmailAddress().getValue()),
                () -> assertEquals(ClosingDate.十日, vendor.getVendorClosingInvoice().getCustomerClosingDay()),
                () -> assertEquals(PaymentMonth.翌月, vendor.getVendorClosingInvoice().getCustomerPaymentMonth()),
                () -> assertEquals(PaymentDay.二十日, vendor.getVendorClosingInvoice().getCustomerPaymentDay()),
                () -> assertEquals(PaymentMethod.手形, vendor.getVendorClosingInvoice().getCustomerPaymentMethod())
        );
    }
}
