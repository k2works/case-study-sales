package com.example.sms.service.master.product;

import com.example.sms.TestDataFactoryImpl;
import com.example.sms.domain.model.master.product.Product;
import com.example.sms.domain.type.product.*;
import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.junit.jupiter.api.Test;

import java.util.Optional;


@Testcontainers
@SpringBootTest
@ActiveProfiles("container")
public class ProductRepositoryContainerTest {
    @Container
    private static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:15"))
                    .withUsername("username")
                    .withPassword("password")
                    .withDatabaseName("postgres");

    @Autowired
    ProductRepository repository;

    @DynamicPropertySource
    static void setup(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
    }

    private Product getProduct(String productCode) {
        return TestDataFactoryImpl.product(productCode, "商品正式名", "商品略称", "商品名カナ", ProductType.その他, 1000, 2000, 3000, TaxType.外税, "00000000", MiscellaneousType.適用外, StockManagementTargetType.対象, StockAllocationType.引当済, "00000000", 5);
    }

    @Test
    void test() {
        Assertions.assertThat(postgres.isRunning()).isTrue();
        Product product = getProduct("99999999");
        repository.save(product);

        final Optional<Product> p = repository.findById("99999999");

        Assertions.assertThat(p.get()).isEqualTo(product);
    }
}

