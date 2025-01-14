package com.example.sms.domain.model.master.partner;

import com.example.sms.domain.type.partner.CustomerBillingCategory;
import com.example.sms.domain.type.partner.CustomerType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("顧客")
class CustomerTest {
    private Customer getCustomer() {
        return Customer.of(
                "001",  // customerCode（顧客コード）
                1,  // customerBranchNumber（顧客枝番）
                1,  // customerCategory（顧客区分）
                "001",  // billingCode（請求先コード）
                1,  // billingBranchNumber（請求先枝番）
                "001",  // collectionCode（回収先コード）
                1,  // collectionBranchNumber（回収先枝番）
                "山田太郎",  // customerName（顧客名）
                "ヤマダタロウ",  // customerNameKana（顧客名カナ）
                "RE001",  // companyRepresentativeCode（自社担当者コード）
                "花子",  // customerRepresentativeName（顧客担当者名）
                "営業部",  // customerDepartmentName（顧客部門名）
                "123-4567",  // customerPostalCode（顧客郵便番号）
                "東京都",  // customerPrefecture（顧客都道府県）
                "新宿区1-1-1",  // customerAddress1（顧客住所１）
                "マンション101号室",  // customerAddress2（顧客住所２）
                "03-1234-5678",  // customerPhoneNumber（顧客電話番号）
                "03-1234-5679",  // customerFaxNumber（顧客FAX番号）
                "example@example.com",  // customerEmailAddress（顧客メールアドレス）
                2,  // customerBillingCategory（顧客請求区分）
                10,  // customerClosingDay1（顧客締日１）
                0,  // customerPaymentMonth1（顧客支払月１）
                10,  // customerPaymentDay1（顧客支払日１）
                1,  // customerPaymentMethod1（顧客支払方法１）
                20,  // customerClosingDay2（顧客締日２）
                1,  // customerPaymentMonth2（顧客支払月２）
                99,  // customerPaymentDay2（顧客支払日２）
                2   // customerPaymentMethod2（顧客支払方法２）
        );
    }

    @Test
    @DisplayName("顧客を作成できる")
    void shouldCreateCustomer() {
        Customer customer = getCustomer();

        assertAll(
                () -> assertEquals("001", customer.getCustomerCode().getCode().getValue()),
                () -> assertEquals(1, customer.getCustomerCode().getBranchNumber()),
                () -> assertEquals(CustomerType.顧客, customer.getCustomerType()),
                () -> assertEquals("001", customer.getBillingCode().getCode().getValue()),
                () -> assertEquals(1, customer.getBillingCode().getBranchNumber()),
                () -> assertEquals("001", customer.getCollectionCode().getCode().getValue()),
                () -> assertEquals(1, customer.getCollectionCode().getBranchNumber()),
                () -> assertEquals("山田太郎", customer.getCustomerName().getValue().getName()),
                () -> assertEquals("ヤマダタロウ", customer.getCustomerName().getValue().getNameKana()),
                () -> assertEquals("RE001", customer.getCompanyRepresentativeCode()),
                () -> assertEquals("花子", customer.getCustomerRepresentativeName()),
                () -> assertEquals("営業部", customer.getCustomerDepartmentName()),
                () -> assertEquals("1234567", customer.getCustomerAddress().getPostalCode().getValue()),
                () -> assertEquals("東京都", customer.getCustomerAddress().getPrefecture().toString()),
                () -> assertEquals("新宿区1-1-1", customer.getCustomerAddress().getAddress1()),
                () -> assertEquals("マンション101号室", customer.getCustomerAddress().getAddress2()),
                () -> assertEquals("03-1234-5678", customer.getCustomerPhoneNumber().getValue()),
                () -> assertEquals("03-1234-5679", customer.getCustomerFaxNumber().getValue()),
                () -> assertEquals("example@example.com", customer.getCustomerEmailAddress().getValue()),
                () -> assertEquals(CustomerBillingCategory.締請求, customer.getInvoice().getCustomerBillingCategory()),
                () -> assertEquals(ClosingDate.十日, customer.getInvoice().getClosingInvoice1().getClosingDay()),
                () -> assertEquals(PaymentMonth.当月, customer.getInvoice().getClosingInvoice1().getPaymentMonth()),
                () -> assertEquals(PaymentDay.十日, customer.getInvoice().getClosingInvoice1().getPaymentDay()),
                () -> assertEquals(PaymentMethod.振込, customer.getInvoice().getClosingInvoice1().getPaymentMethod()),
                () -> assertEquals(ClosingDate.二十日, customer.getInvoice().getClosingInvoice2().getClosingDay()),
                () -> assertEquals(PaymentMonth.翌月, customer.getInvoice().getClosingInvoice2().getPaymentMonth()),
                () -> assertEquals(PaymentDay.末日, customer.getInvoice().getClosingInvoice2().getPaymentDay()),
                () -> assertEquals(PaymentMethod.手形, customer.getInvoice().getClosingInvoice2().getPaymentMethod())
        );
    }

    @Test
    @DisplayName("顧客コードは以下の条件を満たす")
    void shouldCreateCustomerCode() {
        assertDoesNotThrow(() -> CustomerCode.of("001", 0));
        assertThrows(IllegalArgumentException.class, () -> CustomerCode.of(null, 1));
        assertThrows(IllegalArgumentException.class, () -> CustomerCode.of("001", -1));
        assertThrows(IllegalArgumentException.class, () -> CustomerCode.of("001", 1000));
    }

    @Test
    @DisplayName("請求先コードは以下の条件を満たす")
    void shouldCreateBillingCode() {
        assertDoesNotThrow(() -> BillingCode.of("001", 0));
        assertThrows(IllegalArgumentException.class, () -> BillingCode.of(null, 1));
        assertThrows(IllegalArgumentException.class, () -> BillingCode.of("001", -1));
        assertThrows(IllegalArgumentException.class, () -> BillingCode.of("001", 1000));
    }

    @Test
    @DisplayName("回収先コードは以下の条件を満たす")
    void shouldCreateCollectionCode() {
        assertDoesNotThrow(() -> CollectionCode.of("001", 0));
        assertThrows(IllegalArgumentException.class, () -> CollectionCode.of(null, 1));
        assertThrows(IllegalArgumentException.class, () -> CollectionCode.of("001", -1));
        assertThrows(IllegalArgumentException.class, () -> CollectionCode.of("001", 1000));
    }

    @Test
    @DisplayName("メールアドレスは以下の条件を満たす")
    void shouldCreateEmailAddress() {
        assertDoesNotThrow(() -> Email.of("test@example.com"));
        assertThrows(IllegalArgumentException.class, () -> Email.of(null));
        assertThrows(IllegalArgumentException.class, () -> Email.of("test"));
        assertThrows(IllegalArgumentException.class, () -> Email.of("test@"));
        assertThrows(IllegalArgumentException.class, () -> Email.of("test@example"));
        assertThrows(IllegalArgumentException.class, () -> Email.of("test@example."));
        assertThrows(IllegalArgumentException.class, () -> Email.of("test@example.c"));
        assertThrows(IllegalArgumentException.class, () -> Email.of("test@example.com."));
    }
}
