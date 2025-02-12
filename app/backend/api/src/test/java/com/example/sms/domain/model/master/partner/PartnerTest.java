package com.example.sms.domain.model.master.partner;

import com.example.sms.domain.BusinessException;
import com.example.sms.domain.model.master.partner.vendor.VendorType;
import com.example.sms.domain.type.address.Address;
import com.example.sms.domain.type.address.PostalCode;
import com.example.sms.domain.model.master.partner.customer.*;
import com.example.sms.domain.model.master.partner.invoice.*;
import com.example.sms.domain.model.master.partner.vendor.Vendor;
import com.example.sms.domain.model.master.partner.vendor.VendorCode;
import com.example.sms.domain.type.mail.EmailAddress;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("取引先")
class PartnerTest {
    Partner getPartner () {
        return Partner.of(
                "001",
                "取引先名",
                "取引先名カナ",
                1,
                "1234567",
                "東京都",
                "住所１",
                "住所２",
                0,
                0,
                "0001",
                100000,
                100000
        );
    }

    @Test
    @DisplayName("取引先を作成できる")
    void shouldCreatePartner() {
        Partner partner = getPartner();

        assertAll(
                () -> assertEquals("001", partner.getPartnerCode().getValue()),
                () -> assertEquals("取引先名", partner.getPartnerName().getName()),
                () -> assertEquals("取引先名カナ", partner.getPartnerName().getNameKana()),
                () -> assertEquals(VendorType.仕入先, partner.getVendorType()),
                () -> assertEquals("1234567", partner.getAddress().getPostalCode().getValue()),
                () -> assertEquals("東京都", partner.getAddress().getPrefecture().toString()),
                () -> assertEquals("住所１", partner.getAddress().getAddress1()),
                () -> assertEquals("住所２", partner.getAddress().getAddress2()),
                () -> assertEquals(TradeProhibitedFlag.OFF, partner.getTradeProhibitedFlag()),
                () -> assertEquals(MiscellaneousType.対象外, partner.getMiscellaneousType()),
                () -> assertEquals("0001", partner.getPartnerGroupCode().getValue()),
                () -> assertEquals(100000, partner.getCredit().getCreditLimit().getAmount()),
                () -> assertEquals(100000, partner.getCredit().getTemporaryCreditIncrease().getAmount()),
                () -> assertEquals(0, partner.getCustomers().size()),
                () -> assertEquals(0, partner.getVendors().size())
        );
    }

    @Test
    @DisplayName("取引先コードは以下の条件を満たす")
    void shouldCreatePartnerCode() {
        assertDoesNotThrow(() -> PartnerCode.of("001"));
        assertThrows(BusinessException.class, () -> PartnerCode.of(null));
        assertThrows(BusinessException.class, () -> PartnerCode.of("0011"));
        assertThrows(BusinessException.class, () -> PartnerCode.of("001a"));
    }

    @Test
    @DisplayName("取引先名は以下の条件を満たす")
    void shouldCreatePartnerName() {
        assertDoesNotThrow(() -> PartnerName.of("取引先名", "取引先名カナ"));
        assertThrows(BusinessException.class, () -> PartnerName.of(null, "取引先名カナ"));
        assertThrows(BusinessException.class, () -> PartnerName.of("取引先名", null));
        assertThrows(BusinessException.class, () -> PartnerName.of("取引先名", "取引先カナ名".repeat(50)));
        assertThrows(BusinessException.class, () -> PartnerName.of("取引先名".repeat(50), "取引先カナ名"));
        assertThrows(BusinessException.class, () -> PartnerName.of("取引先名".repeat(50), "取引先カナ名".repeat(50)));
    }

    @Test
    @DisplayName("郵便番号は以下の条件を満たす")
    void shouldCreatePostalCode() {
        assertDoesNotThrow(() -> PostalCode.of("1234567"));
        assertDoesNotThrow(() -> PostalCode.of("123-4567"));
        assertThrows(BusinessException.class, () -> PostalCode.of(null));
        assertThrows(BusinessException.class, () -> PostalCode.of("12345678"));
        assertThrows(BusinessException.class, () -> PostalCode.of("XX34567"));
        assertThrows(BusinessException.class, () -> PostalCode.of("123456-"));
        assertThrows(BusinessException.class, () -> PostalCode.of("-123456"));
        assertThrows(BusinessException.class, () -> PostalCode.of("1-23456"));
    }

    @Test
    @DisplayName("住所は以下の条件を満たす")
    void shouldCreateAddress() {
        assertDoesNotThrow(() -> Address.of("1234567", "東京都", "住所１", "住所２"));
        assertDoesNotThrow(() -> Address.of("1234567", "東京都", "住".repeat(40), "住".repeat(40)));
        assertThrows(BusinessException.class, () -> Address.of(null, "東京都", "住所１", "住所２"));
        assertThrows(BusinessException.class, () -> Address.of("1234567", "東京都", "住".repeat(41), "住所２"));
        assertThrows(BusinessException.class, () -> Address.of("1234567", "東京都", "住所１", "住".repeat(41)));
        assertThrows(BusinessException.class, () -> Address.of("1234567", "東京都", "住".repeat(100), "住".repeat(100)));
        assertThrows(BusinessException.class, () -> Address.of("1234567", null, "住所１", "住所２"));
        assertThrows(BusinessException.class, () -> Address.of("1234567", "東京都", null, "住所２"));
        assertThrows(BusinessException.class, () -> Address.of("1234567", "東京都", "住所１", null));
        assertThrows(BusinessException.class, () -> Address.of("1234567", "異世界", "住所１", "住所２"));
        }

    @Test
    @DisplayName("与信は以下の条件を満たす")
    void shouldCreateCredit() {
        assertDoesNotThrow(() -> Credit.of(100000, 100000));
        assertThrows(BusinessException.class, () -> Credit.of(100000001, 100000));
        assertThrows(BusinessException.class, () -> Credit.of(100000, 10000001));
        assertThrows(BusinessException.class, () -> Credit.of(100000001, 10000001));
    }

    @Nested
    @DisplayName("顧客")
    class CustomerTest {
        Customer getCustomer() {
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
            assertThrows(BusinessException.class, () -> CustomerCode.of(null, 1));
            assertThrows(BusinessException.class, () -> CustomerCode.of("001", -1));
            assertThrows(BusinessException.class, () -> CustomerCode.of("001", 1000));
        }

        @Test
        @DisplayName("請求先コードは以下の条件を満たす")
        void shouldCreateBillingCode() {
            assertDoesNotThrow(() -> BillingCode.of("001", 0));
            assertThrows(BusinessException.class, () -> BillingCode.of(null, 1));
            assertThrows(BusinessException.class, () -> BillingCode.of("001", -1));
            assertThrows(BusinessException.class, () -> BillingCode.of("001", 1000));
        }

        @Test
        @DisplayName("回収先コードは以下の条件を満たす")
        void shouldCreateCollectionCode() {
            assertDoesNotThrow(() -> CollectionCode.of("001", 0));
            assertThrows(BusinessException.class, () -> CollectionCode.of(null, 1));
            assertThrows(BusinessException.class, () -> CollectionCode.of("001", -1));
            assertThrows(BusinessException.class, () -> CollectionCode.of("001", 1000));
        }

        @Test
        @DisplayName("メールアドレスは以下の条件を満たす")
        void shouldCreateEmailAddress() {
            assertDoesNotThrow(() -> EmailAddress.of("test@example.com"));
            assertThrows(NullPointerException.class, () -> EmailAddress.of(null));
            assertThrows(IllegalArgumentException.class, () -> EmailAddress.of("test"));
            assertThrows(IllegalArgumentException.class, () -> EmailAddress.of("test@"));
            assertThrows(IllegalArgumentException.class, () -> EmailAddress.of("test@example"));
            assertThrows(IllegalArgumentException.class, () -> EmailAddress.of("test@example."));
            assertThrows(IllegalArgumentException.class, () -> EmailAddress.of("test@example.c"));
            assertThrows(IllegalArgumentException.class, () -> EmailAddress.of("test@example.com."));
        }

        @Nested
        @DisplayName("出荷先")
        class ShippingTest {
            Shipping getShipping() {
                return Shipping.of(
                        "001",
                        1,
                        1,
                        "出荷先",
                        "R001",
                        "123-4567",
                        "新宿区1-1-1",
                        "マンション101号室"
                );
            }

            @Test
            @DisplayName("出荷先を作成できる")
            void shouldCreateShipping() {
                Shipping shipping = getShipping();

                assertAll(
                        () -> assertEquals("001", shipping.getShippingCode().getCustomerCode().getCode().getValue()),
                        () -> assertEquals(1, shipping.getShippingCode().getCustomerCode().getBranchNumber()),
                        () -> assertEquals(1, shipping.getShippingCode().getDestinationNumber()),
                        () -> assertEquals("出荷先", shipping.getDestinationName()),
                        () -> assertEquals("R001", shipping.getRegionCode().getValue()),
                        () -> assertEquals("1234567", shipping.getShippingAddress().getPostalCode().getValue()),
                        () -> assertEquals("新宿区1-1-1", shipping.getShippingAddress().getAddress1()),
                        () -> assertEquals("マンション101号室", shipping.getShippingAddress().getAddress2())
                );
            }
        }

        @Nested
        @DisplayName("請求")
        class InvoiceTest {
            @Test
            @DisplayName("20日締翌月末現金払い条件を設定できる")
            void shouldCreateClosingInvoice() {
                Customer customer = getCustomer();
                ClosingInvoice closingInvoice = ClosingInvoice.of(20, 1, 99, 1);
                Customer invoiceCustomer = customer.of(
                        getCustomer(),
                        closingInvoice,
                        null
                );

                assertAll(
                        () -> assertEquals(ClosingDate.二十日, closingInvoice.getClosingDay()),
                        () -> assertEquals(PaymentMonth.翌月, closingInvoice.getPaymentMonth()),
                        () -> assertEquals(PaymentDay.末日, closingInvoice.getPaymentDay()),
                        () -> assertEquals(PaymentMethod.振込, closingInvoice.getPaymentMethod()),
                        () -> assertEquals(CustomerBillingCategory.締請求, invoiceCustomer.getInvoice().getCustomerBillingCategory()),
                        () -> assertEquals(invoiceCustomer.getInvoice().getClosingInvoice1(), closingInvoice)
                );
            }

            @Test
            @DisplayName("20日締翌月末現金払い条件と10日締翌月手形払い条件を設定できる")
            void shouldCreateClosingInvoice2() {
                Customer customer = getCustomer();
                ClosingInvoice closingInvoice1 = ClosingInvoice.of(20, 1, 99, 1);
                ClosingInvoice closingInvoice2 = ClosingInvoice.of(10, 1, 10, 2);
                Customer invoiceCustomer = customer.of(
                        getCustomer(),
                        closingInvoice1,
                        closingInvoice2
                );

                assertAll(
                        () -> assertEquals(ClosingDate.二十日, closingInvoice1.getClosingDay()),
                        () -> assertEquals(PaymentMonth.翌月, closingInvoice1.getPaymentMonth()),
                        () -> assertEquals(PaymentDay.末日, closingInvoice1.getPaymentDay()),
                        () -> assertEquals(PaymentMethod.振込, closingInvoice1.getPaymentMethod()),
                        () -> assertEquals(ClosingDate.十日, closingInvoice2.getClosingDay()),
                        () -> assertEquals(PaymentMonth.翌月, closingInvoice2.getPaymentMonth()),
                        () -> assertEquals(PaymentDay.十日, closingInvoice2.getPaymentDay()),
                        () -> assertEquals(PaymentMethod.手形, closingInvoice2.getPaymentMethod()),
                        () -> assertEquals(CustomerBillingCategory.締請求, invoiceCustomer.getInvoice().getCustomerBillingCategory()),
                        () -> assertEquals(invoiceCustomer.getInvoice().getClosingInvoice1(), closingInvoice1),
                        () -> assertEquals(invoiceCustomer.getInvoice().getClosingInvoice2(), closingInvoice2)
                );
            }


            @Test
            @DisplayName("１つめがnullの場合は２つめを設定できない")
            void shouldNotCreateInvoice() {
                Customer customer = getCustomer();
                ClosingInvoice closingInvoice2 = ClosingInvoice.of(10, 0, 10, 2);

                assertThrows(BusinessException.class, () -> customer.of(
                        getCustomer(),
                        null,
                        closingInvoice2
                ));
            }
        }
    }

    @Nested
    @DisplayName("仕入先")
    class VendorTest {
        Vendor getVendor() {
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
                    "test@example.com",
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
                    () -> assertEquals("test@example.com", vendor.getVendorEmailAddress().getValue()),
                    () -> assertEquals(ClosingDate.十日, vendor.getVendorClosingInvoice().getClosingDay()),
                    () -> assertEquals(PaymentMonth.翌月, vendor.getVendorClosingInvoice().getPaymentMonth()),
                    () -> assertEquals(PaymentDay.二十日, vendor.getVendorClosingInvoice().getPaymentDay()),
                    () -> assertEquals(PaymentMethod.手形, vendor.getVendorClosingInvoice().getPaymentMethod())
            );
        }

        @Test
        @DisplayName("仕入先コードは以下の条件を満たす")
        void shouldCreateVendorCode() {
            assertDoesNotThrow(() -> VendorCode.of("001", 0));
            assertThrows(BusinessException.class, () -> VendorCode.of(null, 1));
            assertThrows(BusinessException.class, () -> VendorCode.of("001", -1));
            assertThrows(BusinessException.class, () -> VendorCode.of("001", 1000));
        }
    }
}
