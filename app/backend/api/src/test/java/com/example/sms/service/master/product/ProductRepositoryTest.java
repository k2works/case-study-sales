package com.example.sms.service.master.product;

import com.example.sms.TestDataFactoryImpl;
import com.example.sms.domain.model.master.product.*;
import com.example.sms.domain.type.product.*;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@Testcontainers
@ActiveProfiles("container")
@DisplayName("商品レポジトリ")
public class ProductRepositoryTest {
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
    private ProductRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    private Product getProduct(String productCode) {
        return TestDataFactoryImpl.getProduct(productCode, "商品正式名", "商品略称", "商品名カナ", ProductType.その他, 1000, 2000, 3000, TaxType.外税, "00000000", MiscellaneousType.適用外, StockManagementTargetType.対象, StockAllocationType.引当済, "000", 5);
    }

    private Product getProductForBom(String productCode, ProductType productType) {
        return TestDataFactoryImpl.getProduct(productCode, "商品正式名", "商品略称", "商品名カナ", productType, 1000, 2000, 3000, TaxType.外税, "00000000", MiscellaneousType.適用外, StockManagementTargetType.対象, StockAllocationType.引当済, "000", 5);
    }

    private SubstituteProduct getSubstituteProduct(String productCode, String substituteProductCode) {
        return TestDataFactoryImpl.getSubstituteProduct(productCode, substituteProductCode, 1);
    }

    private Bom getBom(String productCode, String componentCode) {
        return TestDataFactoryImpl.getBom(productCode, componentCode, 1);
    }

    private CustomerSpecificSellingPrice getCustomerSpecificSellingPrice(String productCode, String format) {
        return TestDataFactoryImpl.customerSpecificSellingPrice(productCode, format, 1);
    }

    @Nested
    @DisplayName("商品")
    class ProductTest {
        @Test
        @DisplayName("商品一覧を取得できる")
        void shouldRetrieveAllProducts() {
            IntStream.range(0, 10).forEach(i -> {
                Product product = getProduct(String.format("99999%03d", i));
                repository.save(product);
            });

            assertEquals(10, repository.selectAll().size());
        }

        @Test
        @DisplayName("商品を登録できる")
        void shouldRegisterProduct() {
            Product product = getProduct("99999999");

            repository.save(product);

            Product actual = repository.findById(product.getProductCode().getValue()).get();
            assertEquals(product.getProductCode(), actual.getProductCode());
            assertEquals(product.getProductName().getProductFormalName(), actual.getProductName().getProductFormalName());
            assertEquals(product.getProductName().getProductAbbreviation(), actual.getProductName().getProductAbbreviation());
            assertEquals(product.getProductName().getProductNameKana(), actual.getProductName().getProductNameKana());
            assertEquals(product.getProductType(), actual.getProductType());
            assertEquals(product.getSellingPrice(), actual.getSellingPrice());
            assertEquals(product.getPurchasePrice(), actual.getPurchasePrice());
            assertEquals(product.getCostOfSales(), actual.getCostOfSales());
            assertEquals(product.getTaxType(), actual.getTaxType());
            assertEquals(product.getProductCategoryCode(), actual.getProductCategoryCode());
            assertEquals(product.getMiscellaneousType(), actual.getMiscellaneousType());
            assertEquals(product.getStockManagementTargetType(), actual.getStockManagementTargetType());
            assertEquals(product.getStockAllocationType(), actual.getStockAllocationType());
            assertEquals(product.getVendorCode(), actual.getVendorCode());
        }

        @Test
        @DisplayName("商品を更新できる")
        void shouldUpdateProduct() {
            Product product = getProduct("99999999");
            repository.save(product);

            product = repository.findById(product.getProductCode().getValue()).get();
            Product updatedProduct = Product.of(product.getProductCode().getValue(), "更新後商品正式名", "更新後商品略称", "更新後商品名カナ", ProductType.商品, 2000, 3000, 4000, TaxType.内税, "99999999", MiscellaneousType.適用外, StockManagementTargetType.対象, StockAllocationType.引当済, "999", 6);
            repository.save(updatedProduct);

            Product actual = repository.findById(product.getProductCode().getValue()).get();
            assertEquals(updatedProduct, actual);
        }

        @Test
        @DisplayName("商品を削除できる")
        void shouldDeleteProduct() {
            Product product = getProduct("99999999");
            repository.save(product);

            repository.deleteById(product);

            assertEquals(0, repository.selectAll().size());
        }

        //TODO: マルチスレッドだと失敗してもテストは正常終了扱いになるので、コンソールで確認する必要がある
        @Test
        @DisplayName("楽観ロックが正常に動作すること")
        void testOptimisticLockingWithThreads() throws InterruptedException {
            // Productを新規作成して保存
            Product product1 = getProduct("99999999");
            repository.save(product1);

            // スレッド1でproduct1を更新
            Thread thread1 = new Thread(() -> {
                Product updatedProduct1 = getProduct("99999999");
                repository.save(updatedProduct1);
            });

            // スレッド2でproduct2を更新し、例外を確認
            Thread thread2 = new Thread(() -> {
                Product updatedProduct2 = getProduct("99999999");
                assertThrows(ObjectOptimisticLockingFailureException.class, () -> {
                    repository.save(updatedProduct2);
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

    @Nested
    @DisplayName("代替商品")
    class SubstituteProductTest {
        @Test
        @DisplayName("代替商品一覧を取得できる")
        void shouldRetrieveAllSubstituteProducts() {
            Product product = getProduct("99999999");
            List<SubstituteProduct> substituteProducts = IntStream.range(0, 10).mapToObj(i -> getSubstituteProduct(product.getProductCode().getValue(), getProduct(String.format("99999%03d", i)).getProductCode().getValue())).toList();
            Product newProduct = Product.of(product, substituteProducts, List.of(), List.of());
            repository.save(newProduct);

            ProductList actual = repository.selectAll();
            assertEquals(10, actual.asList().getFirst().getSubstituteProduct().size());
        }

        @Test
        @DisplayName("代替商品を登録できる")
        void shouldRegisterSubstituteProduct() {
            Product product = getProduct("99999999");
            SubstituteProduct substituteProduct = getSubstituteProduct(product.getProductCode().getValue(), getProduct("99999000").getProductCode().getValue());
            Product newProduct = Product.of(product, List.of(substituteProduct), List.of(), List.of());
            repository.save(newProduct);

            Product actual = repository.findById(product.getProductCode().getValue()).get();
            assertEquals(1, actual.getSubstituteProduct().size());
            assertEquals(substituteProduct, actual.getSubstituteProduct().get(0));
        }

        @Test
        @DisplayName("代替商品を更新できる")
        void shouldUpdateSubstituteProduct() {
            Product product = getProduct("99999999");
            SubstituteProduct substituteProduct = getSubstituteProduct(product.getProductCode().getValue(), getProduct("99999000").getProductCode().getValue());
            Product newProduct = Product.of(product, List.of(substituteProduct), List.of(), List.of());
            repository.save(newProduct);

            newProduct = repository.findById(product.getProductCode().getValue()).get();
            SubstituteProduct updatedSubstituteProduct = SubstituteProduct.of(product.getProductCode().getValue(), getProduct("99999000").getProductCode().getValue(), 2);
            newProduct = Product.of(newProduct, List.of(updatedSubstituteProduct), List.of(), List.of());
            repository.save(newProduct);

            Product actual = repository.findById(product.getProductCode().getValue()).get();
            assertEquals(1, actual.getSubstituteProduct().size());
            assertEquals(updatedSubstituteProduct, actual.getSubstituteProduct().get(0));
        }

        @Test
        @DisplayName("代替商品を削除できる")
        void shouldDeleteSubstituteProduct() {
            Product product = getProduct("99999999");
            SubstituteProduct substituteProduct = getSubstituteProduct(product.getProductCode().getValue(), getProduct("99999000").getProductCode().getValue());
            Product newProduct = Product.of(product, List.of(substituteProduct), List.of(), List.of());
            repository.save(newProduct);

            Product deleteProduct = repository.findById(product.getProductCode().getValue()).get();
            repository.deleteById(deleteProduct);

            Optional<Product> actual = repository.findById(product.getProductCode().getValue());
            assertEquals(Optional.empty(), actual);
        }
    }

    @Nested
    @DisplayName("部品表")
    class BomTest {
        @Test
        @DisplayName("部品表一覧を取得できる")
        void shouldRetrieveAllBoms() {
            Product product = getProduct("99999999");
            List<Bom> boms = IntStream.range(0, 10).mapToObj(i -> getBom(product.getProductCode().getValue(), "X" + String.format("%02d", i))).toList();
            Product newProduct = Product.of(product, List.of(), boms, List.of());
            repository.save(newProduct);

            ProductList actual = repository.selectAll();
            assertEquals(10, actual.asList().getFirst().getBoms().size());
        }

        @Test
        @DisplayName("部品表一覧を取得できる")
        void shouldRetrieveAllBomsBySelectAllBoms() {
            List<Product> products = List.of(
                    getProductForBom("99999999", ProductType.商品),
                    getProductForBom("99999998", ProductType.製品),
                    getProductForBom("99999997", ProductType.部品),
                    getProductForBom("99999996", ProductType.包材),

                    getProductForBom("99999995", ProductType.その他)
            );
            products.forEach(product -> {
                List<Bom> boms = IntStream.range(0, 10).mapToObj(i -> getBom(product.getProductCode().getValue(), "X" + String.format("%02d", i))).toList();
                Product newProduct = Product.of(product, List.of(), boms, List.of());
                repository.save(newProduct);
            });

            PageInfo<Product> actual = repository.selectAllBoms();
            assertEquals(3, actual.getList().size());
        }

        @Test
        @DisplayName("部品表を登録できる")
        void shouldRegisterBom() {
            Product product = getProduct("99999999");
            Bom bom = getBom(product.getProductCode().getValue(), "X99");
            Product newProduct = Product.of(product, List.of(), List.of(bom), List.of());
            repository.save(newProduct);

            Product actual = repository.findById(product.getProductCode().getValue()).get();
            assertEquals(1, actual.getBoms().size());
            assertEquals(bom, actual.getBoms().get(0));
        }

        @Test
        @DisplayName("部品表を更新できる")
        void shouldUpdateBom() {
            Product product = getProduct("99999999");
            Bom bom = getBom(product.getProductCode().getValue(), "X99");
            Product newProduct = Product.of(product, List.of(), List.of(bom), List.of());
            repository.save(newProduct);

            newProduct = repository.findById(product.getProductCode().getValue()).get();
            Bom updatedBom = Bom.of(product.getProductCode().getValue(), "X99", 2);
            newProduct = Product.of(newProduct, List.of(), List.of(updatedBom), List.of());
            repository.save(newProduct);

            Product actual = repository.findById(product.getProductCode().getValue()).get();
            assertEquals(1, actual.getBoms().size());
            assertEquals(updatedBom, actual.getBoms().get(0));
        }

        @Test
        @DisplayName("部品表を削除できる")
        void shouldDeleteBom() {
            Product product = getProduct("99999999");
            Bom bom = getBom(product.getProductCode().getValue(), "X99");
            Product newProduct = Product.of(product, List.of(), List.of(bom), List.of());
            repository.save(newProduct);

            Product deleteProduct = repository.findById(product.getProductCode().getValue()).get();
            repository.deleteById(deleteProduct);

            Optional<Product> actual = repository.findById(product.getProductCode().getValue());
            assertEquals(Optional.empty(), actual);
        }
    }

    @Nested
    @DisplayName("顧客別販売単価")
    class CustomerSpecificSellingPriceTest {
        @Test
        @DisplayName("顧客別販売単価一覧を取得できる")
        void shouldRetrieveAllCustomerSpecificSellingPrices() {
            Product product = getProduct("99999999");
            List<CustomerSpecificSellingPrice> customerSpecificSellingPrices = IntStream.range(0, 10).mapToObj(i -> getCustomerSpecificSellingPrice(product.getProductCode().getValue(), String.format("%08d", i))).toList();
            Product newProduct = Product.of(product, List.of(), List.of(), customerSpecificSellingPrices);
            repository.save(newProduct);

            ProductList actual = repository.selectAll();
            assertEquals(10, actual.asList().getFirst().getCustomerSpecificSellingPrices().size());
        }

        @Test
        @DisplayName("顧客別販売単価を登録できる")
        void shouldRegisterCustomerSpecificSellingPrice() {
            Product product = getProduct("99999999");
            CustomerSpecificSellingPrice customerSpecificSellingPrice = getCustomerSpecificSellingPrice(product.getProductCode().getValue(), "00000000");
            Product newProduct = Product.of(product, List.of(), List.of(), List.of(customerSpecificSellingPrice));
            repository.save(newProduct);

            Product actual = repository.findById(product.getProductCode().getValue()).get();
            assertEquals(1, actual.getCustomerSpecificSellingPrices().size());
            assertEquals(customerSpecificSellingPrice, actual.getCustomerSpecificSellingPrices().get(0));
        }

        @Test
        @DisplayName("顧客別販売単価を更新できる")
        void shouldUpdateCustomerSpecificSellingPrice() {
            Product product = getProduct("99999999");
            CustomerSpecificSellingPrice customerSpecificSellingPrice = getCustomerSpecificSellingPrice(product.getProductCode().getValue(), "00000000");
            Product newProduct = Product.of(product, List.of(), List.of(), List.of(customerSpecificSellingPrice));
            repository.save(newProduct);

            newProduct = repository.findById(product.getProductCode().getValue()).get();
            CustomerSpecificSellingPrice updatedCustomerSpecificSellingPrice = CustomerSpecificSellingPrice.of(product.getProductCode().getValue(), "00000000", 2);
            newProduct = Product.of(newProduct, List.of(), List.of(), List.of(updatedCustomerSpecificSellingPrice));
            repository.save(newProduct);

            Product actual = repository.findById(product.getProductCode().getValue()).get();
            assertEquals(1, actual.getCustomerSpecificSellingPrices().size());
            assertEquals(updatedCustomerSpecificSellingPrice, actual.getCustomerSpecificSellingPrices().get(0));
        }

        @Test
        @DisplayName("顧客別販売単価を削除できる")
        void shouldDeleteCustomerSpecificSellingPrice() {
            Product product = getProduct("99999999");
            CustomerSpecificSellingPrice customerSpecificSellingPrice = getCustomerSpecificSellingPrice(product.getProductCode().getValue(), "00000000");
            Product newProduct = Product.of(product, List.of(), List.of(), List.of(customerSpecificSellingPrice));
            repository.save(newProduct);

            Product deleteProduct = repository.findById(product.getProductCode().getValue()).get();
            repository.deleteById(deleteProduct);

            Optional<Product> actual = repository.findById(product.getProductCode().getValue());
            assertEquals(Optional.empty(), actual);
        }
    }

}
