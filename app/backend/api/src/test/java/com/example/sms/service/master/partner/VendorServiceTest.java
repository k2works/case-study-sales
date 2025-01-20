package com.example.sms.service.master.partner;

import com.example.sms.IntegrationTest;
import com.example.sms.TestDataFactory;
import com.example.sms.TestDataFactoryImpl;
import com.example.sms.domain.model.common.Email;
import com.example.sms.domain.model.common.FaxNumber;
import com.example.sms.domain.model.common.PhoneNumber;
import com.example.sms.domain.model.common.address.Address;
import com.example.sms.domain.model.master.partner.Partner;
import com.example.sms.domain.model.master.partner.vendor.Vendor;
import com.example.sms.domain.model.master.partner.vendor.VendorCode;
import com.example.sms.domain.model.master.partner.vendor.VendorList;
import com.example.sms.domain.model.master.partner.vendor.VendorName;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;
@IntegrationTest
@DisplayName("仕入先サービス")
class VendorServiceTest {
    @Autowired
    private PartnerService partnerService;
    @Autowired
    private VendorService vendorService;
    @Autowired
    private TestDataFactory testDataFactory;

    @BeforeEach
    void setUp() {
        testDataFactory.setUpForVendorService();
    }

    @Nested
    @DisplayName("仕入先")
    class VendorTest {
        @Test
        @DisplayName("仕入先一覧を取得できる")
        void shouldGetVendorList() {
            VendorList result = vendorService.selectAll();
            assertEquals(3, result.size());
            assertEquals("001", result.asList().getFirst().getVendorCode().getCode().getValue());
        }

        @Nested
        @DisplayName("仕入先を新規登録できる")
        class RegisterVendorTest {
            @Test
            void case_1() {
                Partner partner = TestDataFactoryImpl.getPartner("010");
                partnerService.register(partner);
                Vendor newVendor = TestDataFactoryImpl.getVendor("010", 1);

                vendorService.register(newVendor);

                Vendor result = vendorService.find(VendorCode.of("010", 1));
                assertEquals(newVendor, result);
            }

            @Test
            void case_2() {
                Vendor newVendor = TestDataFactoryImpl.getVendor("010", 1);

                vendorService.register(newVendor);

                Vendor result = vendorService.find(VendorCode.of("010", 1));
                assertNull(result);
            }
        }

        @Nested
        @DisplayName("仕入先を編集できる")
        class SaveVendorTest {
            @Test
            void case_1() {
                Partner partner = TestDataFactoryImpl.getPartner("010");
                partnerService.register(partner);
                Vendor originalVendor = TestDataFactoryImpl.getVendor("010", 1);
                vendorService.register(originalVendor);

                Vendor updatedVendor = Vendor.of(
                        originalVendor.getVendorCode(),
                        VendorName.of("仕入先A", "シイレサキエー"),
                        originalVendor.getVendorContactName(),
                        originalVendor.getVendorDepartmentName(),
                        originalVendor.getVendorAddress(),
                        originalVendor.getVendorPhoneNumber(),
                        originalVendor.getVendorFaxNumber(),
                        originalVendor.getVendorEmailAddress(),
                        originalVendor.getVendorClosingInvoice()
                );
                vendorService.save(updatedVendor);

                Vendor result = vendorService.find(VendorCode.of("010", 1));
                assertNotEquals(originalVendor, result);
                assertEquals(updatedVendor, result);
            }
        }

        @Nested
        @DisplayName("仕入先を削除できる")
        class DeleteVendorTest {
            @Test
            void case_1() {
                Partner partner = TestDataFactoryImpl.getPartner("010");
                partnerService.register(partner);
                Vendor vendor = TestDataFactoryImpl.getVendor("010", 1);
                vendorService.register(vendor);

                vendorService.delete(vendor);

                Vendor result = vendorService.find(VendorCode.of("010", 1));
                assertNull(result);
            }
        }

        @Nested
        @DisplayName("仕入先検索")
        class VendorSearchTest {
            @Test
            @DisplayName("仕入先コードで検索できる")
            void shouldSearchVendorByCode() {
                Partner partner = TestDataFactoryImpl.getPartner("010");
                partnerService.register(partner);
                Vendor originalVendor = TestDataFactoryImpl.getVendor("010", 1);
                vendorService.register(originalVendor);

                VendorCriteria criteria = VendorCriteria.builder()
                        .vendorCode("010")
                        .build();
                PageInfo<Vendor> result = vendorService.searchWithPageInfo(criteria);
                assertEquals(1, result.getList().size());
                assertEquals(1, result.getTotal());
            }

            @Test
            @DisplayName("仕入先名で検索できる")
            void shouldSearchVendorByName() {
                Partner partner = TestDataFactoryImpl.getPartner("010");
                partnerService.register(partner);
                Vendor originalVendor = TestDataFactoryImpl.getVendor("010", 1);
                Vendor updatedVendor = Vendor.of(
                        originalVendor.getVendorCode(),
                        VendorName.of("仕入先A", "シイレサキエー"),
                        originalVendor.getVendorContactName(),
                        originalVendor.getVendorDepartmentName(),
                        originalVendor.getVendorAddress(),
                        originalVendor.getVendorPhoneNumber(),
                        originalVendor.getVendorFaxNumber(),
                        originalVendor.getVendorEmailAddress(),
                        originalVendor.getVendorClosingInvoice()
                );
                vendorService.register(updatedVendor);

                VendorCriteria criteria = VendorCriteria.builder()
                        .vendorName("仕入先A")
                        .build();
                PageInfo<Vendor> result = vendorService.searchWithPageInfo(criteria);
                assertEquals(1, result.getList().size());
                assertEquals(1, result.getTotal());
            }
            @Test
            @DisplayName("仕入先担当者名で検索できる")
            void shouldSearchVendorByContactName() {
                Partner partner = TestDataFactoryImpl.getPartner("010");
                partnerService.register(partner);
                Vendor originalVendor = TestDataFactoryImpl.getVendor("010", 1);
                Vendor updatedVendor = Vendor.of(
                        originalVendor.getVendorCode(),
                        originalVendor.getVendorName(),
                        "担当者A",
                        originalVendor.getVendorDepartmentName(),
                        originalVendor.getVendorAddress(),
                        originalVendor.getVendorPhoneNumber(),
                        originalVendor.getVendorFaxNumber(),
                        originalVendor.getVendorEmailAddress(),
                        originalVendor.getVendorClosingInvoice()
                );
                vendorService.register(updatedVendor);

                VendorCriteria criteria = VendorCriteria.builder()
                        .vendorContactName("担当者A")
                        .build();
                PageInfo<Vendor> result = vendorService.searchWithPageInfo(criteria);
                assertEquals(1, result.getList().size());
                assertEquals(1, result.getTotal());
            }
            @Test
            @DisplayName("仕入先部署名で検索できる")
            void shouldSearchVendorByDepartmentName() {
                Partner partner = TestDataFactoryImpl.getPartner("010");
                partnerService.register(partner);
                Vendor originalVendor = TestDataFactoryImpl.getVendor("010", 1);
                Vendor updatedVendor = Vendor.of(
                        originalVendor.getVendorCode(),
                        originalVendor.getVendorName(),
                        originalVendor.getVendorContactName(),
                        "部署A",
                        originalVendor.getVendorAddress(),
                        originalVendor.getVendorPhoneNumber(),
                        originalVendor.getVendorFaxNumber(),
                        originalVendor.getVendorEmailAddress(),
                        originalVendor.getVendorClosingInvoice()
                );
                vendorService.register(updatedVendor);

                VendorCriteria criteria = VendorCriteria.builder()
                        .vendorDepartmentName("部署A")
                        .build();
                PageInfo<Vendor> result = vendorService.searchWithPageInfo(criteria);
                assertEquals(1, result.getList().size());
                assertEquals(1, result.getTotal());
            }
            @Test
            @DisplayName("仕入先住所で検索できる")
            void shouldSearchVendorByAddress() {
                Partner partner = TestDataFactoryImpl.getPartner("010");
                partnerService.register(partner);
                Vendor originalVendor = TestDataFactoryImpl.getVendor("010", 1);
                Vendor updatedVendor = Vendor.of(
                        originalVendor.getVendorCode(),
                        originalVendor.getVendorName(),
                        originalVendor.getVendorContactName(),
                        originalVendor.getVendorDepartmentName(),
                        Address.of("1234567", "東京都", "新宿区西新宿", "1-1-1"),
                        originalVendor.getVendorPhoneNumber(),
                        originalVendor.getVendorFaxNumber(),
                        originalVendor.getVendorEmailAddress(),
                        originalVendor.getVendorClosingInvoice()
                );
                vendorService.register(updatedVendor);

                VendorCriteria criteria = VendorCriteria.builder()
                        .postalCode("1234567")
                        .prefecture("東京都")
                        .address1("新宿区西新宿")
                        .address2("1-1-1")
                        .build();
                PageInfo<Vendor> result = vendorService.searchWithPageInfo(criteria);
                assertEquals(1, result.getList().size());
                assertEquals(1, result.getTotal());
            }
            @Test
            @DisplayName("仕入先電話番号で検索できる")
            void shouldSearchVendorByPhoneNumber() {
                Partner partner = TestDataFactoryImpl.getPartner("010");
                partnerService.register(partner);
                Vendor originalVendor = TestDataFactoryImpl.getVendor("010", 1);
                Vendor updatedVendor = Vendor.of(
                        originalVendor.getVendorCode(),
                        originalVendor.getVendorName(),
                        originalVendor.getVendorContactName(),
                        originalVendor.getVendorDepartmentName(),
                        originalVendor.getVendorAddress(),
                        PhoneNumber.of("0123456789"),
                        originalVendor.getVendorFaxNumber(),
                        originalVendor.getVendorEmailAddress(),
                        originalVendor.getVendorClosingInvoice()
                );
                vendorService.register(updatedVendor);

                VendorCriteria criteria = VendorCriteria.builder()
                        .vendorPhoneNumber("01-2345-6789")
                        .build();
                PageInfo<Vendor> result = vendorService.searchWithPageInfo(criteria);
                assertEquals(1, result.getList().size());
                assertEquals(1, result.getTotal());
            }
            @Test
            @DisplayName("仕入先FAX番号で検索できる")
            void shouldSearchVendorByFaxNumber() {
                Partner partner = TestDataFactoryImpl.getPartner("010");
                partnerService.register(partner);
                Vendor originalVendor = TestDataFactoryImpl.getVendor("010", 1);
                Vendor updatedVendor = Vendor.of(
                        originalVendor.getVendorCode(),
                        originalVendor.getVendorName(),
                        originalVendor.getVendorContactName(),
                        originalVendor.getVendorDepartmentName(),
                        originalVendor.getVendorAddress(),
                        originalVendor.getVendorPhoneNumber(),
                        FaxNumber.of("0123456789"),
                        originalVendor.getVendorEmailAddress(),
                        originalVendor.getVendorClosingInvoice()
                );
                vendorService.register(updatedVendor);

                VendorCriteria criteria = VendorCriteria.builder()
                        .vendorFaxNumber("01-2345-6789")
                        .build();
                PageInfo<Vendor> result = vendorService.searchWithPageInfo(criteria);
                assertEquals(1, result.getList().size());
                assertEquals(1, result.getTotal());
            }
            @Test
            @DisplayName("仕入先メールアドレスで検索できる")
            void shouldSearchVendorByEmailAddress() {
                Partner partner = TestDataFactoryImpl.getPartner("010");
                partnerService.register(partner);
                Vendor originalVendor = TestDataFactoryImpl.getVendor("010", 1);
                Vendor updatedVendor = Vendor.of(
                        originalVendor.getVendorCode(),
                        originalVendor.getVendorName(),
                        originalVendor.getVendorContactName(),
                        originalVendor.getVendorDepartmentName(),
                        originalVendor.getVendorAddress(),
                        originalVendor.getVendorPhoneNumber(),
                        originalVendor.getVendorFaxNumber(),
                        Email.of("test@example.com"),
                        originalVendor.getVendorClosingInvoice()
                );
                vendorService.register(updatedVendor);

                VendorCriteria criteria = VendorCriteria.builder()
                        .vendorEmailAddress("test@example.com")
                        .build();
                PageInfo<Vendor> result = vendorService.searchWithPageInfo(criteria);
                assertEquals(1, result.getList().size());
                assertEquals(1, result.getTotal());
            }
        }
    }
}