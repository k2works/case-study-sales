package com.example.sms.service.master.partner;

import com.example.sms.IntegrationTest;
import com.example.sms.TestDataFactory;
import com.example.sms.TestDataFactoryImpl;
import com.example.sms.domain.type.mail.Email;
import com.example.sms.domain.type.phone.FaxNumber;
import com.example.sms.domain.type.phone.PhoneNumber;
import com.example.sms.domain.type.address.Address;
import com.example.sms.domain.model.master.partner.customer.CustomerType;
import com.example.sms.domain.model.master.partner.Partner;
import com.example.sms.domain.model.master.partner.customer.*;
import com.example.sms.domain.model.master.region.RegionCode;
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
@DisplayName("顧客サービス")
class CustomerServiceTest {
    @Autowired
    private PartnerService partnerService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private TestDataFactory testDataFactory;

    @BeforeEach
    void setUp() {
        testDataFactory.setUpForCustomerService();
    }

    @Nested
    @DisplayName("顧客")
    class CustomerTest {
        @Test
        @DisplayName("顧客一覧を取得できる")
        void shouldGetCustomerList() {
            CustomerList result = customerService.selectAll();
            assertEquals(3, result.size());
            assertEquals("001", result.asList().getFirst().getCustomerCode().getCode().getValue());
        }

        @Nested
        @DisplayName("顧客を新規登録できる")
        class RegisterCustomerTest {
            @Test
            @DisplayName("出荷先を持たない顧客を新規登録できる")
            void case_1() throws BusinessException {
                Partner partner = TestDataFactoryImpl.getPartner("009");
                partnerService.register(partner);
                Customer newCustomer = TestDataFactoryImpl.getCustomer("009", 1);

                customerService.register(newCustomer);

                Customer result = customerService.find(CustomerCode.of("009", 1));
                assertEquals(newCustomer, result);
            }
            @Test
            @DisplayName("出荷先を持つ顧客を新規登録できる")
            void case_2() throws BusinessException {
                Partner partner = TestDataFactoryImpl.getPartner("009");
                partnerService.register(partner);
                Shipping shipping = TestDataFactoryImpl.getShipping("009", 1,1);

                Customer newCustomer = TestDataFactoryImpl.getCustomer("009", 1);
                newCustomer = Customer.of(newCustomer, List.of(shipping));
                customerService.register(newCustomer);

                Customer result = customerService.find(CustomerCode.of("009", 1));
                assertEquals(newCustomer, result);
                assertEquals(shipping, result.getShippings().getFirst());
            }
            @Test
            @DisplayName("複数の顧客を新規登録できる")
            void case_3() {
                Partner partner = TestDataFactoryImpl.getPartner("009");
                partnerService.register(partner);
                Customer newCustomer1 = TestDataFactoryImpl.getCustomer("009", 1);
                Customer newCustomer2 = TestDataFactoryImpl.getCustomer("009", 2);
                Customer newCustomer3 = TestDataFactoryImpl.getCustomer("009", 3);
                partnerService.register(Partner.ofWithCustomers(partner, List.of(newCustomer1, newCustomer2, newCustomer3)));

                Customer result1 = customerService.find(CustomerCode.of("009", 1));
                Customer result2 = customerService.find(CustomerCode.of("009", 2));
                Customer result3 = customerService.find(CustomerCode.of("009", 3));
                assertEquals(newCustomer1, result1);
                assertEquals(newCustomer2, result2);
                assertEquals(newCustomer3, result3);
            }
            @Test
            @DisplayName("複数の顧客を新規登録できる（既存の顧客コードと重複しない）")
            void case_4() throws BusinessException {
                Partner partner = TestDataFactoryImpl.getPartner("009");
                partnerService.register(partner);
                Customer newCustomer1 = TestDataFactoryImpl.getCustomer("009", 1);
                Customer newCustomer2 = TestDataFactoryImpl.getCustomer("009", 2);
                Customer newCustomer3 = TestDataFactoryImpl.getCustomer("009", 3);
                partnerService.register(Partner.ofWithCustomers(partner, List.of(newCustomer1, newCustomer2, newCustomer3)));

                Customer newCustomer4 = TestDataFactoryImpl.getCustomer("009", 4);
                customerService.register(newCustomer4);

                Partner partnerResult = partnerService.find("009");
                assertEquals(4, partnerResult.getCustomers().size());
            }
            @Test
            @DisplayName("対応する取引先コードが存在しない場合、顧客を新規登録できない")
            void case_5() {
                Customer newCustomer = TestDataFactoryImpl.getCustomer("009", 1);

                assertThrows(BusinessException.class, () -> customerService.register(newCustomer));
            }
            @Test
            @DisplayName("既存の顧客コードと重複する場合、顧客を新規登録できない")
            void case_6() {
                Partner partner = TestDataFactoryImpl.getPartner("009");
                partnerService.register(partner);
                Customer newCustomer1 = TestDataFactoryImpl.getCustomer("009", 1);
                Customer newCustomer2 = TestDataFactoryImpl.getCustomer("009", 2);
                Customer newCustomer3 = TestDataFactoryImpl.getCustomer("009", 3);
                partnerService.register(Partner.ofWithCustomers(partner, List.of(newCustomer1, newCustomer2, newCustomer3)));

                Customer newCustomer4 = TestDataFactoryImpl.getCustomer("009", 2);
                assertThrows(BusinessException.class, () -> customerService.register(newCustomer4));
            }
        }

        @Nested
        @DisplayName("顧客を編集できる")
        class SaveCustomerTest {
            @Test
            void case_1() throws BusinessException {
                Partner partner = TestDataFactoryImpl.getPartner("009");
                partnerService.register(partner);
                Customer originalCustomer = TestDataFactoryImpl.getCustomer("009", 1);
                customerService.register(originalCustomer);

                Customer updatedCustomer = Customer.of(
                        originalCustomer.getCustomerCode(),
                        originalCustomer.getCustomerType(),
                        originalCustomer.getBillingCode(),
                        originalCustomer.getCollectionCode(),
                        CustomerName.of("顧客A", "コキャクエー"),
                        originalCustomer.getCompanyRepresentativeCode(),
                        originalCustomer.getCustomerRepresentativeName(),
                        originalCustomer.getCustomerDepartmentName(),
                        originalCustomer.getCustomerAddress(),
                        originalCustomer.getCustomerPhoneNumber(),
                        originalCustomer.getCustomerFaxNumber(),
                        originalCustomer.getCustomerEmailAddress(),
                        originalCustomer.getInvoice(),
                        originalCustomer.getShippings()
                );
                customerService.save(updatedCustomer);

                Customer result = customerService.find(CustomerCode.of("009", 1));
                assertNotEquals(originalCustomer, result);
                assertEquals(updatedCustomer, result);
            }

            @Test
            void case_2() throws BusinessException {
                Partner partner = TestDataFactoryImpl.getPartner("009");
                partnerService.register(partner);
                Customer originalCustomer = TestDataFactoryImpl.getCustomer("009", 1);
                Shipping shipping = TestDataFactoryImpl.getShipping("009", 1,1);
                originalCustomer = Customer.of(originalCustomer, List.of(shipping));
                customerService.register(originalCustomer);

                Customer updatedCustomer = Customer.of(
                        originalCustomer.getCustomerCode(),
                        originalCustomer.getCustomerType(),
                        originalCustomer.getBillingCode(),
                        originalCustomer.getCollectionCode(),
                        CustomerName.of("顧客A", "コキャクエー"),
                        originalCustomer.getCompanyRepresentativeCode(),
                        originalCustomer.getCustomerRepresentativeName(),
                        originalCustomer.getCustomerDepartmentName(),
                        originalCustomer.getCustomerAddress(),
                        originalCustomer.getCustomerPhoneNumber(),
                        originalCustomer.getCustomerFaxNumber(),
                        originalCustomer.getCustomerEmailAddress(),
                        originalCustomer.getInvoice(),
                        originalCustomer.getShippings()
                );
                customerService.save(updatedCustomer);

                Customer result = customerService.find(CustomerCode.of("009", 1));
                assertNotEquals(originalCustomer, result);
                assertEquals(updatedCustomer, result);
            }

            @Test
            void case_3() {
                Partner partner = TestDataFactoryImpl.getPartner("009");
                partnerService.register(partner);
                Customer originalCustomer1 = TestDataFactoryImpl.getCustomer("009", 1);
                Customer originalCustomer2 = TestDataFactoryImpl.getCustomer("009", 2);
                Customer originalCustomer3 = TestDataFactoryImpl.getCustomer("009", 3);
                partnerService.register(Partner.ofWithCustomers(partner, List.of(originalCustomer1, originalCustomer2, originalCustomer3)));

                Customer updatedCustomer = Customer.of(
                        originalCustomer2.getCustomerCode(),
                        originalCustomer2.getCustomerType(),
                        originalCustomer2.getBillingCode(),
                        originalCustomer2.getCollectionCode(),
                        CustomerName.of("顧客A", "コキャクエー"),
                        originalCustomer2.getCompanyRepresentativeCode(),
                        originalCustomer2.getCustomerRepresentativeName(),
                        originalCustomer2.getCustomerDepartmentName(),
                        originalCustomer2.getCustomerAddress(),
                        originalCustomer2.getCustomerPhoneNumber(),
                        originalCustomer2.getCustomerFaxNumber(),
                        originalCustomer2.getCustomerEmailAddress(),
                        originalCustomer2.getInvoice(),
                        originalCustomer2.getShippings()
                );
                customerService.save(updatedCustomer);

                Customer result = customerService.find(CustomerCode.of("009", 2));
                assertNotEquals(originalCustomer2, result);
                assertEquals(updatedCustomer, result);
            }

            @Test
            void case_4() throws BusinessException {
                Partner partner = TestDataFactoryImpl.getPartner("009");
                partnerService.register(partner);
                Customer originalCustomer = TestDataFactoryImpl.getCustomer("009", 1);
                Shipping shipping = TestDataFactoryImpl.getShipping("009", 1, 1);
                originalCustomer = Customer.of(originalCustomer, List.of(shipping));
                customerService.register(originalCustomer);

                Shipping updatedShipping = Shipping.of(
                        ShippingCode.of("009", 1, 1),
                        "配送先A",
                        RegionCode.of("R001"),
                        Address.of("1234567", "東京都", "新宿区西新宿", "1-1-1")
                );
                Customer updatedCustomer = Customer.of(
                        originalCustomer.getCustomerCode(),
                        originalCustomer.getCustomerType(),
                        originalCustomer.getBillingCode(),
                        originalCustomer.getCollectionCode(),
                        CustomerName.of("顧客A", "コキャクエー"),
                        originalCustomer.getCompanyRepresentativeCode(),
                        originalCustomer.getCustomerRepresentativeName(),
                        originalCustomer.getCustomerDepartmentName(),
                        originalCustomer.getCustomerAddress(),
                        originalCustomer.getCustomerPhoneNumber(),
                        originalCustomer.getCustomerFaxNumber(),
                        originalCustomer.getCustomerEmailAddress(),
                        originalCustomer.getInvoice(),
                        List.of(updatedShipping)
                );
                customerService.save(updatedCustomer);

                Customer result = customerService.find(CustomerCode.of("009", 1));
                assertNotEquals(originalCustomer, result);
                assertEquals(updatedCustomer, result);
                assertEquals(updatedShipping, result.getShippings().getFirst());
            }

            @Test
            void case_5() throws BusinessException {
                Partner partner = TestDataFactoryImpl.getPartner("009");
                partnerService.register(partner);
                Customer originalCustomer = TestDataFactoryImpl.getCustomer("009", 1);
                Shipping shipping1 = TestDataFactoryImpl.getShipping("009", 1, 1);
                Shipping shipping2 = TestDataFactoryImpl.getShipping("009", 2, 1);
                originalCustomer = Customer.of(originalCustomer, List.of(shipping1, shipping2));
                customerService.register(originalCustomer);

                Shipping updatedShipping = Shipping.of(
                        ShippingCode.of("009", 1, 1),
                        "配送先A",
                        RegionCode.of("R001"),
                        Address.of("1234567", "東京都", "新宿区西新宿", "1-1-1")
                );
                Customer updatedCustomer = Customer.of(
                        originalCustomer.getCustomerCode(),
                        originalCustomer.getCustomerType(),
                        originalCustomer.getBillingCode(),
                        originalCustomer.getCollectionCode(),
                        CustomerName.of("顧客A", "コキャクエー"),
                        originalCustomer.getCompanyRepresentativeCode(),
                        originalCustomer.getCustomerRepresentativeName(),
                        originalCustomer.getCustomerDepartmentName(),
                        originalCustomer.getCustomerAddress(),
                        originalCustomer.getCustomerPhoneNumber(),
                        originalCustomer.getCustomerFaxNumber(),
                        originalCustomer.getCustomerEmailAddress(),
                        originalCustomer.getInvoice(),
                        List.of(updatedShipping, shipping2)
                );
                customerService.save(updatedCustomer);

                Customer result = customerService.find(CustomerCode.of("009", 1));
                assertNotEquals(originalCustomer, result);
                assertEquals(updatedCustomer, result);
                assertEquals(2, result.getShippings().size());
                assertEquals(updatedShipping, result.getShippings().getFirst());
            }
        }

        @Nested
        @DisplayName("顧客を削除できる")
        class DeleteCustomerTest {
            @Test
            void case_1() throws BusinessException {
                Partner partner = TestDataFactoryImpl.getPartner("009");
                partnerService.register(partner);
                Customer customer = TestDataFactoryImpl.getCustomer("009", 1);
                customerService.register(customer);

                customerService.delete(customer);

                Customer result = customerService.find(CustomerCode.of("009",1));
                assertNull(result);
                Partner partnerResult = partnerService.find("009");
                assertEquals(0, partnerResult.getCustomers().size());
            }
            @Test
            void case_2() {
                Partner partner = TestDataFactoryImpl.getPartner("009");
                partnerService.register(partner);
                Customer customer1 = TestDataFactoryImpl.getCustomer("009", 1);
                Customer customer2 = TestDataFactoryImpl.getCustomer("009", 2);
                Customer customer3 = TestDataFactoryImpl.getCustomer("009", 3);
                partnerService.register(Partner.ofWithCustomers(partner, List.of(customer1, customer2, customer3)));

                customerService.delete(customer2);

                Customer result = customerService.find(CustomerCode.of("009", 2));
                assertNull(result);
                Partner partnerResult = partnerService.find("009");
                assertEquals(2, partnerResult.getCustomers().size());
            }
            @Test
            void case_3() {
                Partner partner = TestDataFactoryImpl.getPartner("009");
                partnerService.register(partner);
                Customer customer1 = TestDataFactoryImpl.getCustomer("009", 1);
                Customer customer2 = TestDataFactoryImpl.getCustomer("009", 2);
                Customer customer3 = TestDataFactoryImpl.getCustomer("009", 3);
                Shipping shipping1 = TestDataFactoryImpl.getShipping("009", 1, 1);
                Shipping shipping2 = TestDataFactoryImpl.getShipping("009", 2, 2);
                Shipping shipping3 = TestDataFactoryImpl.getShipping("009", 3, 3);
                customer1 = Customer.of(customer1, List.of(shipping1));
                customer2 = Customer.of(customer2, List.of(shipping2));
                customer3 = Customer.of(customer3, List.of(shipping3));
                partnerService.register(Partner.ofWithCustomers(partner, List.of(customer1, customer2, customer3)));

                customerService.delete(customer2);

                Customer result = customerService.find(CustomerCode.of("009", 2));
                assertNull(result);
                Partner partnerResult = partnerService.find("009");
                assertEquals(2, partnerResult.getCustomers().size());
                assertEquals(1, partnerResult.getCustomers().getFirst().getShippings().size());
            }
        }

        @Nested
        @DisplayName("顧客検索")
        class CustomerSearchTest {
            @Test
            @DisplayName("顧客コードで検索できる")
            void shouldSearchCustomerByCode() throws BusinessException {
                Partner partner = TestDataFactoryImpl.getPartner("009");
                partnerService.register(partner);
                Customer originalCustomer = TestDataFactoryImpl.getCustomer("009", 1);
                customerService.register(originalCustomer);

                CustomerCriteria criteria = CustomerCriteria.builder()
                        .customerCode("009")
                        .build();
                PageInfo<Customer> result = customerService.searchWithPageInfo(criteria);
                assertEquals(1, result.getList().size());
                assertEquals(1, result.getTotal());
            }

            @Test
            @DisplayName("顧客名で検索できる")
            void shouldSearchCustomerByName() throws BusinessException {
                Partner partner = TestDataFactoryImpl.getPartner("009");
                partnerService.register(partner);
                Customer originalCustomer = TestDataFactoryImpl.getCustomer("009",1);
                Customer updatedCustomer = Customer.of(
                        originalCustomer.getCustomerCode(),
                        originalCustomer.getCustomerType(),
                        originalCustomer.getBillingCode(),
                        originalCustomer.getCollectionCode(),
                        CustomerName.of("顧客A", "コキャクエー"),
                        originalCustomer.getCompanyRepresentativeCode(),
                        originalCustomer.getCustomerRepresentativeName(),
                        originalCustomer.getCustomerDepartmentName(),
                        originalCustomer.getCustomerAddress(),
                        originalCustomer.getCustomerPhoneNumber(),
                        originalCustomer.getCustomerFaxNumber(),
                        originalCustomer.getCustomerEmailAddress(),
                        originalCustomer.getInvoice(),
                        originalCustomer.getShippings()
                );
                customerService.register(updatedCustomer);

                CustomerCriteria criteria = CustomerCriteria.builder()
                        .customerName("顧客A")
                        .build();
                PageInfo<Customer> result = customerService.searchWithPageInfo(criteria);
                assertEquals(1, result.getList().size());
                assertEquals(1, result.getTotal());
            }

            @Test
            @DisplayName("顧客名カナで検索できる")
            void shouldSearchCustomerByNameKana() throws BusinessException {
                Partner partner = TestDataFactoryImpl.getPartner("009");
                partnerService.register(partner);
                Customer originalCustomer = TestDataFactoryImpl.getCustomer("009",1);
                Customer updatedCustomer = Customer.of(
                        originalCustomer.getCustomerCode(),
                        originalCustomer.getCustomerType(),
                        originalCustomer.getBillingCode(),
                        originalCustomer.getCollectionCode(),
                        CustomerName.of("顧客A", "コキャクエー"),
                        originalCustomer.getCompanyRepresentativeCode(),
                        originalCustomer.getCustomerRepresentativeName(),
                        originalCustomer.getCustomerDepartmentName(),
                        originalCustomer.getCustomerAddress(),
                        originalCustomer.getCustomerPhoneNumber(),
                        originalCustomer.getCustomerFaxNumber(),
                        originalCustomer.getCustomerEmailAddress(),
                        originalCustomer.getInvoice(),
                        originalCustomer.getShippings()
                );
                customerService.register(updatedCustomer);

                CustomerCriteria criteria = CustomerCriteria.builder()
                        .customerNameKana("コキャクエー")
                        .build();
                PageInfo<Customer> result = customerService.searchWithPageInfo(criteria);
                assertEquals(1, result.getList().size());
                assertEquals(1, result.getTotal());
            }

            @Test
            @DisplayName("顧客区分で検索できる")
            void shouldSearchCustomerByType() throws BusinessException {
                Partner partner = TestDataFactoryImpl.getPartner("009");
                partnerService.register(partner);
                Customer originalCustomer = TestDataFactoryImpl.getCustomer("009", 1);
                Customer updatedCustomer = Customer.of(
                        originalCustomer.getCustomerCode(),
                        CustomerType.顧客でない,
                        originalCustomer.getBillingCode(),
                        originalCustomer.getCollectionCode(),
                        originalCustomer.getCustomerName(),
                        originalCustomer.getCompanyRepresentativeCode(),
                        originalCustomer.getCustomerRepresentativeName(),
                        originalCustomer.getCustomerDepartmentName(),
                        originalCustomer.getCustomerAddress(),
                        originalCustomer.getCustomerPhoneNumber(),
                        originalCustomer.getCustomerFaxNumber(),
                        originalCustomer.getCustomerEmailAddress(),
                        originalCustomer.getInvoice(),
                        originalCustomer.getShippings()
                );
                customerService.register(updatedCustomer);

                CustomerCriteria criteria = CustomerCriteria.builder()
                        .customerType(CustomerType.顧客でない.getValue())
                        .build();
                PageInfo<Customer> result = customerService.searchWithPageInfo(criteria);
                assertEquals(1, result.getList().size());
                assertEquals(1, result.getTotal());
            }

            @Test
            @DisplayName("請求先コードで検索できる")
            void shouldSearchCustomerByBillingCode() throws BusinessException {
                Partner partner = TestDataFactoryImpl.getPartner("009");
                partnerService.register(partner);
                Customer originalCustomer = TestDataFactoryImpl.getCustomer("009", 1);
                Customer updatedCustomer = Customer.of(
                        originalCustomer.getCustomerCode(),
                        originalCustomer.getCustomerType(),
                        BillingCode.of("009", 1), // 請求先コード
                        originalCustomer.getCollectionCode(),
                        originalCustomer.getCustomerName(),
                        originalCustomer.getCompanyRepresentativeCode(),
                        originalCustomer.getCustomerRepresentativeName(),
                        originalCustomer.getCustomerDepartmentName(),
                        originalCustomer.getCustomerAddress(),
                        originalCustomer.getCustomerPhoneNumber(),
                        originalCustomer.getCustomerFaxNumber(),
                        originalCustomer.getCustomerEmailAddress(),
                        originalCustomer.getInvoice(),
                        originalCustomer.getShippings()
                );
                customerService.register(updatedCustomer);

                CustomerCriteria criteria = CustomerCriteria.builder()
                        .billingCode("009")
                        .build();
                PageInfo<Customer> result = customerService.searchWithPageInfo(criteria);
                assertEquals(1, result.getList().size());
                assertEquals(1, result.getTotal());
            }

            @Test
            @DisplayName("回収先コードで検索できる")
            void shouldSearchCustomerByCollectionCode() throws BusinessException {
                Partner partner = TestDataFactoryImpl.getPartner("009");
                partnerService.register(partner);
                Customer originalCustomer = TestDataFactoryImpl.getCustomer("009", 1);
                Customer updatedCustomer = Customer.of(
                        originalCustomer.getCustomerCode(),
                        originalCustomer.getCustomerType(),
                        originalCustomer.getBillingCode(),
                        CollectionCode.of("009", 1), // 回収先コード
                        originalCustomer.getCustomerName(),
                        originalCustomer.getCompanyRepresentativeCode(),
                        originalCustomer.getCustomerRepresentativeName(),
                        originalCustomer.getCustomerDepartmentName(),
                        originalCustomer.getCustomerAddress(),
                        originalCustomer.getCustomerPhoneNumber(),
                        originalCustomer.getCustomerFaxNumber(),
                        originalCustomer.getCustomerEmailAddress(),
                        originalCustomer.getInvoice(),
                        originalCustomer.getShippings()
                );
                customerService.register(updatedCustomer);

                CustomerCriteria criteria = CustomerCriteria.builder()
                        .collectionCode("009")
                        .build();
                PageInfo<Customer> result = customerService.searchWithPageInfo(criteria);
                assertEquals(1, result.getList().size());
                assertEquals(1, result.getTotal());
            }

            @Test
            @DisplayName("自社担当者コードで検索できる")
            void shouldSearchCustomerByCompanyRepresentativeCode() throws BusinessException {
                Partner partner = TestDataFactoryImpl.getPartner("009");
                partnerService.register(partner);
                Customer originalCustomer = TestDataFactoryImpl.getCustomer("009", 1);
                Customer updatedCustomer = Customer.of(
                        originalCustomer.getCustomerCode(),
                        originalCustomer.getCustomerType(),
                        originalCustomer.getBillingCode(),
                        originalCustomer.getCollectionCode(),
                        originalCustomer.getCustomerName(),
                        "009", // 自社担当者コード
                        originalCustomer.getCustomerRepresentativeName(),
                        originalCustomer.getCustomerDepartmentName(),
                        originalCustomer.getCustomerAddress(),
                        originalCustomer.getCustomerPhoneNumber(),
                        originalCustomer.getCustomerFaxNumber(),
                        originalCustomer.getCustomerEmailAddress(),
                        originalCustomer.getInvoice(),
                        originalCustomer.getShippings()
                );
                customerService.register(updatedCustomer);

                CustomerCriteria criteria = CustomerCriteria.builder()
                        .companyRepresentativeCode("009")
                        .build();
                PageInfo<Customer> result = customerService.searchWithPageInfo(criteria);
                assertEquals(1, result.getList().size());
                assertEquals(1, result.getTotal());
            }

            @Test
            @DisplayName("顧客担当者名で検索できる")
            void shouldSearchCustomerByCustomerRepresentativeName() throws BusinessException {
                Partner partner = TestDataFactoryImpl.getPartner("009");
                partnerService.register(partner);
                Customer originalCustomer = TestDataFactoryImpl.getCustomer("009", 1);
                Customer updatedCustomer = Customer.of(
                        originalCustomer.getCustomerCode(),
                        originalCustomer.getCustomerType(),
                        originalCustomer.getBillingCode(),
                        originalCustomer.getCollectionCode(),
                        originalCustomer.getCustomerName(),
                        originalCustomer.getCompanyRepresentativeCode(),
                        "担当者名", // 顧客担当者名
                        originalCustomer.getCustomerDepartmentName(),
                        originalCustomer.getCustomerAddress(),
                        originalCustomer.getCustomerPhoneNumber(),
                        originalCustomer.getCustomerFaxNumber(),
                        originalCustomer.getCustomerEmailAddress(),
                        originalCustomer.getInvoice(),
                        originalCustomer.getShippings()
                );
                customerService.register(updatedCustomer);

                CustomerCriteria criteria = CustomerCriteria.builder()
                        .customerRepresentativeName("担当者名")
                        .build();
                PageInfo<Customer> result = customerService.searchWithPageInfo(criteria);
                assertEquals(1, result.getList().size());
                assertEquals(1, result.getTotal());
            }

            @Test
            @DisplayName("顧客部署名で検索できる")
            void shouldSearchCustomerByCustomerDepartmentName() throws BusinessException {
                Partner partner = TestDataFactoryImpl.getPartner("009");
                partnerService.register(partner);
                Customer originalCustomer = TestDataFactoryImpl.getCustomer("009", 1);
                Customer updatedCustomer = Customer.of(
                        originalCustomer.getCustomerCode(),
                        originalCustomer.getCustomerType(),
                        originalCustomer.getBillingCode(),
                        originalCustomer.getCollectionCode(),
                        originalCustomer.getCustomerName(),
                        originalCustomer.getCompanyRepresentativeCode(),
                        originalCustomer.getCustomerRepresentativeName(),
                        "部署名", // 顧客部署名
                        originalCustomer.getCustomerAddress(),
                        originalCustomer.getCustomerPhoneNumber(),
                        originalCustomer.getCustomerFaxNumber(),
                        originalCustomer.getCustomerEmailAddress(),
                        originalCustomer.getInvoice(),
                        originalCustomer.getShippings()
                );
                customerService.register(updatedCustomer);

                CustomerCriteria criteria = CustomerCriteria.builder()
                        .customerDepartmentName("部署名")
                        .build();
                PageInfo<Customer> result = customerService.searchWithPageInfo(criteria);
                assertEquals(1, result.getList().size());
                assertEquals(1, result.getTotal());
            }

            @Test
            @DisplayName("住所で検索できる")
            void shouldSearchCustomerByAddress() throws BusinessException {
                Partner partner = TestDataFactoryImpl.getPartner("009");
                partnerService.register(partner);
                Customer originalCustomer = TestDataFactoryImpl.getCustomer("009", 1);
                Customer updatedCustomer = Customer.of(
                        originalCustomer.getCustomerCode(),
                        originalCustomer.getCustomerType(),
                        originalCustomer.getBillingCode(),
                        originalCustomer.getCollectionCode(),
                        originalCustomer.getCustomerName(),
                        originalCustomer.getCompanyRepresentativeCode(),
                        originalCustomer.getCustomerRepresentativeName(),
                        originalCustomer.getCustomerDepartmentName(),
                        Address.of("1234567", "東京都", "新宿区西新宿", "1-1-1"),
                        originalCustomer.getCustomerPhoneNumber(),
                        originalCustomer.getCustomerFaxNumber(),
                        originalCustomer.getCustomerEmailAddress(),
                        originalCustomer.getInvoice(),
                        originalCustomer.getShippings()
                );
                customerService.register(updatedCustomer);

                CustomerCriteria criteria = CustomerCriteria.builder()
                        .postalCode("1234567")
                        .prefecture("東京都")
                        .address1("新宿区西新宿")
                        .address2("1-1-1")
                        .build();
                PageInfo<Customer> result = customerService.searchWithPageInfo(criteria);
                assertEquals(1, result.getList().size());
                assertEquals(1, result.getTotal());
            }

            @Test
            @DisplayName("電話番号で検索できる")
            void shouldSearchCustomerByPhoneNumber() throws BusinessException {
                Partner partner = TestDataFactoryImpl.getPartner("009");
                partnerService.register(partner);
                Customer customer = TestDataFactoryImpl.getCustomer("009", 1);
                Customer updatedCustomer = Customer.of(
                        customer.getCustomerCode(),
                        customer.getCustomerType(),
                        customer.getBillingCode(),
                        customer.getCollectionCode(),
                        customer.getCustomerName(),
                        customer.getCompanyRepresentativeCode(),
                        customer.getCustomerRepresentativeName(),
                        customer.getCustomerDepartmentName(),
                        customer.getCustomerAddress(),
                        PhoneNumber.of("0312345679"), // 電話番号
                        customer.getCustomerFaxNumber(),
                        customer.getCustomerEmailAddress(),
                        customer.getInvoice(),
                        customer.getShippings()
                );
                customerService.register(updatedCustomer);

                CustomerCriteria criteria = CustomerCriteria.builder()
                        .customerPhoneNumber("03-1234-5679")
                        .build();
                PageInfo<Customer> result = customerService.searchWithPageInfo(criteria);
                assertEquals(1, result.getList().size());
                assertEquals(1, result.getTotal());
            }

            @Test
            @DisplayName("FAX番号で検索できる")
            void shouldSearchCustomerByFaxNumber() throws BusinessException {
                Partner partner = TestDataFactoryImpl.getPartner("009");
                partnerService.register(partner);
                Customer customer = TestDataFactoryImpl.getCustomer("009", 1);
                Customer updatedCustomer = Customer.of(
                        customer.getCustomerCode(),
                        customer.getCustomerType(),
                        customer.getBillingCode(),
                        customer.getCollectionCode(),
                        customer.getCustomerName(),
                        customer.getCompanyRepresentativeCode(),
                        customer.getCustomerRepresentativeName(),
                        customer.getCustomerDepartmentName(),
                        customer.getCustomerAddress(),
                        customer.getCustomerPhoneNumber(),
                        FaxNumber.of("0398765439"), // FAX番号
                        customer.getCustomerEmailAddress(),
                        customer.getInvoice(),
                        customer.getShippings()
                );
                customerService.register(updatedCustomer);

                CustomerCriteria criteria = CustomerCriteria.builder()
                        .customerFaxNumber("03-9876-5439")
                        .build();
                PageInfo<Customer> result = customerService.searchWithPageInfo(criteria);
                assertEquals(1, result.getList().size());
                assertEquals(1, result.getTotal());
            }

            @Test
            @DisplayName("メールアドレスで検索できる")
            void shouldSearchCustomerByEmailAddress() throws BusinessException {
                Partner partner = TestDataFactoryImpl.getPartner("009");
                partnerService.register(partner);
                Customer customer = TestDataFactoryImpl.getCustomer("009", 1);
                Customer updatedCustomer = Customer.of(
                        customer.getCustomerCode(),
                        customer.getCustomerType(),
                        customer.getBillingCode(),
                        customer.getCollectionCode(),
                        customer.getCustomerName(),
                        customer.getCompanyRepresentativeCode(),
                        customer.getCustomerRepresentativeName(),
                        customer.getCustomerDepartmentName(),
                        customer.getCustomerAddress(),
                        customer.getCustomerPhoneNumber(),
                        customer.getCustomerFaxNumber(),
                        Email.of("customer@example.com"), // メールアドレス
                        customer.getInvoice(),
                        customer.getShippings()
                );
                customerService.register(updatedCustomer);

                CustomerCriteria criteria = CustomerCriteria.builder()
                        .customerEmailAddress("customer@example.com")
                        .build();
                PageInfo<Customer> result = customerService.searchWithPageInfo(criteria);
                assertEquals(1, result.getList().size());
                assertEquals(1, result.getTotal());
            }

            @Test
            @DisplayName("検索条件未該当（データが0件）")
            void shouldReturnNoResultsIfCustomerDoesNotExist() {
                Partner partner = TestDataFactoryImpl.getPartner("010"); // 違うデータを登録
                partnerService.register(partner);

                CustomerCriteria criteria = CustomerCriteria.builder()
                        .customerCode("999") // 存在しない顧客コード
                        .build();
                PageInfo<Customer> result = customerService.searchWithPageInfo(criteria);
                assertEquals(0, result.getList().size());
                assertEquals(0, result.getTotal());
            }

            @Test
            @DisplayName("複数条件で検索できる")
            void shouldSearchCustomerByMultipleCriteria() throws BusinessException {
                Partner partner = TestDataFactoryImpl.getPartner("009");
                partnerService.register(partner);
                Customer customer = TestDataFactoryImpl.getCustomer("009", 1);
                Customer updatedCustomer = Customer.of(
                        customer.getCustomerCode(),
                        customer.getCustomerType(),
                        customer.getBillingCode(),
                        customer.getCollectionCode(),
                        CustomerName.of("顧客A", "コキャクエー"),
                        customer.getCompanyRepresentativeCode(),
                        customer.getCustomerRepresentativeName(),
                        customer.getCustomerDepartmentName(),
                        Address.of("1234567", "東京都", "新宿区西新宿", "1-1-1"),
                        PhoneNumber.of("0312345678"), // 電話番号
                        FaxNumber.of("0398765432"), // FAX番号
                        Email.of("customer@example.com"), // メールアドレス
                        customer.getInvoice(),
                        customer.getShippings()
                );
                customerService.register(updatedCustomer);

                CustomerCriteria criteria = CustomerCriteria.builder()
                        .customerName("顧客A")
                        .postalCode("1234567")
                        .customerPhoneNumber("03-1234-5678")
                        .build();
                PageInfo<Customer> result = customerService.searchWithPageInfo(criteria);
                assertEquals(1, result.getList().size());
                assertEquals(1, result.getTotal());
            }
        }
    }
}
