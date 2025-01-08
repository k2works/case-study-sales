package com.example.sms.service.master.partner;

import com.example.sms.domain.model.master.partner.Customer;
import com.example.sms.domain.model.master.partner.Partner;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Testcontainers
@ActiveProfiles("container")
@DisplayName("取引先レポジトリ")
public class PartnerRepositoryTest {
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
        return  Partner.of(
                partnerCode,
                "取引先名A",
                "トリヒキサキメイエー",
                1,
                "1234567",
                "東京都",
                "中央区銀座1-1-1",
                "ビル名201",
                0,
                2,
                "PG01",
                1000000,
                50000
        );
    }

    private Customer getCustomer(String customerCode, Integer customerBranchNumber) {
        return Customer.of(
                customerCode,  // customerCode（顧客コード）
                customerBranchNumber,  // customerBranchNumber（顧客枝番）
                1,  // customerCategory（顧客区分）
                "B001",  // billingCode（請求先コード）
                1,  // billingBranchNumber（請求先枝番）
                "C001",  // collectionCode（回収先コード）
                1,  // collectionBranchNumber（回収先枝番）
                "山田太郎",  // customerName（顧客名）
                "ヤマダタロウ",  // customerNameKana（顧客名カナ）
                "REP001",  // companyRepresentativeCode（自社担当者コード）
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
                1,  // customerPaymentMonth1（顧客支払月１）
                15,  // customerPaymentDay1（顧客支払日１）
                3,  // customerPaymentMethod1（顧客支払方法１）
                20,  // customerClosingDay2（顧客締日２）
                2,  // customerPaymentMonth2（顧客支払月２）
                30,  // customerPaymentDay2（顧客支払日２）
                4   // customerPaymentMethod2（顧客支払方法２）
        );
    }

    @Nested
    @DisplayName("取引先")
    class PartnerTest {
        @Test
        @DisplayName("取引先一覧を取得できる")
        void shouldRetrieveAllPartners() {
            IntStream.range(0, 10).forEach(i -> {
                Partner partner = getPartner(String.format("P%03d", i));
                repository.save(partner);
            });
            assertEquals(10, repository.selectAll().asList().size());
        }

        @Test
        @DisplayName("取引先を登録できる")
        void shouldRegisterPartner() {
            Partner partner = getPartner("P001");
            repository.save(partner);
            Partner actual = repository.findById("P001").orElseThrow();
            assertEquals(partner.getPartnerCode(), actual.getPartnerCode());
            assertEquals(partner.getPartnerName(), actual.getPartnerName());
        }

        @Test
        @DisplayName("取引先を更新できる")
        void shouldUpdatePartner() {
            Partner partner = getPartner("P001");
            repository.save(partner);

            Partner updatedPartner = Partner.of(
                    "P001",
                    "取引先名B",
                    "トリヒキサキメイエー",
                    1,
                    "1234567",
                    "東京都",
                    "中央区銀座1-1-1",
                    "ビル名201",
                    0,
                    2,
                    "PG01",
                    1000000,
                    50000
            );
            repository.save(updatedPartner);

            Partner actual = repository.findById("P001").orElseThrow();
            assertEquals(updatedPartner.getPartnerName(), actual.getPartnerName());
        }

        @Test
        @DisplayName("取引先を削除できる")
        void shouldDeletePartner() {
            Partner partner = getPartner("P001");
            repository.save(partner);

            repository.deleteById(partner);
            Optional<Partner> actual = repository.findById("P001");
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
                Partner partner = getPartner(String.format("P%03d", i));
                List<Customer> customers = IntStream.range(0, 2).collect(ArrayList::new, (list, j) -> {
                    Customer customer = getCustomer(partner.getPartnerCode(), j);
                    list.add(customer);
                }, ArrayList::addAll);
                repository.save(Partner.of(partner, customers));
            });

            assertEquals(10, repository.selectAll().asList().size());
            assertEquals(20, repository.selectAll().asList().stream().map(Partner::getCustomers).mapToInt(List::size).sum());
        }

        @Test
        @DisplayName("顧客を登録できる")
        void shouldRegisterCustomer() {
            Partner partner = getPartner("P001");
            Customer customer = getCustomer(partner.getPartnerCode(), 1);
            Partner savePartner = Partner.of(partner, List.of(customer));
            repository.save(savePartner);

            Partner actual = repository.findById("P001").orElseThrow();

            assertEquals(1, actual.getCustomers().size());
        }

        @Test
        @DisplayName("顧客を更新できる")
        void shouldUpdateCustomer() {
            Partner partner = getPartner("P001");
            Customer customer = getCustomer(partner.getPartnerCode(), 1);
            Partner savePartner = Partner.of(partner, List.of(customer));
            repository.save(savePartner);

            Customer updatedCustomer = getCustomer(partner.getPartnerCode(), 1);
            updatedCustomer =  Customer.of(
                    updatedCustomer.getCustomerCode(),
                    updatedCustomer.getCustomerBranchNumber(),
                    updatedCustomer.getCustomerCategory(),
                    updatedCustomer.getCollectionCode(),
                    updatedCustomer.getCollectionBranchNumber(),
                    updatedCustomer.getBillingCode(),
                    updatedCustomer.getBillingBranchNumber(),
                    "顧客名B",
                    updatedCustomer.getCustomerNameKana(),
                    updatedCustomer.getCompanyRepresentativeCode(),
                    updatedCustomer.getCustomerRepresentativeName(),
                    updatedCustomer.getCustomerDepartmentName(),
                    updatedCustomer.getCustomerPostalCode(),
                    updatedCustomer.getCustomerPrefecture(),
                    updatedCustomer.getCustomerAddress1(),
                    updatedCustomer.getCustomerAddress2(),
                    updatedCustomer.getCustomerPhoneNumber(),
                    updatedCustomer.getCustomerFaxNumber(),
                    updatedCustomer.getCustomerEmailAddress(),
                    updatedCustomer.getCustomerBillingCategory(),
                    updatedCustomer.getCustomerClosingDay1(),
                    updatedCustomer.getCustomerPaymentMonth1(),
                    updatedCustomer.getCustomerPaymentDay1(),
                    updatedCustomer.getCustomerPaymentMethod1(),
                    updatedCustomer.getCustomerClosingDay2(),
                    updatedCustomer.getCustomerPaymentMonth2(),
                    updatedCustomer.getCustomerPaymentDay2(),
                    updatedCustomer.getCustomerPaymentMethod2()
            );
            Partner updatedPartner = Partner.of(partner, List.of(updatedCustomer));
            repository.save(updatedPartner);

            Partner actual = repository.findById("P001").orElseThrow();
            assertEquals("顧客名B", actual.getCustomers().getFirst().getCustomerName());
        }

        @Test
        @DisplayName("顧客を削除できる")
        void shouldDeleteCustomer() {
            Partner partner = getPartner("P001");
            List<Customer> customers = IntStream.range(0, 2).collect(ArrayList::new, (list, j) -> {
                Customer customer = getCustomer(partner.getPartnerCode(), j);
                list.add(customer);
            }, ArrayList::addAll);
            Partner savePartner = Partner.of(partner, customers);
            repository.save(savePartner);

            savePartner = Partner.of(partner, List.of());
            repository.save(savePartner);

            Partner actual = repository.findById("P001").orElseThrow();
            assertEquals(0, actual.getCustomers().size());
        }
    }


    @Nested
    @DisplayName("取引先ページング")
    class PartnerPaginationTest {
        @Test
        @DisplayName("ページング情報を取得できる")
        void shouldRetrieveAllPartnersWithPageInfo() {
            IntStream.range(0, 15).forEach(i -> {
                Partner partner = getPartner(String.format("P%03d", i));
                repository.save(partner);
            });

            PageInfo<Partner> pageInfo = repository.selectAllWithPageInfo();
            assertEquals(15, pageInfo.getList().size());
        }
    }

    @Nested
    @DisplayName("取引先ページング")
    class CustomerPaginationTest {
        @Test
        @DisplayName("ページング情報を取得できる")
        void shouldRetrieveAllPartnersWithPageInfo() {
            Partner partner = getPartner("P001");
            Partner finalPartner = partner;
            List<Customer> customers = IntStream.range(0, 15).collect(ArrayList::new, (list, j) -> {
                Customer customer = getCustomer(finalPartner.getPartnerCode(), j);
                list.add(customer);
            }, ArrayList::addAll);
            partner = Partner.of(getPartner("P001"), customers);
            repository.save(partner);

            PageInfo<Customer> pageInfo = repository.selectAllCustomerWithPageInfo();

            assertEquals(15, pageInfo.getList().size());
        }
    }
}