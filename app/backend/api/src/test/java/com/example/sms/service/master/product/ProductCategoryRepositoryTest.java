package com.example.sms.service.master.product;

import com.example.sms.TestDataFactoryImpl;
import com.example.sms.domain.model.master.product.Product;
import com.example.sms.domain.model.master.product.ProductCategory;
import com.example.sms.domain.model.master.product.ProductCategoryList;
import com.example.sms.domain.type.product.*;
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
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@Testcontainers
@ActiveProfiles("container")
@DisplayName("商品分類レポジトリ")
public class ProductCategoryRepositoryTest {
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
    private ProductCategoryRepository repository;
    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        productRepository.deleteAll();
    }

    private ProductCategory getProductCategory(String productCategoryCode) {
        return TestDataFactoryImpl.getProductCategory(productCategoryCode, "商品分類名", 0, "商品分類パス", 1);
    }

    private Product getProduct(String productCode) {
        return TestDataFactoryImpl.getProduct(productCode, "商品正式名", "商品略称", "商品名カナ", ProductType.その他, 1000, 2000, 3000, TaxType.外税, "99999999", MiscellaneousType.適用外, StockManagementTargetType.対象, StockAllocationType.引当済, "000", 5);
    }

    @Nested
    @DisplayName("商品分類")
    class ProductCategoryTest {
        @Test
        @DisplayName("商品分類一覧を取得できる")
        void shouldRetrieveAllProductsCategory() {
            IntStream.range(0, 10).mapToObj(i -> getProductCategory("0000000" + i)).forEach(repository::save);

            ProductCategoryList actual = repository.selectAll();
            assertEquals(10, actual.size());
        }

        @Test
        @DisplayName("商品一覧を取得できる")
        void shouldRetrieveAllProducts() {
            ProductCategory productCategory = getProductCategory("99999999");
            List<Product> products = IntStream.range(0, 10).mapToObj(i -> getProduct("9999900" + i)).toList();
            products.forEach(productRepository::save);
            ProductCategory newProductCategory = ProductCategory.of(productCategory, products);

            repository.save(newProductCategory);

            ProductCategoryList actual = repository.selectAll();
            assertEquals(10, actual.asList().getFirst().getProducts().size());
        }

        @Test
        @DisplayName("商品分類を登録できる")
        void shouldSaveProductCategory() {
            ProductCategory productCategory = getProductCategory("99999999");

            repository.save(productCategory);

            ProductCategoryList actual = repository.selectAll();
            assertEquals(1, actual.size());
        }

        @Test
        @DisplayName("商品分類を更新できる")
        void shouldUpdateProductCategory() {
            ProductCategory productCategory = getProductCategory("99999999");
            repository.save(productCategory);

            ProductCategory newProductCategory = ProductCategory.of(productCategory.getProductCategoryCode().getValue(), "商品分類名2", productCategory.getProductCategoryHierarchy(), productCategory.getProductCategoryPath(), productCategory.getLowestLevelDivision());
            repository.save(newProductCategory);

            ProductCategory actual = repository.findById("99999999").get();
            assertEquals("商品分類名2", actual.getProductCategoryName());
        }

        @Test
        @DisplayName("商品分類を削除できる")
        void shouldDeleteProductCategory() {
            ProductCategory productCategory = getProductCategory("99999999");
            repository.save(productCategory);

            repository.deleteById(productCategory);

            ProductCategoryList actual = repository.selectAll();
            assertEquals(0, actual.size());
        }
    }
}
