package com.example.sms.service.master.partner;

import com.example.sms.IntegrationTest;
import com.example.sms.TestDataFactory;
import com.example.sms.TestDataFactoryImpl;
import com.example.sms.domain.model.master.partner.customer.Customer;
import com.example.sms.domain.model.master.partner.vendor.Vendor;
import com.example.sms.domain.type.address.Address;
import com.example.sms.domain.type.address.PostalCode;
import com.example.sms.domain.type.address.Prefecture;
import com.example.sms.domain.model.master.partner.*;
import com.example.sms.domain.model.master.partner.vendor.VendorType;
import com.example.sms.service.BusinessException;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@IntegrationTest
@DisplayName("取引先サービス")
class PartnerServiceTest {

    @Autowired
    private PartnerService partnerService;

    @Autowired
    private PartnerCategoryService partnerCategoryService;

    @Autowired
    private TestDataFactory testDataFactory;

    @BeforeEach
    void setUp() {
        testDataFactory.setUpForPartnerService();
    }

    @Nested
    @DisplayName("取引先")
    class PartnerTest {

        @Test
        @DisplayName("取引先一覧を取得できる")
        void shouldGetPartnerList() {
            PartnerList result = partnerService.selectAll();
            assertEquals(4, result.size());
            assertEquals("001", result.asList().getFirst().getPartnerCode().getValue());
        }

        @Test
        @DisplayName("取引先を新規登録できる")
        void shouldRegisterPartner() {
            Partner newPartner = TestDataFactoryImpl.getPartner("009");
            partnerService.register(newPartner);
            Partner result = partnerService.find("009");
            assertEquals(newPartner, result);
        }

        @Nested
        @DisplayName("取引先を編集できる")
        class shouldSavePartner {
            @Test
            @DisplayName("取引先のみ存在する場合")
            void case_1() {
                Partner originalPartner = TestDataFactoryImpl.getPartner("009");
                Partner updatedPartner = Partner.of(
                        originalPartner.getPartnerCode(),
                        PartnerName.of("取引先B", "取引先B"),
                        originalPartner.getVendorType(),
                        originalPartner.getAddress(),
                        originalPartner.getTradeProhibitedFlag(),
                        originalPartner.getMiscellaneousType(),
                        originalPartner.getPartnerGroupCode(),
                        originalPartner.getCredit(),
                        List.of(),
                        List.of()
                );

                partnerService.save(originalPartner);
                partnerService.save(updatedPartner);

                Partner result = partnerService.find("009");
                assertNotEquals(originalPartner, result);
                assertEquals(updatedPartner, result);
            }

            @Test
            @DisplayName("取引先と顧客が存在する場合")
            void case_2() {
                Partner originalPartner = TestDataFactoryImpl.getPartner("001");
                Customer customer = TestDataFactoryImpl.getCustomer("001", 1);
                Partner updatedPartner = Partner.of(
                        originalPartner.getPartnerCode(),
                        originalPartner.getPartnerName(),
                        originalPartner.getVendorType(),
                        originalPartner.getAddress(),
                        originalPartner.getTradeProhibitedFlag(),
                        originalPartner.getMiscellaneousType(),
                        originalPartner.getPartnerGroupCode(),
                        originalPartner.getCredit(),
                        List.of(customer),
                        List.of()
                );

                partnerService.save(updatedPartner);

                Partner result = partnerService.find("001");
                assertNotEquals(originalPartner, result);
                assertEquals(updatedPartner, result);
                assertEquals(customer, result.getCustomers().getFirst());
            }

            @Test
            @DisplayName("取引先と仕入先が存在する場合")
            void case_3() {
                Partner originalPartner = TestDataFactoryImpl.getPartner("001");
                Vendor vendor = TestDataFactoryImpl.getVendor("001", 1);
                Partner updatedPartner = Partner.of(
                        originalPartner.getPartnerCode(),
                        originalPartner.getPartnerName(),
                        VendorType.仕入先でない,
                        originalPartner.getAddress(),
                        originalPartner.getTradeProhibitedFlag(),
                        originalPartner.getMiscellaneousType(),
                        originalPartner.getPartnerGroupCode(),
                        originalPartner.getCredit(),
                        List.of(),
                        List.of(vendor)
                );

                partnerService.save(updatedPartner);

                Partner result = partnerService.find("001");
                assertNotEquals(originalPartner, result);
                assertEquals(updatedPartner, result);
                assertEquals(vendor, result.getVendors().getFirst());
            }

        }

        @Nested
        @DisplayName("取引先を削除できる")
        class shouldDeletePartner {
            @Test
            @DisplayName("取引先のみ存在する場合")
            void case_1() throws BusinessException {
                Partner originalPartner = TestDataFactoryImpl.getPartner("009");
                partnerService.save(originalPartner);
                partnerService.delete(originalPartner);

                Partner result = partnerService.find("009");
                assertNull(result);
            }

            @Test
            @DisplayName("取引先と顧客が存在する場合")
            void case_2() throws BusinessException {
                Partner originalPartner = TestDataFactoryImpl.getPartner("001");
                Customer customer = TestDataFactoryImpl.getCustomer("001", 1);
                Partner updatedPartner = Partner.of(
                        originalPartner.getPartnerCode(),
                        originalPartner.getPartnerName(),
                        originalPartner.getVendorType(),
                        originalPartner.getAddress(),
                        originalPartner.getTradeProhibitedFlag(),
                        originalPartner.getMiscellaneousType(),
                        originalPartner.getPartnerGroupCode(),
                        originalPartner.getCredit(),
                        List.of(customer),
                        List.of()
                );

                partnerService.save(updatedPartner);
                partnerService.delete(updatedPartner);

                Partner result = partnerService.find("001");
                assertNull(result);
            }

            @Test
            @DisplayName("取引先と仕入先が存在する場合")
            void case_3() throws BusinessException {
                Partner originalPartner = TestDataFactoryImpl.getPartner("001");
                Vendor vendor = TestDataFactoryImpl.getVendor("001", 1);
                Partner updatedPartner = Partner.of(
                        originalPartner.getPartnerCode(),
                        originalPartner.getPartnerName(),
                        VendorType.仕入先でない,
                        originalPartner.getAddress(),
                        originalPartner.getTradeProhibitedFlag(),
                        originalPartner.getMiscellaneousType(),
                        originalPartner.getPartnerGroupCode(),
                        originalPartner.getCredit(),
                        List.of(),
                        List.of(vendor)
                );

                partnerService.save(updatedPartner);
                partnerService.delete(updatedPartner);

                Partner result = partnerService.find("001");
                assertNull(result);
            }

            @Test
            @DisplayName("分類に紐づく取引先は削除できない")
            void case_4() {
                Partner originalPartner = TestDataFactoryImpl.getPartner("001");
                Partner updatedPartner = Partner.of(
                        originalPartner.getPartnerCode(),
                        originalPartner.getPartnerName(),
                        originalPartner.getVendorType(),
                        originalPartner.getAddress(),
                        originalPartner.getTradeProhibitedFlag(),
                        originalPartner.getMiscellaneousType(),
                        PartnerGroupCode.of("9999"),
                        originalPartner.getCredit(),
                        List.of(),
                        List.of()
                );
                PartnerCategoryAffiliation partnerCategoryAffiliation = TestDataFactoryImpl.getPartnerCategoryAffiliation("1", "001", "01");
                List<PartnerCategoryAffiliation> partnerCategoryAffiliations = List.of(partnerCategoryAffiliation);
                PartnerCategoryItem partnerCategoryItem = PartnerCategoryItem.of(TestDataFactoryImpl.getPartnerCategoryItem("1", "01"), partnerCategoryAffiliations);
                List<PartnerCategoryItem> partnerCategoryItems = List.of(partnerCategoryItem);
                PartnerCategoryType partnerCategory = PartnerCategoryType.of(TestDataFactoryImpl.getPartnerCategoryType("1"), partnerCategoryItems);
                partnerCategoryService.register(partnerCategory);

                partnerService.save(updatedPartner);

                assertThrows(BusinessException.class, () -> partnerService.delete(updatedPartner));
            }

        }

        @Nested
        @DisplayName("取引先検索")
        class PartnerSearchTest {

            @Test
            @DisplayName("取引先コードで検索できる")
            void shouldSearchPartnerByCode() {
                Partner originalPartner = TestDataFactoryImpl.getPartner("009");
                Partner updatedPartner = Partner.of(
                        originalPartner.getPartnerCode(),
                        originalPartner.getPartnerName(),
                        originalPartner.getVendorType(),
                        originalPartner.getAddress(),
                        originalPartner.getTradeProhibitedFlag(),
                        originalPartner.getMiscellaneousType(),
                        originalPartner.getPartnerGroupCode(),
                        originalPartner.getCredit(),
                        List.of(),
                        List.of()
                );
                partnerService.save(updatedPartner);

                PartnerCriteria criteria = PartnerCriteria.builder()
                        .partnerCode("009")
                        .build();
                PageInfo<Partner> result = partnerService.searchWithPageInfo(criteria);

                assertEquals(1, result.getList().size());
                assertEquals(1, result.getTotal());
            }

            @Test
            @DisplayName("取引先名で検索できる")
            void shouldSearchPartnerByName() {
                Partner originalPartner = TestDataFactoryImpl.getPartner("009");
                Partner updatedPartner = Partner.of(
                        originalPartner.getPartnerCode(),
                        PartnerName.of("取引先A", "トリヒキサキエー"),
                        originalPartner.getVendorType(),
                        originalPartner.getAddress(),
                        originalPartner.getTradeProhibitedFlag(),
                        originalPartner.getMiscellaneousType(),
                        originalPartner.getPartnerGroupCode(),
                        originalPartner.getCredit(),
                        List.of(),
                        List.of()
                );

                partnerService.save(updatedPartner);
                PartnerCriteria criteria = PartnerCriteria.builder()
                        .partnerName("取引先A")
                        .build();
                PageInfo<Partner> result = partnerService.searchWithPageInfo(criteria);

                assertEquals(1, result.getList().size());
                assertEquals(1, result.getTotal());
            }

            @Test
            @DisplayName("取引先名カナで検索できる")
            void shouldSearchPartnerByNameKana() {
                Partner originalPartner = TestDataFactoryImpl.getPartner("009");
                Partner updatedPartner = Partner.of(
                        originalPartner.getPartnerCode(),
                        PartnerName.of("取引先A", "トリヒキサキエー"),
                        originalPartner.getVendorType(),
                        originalPartner.getAddress(),
                        originalPartner.getTradeProhibitedFlag(),
                        originalPartner.getMiscellaneousType(),
                        originalPartner.getPartnerGroupCode(),
                        originalPartner.getCredit(),
                        List.of(),
                        List.of()
                );

                partnerService.save(updatedPartner);
                PartnerCriteria criteria = PartnerCriteria.builder()
                        .partnerNameKana("トリヒキサキエー")
                        .build();
                PageInfo<Partner> result = partnerService.searchWithPageInfo(criteria);

                assertEquals(1, result.getList().size());
                assertEquals(1, result.getTotal());
            }

            @Test
            @DisplayName("仕入先区分で検索できる")
            void shouldSearchPartnerByVendorType() {
                Partner originalPartner = TestDataFactoryImpl.getPartner("009");
                Partner updatedPartner = Partner.of(
                        originalPartner.getPartnerCode(),
                        originalPartner.getPartnerName(),
                        VendorType.仕入先でない,
                        originalPartner.getAddress(),
                        originalPartner.getTradeProhibitedFlag(),
                        originalPartner.getMiscellaneousType(),
                        originalPartner.getPartnerGroupCode(),
                        originalPartner.getCredit(),
                        List.of(),
                        List.of()
                );

                partnerService.save(updatedPartner);
                PartnerCriteria criteria = PartnerCriteria.builder()
                        .vendorType(VendorType.仕入先でない.getValue())
                        .build();
                PageInfo<Partner> result = partnerService.searchWithPageInfo(criteria);

                assertEquals(1, result.getList().size());
                assertEquals(1, result.getTotal());
            }

            @Test
            @DisplayName("郵便番号で検索できる")
            void shouldSearchPartnerByPostalCode() {
                Partner originalPartner = TestDataFactoryImpl.getPartner("009");
                Partner updatedPartner = Partner.of(
                        originalPartner.getPartnerCode(),
                        originalPartner.getPartnerName(),
                        originalPartner.getVendorType(),
                        Address.of("731-0102", Prefecture.広島県.toString(), "広島市安佐南区", "川内６丁目"),
                        originalPartner.getTradeProhibitedFlag(),
                        originalPartner.getMiscellaneousType(),
                        originalPartner.getPartnerGroupCode(),
                        originalPartner.getCredit(),
                        List.of(),
                        List.of()
                );

                partnerService.save(updatedPartner);
                PartnerCriteria criteria = PartnerCriteria.builder()
                        .postalCode(PostalCode.of("731-0102").getValue())
                        .build();
                PageInfo<Partner> result = partnerService.searchWithPageInfo(criteria);

                assertEquals(1, result.getList().size());
                assertEquals(1, result.getTotal());
            }

            @Test
            @DisplayName("都道府県で検索できる")
            void shouldSearchPartnerByPrefecture() {
                Partner originalPartner = TestDataFactoryImpl.getPartner("009");
                Partner updatedPartner = Partner.of(
                        originalPartner.getPartnerCode(),
                        originalPartner.getPartnerName(),
                        originalPartner.getVendorType(),
                        Address.of("731-0102", Prefecture.広島県.toString(), "広島市安佐南区", "川内６丁目"),
                        originalPartner.getTradeProhibitedFlag(),
                        originalPartner.getMiscellaneousType(),
                        originalPartner.getPartnerGroupCode(),
                        originalPartner.getCredit(),
                        List.of(),
                        List.of()
                );

                partnerService.save(updatedPartner);
                PartnerCriteria criteria = PartnerCriteria.builder()
                        .prefecture(Prefecture.広島県.toString())
                        .build();
                PageInfo<Partner> result = partnerService.searchWithPageInfo(criteria);

                assertEquals(1, result.getList().size());
                assertEquals(1, result.getTotal());
            }

            @Test
            @DisplayName("住所1で検索できる")
            void shouldSearchPartnerByAddress1() {
                Partner originalPartner = TestDataFactoryImpl.getPartner("009");
                Partner updatedPartner = Partner.of(
                        originalPartner.getPartnerCode(),
                        originalPartner.getPartnerName(),
                        originalPartner.getVendorType(),
                        Address.of("731-0102", Prefecture.広島県.toString(), "広島市安佐南区", "川内６丁目"),
                        originalPartner.getTradeProhibitedFlag(),
                        originalPartner.getMiscellaneousType(),
                        originalPartner.getPartnerGroupCode(),
                        originalPartner.getCredit(),
                        List.of(),
                        List.of()
                );

                partnerService.save(updatedPartner);
                PartnerCriteria criteria = PartnerCriteria.builder()
                        .address1("広島市")
                        .build();
                PageInfo<Partner> result = partnerService.searchWithPageInfo(criteria);

                assertEquals(1, result.getList().size());
                assertEquals(1, result.getTotal());
            }

            @Test
            @DisplayName("住所2で検索できる")
            void shouldSearchPartnerByAddress2() {
                Partner originalPartner = TestDataFactoryImpl.getPartner("009");
                Partner updatedPartner = Partner.of(
                        originalPartner.getPartnerCode(),
                        originalPartner.getPartnerName(),
                        originalPartner.getVendorType(),
                        Address.of("731-0102", Prefecture.広島県.toString(), "広島市安佐南区", "川内６丁目"),
                        originalPartner.getTradeProhibitedFlag(),
                        originalPartner.getMiscellaneousType(),
                        originalPartner.getPartnerGroupCode(),
                        originalPartner.getCredit(),
                        List.of(),
                        List.of()
                );

                partnerService.save(updatedPartner);
                PartnerCriteria criteria = PartnerCriteria.builder()
                        .address2("川内")
                        .build();
                PageInfo<Partner> result = partnerService.searchWithPageInfo(criteria);

                assertEquals(1, result.getList().size());
                assertEquals(1, result.getTotal());
            }

            @Test
            @DisplayName("取引禁止フラグで検索できる")
            void shouldSearchPartnerByTradeProhibitedFlag() {
                Partner originalPartner = TestDataFactoryImpl.getPartner("009");
                Partner updatedPartner = Partner.of(
                        originalPartner.getPartnerCode(),
                        originalPartner.getPartnerName(),
                        originalPartner.getVendorType(),
                        originalPartner.getAddress(),
                        TradeProhibitedFlag.ON,
                        originalPartner.getMiscellaneousType(),
                        originalPartner.getPartnerGroupCode(),
                        originalPartner.getCredit(),
                        List.of(),
                        List.of()
                );

                partnerService.save(updatedPartner);
                PartnerCriteria criteria = PartnerCriteria.builder()
                        .tradeProhibitedFlag(TradeProhibitedFlag.ON.getValue())
                        .build();
                PageInfo<Partner> result = partnerService.searchWithPageInfo(criteria);

                assertEquals(1, result.getList().size());
                assertEquals(1, result.getTotal());
            }

            @Test
            @DisplayName("雑区分で検索できる")
            void shouldSearchPartnerByMiscellaneousType() {
                Partner originalPartner = TestDataFactoryImpl.getPartner("009");
                Partner updatedPartner = Partner.of(
                        originalPartner.getPartnerCode(),
                        originalPartner.getPartnerName(),
                        originalPartner.getVendorType(),
                        originalPartner.getAddress(),
                        originalPartner.getTradeProhibitedFlag(),
                        MiscellaneousType.対象,
                        originalPartner.getPartnerGroupCode(),
                        originalPartner.getCredit(),
                        List.of(),
                        List.of()
                );

                partnerService.save(updatedPartner);
                PartnerCriteria criteria = PartnerCriteria.builder()
                        .miscellaneousType(MiscellaneousType.対象.getCode())
                        .build();
                PageInfo<Partner> result = partnerService.searchWithPageInfo(criteria);

                assertEquals(1, result.getList().size());
                assertEquals(1, result.getTotal());
            }

            @Test
            @DisplayName("取引先グループコードで検索できる")
            void shouldSearchPartnerByPartnerGroupCode() {
                Partner originalPartner = TestDataFactoryImpl.getPartner("009");
                Partner updatedPartner = Partner.of(
                        originalPartner.getPartnerCode(),
                        originalPartner.getPartnerName(),
                        originalPartner.getVendorType(),
                        originalPartner.getAddress(),
                        originalPartner.getTradeProhibitedFlag(),
                        originalPartner.getMiscellaneousType(),
                        PartnerGroupCode.of("9999"),
                        originalPartner.getCredit(),
                        List.of(),
                        List.of()
                );

                partnerService.save(updatedPartner);
                PartnerCriteria criteria = PartnerCriteria.builder()
                        .partnerGroupCode("9999")
                        .build();
                PageInfo<Partner> result = partnerService.searchWithPageInfo(criteria);

                assertEquals(1, result.getList().size());
                assertEquals(1, result.getTotal());
            }

            @Test
            @DisplayName("与信限度額で検索できる")
            void shouldSearchPartnerByCreditLimit() {
                Partner originalPartner = TestDataFactoryImpl.getPartner("009");
                Partner updatedPartner = Partner.of(
                        originalPartner.getPartnerCode(),
                        originalPartner.getPartnerName(),
                        originalPartner.getVendorType(),
                        originalPartner.getAddress(),
                        originalPartner.getTradeProhibitedFlag(),
                        originalPartner.getMiscellaneousType(),
                        originalPartner.getPartnerGroupCode(),
                        Credit.of(1000, 10000),
                        List.of(),
                        List.of()
                );

                partnerService.save(updatedPartner);
                PartnerCriteria criteria = PartnerCriteria.builder()
                        .creditLimit(1000)
                        .build();
                PageInfo<Partner> result = partnerService.searchWithPageInfo(criteria);

                assertEquals(1, result.getList().size());
                assertEquals(1, result.getTotal());

                criteria = PartnerCriteria.builder()
                        .creditLimit(999)
                        .build();
                result = partnerService.searchWithPageInfo(criteria);

                assertEquals(0, result.getList().size());
                assertEquals(0, result.getTotal());

                criteria = PartnerCriteria.builder()
                        .creditLimit(1001)
                        .build();
                result = partnerService.searchWithPageInfo(criteria);

                assertEquals(0, result.getList().size());
                assertEquals(0, result.getTotal());
            }

            @Test
            @DisplayName("与信一時増加枠で検索できる")
            void shouldSearchPartnerByTemporaryCreditIncrease() {
                Partner originalPartner = TestDataFactoryImpl.getPartner("009");
                Partner updatedPartner = Partner.of(
                        originalPartner.getPartnerCode(),
                        originalPartner.getPartnerName(),
                        originalPartner.getVendorType(),
                        originalPartner.getAddress(),
                        originalPartner.getTradeProhibitedFlag(),
                        originalPartner.getMiscellaneousType(),
                        originalPartner.getPartnerGroupCode(),
                        Credit.of(1000, 10000),
                        List.of(),
                        List.of()
                );

                partnerService.save(updatedPartner);
                PartnerCriteria criteria = PartnerCriteria.builder()
                        .temporaryCreditIncrease(10000)
                        .build();
                PageInfo<Partner> result = partnerService.searchWithPageInfo(criteria);

                assertEquals(1, result.getList().size());
                assertEquals(1, result.getTotal());

                criteria = PartnerCriteria.builder()
                        .temporaryCreditIncrease(9999)
                        .build();
                result = partnerService.searchWithPageInfo(criteria);

                assertEquals(0, result.getList().size());
                assertEquals(0, result.getTotal());

                criteria = PartnerCriteria.builder()
                        .temporaryCreditIncrease(10001)
                        .build();
                result = partnerService.searchWithPageInfo(criteria);

                assertEquals(0, result.getList().size());
                assertEquals(0, result.getTotal());
            }
        }
    }
}
