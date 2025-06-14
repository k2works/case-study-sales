package com.example.sms.service.master.partner;

import com.example.sms.TestDataFactoryImpl;
import com.example.sms.domain.model.master.partner.customer.Customer;
import com.example.sms.domain.model.master.partner.Partner;
import com.example.sms.domain.model.master.partner.customer.Shipping;
import com.example.sms.domain.model.master.partner.vendor.Vendor;
import com.example.sms.infrastructure.datasource.ObjectOptimisticLockingFailureException;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@ActiveProfiles("container")
@DisplayName("取引先レポジトリ")
class PartnerRepositoryTest {
    @Container
    private static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:15"))
                    .withUsername("root")
                    .withPassword("password")
                    .withDatabaseName("postgres");

    @DynamicPropertySource
    static void setup(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
    }

    @Autowired
    private PartnerRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    private Partner getPartner(String partnerCode) {
        return TestDataFactoryImpl.getPartner(partnerCode);
    }

    private Customer getCustomer(String customerCode, Integer customerBranchNumber) {
        return TestDataFactoryImpl.getCustomer(customerCode, customerBranchNumber);
    }

    private Shipping getShipping(String customerCode, Integer destinationNumber, Integer customerBranchNumber) {
        return TestDataFactoryImpl.getShipping(customerCode, destinationNumber, customerBranchNumber);
    }

    private Vendor getVendor(String vendorCode, Integer vendorBranchCode) {
        return TestDataFactoryImpl.getVendor(vendorCode, vendorBranchCode);
    }

    @Nested
    @DisplayName("取引先")
    class PartnerTest {
        @Test
        @DisplayName("取引先一覧を取得できる")
        void shouldRetrieveAllPartners() {
            IntStream.range(0, 10).forEach(i -> {
                Partner partner = getPartner(String.format("%03d", i));
                repository.save(partner);
            });
            assertEquals(10, repository.selectAll().asList().size());
        }

        @Test
        @DisplayName("取引先を登録できる")
        void shouldRegisterPartner() {
            Partner partner = getPartner("001");
            repository.save(partner);
            Partner actual = repository.findById("001").orElseThrow();
            assertEquals(partner, actual);
        }

        @Test
        @DisplayName("取引先を更新できる")
        void shouldUpdatePartner() {
            Partner partner = getPartner("001");
            repository.save(partner);

            Partner updatedPartner = Partner.of(
                    "001",
                    "取引先名B",
                    "トリヒキサキメイエー",
                    1,
                    "1234567",
                    "東京都",
                    "中央区銀座1-1-1",
                    "ビル名201",
                    0,
                    0,
                    "PG01",
                    1000000,
                    50000
            );
            repository.save(updatedPartner);

            Partner actual = repository.findById("001").orElseThrow();
            assertEquals(updatedPartner.getPartnerName(), actual.getPartnerName());
            assertNotEquals(partner, actual);
        }

        @Test
        @DisplayName("取引先を削除できる")
        void shouldDeletePartner() {
            Partner partner = getPartner("001");
            repository.save(partner);

            repository.deleteById(partner);
            Optional<Partner> actual = repository.findById("001");
            assertEquals(Optional.empty(), actual);
        }

        @Test
        @DisplayName("取引先を削除できる")
        void shouldDeletePartnerWithReference() {
            Partner partner = getPartner("001");
            Customer customer = getCustomer(partner.getPartnerCode().getValue(), 1);
            Shipping shipping = getShipping(partner.getPartnerCode().getValue(), 1, 1);
            Shipping shipping2 = getShipping(partner.getPartnerCode().getValue(), 2, 1);
            customer = Customer.of(customer, List.of(shipping, shipping2));
            Partner savePartner = Partner.ofWithCustomers(partner, List.of(customer));
            repository.save(savePartner);

            repository.deleteById(partner);
            Optional<Partner> actual = repository.findById("001");
            assertEquals(Optional.empty(), actual);
        }
    }

    @Nested
    @DisplayName("顧客")
    class CustomerTest {
        @Test
        @DisplayName("顧客一覧を取得できる")
        void shouldRetrieveAllCustomers() {
            IntStream.range(0, 10).forEach(i -> {
                Partner partner = getPartner(String.format("%03d", i));
                List<Customer> customers = IntStream.range(0, 2).collect(ArrayList::new, (list, j) -> {
                    Customer customer = getCustomer(partner.getPartnerCode().getValue(), j);
                    list.add(customer);
                }, ArrayList::addAll);
                repository.save(Partner.ofWithCustomers(partner, customers));
            });

            assertEquals(10, repository.selectAll().asList().size());
            assertEquals(20, repository.selectAll().asList().stream().map(Partner::getCustomers).mapToInt(List::size).sum());
        }

        @Test
        @DisplayName("顧客を登録できる")
        void shouldRegisterCustomer() {
            Partner partner = getPartner("001");
            Customer customer = getCustomer(partner.getPartnerCode().getValue(), 1);
            Partner savePartner = Partner.ofWithCustomers(partner, List.of(customer));
            repository.save(savePartner);

            Partner actual = repository.findById("001").orElseThrow();

            assertEquals(1, actual.getCustomers().size());
            assertEquals(customer, actual.getCustomers().getFirst());
        }

        @Test
        @DisplayName("顧客を更新できる")
        void shouldUpdateCustomer() {
            Partner partner = getPartner("001");
            Customer customer = getCustomer(partner.getPartnerCode().getValue(), 1);
            Customer customer2 = getCustomer(partner.getPartnerCode().getValue(), 2);
            Partner savePartner = Partner.ofWithCustomers(partner, List.of(customer, customer2));
            repository.save(savePartner);

            Customer updatedCustomer = getCustomer(partner.getPartnerCode().getValue(), 1);
            updatedCustomer =  Customer.of(
                    updatedCustomer.getCustomerCode().getCode().getValue(),
                    updatedCustomer.getCustomerCode().getBranchNumber(),
                    updatedCustomer.getCustomerType().getValue(),
                    updatedCustomer.getBillingCode().getCode().getValue(),
                    updatedCustomer.getBillingCode().getBranchNumber(),
                    updatedCustomer.getCollectionCode().getCode().getValue(),
                    updatedCustomer.getCollectionCode().getBranchNumber(),
                    "顧客名B",
                    updatedCustomer.getCustomerName().getValue().getNameKana(),
                    updatedCustomer.getCompanyRepresentativeCode(),
                    updatedCustomer.getCustomerRepresentativeName(),
                    updatedCustomer.getCustomerDepartmentName(),
                    updatedCustomer.getCustomerAddress().getPostalCode().getValue(),
                    updatedCustomer.getCustomerAddress().getPrefecture().toString(),
                    updatedCustomer.getCustomerAddress().getAddress1(),
                    updatedCustomer.getCustomerAddress().getAddress2(),
                    updatedCustomer.getCustomerPhoneNumber().getValue(),
                    updatedCustomer.getCustomerFaxNumber().getValue(),
                    updatedCustomer.getCustomerEmailAddress().getValue(),
                    updatedCustomer.getBilling().getCustomerBillingCategory().getValue(),
                    updatedCustomer.getBilling().getClosingBilling1().getClosingDay().getValue(),
                    updatedCustomer.getBilling().getClosingBilling1().getPaymentMonth().getValue(),
                    updatedCustomer.getBilling().getClosingBilling1().getPaymentDay().getValue(),
                    updatedCustomer.getBilling().getClosingBilling1().getPaymentMethod().getValue(),
                    updatedCustomer.getBilling().getClosingBilling2().getClosingDay().getValue(),
                    updatedCustomer.getBilling().getClosingBilling2().getPaymentMonth().getValue(),
                    updatedCustomer.getBilling().getClosingBilling2().getPaymentDay().getValue(),
                    updatedCustomer.getBilling().getClosingBilling2().getPaymentMethod().getValue()
            );
            Partner updatedPartner = Partner.ofWithCustomers(partner, List.of(updatedCustomer, customer2));
            repository.save(updatedPartner);

            Partner actual = repository.findById("001").orElseThrow();
            assertEquals(2, actual.getCustomers().size());
            assertEquals("顧客名B", actual.getCustomers().getFirst().getCustomerName().getValue().getName());
        }

        @Test
        @DisplayName("顧客を削除できる")
        void shouldDeleteCustomer() {
            Partner partner = getPartner("001");
            List<Customer> customers = IntStream.range(0, 2).collect(ArrayList::new, (list, j) -> {
                Customer customer = getCustomer(partner.getPartnerCode().getValue(), j);
                list.add(customer);
            }, ArrayList::addAll);
            Partner savePartner = Partner.ofWithCustomers(partner, customers);
            repository.save(savePartner);

            savePartner = Partner.ofWithCustomers(partner, List.of());
            repository.save(savePartner);

            Partner actual = repository.findById("001").orElseThrow();
            assertEquals(0, actual.getCustomers().size());
        }
    }

    @Nested
    @DisplayName("出荷先")
    class ShippingTest {
        @Test
        @DisplayName("出荷先一覧を取得できる")
        void shouldRetrieveAllShipping() {
            IntStream.range(0, 10).forEach(i -> {
                Partner partner = getPartner(String.format("%03d", i));
                List<Customer> customers = IntStream.range(0, 2).collect(ArrayList::new, (list, j) -> {
                    Customer customer = getCustomer(partner.getPartnerCode().getValue(), j);

                    List<Shipping> shippings = IntStream.range(0, 2).collect(ArrayList::new, (list2, k) -> {
                        Shipping shipping = getShipping(partner.getPartnerCode().getValue(), k, j);
                        list2.add(shipping);
                    }, ArrayList::addAll);

                    customer = Customer.of(customer, shippings);
                    list.add(customer);
                }, ArrayList::addAll);

                repository.save(Partner.ofWithCustomers(partner, customers));
            });

            assertEquals(10, repository.selectAll().asList().size());
            assertEquals(20, repository.selectAll().asList().stream().map(Partner::getCustomers).mapToInt(List::size).sum());
            assertEquals(40, repository.selectAll().asList().stream().map(Partner::getCustomers).mapToInt(list -> list.stream().map(Customer::getShippings).mapToInt(List::size).sum()).sum());
        }

        @Test
        @DisplayName("出荷先を登録できる")
        void shouldRegisterShipping() {
            Partner partner = getPartner("001");
            Customer customer = getCustomer(partner.getPartnerCode().getValue(), 1);
            Shipping shipping = getShipping(partner.getPartnerCode().getValue(), 1, 1);
            customer = Customer.of(customer, List.of(shipping));
            Partner savePartner = Partner.ofWithCustomers(partner, List.of(customer));
            repository.save(savePartner);

            Partner actual = repository.findById("001").orElseThrow();

            assertEquals(1, actual.getCustomers().size());
            assertEquals(1, actual.getCustomers().getFirst().getShippings().size());
            assertEquals(shipping, actual.getCustomers().getFirst().getShippings().getFirst());
        }

        @Test
        @DisplayName("出荷先を更新できる")
        void shouldUpdateShipping() {
            Partner partner = getPartner("001");
            Customer customer = getCustomer(partner.getPartnerCode().getValue(), 1);
            Shipping shipping = getShipping(partner.getPartnerCode().getValue(), 1, 1);
            Shipping shipping2 = getShipping(partner.getPartnerCode().getValue(), 2, 1);
            customer = Customer.of(customer, List.of(shipping, shipping2));
            Partner savePartner = Partner.ofWithCustomers(partner, List.of(customer));
            repository.save(savePartner);

            Shipping updatedShipping = getShipping(partner.getPartnerCode().getValue(), 1, 1);
            updatedShipping = Shipping.of(
                    updatedShipping.getShippingCode().getCustomerCode().getCode().getValue(),
                    updatedShipping.getShippingCode().getDestinationNumber(),
                    updatedShipping.getShippingCode().getCustomerCode().getBranchNumber(),
                    "出荷先名B",
                    updatedShipping.getRegionCode().getValue(),
                    updatedShipping.getShippingAddress().getPostalCode().getValue(),
                    updatedShipping.getShippingAddress().getAddress1(),
                    updatedShipping.getShippingAddress().getAddress2()
            );
            customer = Customer.of(customer, List.of(updatedShipping, shipping2));
            Partner updatedPartner = Partner.ofWithCustomers(partner, List.of(customer));
            repository.save(updatedPartner);

            Partner actual = repository.findById("001").orElseThrow();

            assertEquals(1, actual.getCustomers().size());
            assertEquals(2, actual.getCustomers().getFirst().getShippings().size());
            assertEquals("出荷先名B", actual.getCustomers().getFirst().getShippings().getFirst().getDestinationName());
        }

        @Test
        @DisplayName("出荷先を削除できる")
        void shouldDeleteShipping() {
            Partner partner = getPartner("001");
            Customer customer = getCustomer(partner.getPartnerCode().getValue(), 1);
            Shipping shipping = getShipping(partner.getPartnerCode().getValue(), 1, 1);
            Shipping shipping2 = getShipping(partner.getPartnerCode().getValue(), 2, 1);
            customer = Customer.of(customer, List.of(shipping, shipping2));
            Partner savePartner = Partner.ofWithCustomers(partner, List.of(customer));
            repository.save(savePartner);

            customer = Customer.of(customer, List.of());
            Partner updatedPartner = Partner.ofWithCustomers(partner, List.of(customer));
            repository.save(updatedPartner);

            Partner actual = repository.findById("001").orElseThrow();

            assertEquals(1, actual.getCustomers().size());
            assertEquals(0, actual.getCustomers().getFirst().getShippings().size());
        }
    }

    @Nested
    @DisplayName("仕入先")
    class VendorTest {
        @Test
        @DisplayName("仕入先一覧を取得できる")
        void shouldRetrieveAllVendors() {
            IntStream.range(0, 10).forEach(i -> {
                Partner partner = getPartner(String.format("%03d", i));
                List<Vendor> vendors = IntStream.range(0, 2).collect(ArrayList::new, (list, j) -> {
                    Vendor vendor = getVendor(partner.getPartnerCode().getValue(), j);
                    list.add(vendor);
                }, ArrayList::addAll);
                repository.save(Partner.ofWithVendors(partner, vendors));
            });

            assertEquals(10, repository.selectAll().asList().size());
            assertEquals(20, repository.selectAll().asList().stream().map(Partner::getVendors).mapToInt(List::size).sum());
        }

        @Test
        @DisplayName("仕入先を登録できる")
        void shouldRegisterVendor() {
            Partner partner = getPartner("001");
            Vendor vendor = getVendor(partner.getPartnerCode().getValue(), 1);
            Partner savePartner = Partner.ofWithVendors(partner, List.of(vendor));
            repository.save(savePartner);

            Partner actual = repository.findById("001").orElseThrow();

            assertEquals(1, actual.getVendors().size());
            assertEquals(vendor, actual.getVendors().getFirst());
        }

        @Test
        @DisplayName("仕入先を更新できる")
        void shouldUpdateVendor() {
            Partner partner = getPartner("001");
            Vendor vendor = getVendor(partner.getPartnerCode().getValue(), 1);
            Vendor vendor2 = getVendor(partner.getPartnerCode().getValue(), 2);
            Partner savePartner = Partner.ofWithVendors(partner, List.of(vendor, vendor2));
            repository.save(savePartner);

            Vendor updatedVendor = getVendor(partner.getPartnerCode().getValue(), 1);
            updatedVendor = Vendor.of(
                    updatedVendor.getVendorCode().getCode().getValue(),
                    updatedVendor.getVendorCode().getBranchNumber(),
                    "仕入先名B",
                    updatedVendor.getVendorName().getValue().getNameKana(),
                    updatedVendor.getVendorContactName(),
                    updatedVendor.getVendorDepartmentName(),
                    updatedVendor.getVendorAddress().getPostalCode().getValue(),
                    updatedVendor.getVendorAddress().getPrefecture().toString(),
                    updatedVendor.getVendorAddress().getAddress1(),
                    updatedVendor.getVendorAddress().getAddress2(),
                    updatedVendor.getVendorPhoneNumber().getValue(),
                    updatedVendor.getVendorFaxNumber().getValue(),
                    updatedVendor.getVendorEmailAddress().getValue(),
                    updatedVendor.getVendorClosingBilling().getClosingDay().getValue(),
                    updatedVendor.getVendorClosingBilling().getPaymentMonth().getValue(),
                    updatedVendor.getVendorClosingBilling().getPaymentDay().getValue(),
                    updatedVendor.getVendorClosingBilling().getPaymentMethod().getValue()
            );
            Partner updatedPartner = Partner.ofWithVendors(partner, List.of(updatedVendor, vendor2));
            repository.save(updatedPartner);

            Partner actual = repository.findById("001").orElseThrow();
            assertEquals(2, actual.getVendors().size());
            assertEquals("仕入先名B", actual.getVendors().getFirst().getVendorName().getValue().getName());
        }

        @Test
        @DisplayName("仕入先を削除できる")
        void shouldDeleteVendor() {
            Partner partner = getPartner("001");
            Vendor vendor = getVendor(partner.getPartnerCode().getValue(), 1);
            Partner savePartner = Partner.ofWithVendors(partner, List.of(vendor));
            repository.save(savePartner);

            savePartner = Partner.ofWithVendors(partner, List.of());
            repository.save(savePartner);

            Partner actual = repository.findById("001").orElseThrow();
            assertEquals(0, actual.getVendors().size());
        }
    }

    @Nested
    @DisplayName("取引先ページング")
    class PartnerPaginationTest {
        @Test
        @DisplayName("ページング情報を取得できる")
        void shouldRetrieveAllPartnersWithPageInfo() {
            IntStream.range(0, 15).forEach(i -> {
                Partner partner = getPartner(String.format("%03d", i));
                repository.save(partner);
            });

            PageInfo<Partner> pageInfo = repository.selectAllWithPageInfo();
            assertEquals(15, pageInfo.getList().size());
        }
    }

    @Nested
    @DisplayName("顧客ページング")
    class CustomerPaginationTest {
        @Test
        @DisplayName("ページング情報を取得できる")
        void shouldRetrieveAllPartnersWithPageInfo() {
            Partner partner = getPartner("001");
            Partner finalPartner = partner;
            List<Customer> customers = IntStream.range(0, 15).collect(ArrayList::new, (list, j) -> {
                Customer customer = getCustomer(finalPartner.getPartnerCode().getValue(), j);
                list.add(customer);
            }, ArrayList::addAll);
            partner = Partner.ofWithCustomers(getPartner("001"), customers);
            repository.save(partner);

            PageInfo<Customer> pageInfo = repository.selectAllCustomerWithPageInfo();

            assertEquals(15, pageInfo.getList().size());
        }
    }

    @Nested
    @DisplayName("仕入先ページング")
    class VendorPaginationTest {
        @Test
        @DisplayName("ページング情報を取得できる")
        void shouldRetrieveAllPartnersWithPageInfo() {
            Partner partner = getPartner("001");
            Partner finalPartner = partner;
            List<Vendor> vendors = IntStream.range(0, 15).collect(ArrayList::new, (list, j) -> {
                Vendor vendor = getVendor(finalPartner.getPartnerCode().getValue(), j);
                list.add(vendor);
            }, ArrayList::addAll);
            partner = Partner.ofWithVendors(getPartner("001"), vendors);
            repository.save(partner);

            PageInfo<Vendor> pageInfo = repository.selectAllVendorWithPageInfo();

            assertEquals(15, pageInfo.getList().size());
        }
    }

    //TODO: マルチスレッドだと失敗してもテストは正常終了扱いになるので、コンソールで確認する必要がある
    @Test
    @DisplayName("楽観ロックが正常に動作すること")
    void testOptimisticLockingWithThreads() throws InterruptedException {
        Partner partner = getPartner("999");
        repository.save(partner);

        Thread thread1 = new Thread(() -> {
            Partner updatedPartner1 = getPartner("999");
            repository.save(updatedPartner1);
        });

        Thread thread2 = new Thread(() -> {
            Partner updatedPartner2 = getPartner("999");
            assertThrows(ObjectOptimisticLockingFailureException.class, () -> {
                repository.save(updatedPartner2);
            });
        });

        // スレッドを開始
        thread1.start();
        thread2.start();

        // スレッドの終了を待機
        thread1.join();
        thread2.join();
    }
}